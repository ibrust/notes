# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import select
import sys
import signal 
import utility_functions

HOST = "localhost"

# select enables the server to handle client requests without creating a new thread or process for each client 
# the following chat server demos this functionality 

# select.select(rlist, wlist, xlist[, timeout])
# the first three arguments to select.select are sequences of 'waitable objects': either integers representing file descriptors 
# or objects with a parameterless method named fileno() returning such an integer
# rlist: wait until ready for reading
# wlist: wait until ready for writing
# xlist: wait for an “exceptional condition”
# The return value is a triple of lists of objects that are ready: subsets of the first three arguments.

class ChatServer(object):

    def __init__(self, port, maxQueuedConnections = 5):
        self.total_clients = 0
        self.clientAddresses = {}
        self.clientLoginNames = {}
        self.output_sockets = []
        
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)
        self.server_socket.bind((HOST, int(port)))
        self.server_socket.listen(maxQueuedConnections)

        signal.signal(signal.SIGINT, self.sighandler)

    def sighandler(self, signum, frame):
        print("server closing all sockets")
        for s in output_sockets:
            s.close()
        self.server_socket.close()

    def get_client_name(self, client):
        host = self.clientAddresses[client][0]
        loginName = self.clientLoginNames[client]
        
        return "@".join((loginName, host))

    def run(self):
        input_file_descriptors = [self.server_socket, sys.stdin]
        self.output_sockets = []
        
        print("running server")
        running = True 
        while running:
            try:
                selected_input_FDs, selected_output_sockets, _ = select.select(input_file_descriptors, self.output_sockets, [])
            except select.error as e: 
                print("select error: ", e)
                break

            for input_fd in selected_input_FDs:
                if input_fd == self.server_socket:          # handle requests to servers socket; this sets up connections between clients 
                    client_socket_connection, client_address = self.server_socket.accept()

                    client_login_name = utility_functions.receive(client_socket_connection).split("NAME: ")[1]
                    utility_functions.send(client_socket_connection, "CLIENT: " + str(client_address[0]))

                    input_file_descriptors.append(client_socket_connection)
                    self.clientAddresses[client_socket_connection] = client_address 
                    self.clientLoginNames[client_socket_connection] = client_login_name

                    self.total_clients += 1
                    message = "\n(Connected: new client %d from %s)" % (self.total_clients, self.get_client_name)
                    for s in self.output_sockets:
                        utility_functions.send(s, message)
                    self.output_sockets.append(client_socket_connection)

                elif input_fd == sys.stdin:                 # handle command line input, which kills server 
                    _ = sys.stdin.readline()
                    running = False  
                else:                                       # handle all client sockets 
                    try:
                        received_data = utility_functions.receive(input_fd)
                        if received_data:
                            message = "\n#[" + self.get_client_name(input_fd) + "]>>" + received_data
                            for output_fd in self.output_sockets:
                                if output_fd != input_fd:               # handles communication between clients
                                    utility_functions.send(output_fd, message) 
                        else:
                            print("chat server %d ending connection with client" % input_fd.fileno())

                            self.total_clients -= 1
                            input_fd.close()
                            input_file_descriptors.remove(input_fd)
                            self.output_sockets.remove(input_fd)

                            message = "\n(closed connection with client: %s" % self.get_client_name(input_fd)
                            for output_fd in self.output_sockets:
                                utility_functions.send(output_fd, message)
                    except socket.error as e:
                        print("socket error, terminating connection with client %s: " % self.get_client_name(input_fd), e)
                        input_file_descriptors.remove(input_fd)
                        self.output_sockets.remove(input_fd)

        self.server_socket.close()