# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import os 
import threading
import socketserver 

HOST = "localhost"
PORT = 0                # 0 tells the OS to pick a port dynamically 
BUF_SIZE = 2048

# socketserver module comes with python
# read more about these classes to figure out what they do if you ever need them 
# https://docs.python.org/3/library/socketserver.html

# remember that threads in python aren't true threads due to the global interpreter lock
# maybe this class could somehow be useful for adding asynchrony to a server, used in combination with forks to maximize efficiency & CPU utilization?
# I'm not sure how many processes you should fork to maximize CPU utilization. You don't want to cause swapping of processes. 

def call_client(ip,  port, message):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))
    try:
        s.sendall(bytes(message, "utf-8"))
        response = s.recv(BUF_SIZE)
        print("client received:", response)
    finally:
        s.close()


class PseudoThreadingServerRequestHandler(socketserver.BaseRequestHandler):
    def handle(self):
        received_message = self.request.recv(BUF_SIZE)
        cur_thread = threading.current_thread()
        response = "%s: %s" % (cur_thread.name, received_message)
        self.request.sendall(bytes(response, "utf-8"))

class PseudoThreadingServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    pass            #nothing needs adding here, everything is implemented in the parent classes


def main():
    server = PseudoThreadingServer((HOST, PORT), PseudoThreadingServerRequestHandler)
    ip, port = server.server_address            # retrieve the dynamic port number assigned w/ 0 

    server_thread = threading.Thread(target=server.serve_forever)
    server_thread.daemon = True 
    server_thread.start()
    print("server loop running on thread:", server_thread.name)

    call_client(ip, port, "CAW, CAW!")
    call_client(ip, port, "BAAAH, BAAAH!")
    call_client(ip, port, "MOOO, MOOO!")

    server.shutdown()

if __name__ == "__main__":
    main()
