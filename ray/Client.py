import ray
from NameNode import NameNode


COMMAND_INSTRUCTION = "Please enter command: add [artifact_name] [artifact_text] or reamove/get [artifact_name] or list [data_node_number]"



def handle_command(command, name_node):
    command_parts = command.split()

    if len(command_parts) < 1:
        return COMMAND_INSTRUCTION
    
    action = command_parts[0]
    arguments = command_parts[1:]

    if action == "add":
        if len(arguments) < 2:
            return COMMAND_INSTRUCTION
        artifact_text = " ".join(arguments[1:])
        return ray.get(name_node.add_artifact.remote(arguments[0], artifact_text))
    
    elif action == "update":
        if len(arguments) < 2:
            return COMMAND_INSTRUCTION
        artifact_text = " ".join(arguments[1:])
        return ray.get(name_node.update_artifact.remote(arguments[0], artifact_text))
        
    elif action == "remove":
        if len(arguments) != 1:
            return COMMAND_INSTRUCTION
        return ray.get(name_node.remove_artifact.remote(arguments[0]))
        
    
    elif action == "get":
        if len(arguments) != 1:
            return COMMAND_INSTRUCTION
        return ray.get(name_node.get_artifact.remote(arguments[0]))
        
    elif action == "list":
        if len(arguments) != 1:
            return COMMAND_INSTRUCTION 
        return ray.get(name_node.list_data_node.remote(int(arguments[0])))
    
    elif action == "kill":
        if len(arguments) != 1:
            return COMMAND_INSTRUCTION 
        return ray.get(name_node.kill_data_node.remote(int(arguments[0])))
    
    elif action == "alive":
        return ray.get(name_node._make_sure_data_nodes_are_alive.remote())
    
    elif action == "check":
        return ray.get(name_node.get_data_nodes_pid.remote())
    
    else:
        return COMMAND_INSTRUCTION
        



if __name__ == "__main__":

    if ray.is_initialized:
        ray.shutdown()
    ray.init(ignore_reinit_error=True)

    no_data_nodes = 5
    chunk_size = 10
    no_data_nodes_per_chunk = 3

    print("Hello! " + COMMAND_INSTRUCTION)
    
    name_node = NameNode.remote(no_data_nodes, chunk_size, no_data_nodes_per_chunk)

    while True:
        command = input("> ")

        if command == "exit":
            ray.shutdown()
            break
        
        message = handle_command(command, name_node)
        print(message)
    
    ray.shutdown()

        