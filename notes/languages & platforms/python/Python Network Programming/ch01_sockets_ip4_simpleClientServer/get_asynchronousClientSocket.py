# !/user/bin/env python         # use on Ubuntu, or something like this

import socket

def get_asynchronousClientSocket(ip, port):
    s = socket.socket(AF_INET, socket.SOCK_STREAM)
    s.setblocking(false)
    s.connect((ip, port))

    return s