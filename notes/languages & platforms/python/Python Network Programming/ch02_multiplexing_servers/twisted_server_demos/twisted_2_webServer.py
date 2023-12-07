# !/user/bin/env python         # use on Ubuntu, or something like this


# pip3 install twisted 
# https://twistedmatrix.com/trac
# Twisted is an event-driven networking engine 
# it has implementations for epoll, kqueue, & many other types of event handling mechanisms 
# documentation: https://twistedmatrix.com/trac/wiki/Documentation
# API reference: https://twistedmatrix.com/documents/current/api/

# Twisted includes an event-driven web server. 
# Here's a sample web application; notice how the resource object persists in memory, rather than being recreated on each request:

from twisted.web import server, resource
from twisted.internet import reactor, endpoints

class Counter(resource.Resource):
    isLeaf = True
    numberRequests = 0

    def render_GET(self, request):
        self.numberRequests += 1
        request.setHeader(b"content-type", b"text/plain")
        content = u"I am request #{}\n".format(self.numberRequests)
        return content.encode("ascii")

endpoints.serverFromString(reactor, "tcp:8080").listen(server.Site(Counter()))
reactor.run()

# info on developing web apps with twisted: https://twistedmatrix.com/documents/current/web/howto/web-in-60/index.html
# info on twisted's templates: https://twistedmatrix.com/documents/current/web/howto/twisted-templates.html
# info on twisted's HTTP client: https://twistedmatrix.com/documents/current/web/howto/client.html
