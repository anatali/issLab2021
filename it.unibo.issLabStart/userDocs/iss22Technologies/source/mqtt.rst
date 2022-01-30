.. role:: red
.. role:: blue 
.. role:: remark

===================================
MQTT
===================================


MQTT (Message Queue Telemetry Transport) è un protocollo applicativo che funziona 
su stack TCP, basato sul paradigma publish/subscribe.

Il protocollo MQTT è leggero e flessibile e consente l’implementazione sia su 
dispositivi hardware fortemente vincolati, sia su reti ad elevata latenza 
o a larghezza di banda limitata in quanto risulta particolarmente 
resiliente in fase di comunicazione dei dati.

Tanto i mittenti quanto i destinatari sono client MQTT che possono comunicare 
esclusivamente attraverso il :blue:`message broker`.

Ogni messaggio MQTT ha un comando e un payload. 
Il comando definisce il tipo di messaggio (per esempio CONNECT o SUBSCRIBE).

i messaggi MQTT sono organizzati per :blue:`topic` (argomento).



La libreria che implementa MQTT in Java è ``org.eclipse.paho.client.mqttv3``.

---------------------------------------
Qos 
---------------------------------------

I livelli QOS sono un modo per garantire la consegna dei messaggi e si riferiscono 
alla connessione tra un broker e un client .

- Qos 0:  È la modalità di trasferimento più inaffidabile. 
  Il messaggio non viene memorizzato nel mittente e non viene riconosciuto.
  Il messaggio verrà recapitato una sola volta o per niente.

- Qos 1: garantisce che il messaggio venga recapitato almeno una volta, 
  ma che possa essere recapitato più di una volta.  Il mittente invia un messaggio 
  e attende una conferma ( PUBACK )


  .. image:: ./_static/img/Tecno/mqttQos1.PNG 

- Qos 2: garantisce che il messaggio venga recapitato una sola volta. 
  Questo è il metodo più lento in quanto richiede 4 messaggi.

  .. image:: ./_static/img/Tecno/mqttQos2.PNG 


---------------------------------------
Esperimenti in Python
---------------------------------------

La libreria che implementa MQTT in Python è ``paho-mqtt``.

.. code::

    pip install paho-mqtt
