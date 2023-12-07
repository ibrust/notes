# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import select
import sys
import signal 
import utility_functions

HOST = "localhost"

class ChatClient(object):

    def __init__(self, name, port):
        self.name = name 
        self.connected = False
        self.port = port 
        self.prompt = ""

        try: 
            self.client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.client_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)
            self.client_socket.connect((HOST, int(self.port)))

            utility_functions.send(self.client_socket, "NAME: " + self.name)
            message = utility_functions.receive(self.client_socket)
            received_address = message.split("CLIENT: ")[1]

            self.prompt = "[" + "@".join((self.name, received_address)) + "]> "

            self.connected = True
            print("connected to chat server")
        except socket.error as e:
            print("failed to connect to server")
            sys.exit(1)
    
    def run(self):
        print("running client")
        while self.connected:
            try:
                sys.stdout.write(self.prompt)
                sys.stdout.flush()

                selected_input_FDs, _, _ = select.select([0, self.client_socket], [], [])           # looks like 0 is stdin

                for input_FD in selected_input_FDs:                             

                    if input_FD == 0:                                           # reads in the users chat message & sends it to other clients 
                        user_input_message = sys.stdin.readline().strip()
                        if user_input_message: 
                            utility_functions.send(self.client_socket, user_input_message)

                    elif input_FD == self.client_socket:                        # receive message from other clients 
                        received_message = utility_functions.receive(self.client_socket)
                        if not received_message:
                            print("client shutting down...")
                            self.connected = False 
                            break 
                        else:
                            sys.stdout.write(received_message + "\n")
                            sys.stdout.flush()
            except KeyboardInterrupt:
                print("client interrupted, closing connection")
                self.client_socket.close()
            




