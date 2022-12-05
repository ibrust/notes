# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import os 
import threading
import socketserver 

HOST = "localhost"
PORT = 0                # 0 tells the OS to pick a port dynamically 
BUF_SIZE = 2048
ECHO_MSG = "COO, COO!"

# socketserver module comes with python
# read more about these classes to figure out what they do if you ever need them 
# https://docs.python.org/3/library/socketserver.html

class ForkedClient():
    def __init__(self, ip, port):
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.connect((ip, port))

    def run(self):
        bytes_sent = self.s.send(bytes(ECHO_MSG, 'utf-8'))
        print("client pid", os.getpid(), "sent:", bytes_sent)

        response = self.s.recv(BUF_SIZE)
        print("client pid", os.getpid(), "received:", response)

    def shutdown(self):
        print("client pid", os.getpid(), "closing socket")
        self.s.close()


class ForkingServerRequestHandler(socketserver.BaseRequestHandler):
    def handle(self):
        received_message = str(self.request.recv(BUF_SIZE),  "utf-8")

        self.request.send(bytes(received_message, "utf-8"))
        print("server pid", os.getpid(), "echoed:", received_message)

class ForkingServer(socketserver.ForkingMixIn, socketserver.TCPServer):
    pass            #nothing needs adding here, everything is implemented in the parent classes


def main():
    server = ForkingServer((HOST, PORT), ForkingServerRequestHandler)
    ip, port = server.server_address            # retrieve the dynamic port number assigned w/ 0 

    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.setDaemon(True)               # don't hang on exit
    server_thread.start()
    print("server pid:", os.getpid())

    client1 = ForkedClient(ip, port)
    client1.run()

    client2 = ForkedClient(ip, port)
    client2.run()

    server.shutdown()
    client1.shutdown()
    client2.shutdown()
    server.socket.close()

if __name__ == "__main__":
    main()
