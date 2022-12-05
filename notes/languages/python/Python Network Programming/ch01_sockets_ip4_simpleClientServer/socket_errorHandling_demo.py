# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys
import argparse

# example runs: 
# python3 socket_errorHandling_demo.py --host=www.pytgo.org --port=8080 --file=1_7_python.py
# python3 socket_errorHandling_demo.py --host=www.python.org --port=80 --file=1_7_python.py

def main():
    parser = argparse.ArgumentParser(description = "socket error handling demo")

    parser.add_argument("--host", action="store", dest="host", required=False)
    parser.add_argument("--port", action="store", dest="port", required=False)
    parser.add_argument("--file", action="store", dest="file", required=False)

    arguments = parser.parse_args()
    host = arguments.host
    port = int(arguments.port)
    filename = arguments.file

    # setup socket
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    except socket.error as e:
        print("error creating socket: ", e)
        exit(1)

    # connect to server 
    try:
        s.connect((host, port))
    except socket.gaierror as e:
        print("addressing error, cannot setup connection: ", e)
        exit(1)
    except socket.error as e:
        print("connection error: ", e)
        exit(1)

    # send data to server
    try:
        msg = "GET " + filename + " HTTP/1.0\r\n\r\n"
        s.sendall(msg.encode("utf-8"))
    except socket.error as e:
        print("error sending message: ", e)

    # wait for response
    while 1:
        try:
            buf = s.recv(2048)
        except socket.error as e:
            print("error receiviinig data: ", e)
            exit(1)
        if not len(buf):
            break
        sys.stdout.write(buf.decode("utf-8"))

if __name__ == "__main__":
    main()