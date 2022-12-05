# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys

#big to little endian conversion (i.e. network to host)
#if network & host byte order are different (not always the case) then an input of 1 will output in hex: 01 00 00 00 i.e. 16777216
def networkToHost_converter(data, size = "32bit"):
    if data == None or (size != "32bit" and size != "16bit"):
        return "invalid input"
    else:
        try:
            numericalData = int(data)
            if size == "32bit":                             # htonl() converts a 32 bit positive integer from little to big endian
                return socket.ntohl(numericalData)          # htons() does the same for a 16 bit positive number
            elif size == "16bit":                           # if the host order and the network order are same nothing changes 
                return socket.ntohs(numericalData)
        except:                                             # these functions both will raise an OverflowError if a negative value is passed
            return "invalid input"

if __name__ == "__main__":
    data = sys.argv[1] if len(sys.argv) > 1 else None
    size = sys.argv[2] if len(sys.argv) > 2 else "32bit"

    print(networkToHost_converter(data, size))