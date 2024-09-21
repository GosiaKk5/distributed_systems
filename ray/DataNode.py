import ray
import os


@ray.remote
class DataNode:
    def __init__(self):
        self.chunks = {}

    def get_pid(self):
        # Return the current process ID
        return os.getpid()

    def add_chunk(self, artifact_name, chunk_number, chunk_text):
        self.chunks[(artifact_name, chunk_number)] = chunk_text
    
    def remove_chunk(self, artifact_name, chunk_number):
        del self.chunks[(artifact_name, chunk_number)]

    def get_chunk(self, artifact_name, chunk_number):
        return self.chunks[(artifact_name, chunk_number)]
    
    def list_data_node(self):
        listed_data_node = ""
        for chunk_id, text in self.chunks.items():
            listed_data_node += chunk_id[0] + " " + str(chunk_id[1]) + " " + text + "\n"

        return listed_data_node
    
    def is_alive(self):
        return True
