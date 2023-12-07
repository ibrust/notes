# !/user/bin/env python         # use on Ubuntu, or something like this


# pip3 install twisted 
# https://twistedmatrix.com/trac
# Twisted is an event-driven networking engine 
# it has implementations for epoll, kqueue, & many other types of event handling mechanisms 
# documentation: https://twistedmatrix.com/trac/wiki/Documentation
# API reference: https://twistedmatrix.com/documents/current/api/

# Here's a simple publish/subscribe server, where clients see all messages posted by other clients:
# You can test this out by opening two terminals and doing telnet localhost 1025 in each, then typing things.

from twisted.internet import reactor, protocol, endpoints
from twisted.protocols import basic

class PubProtocol(basic.LineReceiver):
    def __init__(self, factory):
        self.factory = factory

    def connectionMade(self):
        self.factory.clients.add(self)

    def connectionLost(self, reason):
        self.factory.clients.remove(self)

    def lineReceived(self, line):
        for c in self.factory.clients:
            source = u"<{}> ".format(self.transport.getHost()).encode("ascii")
            c.sendLine(source + line)

class PubFactory(protocol.Factory):
    def __init__(self):
        self.clients = set()

    def buildProtocol(self, addr):
        return PubProtocol(self)

endpoints.serverFromString(reactor, "tcp:1025").listen(PubFactory())
reactor.run()

