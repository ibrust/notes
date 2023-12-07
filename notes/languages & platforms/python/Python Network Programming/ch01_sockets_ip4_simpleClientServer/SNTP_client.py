# !/user/bin/env python         # use on Ubuntu, or something like this

import socket
import struct 
import sys 
import time 

NTP_SERVER = "0.uk.pool.ntp.org"
TIME1970 = 2208988800

def fetch_sntp_time(): 
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)                    # create UDP socket 
    data = '\x1b' + 47 * '\0'                                               # formatted message for NTP server 

    s.sendto(data.encode('utf-8'), (NTP_SERVER, 123))
    data, address = s.recvfrom(1024)
    t = struct.unpack("!12I", data)[10]
    t -= TIME1970                                                   
    print("Time: ", time.ctime(t))

if __name__ == "__main__":
    fetch_sntp_time()

# with this method & the SNTP protocol you can get the time without use of a 3rd party library

# struct performs conversions between Python values and C structs represented as Python bytes objects
# it is used in handling binary data from files or sockets 
# it uses format strings as compact descriptors of the layout of C structs and the intended conversion to python 