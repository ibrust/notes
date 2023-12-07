# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys

def get_remoteIPv4Address(remote_host_name = "www.python.org"):
    try:
        ip_address = socket.gethostbyname(remote_host_name)     # this performs a DNS lookup
        return ip_address
    except socket.error as errorMessage:
        print("error: ", errorMessage)
        return errorMessage

if __name__ == '__main__':
    remote_host_name = sys.argv[1] if len(sys.argv) > 1 else "www.python.org"
    print(get_remoteIPv4Address(remote_host_name))
