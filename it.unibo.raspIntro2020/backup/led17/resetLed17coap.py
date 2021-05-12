# -------------------------------------------------------------------------------
# resetLed17coap.py
# -------------------------------------------------------------------------------

from webiopi.protocols.coap import *
addr   = "192.168.1.4"
client = COAPClient()
client.sendRequest( COAPPost("coap://192.168.1.4/GPIO/17/function/out ") )
response = client.sendRequest( COAPPost("coap://192.168.1.4/GPIO/17/value/0 "))
print("response="+str(response))