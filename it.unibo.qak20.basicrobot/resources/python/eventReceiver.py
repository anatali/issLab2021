####
# eventReceiver.py
####
import time
import paho.mqtt.client as paho

brokerAddr="localhost"

def on_message(client, userdata, message) :   #define callback
    evMsg = str( message.payload.decode("utf-8")  )
    print("evMsg=", evMsg  )
    
client= paho.Client("receiver")      
client.on_message=on_message            # Bind function to callback

client.connect(brokerAddr)              #connect
print("connected to broker ", brokerAddr)
print("subscribing to unibo/qak/events")
client.subscribe("unibo/qak/events")      #subscribe
    
print("collecting values; please wait ..." )
client.loop_start()             #start loop to process received messages
time.sleep(30)
client.disconnect()             #disconnect
print("bye")
client.loop_stop()              #stop loop        