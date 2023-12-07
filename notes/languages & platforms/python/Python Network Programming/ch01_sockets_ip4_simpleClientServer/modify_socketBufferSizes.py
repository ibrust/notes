# !/user/bin/env python         # use on Ubuntu, or something like this

import socket

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# change send buffers size 
initialSendBufferSize = s.getsockopt(socket.SOL_SOCKET, socket.SO_SNDBUF)
s.setsockopt(socket.SOL_TCP, socket.TCP_NODELAY, 1)
s.setsockopt(socket.SOL_SOCKET, socket.SO_SNDBUF, 4096)
modifiedSendBufferSize = s.getsockopt(socket.SOL_SOCKET, socket.SO_SNDBUF)

print("initial sendbuffer size: ", initialSendBufferSize)
print("modified sendbuffer size: ", modifiedSendBufferSize)

# change receive buffers size 
initialReceiveBufferSize = s.getsockopt(socket.SOL_SOCKET, socket.SO_RCVBUF)
s.setsockopt(socket.SOL_TCP, socket.TCP_NODELAY, 1)
s.setsockopt(socket.SOL_SOCKET, socket.SO_RCVBUF, 4096)
modifiedReceiveBufferSize = s.getsockopt(socket.SOL_SOCKET, socket.SO_RCVBUF)

print("modified receivebuffer size: ", modifiedReceiveBufferSize)
print("initial receivebuffer size: ", initialReceiveBufferSize)

#---------------------------------------------------------------------------------------

# socket.setsockopt(level, optname, value: int)
# socket.setsockopt(level, optname, value: buffer)
# socket.setsockopt(level, optname, None, optlen: int)
# Sets the value of the given socket option (see the Unix manual page setsockopt(2)).
# The value can be an integer, None or a bytes-like object representing a buffer. 
# In the later case it is up to the caller to ensure that the bytestring contains the proper bits 
# (see the optional built-in module struct for a way to encode C structures as bytestrings). 
# When value is set to None, optlen argument is required. It’s equivalent to the setsockopt() C function with optval=NULL and optlen=optlen.
