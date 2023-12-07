# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys

# translate a port number into a well known service name
def get_wellKnownServiceName(ports = [80, 25, 53]):
    services = []
    for port in ports:
        try:
            service = socket.getservbyport(port)
            services.append((port, service))
        except socket.error as errorMessage: 
            services.append(errorMessage)
    return services

#specify ports on the command line as a single string: "23, 46, 53", etc.
if __name__ == "__main__":
    ports = list(map(int, sys.argv[1].split())) if len(sys.argv) > 1 else [80, 25, 53]
    services = get_wellKnownServiceName(ports)
    for service in services:
        print("port ", service[0], ": ", service[1], sep="")