# !/user/bin/env python         # use on Ubuntu, or something like this


# pip3 install twisted 
# https://twistedmatrix.com/trac
# Twisted is an event-driven networking engine 
# it has implementations for epoll, kqueue, & many other types of event handling mechanisms 
# documentation: https://twistedmatrix.com/trac/wiki/Documentation
# API reference: https://twistedmatrix.com/documents/current/api/

# Twisted includes an SSH client & server, "conch" (i.e.: the Twisted Shell).

import sys, os

from twisted.internet import protocol, defer, endpoints, task
from twisted.conch.endpoints import SSHCommandClientEndpoint

async def main(reactor, username="alice", sshhost="example.com", portno="22"):
    envAgent = endpoints.UNIXClientEndpoint(reactor, os.environ["SSH_AUTH_SOCK"])
    endpoint = SSHCommandClientEndpoint.newConnection(
        reactor, "echo 'hello world'", username, sshhost,
        int(portno), agentEndpoint=envAgent,
    )

    class ShowOutput(protocol.Protocol):
        received = b""
        def dataReceived(self, data):
            self.received += data
        def connectionLost(self, reason):
            finished.callback(self.received)

    finished = defer.Deferred()
    factory = protocol.Factory.forProtocol(ShowOutput)
    await endpoint.connect(factory)
    print("SSH response:", await finished)

task.react(lambda *a, **k: defer.ensureDeferred(main(*a, **k)), sys.argv[1:])

# You can use this client to run "hello world" on any SSH server that your local SSH agent can authenticate to, 
# if you pass your username, host name, and optionally port number on the command line.

