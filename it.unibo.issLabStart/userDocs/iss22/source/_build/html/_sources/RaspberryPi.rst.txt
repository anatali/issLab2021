.. contents:: Overview
   :depth: 4
.. role:: red
.. role:: blue 
.. role:: remark
 

======================================
RaspberryPi
======================================

.. csv-table::  
   :align: left
   :header: "Hardware Model B+", "WiringPi"
   :widths: 40, 60
   :width: 100%

   ".. image:: ./_static/img/rasp/Raspberry-Pi-GPIO-Layout-Model-B-Plus.png 
    :width: 80%", ".. code::

      Alimentazione richiesta: 5V e 2.5Amps.

      Installazione libreria di accesso GPIO:
         
         sudo apt-get install wiringpi
   "


------------------
wiringpi
------------------
``WiringPi`` include un'utilità da riga di comando 
``gpio`` che può essere utilizzata per programmare 
e configurare i pin GPIO. 

.. code::

    sudo apt-get install wiringpi

    gpio -v
        Raspberry Pi Details:
        Type: Model B+, Revision: 02, Memory: 512MB, Maker: Sony
        * Device tree is enabled.
        * This Raspberry Pi supports user-level GPIO access.

    Esempi:
        gpio unexportall
        gpio export 25 out
        gpio export 1 out
        gpio write 25 0
        gpio write 1 0

.. csv-table::  
   :align: left
   :header: "Hardware", "GPIO"
   :widths: 30, 60
   :width: 100%

   "``gpio readall``", ".. code::

      +-----+-----+---------+------+---+---Pi B+--+---+------+---------+-----+-----+
      | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
      +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
      |     |     |    3.3v |      |   |  1 || 2  |   |      | 5v      |     |     |
      |   2 |   8 |   SDA.1 | ALT0 | 1 |  3 || 4  |   |      | 5v      |     |     |
      |   3 |   9 |   SCL.1 | ALT0 | 1 |  5 || 6  |   |      | 0v      |     |     |
      |   4 |   7 | GPIO. 7 |   IN | 1 |  7 || 8  | 1 | ALT0 | TxD     | 15  | 14  |
      |     |     |      0v |      |   |  9 || 10 | 1 | ALT0 | RxD     | 16  | 15  |
      |  17 |   0 | GPIO. 0 |   IN | 0 | 11 || 12 | 0 | IN   | GPIO. 1 | 1   | 18  |
      |  27 |   2 | GPIO. 2 |   IN | 0 | 13 || 14 |   |      | 0v      |     |     |
      |  22 |   3 | GPIO. 3 |   IN | 0 | 15 || 16 | 0 | IN   | GPIO. 4 | 4   | 23  |
      |     |     |    3.3v |      |   | 17 || 18 | 0 | IN   | GPIO. 5 | 5   | 24  |
      |  10 |  12 |    MOSI |   IN | 0 | 19 || 20 |   |      | 0v      |     |     |
      |   9 |  13 |    MISO |   IN | 0 | 21 || 22 | 0 | OUT  | GPIO. 6 | 6   | 25  |
      |  11 |  14 |    SCLK |   IN | 0 | 23 || 24 | 1 | IN   | CE0     | 10  | 8   |
      |     |     |      0v |      |   | 25 || 26 | 1 | IN   | CE1     | 11  | 7   |
      |   0 |  30 |   SDA.0 |   IN | 1 | 27 || 28 | 1 | IN   | SCL.0   | 31  | 1   |
      |   5 |  21 | GPIO.21 |   IN | 1 | 29 || 30 |   |      | 0v      |     |     |
      |   6 |  22 | GPIO.22 |   IN | 1 | 31 || 32 | 0 | IN   | GPIO.26 | 26  | 12  |
      |  13 |  23 | GPIO.23 |   IN | 0 | 33 || 34 |   |      | 0v      |     |     |
      |  19 |  24 | GPIO.24 |   IN | 0 | 35 || 36 | 0 | IN   | GPIO.27 | 27  | 16  |
      |  26 |  25 | GPIO.25 |   IN | 0 | 37 || 38 | 0 | IN   | GPIO.28 | 28  | 20  |
      |     |     |      0v |      |   | 39 || 40 | 0 | IN   | GPIO.29 | 29  | 21  |
      +-----+-----+---------+------+---+----++----+---+------+---------+-----+-----+
      | BCM | wPi |   Name  | Mode | V | Physical | V | Mode | Name    | wPi | BCM |
      +-----+-----+---------+------+---+---Pi B+--+---+------+---------+-----+-----+
      "
       
---------------------------------
Primi programmi
---------------------------------

Colleghiamo l'anodo (gambo lungo, +) di un LED al GPIO pin25 
(fisico 35) e il catodo (gambo corto, -) a GND.

+++++++++++++++++++++++++++++++++++++++++
Accediamo il led usando shell script
+++++++++++++++++++++++++++++++++++++++++

In un file ``led25OnOff.sh`` scriviamo:

.. code::

   echo Unexporting.
   echo 25 > /sys/class/gpio/unexport #
   echo 25 > /sys/class/gpio/export #
   cd /sys/class/gpio/gpio25 #

   echo Setting direction to out.
   echo out > direction #
   echo Setting pin high.
   echo 1 > value #
   sleep 1 #
   echo Setting pin low
   echo 0 > value #
   sleep 1 #
   echo Setting pin high.
   echo 1 > value #
   sleep 1 #
   echo Setting pin low
   echo 0 > value #

Uso: ``sudo bash led25OnOff.sh``

+++++++++++++++++++++++++++++++++
Accediamo il led usando gpio
+++++++++++++++++++++++++++++++++

In un file ``led25Gpio.sh`` scriviamo:

.. code::
   
   gpio readall #
   echo Setting direction to out
   gpio mode 6 out #
   echo Write 1
   gpio write 6 1 #
   sleep 1 #
   echo Write 0
   gpio write 6 0 #

Uso: ``bash led25Gpio.sh`` 

+++++++++++++++++++++++++++++++++
Accediamo il led usando Python
+++++++++++++++++++++++++++++++++

In un file ``ledPython25.py`` scriviamo:

.. code::

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
   while True:
      GPIO.output(25,GPIO.HIGH)
      time.sleep(1)
      GPIO.output(25,GPIO.LOW)
      time.sleep(1)

Uso: ``python ledPython25.py``



.. Mettte le immagini fianco a fianco
.. .. image:: ./_static/img/rasp/Raspberry-Pi-GPIO-Layout-Model-B-Plus.png
..   :width: 20%
.. .. image:: ./_static/img/rasp/Raspberry-Pi-GPIO-Layout-Model-B-Plus.png
..   :width: 20%  

