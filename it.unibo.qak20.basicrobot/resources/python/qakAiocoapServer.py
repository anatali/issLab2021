import time
import datetime
import logging
import asyncio
import aiocoap.resource as resource
import aiocoap
import serial

ser      = serial.Serial('/dev/ttyUSB0', 115200)
commands = ["h","w","s","r","l","a","d","z","x"]    ##

if ser.isOpen():
	try:
		time.sleep(2)  		#give the Arduino time to reboot
		print( "Arduino serial READY ...  "  )
	except:
		print( "error communicating...  "  )
else:
		print( "cannot open serial port " )


# ----------------------------------------------------------------------------------
class BasicrobotResource(resource.Resource):
    """Example resource which supports the GET and PUT methods.  """

    def __init__(self):
        super().__init__()
        self.set_content(b"basic robot control. \n")

    def set_content(self, content):
        self.content = content
        ser.write( content  )
        
    async def render_get(self, request):
        return aiocoap.Message(payload=self.content)

    async def render_put(self, request):
        payload = request.payload.decode("utf-8")
        # msg(cmd,dispatch,kt,basicrobot,w,N)
        msgArray = payload.split(",")
        dest = msgArray[3] 
        move = msgArray[4]  						## get the move
        print('PUT payload: %s move: %s  dest: %s'   % (payload , move, dest ) )
        if move in commands :
        	self.set_content( move.encode("utf-8") )
        	return aiocoap.Message(code=aiocoap.CHANGED, payload=self.content)
        else :
        	return aiocoap.Message(code=aiocoap.CHANGED, payload="unknwon".encode("utf-8"))


class TimeResource(resource.ObservableResource):
    """Example resource that can be observed. The `notify` method keeps
    scheduling itself, and calles `update_state` to trigger sending
    notifications."""

    def __init__(self):
        super().__init__()

        self.handle = None

    def notify(self):
        self.updated_state()
        self.reschedule()

    def reschedule(self):
        self.handle = asyncio.get_event_loop().call_later(5, self.notify)

    def update_observation_count(self, count):
        if count and self.handle is None:
            print("Starting the clock")
            self.reschedule()
        if count == 0 and self.handle:
            print("Stopping the clock")
            self.handle.cancel()
            self.handle = None

    async def render_get(self, request):
        payload = datetime.datetime.now().\
                strftime("%Y-%m-%d %H:%M").encode('ascii')
        return aiocoap.Message(payload=payload)

# logging setup

logging.basicConfig(level=logging.INFO)
logging.getLogger("coap-server").setLevel(logging.DEBUG)

'''
	path = contextName / actorName  & payload
Example	
	path = ctxRobot / basicrobot  payload=msg(cmd,dispatch,SENDER,basicrobot,MOVE,N)
'''
def main():
    # Resource tree creation
    root = resource.Site()

    root.add_resource(['.well-known', 'core'],
            resource.WKCResource(root.get_resources_as_linkheader))
    root.add_resource(['time'], TimeResource())
    root.add_resource(['ctxRobot', 'basicrobot'], BasicrobotResource())
  
    asyncio.Task(aiocoap.Context.create_server_context(root))
    asyncio.get_event_loop().run_forever()

if __name__ == "__main__":
    main()

"""
Usage: 
	aiocoap-client coap://localhost/.well-known/core
	aiocoap-client coap://localhost/robot/basicrobot

"""