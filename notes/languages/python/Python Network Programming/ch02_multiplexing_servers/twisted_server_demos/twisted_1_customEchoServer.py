# !/user/bin/env python         # use on Ubuntu, or something like this


# pip3 install twisted 
# https://twistedmatrix.com/trac
# Twisted is an event-driven networking engine 
# it has implementations for epoll, kqueue, & many other types of event handling mechanisms 
# documentation: https://twistedmatrix.com/trac/wiki/Documentation
# API reference: https://twistedmatrix.com/documents/current/api/

# Twisted makes it easy to implement custom network applications. 
# Here's a TCP server that echoes back everything that's written to it.

from twisted.internet import protocol, reactor, endpoints

class Echo(protocol.Protocol):
    def dataReceived(self, data):
        self.transport.write(data)

class EchoFactory(protocol.Factory):
    def buildProtocol(self, addr):
        return Echo()

endpoints.serverFromString(reactor, "tcp:1234").listen(EchoFactory())
reactor.run()

# more info on writing twisted servers: https://twistedmatrix.com/documents/current/core/howto/servers.html
# more info on writing twisted clients: https://twistedmatrix.com/documents/current/core/howto/clients.html
# more info on twisted core networking libraries: https://twistedmatrix.com/documents/current/core/howto/index.html