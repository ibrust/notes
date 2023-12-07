# !/user/bin/env python         # use on Ubuntu, or something like this

import socket

def get_localIPv4Address():
    local_host_name = socket.gethostname()
    ip_address = socket.gethostbyname(local_host_name)
    return ip_address

if __name__ == '__main__':                          # if you run from the command line, __name__ will be __main__
    print(get_localIPv4Address())                   # otherwise if you import the module this won't run, and you can call this function manually

