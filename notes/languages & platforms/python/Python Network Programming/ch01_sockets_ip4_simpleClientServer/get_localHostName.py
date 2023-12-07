# !/user/bin/env python         # use on Ubuntu, or something like this

import socket

def get_localHostName():         
    return socket.gethostname()             # you assigned this hostname to your computer when your OS was configured (it isn't unique)

if __name__ == '__main__':
    print(get_localHostName())