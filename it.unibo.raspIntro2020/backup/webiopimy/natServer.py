from webiopi.protocols import http
from webiopi.protocols import rest

http.HTTPServer("localhost", 8020, rest.RESTHandler(), None, "/home/pi/nat/WebIOPi-0.7.1/htdocs", None)
#self, host, port, handler, context, docroot, index, auth=None, realm=None