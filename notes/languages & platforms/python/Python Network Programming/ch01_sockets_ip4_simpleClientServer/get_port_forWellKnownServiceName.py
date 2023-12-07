# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys

# translate a well known service name into a port number
def get_portNumberForServiceName(serviceName = "http", protocol = "tcp"):
    try:
        port = socket.getservbyname(serviceName, protocol)     # getservbyname() takes a network service name and an underlying protocol name
        return port                                                 # and returns the port number on which the service is defined by /etc/services
    except socket.error as errorMessage:
        return errorMessagee

if __name__ == "__main__":
    serviceName = sys.argv[1] if len(sys.argv) > 1 else "http"
    port = get_portNumberForServiceName(serviceName)
    print("port for ", serviceName, " is: ", port, sep="")