# !/user/bin/env python         # use on Ubuntu, or something like this

import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print("default socket timeout: ", s.gettimeout())           # a timeout value of None means timeouts are disabled
s.settimeout(100)
print("new socket timeout: ", s.gettimeout())