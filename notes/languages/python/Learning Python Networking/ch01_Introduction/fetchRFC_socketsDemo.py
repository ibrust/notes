import sys, socket 

# requests is generally considered simpler than urllib or sockets

def getRFCText(rfcNumber):
    host = 'www.rfc-editor.org'
    port = 80 
    __socket = socket.create_connection((host, port))
    requestString = ('GET /rfc/rfc{rfcNumber}.txt HTTP/1.1\r\n'
                     'Host: {host}:{port}\r\n'
                     'User-Agent: Python {version}\r\n'
                     'Connection: close\r\n'
                     '\r\n')
    requestString.format(rfcNumber=rfcNumber, host=host, port=port, version=sys.version_info[0])

    __socket.sendall(requestString.encode('ascii'))

    receivedBytes = bytearray() 
    while (True): 
        buf = __socket.recv(4096) 
        if not len(buf): 
            break 
        receivedBytes += buf 

    return receivedBytes.decode('utf-8')


if __name__ == "__main__":
    try: 
        print(getRFCText(int(sys.argv[1])))
    except (IndexError, ValueError):
        print("supply RFC number as command line argument") 
        sys.exit(2)
