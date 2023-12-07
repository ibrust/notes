# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys 
import argparse 

host = "localhost"


def TCP_echo_client(message, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((host, port))

    try:
        s.sendall(message.encode("utf-8"))

        total_received = 0 
        total_expected = len(message)               # expected & received will be equal sizes because the string was converted to utf-8 

        while total_received < total_expected:
            bytes_received = s.recv(16)
            total_received += len(bytes_received)   # not sure why len is necessary here 
        print("final total bytes received:", total_received)
    except socket.error as e:
        print("socket error: ", str(e))
    except Exception as e: 
        print("other exception: ", str(e))
    finally: 
        print("closing socket connection")
        s.close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="echo client")
    parser.add_argument("--port", action="store", dest="port", type=int, required=True)
    parser.add_argument("--message", action="store", dest="message", required=True)

    args = parser.parse_args()

    port = args.port 
    message = args.message

    TCP_echo_client(message,  port)