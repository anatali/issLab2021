# -------------------------------------------------------------
# ledPython25.py
# Key-point: we can manage a Led connected on the GPIO pin 25
# using the python language.
#	sudo python ledPython25.py
# -------------------------------------------------------------
import RPi.GPIO as GPIO 
import time

'''
----------------------------------
CONFIGURATION
----------------------------------
'''
GPIO.setmode(GPIO.BCM)
GPIO.setup(25,GPIO.OUT)

'''
----------------------------------
main activity
----------------------------------
'''

def turnOn():
	GPIO.output(25,GPIO.HIGH)
	
def turnOff():
	GPIO.output(25,GPIO.LOW)

counter = 0	    

while counter < 3:
    counter=counter+1
    turnOn()
    time.sleep(1)
    turnOff()
    time.sleep(1)
	    