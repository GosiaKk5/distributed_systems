import ray
from DataNode import DataNode
import random

@ray.remote
class NameNode:
    def __init__(self, no_data_nodes, chunk_size, no_data_nodes_per_chunk):
        self.data_nodes = [DataNode.remote() for _ in range(no_data_nodes)]
        self.chunk_size = chunk_size
        self.no_data_nodes_per_chunk = no_data_nodes_per_chunk
        self.no_data_nodes = no_data_nodes
        self.chunk_locations = {}

    
    #to delate
    def get_data_nodes_pid(self):
        
        message =""
        for i, data_node in enumerate(self.data_nodes):

            # Get the PID of the actor
            pid = ray.get(data_node.get_pid.remote())

            message += f"{i}: {pid} \n"

        return message

    ####################3
    
    def add_artifact(self, artifact_name, artifact_text):
        self._make_sure_data_nodes_are_alive()

        if artifact_name in self.chunk_locations.keys():
            return "There is artifact with this name already."
        
        self.chunk_locations[artifact_name] = {}
        chunks = self._divide_text_into_chunks(artifact_text)
        remote_ids = []

        for (chunk_number, chunk) in enumerate(chunks):
            data_nodes = random.sample(self.data_nodes, min(self.no_data_nodes_per_chunk, len(self.data_nodes)))
            
            for data_node in data_nodes:
                remote_ids.append(data_node.add_chunk.remote(artifact_name, chunk_number, chunk))
                self._add_chunk_in_dict(artifact_name, chunk_number, data_node)

        ray.get(remote_ids)

        return "Artifact added"

    def get_artifact(self, artifact_name):
        self._make_sure_data_nodes_are_alive()

        if artifact_name not in self.chunk_locations.keys():
            return "There is no artifact with this name."
        
        artifact_text = ""
        for chunk_id, data_nodes in self.chunk_locations[artifact_name].items():
            result_ids = [data_node.get_chunk.remote(artifact_name, chunk_id) for data_node in data_nodes]
            ready_id, remaining_ids = ray.wait(result_ids, num_returns=1)
            artifact_text += ray.get(ready_id[0])

        return artifact_text
    
    def remove_artifact(self, artifact_name):
        self._make_sure_data_nodes_are_alive()

        if artifact_name not in self.chunk_locations.keys():
            return "There is no artifact with this name."
        
        for chunk_id, data_nodes in self.chunk_locations[artifact_name].items():
            result_ids = [data_node.remove_chunk.remote(artifact_name, chunk_id) for data_node in data_nodes]

        ray.get(result_ids)
        del self.chunk_locations[artifact_name]

        return "Artifact deleted"
    
    def update_artifact(self, artifact_name, artifact_text):
        self.remove_artifact(artifact_name)
        return self.add_artifact(artifact_name, artifact_text)
    
    def list_data_node(self, index):
        self._make_sure_data_nodes_are_alive()

        if index < self.no_data_nodes:
            return ray.get(self.data_nodes[index].list_data_node.remote())
        
    def kill_data_node(self, index):
        ray.kill(self.data_nodes[index])

        return "Data node " + str(index) + " was killed"
        

    
    def _make_sure_data_nodes_are_alive(self):
        for data_node in self.data_nodes:
            if not self._is_data_node_alive(data_node):
                self._replace_dead_data_node(data_node)

        return "All alive"
                
        

    def _replace_dead_data_node(self, dead_data_node):
        new_data_node = DataNode.remote()

        result_ids = []

        for artifact_name, artfiact_chunks_locations in self.chunk_locations.items():
            for chunk_id, data_nodes in artfiact_chunks_locations.items():
                for index, data_node in enumerate(data_nodes):
                    if data_node == dead_data_node:
                        self.chunk_locations[artifact_name][chunk_id][index] = new_data_node
                        result_ids.append(new_data_node.add_chunk.remote(artifact_name,
                                                                        chunk_id,
                                                                        self._get_chunk_from_diffrent_data_node(artifact_name, chunk_id, new_data_node)))
        ray.get(result_ids)
        
        for index, data_node in enumerate(self.data_nodes):
            if data_node == dead_data_node:
                self.data_nodes[index] = new_data_node
                print(f"data node {index} replaced")

    
    def _is_data_node_alive(self, data_node):
        try:
            result = ray.get(data_node.is_alive.remote())
            return result
        except ray.exceptions.RayActorError:
            return False
        
    def _get_chunk_from_diffrent_data_node(self, artifact_name, chunk_id, new_data_node):
        data_nodes_with_chunk = self.chunk_locations[artifact_name][chunk_id]
        alive_data_nodes_with_chunk = [data_node for data_node in data_nodes_with_chunk if data_node!=new_data_node]
        

        result_ids = [data_node.get_chunk.remote(artifact_name, chunk_id) for data_node in alive_data_nodes_with_chunk]
        ready_id, remaining_ids = ray.wait(result_ids, num_returns=1)
        
        return ray.get(ready_id[0])
    
    def _divide_text_into_chunks(self, text):
        chunks = [text[i:i+self.chunk_size] for i in range(0, len(text), self.chunk_size)]
        return chunks

    
    def _add_chunk_in_dict(self, artifact_name, chunk_number, data_node):
        artifact_dict = self.chunk_locations[artifact_name]

        if chunk_number in artifact_dict:
            artifact_dict[chunk_number].append(data_node)
        else:
            artifact_dict[chunk_number] = [data_node]


       
    