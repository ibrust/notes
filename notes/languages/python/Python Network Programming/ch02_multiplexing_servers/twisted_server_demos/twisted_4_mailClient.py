# !/user/bin/env python         # use on Ubuntu, or something like this


# pip3 install twisted 
# https://twistedmatrix.com/trac
# Twisted is an event-driven networking engine 
# it has implementations for epoll, kqueue, & many other types of event handling mechanisms 
# documentation: https://twistedmatrix.com/trac/wiki/Documentation
# API reference: https://twistedmatrix.com/documents/current/api/

# Twisted includes a sophisticated IMAP4 client library.

import sys

from twisted.internet import protocol, defer, endpoints, task
from twisted.mail import imap4
from twisted.python import failure


async def main(
    reactor, username="alice", password="secret", strport="tls:example.com:993"
):
    endpoint = endpoints.clientFromString(reactor, strport)
    factory = protocol.Factory.forProtocol(imap4.IMAP4Client)
    try:
        client = await endpoint.connect(factory)
        await client.login(username.encode("utf-8"),
                           password.encode("utf-8"))
        await client.select("INBOX")
        info = await client.fetchEnvelope(imap4.MessageSet(1))
        print("First message subject:", info[1]["ENVELOPE"][1])
    except:
        print("IMAP4 client interaction failed")
        print(failure.Failure().getTraceback())


task.react(lambda *a, **k: defer.ensureDeferred(main(*a, **k)), sys.argv[1:])