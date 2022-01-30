#import paho.mqtt.testclient as mqtt
import testclient as mqtt
import time
broker="192.168.1.184"
#broker="broker.hivemq.com"
#broker="iot.eclipse.org"
port =1883
keepalive=600

def on_log(client, userdata, level, buf):
   print(buf) 
def on_connect(client, userdata, flags, rc):
    if rc==0:
        client.connected_flag=True #set flag
        print("connected OK")
    else:
        print("Bad connection Returned code=",rc)
        client.loop_stop()  
def on_disconnect(client, userdata, rc):
   print("client disconnected ok",rc)
   if rc!=0:
      client.reconnect()

def on_publish(client, userdata, mid):
    print("In on_pub callback mid= "  ,mid)
count=0
mqtt.Client.connected_flag=False#create flag in class
mqtt.Client.suppress_puback_flag=False
mqtt.Client.suppress_pubrel_flag=False
client = mqtt.Client("python1")             #create new instance 
client.on_log=on_log
client.on_connect = on_connect
client.on_disconnect = on_disconnect
client.on_publish = on_publish
client.connect(broker,port,keepalive)           #establish connection
#client.loop_start()
while not client.connected_flag: #wait in loop
   client.loop()
   print("In wait loop")
   time.sleep(1)
print("out messages ",client._out_messages)
print("\npublishing qos of 2 - Test message 1")
client.loop()
ret=client.publish("hub01/sensor1","Test message 1",2)    #publish
#print("published return=",ret)
print("outgoing messages queued ",len(client._out_messages))
time.sleep(1)
client.loop()
time.sleep(1)
client.loop()
time.sleep(1)
client.loop()
print("outgoing messages queued ",len(client._out_messages))
print ("\nSuppress pubrec")
client.suppress_pubrel_flag=True
print("\npublishing qos of 2 - Test message 2")
ret=client.publish("hub01/sensor1","Test message 2",2)    #publish
print("outgoing messages queued ",len(client._out_messages))
#print("\npublished return=",ret)
client.loop()
time.sleep(3)
while True:
   client.loop()
   time.sleep(1)
   count+=1
   #print("count =",count,"  flag", client.suppress_puback_flag)
   #print("count =",count," pubrel_flag ", client.suppress_pubrel_flag)
   #print("count =",count," pubcomp_flag ", client.suppress_puback_flag)
   print("count =",count)
   if count==5:
      print("outgoing messages queued ",len(client._out_messages))
      print("\npublishing qos of 0- Test message 0")
      ret=client.publish("hub01/sensor0","Test message 0",0)    #publish
      print("\npublishing qos of 2 - Test message 3")
      ret=client.publish("hub01/sensor1","Test message 3",2)    #publish
      print("outgoing messages queued ",len(client._out_messages))
   if count==200:
      print("\ndisconnecting")
      client.disconnect()
   if count==200:
      client.connect(broker,port)           #establish connection
   if count==11:
      print("\ndisconnecting")
      client.disconnect()
   if count==13:
      print("\nRe-connecting")
      client.connect(broker,port)           #establish connection
      print("outgoing messages queued ",len(client._out_messages))
   if count==15:
      print("outgoing messages queued ",len(client._out_messages))
      print ("\nun-Suppress pubrec")
      client.suppress_pubrel_flag=False
      #print ("\Suppress pubcomp")
      #client.suppress_puback_flag=True
   if count==22:
      print("outgoing messages queued ",len(client._out_messages))
      break 
      print ("un-Suppress pubcomp")
      client.suppress_puback_flag=False
   if count==30:
      print("outgoing messages queued ",len(client._out_messages))
      break         

client.disconnect()


