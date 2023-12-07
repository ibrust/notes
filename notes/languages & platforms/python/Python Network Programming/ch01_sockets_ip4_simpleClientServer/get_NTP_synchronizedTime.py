# !/user/bin/env python         # use on Ubuntu, or something like this

import ntplib
from datetime import datetime

# RFC for network protocol time: https://www.rfc-editor.org/rfc/rfc5905
# wikipedia article: https://en.wikipedia.org/wiki/Network_Time_Protocol

# install ntplib with <pip install ntplib>
# ntplib is the python package for using network time protocol
# this is used for getting accurate time (not sure how accurate) 
# with it you can apparently synchronize your machine to an accurate time 
# though NTP can be accurate, apparently ntplib is not as accurate, so don't use it when complete accuracy is necessary
# "don't use ntplib, because it doesn't parse root_delay correctly
#  See tools.ietf.org/html/rfc5905 "NTP short format" which is incoherent with github.com/Tipoca/ntplib/blob/master/ntplib.py#L185"

def print_synchronized_time():
    ntp_client = ntplib.NTPClient()
    response = ntp_client.request("pool.ntp.org")

    orig_time = datetime.fromtimestamp(response.orig_time)              #time leaving client (clients timestamp)
    recv_time = datetime.fromtimestamp(response.recv_time)              #time arriving at server (servers timestamp)
    tx_time = datetime.fromtimestamp(response.tx_time)                  #time leaving server (servers timestamp)
    dest_time = datetime.fromtimestamp(response.dest_time)              #time returning to client (clients timestamp)
    delay = datetime.fromtimestamp(response.delay)                      #round trip delay: recv_time - orig_time + dest_time - tx_time
    offset = datetime.fromtimestamp(response.offset)                    #offset between the two clocks: (recv_time - orig_time + tx_time - dest_time) / 2
    adjusted_tx_time = datetime.fromtimestamp(response.tx_time + response.delay/2)      # these two adjusted times end up being exactly in sync
    adjusted_dest_time = datetime.fromtimestamp(response.dest_time + response.offset)

    print(orig_time.strftime("orig_time: %a %b %d %H:%M:%S.%f"))
    print(recv_time.strftime("recv_time: %a %b %d %H:%M:%S.%f"))
    print(tx_time.strftime("tx_time: %a %b %d %H:%M:%S.%f"))
    print(dest_time.strftime("dest_time: %a %b %d %H:%M:%S.%f"))
    print(adjusted_tx_time.strftime("adjusted_tx_time: %a %b %d %H:%M:%S.%f"))
    print(adjusted_dest_time.strftime("adjusted_dest_time: %a %b %d %H:%M:%S.%f"))

    # try syncing up a client & server app with this code & test this out. 
    # it's capable of accuracy down to the microsecond, this could be very powerful
    # it would be especially useful if you could get the OS's clock to sync accurately.

if __name__ == "__main__":
    print_synchronized_time()

