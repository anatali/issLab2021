import webiopi
import datetime

GPIO = webiopi.GPIO

LIGHT = 17 		# GPIO pin using BCM numbering

 
# setup function is automatically called at WebIOPi startup
def setup():
    # set the GPIO used by the light to output
    GPIO.setFunction(LIGHT, GPIO.OUT)
    # retrieve current datetime
    now = datetime.datetime.now()
    print("			scriptLed17Blink | SETUP at:" + str(now) )       
    state = True
    for x in range(0, 6):   
	    GPIO.digitalWrite(LIGHT, state)
	    state = not state
	    # gives CPU some time before looping again
	    webiopi.sleep(0.5)


# destroy function is called at WebIOPi shutdown
def destroy():
    GPIO.digitalWrite(LIGHT, GPIO.LOW)

