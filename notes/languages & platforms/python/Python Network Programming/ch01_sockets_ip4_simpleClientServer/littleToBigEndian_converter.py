# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import sys

#little to big endian conversion is also called host to network conversion
def hostToNetwork_converter(data, size = "32bit"):
    if data == None or (size != "32bit" and size != "16bit"):
        return "invalid input"
    else:
        try:
            numericalData = int(data)
            if size == "32bit":                             # htonl() converts a 32 bit positive integer from little to big endian
                return socket.htonl(numericalData)          # htons() does the same for a 16 bit positive number
            elif size == "16bit":                           # if the host order and the network order are same nothing changes 
                return socket.htons(numericalData)          
        except:                                             # these functions both will raise an OverflowError if a negative value is passed
            return "invalid input"

if __name__ == "__main__":
    data = sys.argv[1] if len(sys.argv) > 1 else None
    size = sys.argv[2] if len(sys.argv) > 2 else "32bit"

    print(hostToNetwork_converter(data, size))