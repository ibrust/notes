# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import select
import sys
import signal
import argparse

# python b strings (binary strings) are a series of octets (bytes). not sure yet whether this is different than utf-8, or how it is 
EOL1 = b"\n\n"
EOL2 = b"\n\r\n"
SERVER_RESPONSE = b"""HTTP/1.1 200 OK\r\n
                      Date: Fri, 19 Nov 2021 23:23:23 EST\r\n
                      Content-Type: text/plain\r\n
                      Content-Length: 25\r\n
                      \r\n
                      Hello from kqueue Server!"""

# there are a variety of OS-specific techniques that achieve what traditional select.select() does but more efficiently
# on Mac and BSD systems use select.kqueue()
# on Linux 2.5+ use select.epoll() 
# on Solaris and derivatives use select.devpoll() 
# there's also select.poll(), a modern update to select.select() that works across OS's but isn't as efficient as epoll, kqueue, or devpoll
# poll() and select() keep the code simpelr than other OS-specific approaches (which are event based)
# for performance, once you reach about a hundred connections, stop using poll() and select() and use the event based options instead 


# this web server should return a readable line of text to any connecting web browser 
class kqueue_webServer(object):

    def __init__(self, host="localhost", port=0):
        self.serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.serverSocket.bind((host, port))
        self.serverSocket.listen(5)
        self.serverSocket.setblocking(False)
        # socket.TCP_NODELAY causes the TCP socket to send data immediately rather than place the data in a buffer & wait to send it 
        self.serverSocket.setsockopt(socket.IPPROTO_TCP, socket.TCP_NODELAY, 1)

        # create the kqueue 
        self.__kqueue__ = select.kqueue()
        # create the events that the kqueue will initially listen for 
        self.serverReadEvent = select.kevent(self.serverSocket)

    def run(self):
        print("listening for kevents")
        eventList = [self.serverReadEvent]
        clientConnections = {}
        clientWriteEvents = {}
        clientReadEvents = {}
        requests = {}               # used to aggregate the request, which may come in in pieces 
        while True:
            kevents = self.__kqueue__.control(eventList, 1000, None)
            for kevent in kevents:
                # ident - value used to identify the event. The interpretation depends on the filter but it’s usually the file descriptor.
                fd = kevent.ident
                
                # handles the initial connection request to the listening socket, saves the new connection & adds an event to handle future client requests
                if kevent.filter == select.KQ_FILTER_READ and fd == self.serverSocket.fileno():
                    clientConnection, _ = self.serverSocket.accept()
                    clientConnection.setblocking(False)

                    clientConnections[clientConnection.fileno()] = clientConnection

                    clientReadEvents[clientConnection.fileno()] = select.kevent(clientConnection)
                    eventList.append(clientReadEvents[clientConnection.fileno()])

                    requests[clientConnection.fileno()] = b""

                # handles requests from the client over the clientConnection 
                elif kevent.filter == select.KQ_FILTER_READ and fd in clientReadEvents.keys() and clientReadEvents[fd] in eventList:
                    requests[fd] += clientConnections[fd].recv(1024)
                    if EOL1 in requests[fd] or EOL2 in requests[fd]:
                        print("\n", requests[fd].decode(), "\n")
                        clientWriteEvents[fd] = select.kevent(clientConnections[fd], filter=select.KQ_FILTER_WRITE)
                        eventList.append(clientWriteEvents[fd])
                        eventList.remove(clientReadEvents[fd])
                        clientConnections[fd].send(SERVER_RESPONSE)
                elif kevent.filter == select.KQ_FILTER_WRITE and fd in clientWriteEvents.keys() and clientWriteEvents[fd] in eventList:
                    eventList.remove(clientWriteEvents[fd])
                    # difference between close() and shutdown(): https://stackoverflow.com/questions/4160347/close-vs-shutdown-socket/23483487#23483487 
                    clientConnections[fd].shutdown(socket.SHUT_RDWR)
                elif kevent.flags & select.KQ_EV_EOF:       # flags EOF, ADD, and SYSFLAGS seem to be all set coming in here
                    clientConnections[fd].close()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="kqueue web server")
    parser.add_argument("--port", action="store", dest="port", type=int, required=True)

    received_args = parser.parse_args()
    port = received_args.port 

    server = kqueue_webServer(port=port)
    server.run()