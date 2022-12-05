# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys 
import argparse 

host = "localhost"
buffer_size = 2048

def UDP_echo_server(port):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)                # no need to make UDP reusable?

    s.bind((host, port))

    while True:
        data, address = s.recvfrom(buffer_size)
        if data:
            bytes_received = s.sendto(data, address)
            print("sent", bytes_received, "bytes back to", address)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="TCP echo server")
    parser.add_argument("--port", action="store", dest="port", type=int, required=True)
    args = parser.parse_args()
    port = args.port 

    UDP_echo_server(port)