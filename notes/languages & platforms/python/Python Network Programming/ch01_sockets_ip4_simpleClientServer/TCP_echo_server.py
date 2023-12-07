# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys 
import argparse 

host = "localhost"
buffer_size = 2048
max_queued_connections = 5 

def TCP_echo_server(port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)

    s.bind((host, port))
    s.listen(max_queued_connections)

    while True:
        connection, address = s.accept()
        message = connection.recv(buffer_size)
        if bytes_received:
            connection.send(message)
            print("message received: ", message)
        connection.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="TCP echo server")
    parser.add_argument("--port", action="store", dest="port", type=int, required=True)
    args = parser.parse_args()
    port = args.port 

    TCP_echo_server(port)