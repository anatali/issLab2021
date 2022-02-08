.. role:: red 
.. role:: blue 
.. role:: remark
  
.. _tuProlog: https://apice.unibo.it/xwiki/bin/view/Tuprolog/

==================================================
Verso un framework per la interazione distribuita
==================================================

---------------------------------------
TODOFeb7
---------------------------------------

:ref:`LedApplHandler`

- Introdurre ``IApplLogic`` , ``LedApplLogic`` e ``SonarApplLogic``
- Introdurre gli handler come wrapper di ``IApplLogic`` capaci di elaborare comandi e inviare risposte
- Introdurre i criteri per armonizzare i diversi supporti (TCP, MQTT, CoAP) nel ``Context2021``
- Individuare i punti in cui occorre tenere conto dello specifico protocollo per definire i parametri
  delle *operazioni astratte*

.. image:: ./_static/img/Architectures/framework0.PNG
   :align: center  
   :width: 60%


---------------------------------------
Il caso di Coap
---------------------------------------

La libreria ``org.eclipse.californium`` offre ``CoapServer`` che viene decorato da ``CoapApplServer``.

La classe ``CoapResource`` viene decorata da ``ApplResourceCoap`` per implementare ``IApplMsgHandler``.
In questo modo una specializzazione come ``LedResourceCoap`` può operare come componente da aggiungere 
al sistema tramite ``CoapApplServer`` che la ``Context2021.create()`` riduce a ``CoapServer`` in cui 
sono registrate le risorse.


---------------------------------------
Il caso di MQTT
---------------------------------------

---------------------------------
Supporti per altri protocolli
---------------------------------

Udp, Bluetooth  ``unibonoawtsupports.jar``
 
+++++++++++++++++++++++++++++++++++++++++++++++
La libreria ``unibonoawtsupports.jar``
+++++++++++++++++++++++++++++++++++++++++++++++

  
---------------------------------
Supporti per HTTP
---------------------------------

.. code:: Java

  HttpURLConnection con =
  IssHttpSupport

