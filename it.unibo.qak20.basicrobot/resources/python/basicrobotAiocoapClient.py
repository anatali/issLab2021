import logging
import asyncio

from aiocoap import *

logging.basicConfig(level=logging.INFO)

async def moveRobot( move, context ):
    payload  = move.encode()  
    request  = Message(code=PUT, payload=payload, uri="coap://localhost/robot/basicrobot")
    response = await context.request(request).response
    print('Result: %s\n%r'%(response.code, response.payload))
    
async def console(context) :  
    print("console  STARTS :"   )
    cmd =  str( input() )
    print("console  cmd= :" , cmd  )   # w,s,a,d,r,l,z,x
    while( cmd != "q"  ) :
        print( cmd )
        await moveRobot( cmd, context )
        cmd = str(input() )

async def main():
    """Perform a single PUT request to localhost on the default port, URI
    "/robot/basicrobot". The request is sent 2 seconds after initialization.
    """

    context = await Context.create_client_context()
    await asyncio.sleep(2)

    payload = b"r" 
    request = Message(code=PUT, payload=payload, uri="coap://localhost:5683/robot/basicrobot")
    response = await context.request(request).response
    print('Result: %s\n%r'%(response.code, response.payload))

    await console(context) 
    
if __name__ == "__main__":
    asyncio.get_event_loop().run_until_complete(main())

    
    
""" 
    while( len(cmd)==1 and cmd != "q"  ) :
        print( cmd )
        req = Message(code=PUT, payload=cmd.encode(), uri="coap://localhost:8018/ctxBasicRobot/basicrobot")
        try:
            response = await context.request(req).response
        except Exception as e:
            print('Failed to fetch resource:')
            print(e)
        else:
            print('Result: %s\n%r'%(response.code, response.payload))        
        cmd = str(input() )
"""         