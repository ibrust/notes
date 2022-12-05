# !/user/bin/env python         # use on Ubuntu, or something like this

import argparse
import select_chatClient
import select_chatServer

SERVER_NAME = "server"

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="select chat server")
    parser.add_argument("--name", action="store", dest="name", required=True)
    parser.add_argument("--port", action="store", dest="port", required=True)

    input_args = parser.parse_args()
    port = input_args.port 
    name = input_args.name 

    if name == SERVER_NAME:
        server = select_chatServer.ChatServer(port)
        server.run()
    else:
        client = select_chatClient.ChatClient(name=name, port=port)
        client.run()