from webiopi.protocols.coap import *
from time import sleep

client = COAPClient()
client.sendRequest(COAPPost("coap://192.168.1.7:5683/GPIO/17/function/out"))
state = True

for x in range(0, 5): 
    response = client.sendRequest(COAPPost("coap://192.168.1.3:5683/GPIO/17/value/%d" % state))
    if response:
        print("Received response:\n%s" % response)
        state = not state
    else:
        print("No response received")
    sleep(1.0)

# See https://docs.python.org/3/tutorial/modules.html
# See video: https://intellipaat.com/blog/tutorial/python-tutorial/
# http://192.168.1.3:8000/app/gpio-list