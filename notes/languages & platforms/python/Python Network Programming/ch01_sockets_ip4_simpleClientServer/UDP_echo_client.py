# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys 
import argparse 

host = "localhost"
buffer_size = 2048

def UDP_echo_client(message, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    try:
        sent = s.sendto(message.encode("utf-8"), (host, port))

        data, server = s.recvfrom(buffer_size)
        print("received data: ", data)
    finally:
        print("closing socket")
        s.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="echo client")
    parser.add_argument("--port", action="store", dest="port", type=int, required=True)
    parser.add_argument("--message", action="store", dest="message", required=True)

    args = parser.parse_args()

    port = args.port 
    message = args.message

    UDP_echo_client(message,  port)