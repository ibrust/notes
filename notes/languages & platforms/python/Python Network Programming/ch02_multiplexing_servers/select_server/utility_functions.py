# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import pickle 
import struct 

# recv's return value is a bytes object of the data received. 
# its argument indicates the max amount of data to be received at once
# For best match with hardware and network realities its argument should be a fairly small power of 2, like 4096.

def send(_socket_, *args):
    serialized_args = pickle.dumps(args)
    size_inNetworkByteOrder = socket.htonl(len(serialized_args))
    size_networkFormatted = struct.pack("L", size_inNetworkByteOrder)           # "L" converts to C 'unsigned long' format 
    _socket_.send(size_networkFormatted)
    _socket_.send(serialized_args)

def receive(_socket_):
    struct_size_unsignedLong = struct.calcsize("L")
    size_networkFormatted = _socket_.recv(struct_size_unsignedLong)

    size_inPythonFormat = 0
    try:
        size_inPythonFormat = socket.ntohl(struct.unpack("L", size_networkFormatted)[0])
    except struct.error as e:
        print("error unpacking data: ", e)
        return

    message_buffer = ""
    while len(message_buffer) < size_inPythonFormat:
        message_buffer = _socket_.recv(size_inPythonFormat - len(message_buffer))

    return pickle.loads(message_buffer)[0]