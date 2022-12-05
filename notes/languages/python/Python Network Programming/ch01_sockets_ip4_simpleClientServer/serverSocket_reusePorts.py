# !/user/bin/env python         # use on Ubuntu, or something like this

import socket

# if you open a server socket on a specific port, tear it down, & try to reopen it... this will throw an error 
# unless you setsockopt with socket.SO_REUSEADDR, which allows you to reuse the old port

port = 8282 
reusableSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
reusableSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
reusableSocket.bind(("", port))         # bind to localhost (I think)
reusableSocket.listen() 

while(true):
    try:
        connection, address = reusableSocket.accept()
        print("connected to localhost")
    except socket.error as errorMessage:
        print("error: ", errorMessage)
