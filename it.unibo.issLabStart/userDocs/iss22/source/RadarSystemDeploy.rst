.. role:: red 
.. role:: blue 
.. role:: remark

La distribuzione del *RadarSystem* assume due forme:

- la forma di una libreria di nome ``it.unibo.enablerCleanArch-1.0.jar`` prodotta dal progetto it.unibo.enablerCleanArch_
- la forma di una applicazione web (che utiliza la libreria precedente) prodotta dal progetto it.unibo.msenabler_


.. _enablerCleanArch:

---------------------------------------------------
it.unibo.enablerCleanArch
---------------------------------------------------

Il progetto *it.unibo.enablerCleanArch* è sviluppato in ``Java8`` e fornisce il programma
``AllMainRadarLed`` che permette di selezionare ed eseguire diverse configurazioni applicative.

.. code:: 

  1    LedUsageMain 
  a    RadarSystemDevicesOnRaspMqtt
  A    RadarSystemMainOnPcMqtt
  2    SonarUsageMainWithEnablerTcp
  3    SonarUsageMainWithContextTcp 
  4    SonarUsageMainWithContextMqtt
  5    SonarUsageMainCoap
  6    RadarSystemAllOnPc
  7    RadarSystemDevicesOnRasp
  8    RadaSystemMainCoap
  9    RadarSystemMainOnPcCoap

Selezionando **a** si esegue la parte di applicazione che attiva i dispositivi Led e Sonar sul Raspberry.
A queta parte corrisponde la parte di applicazione  **A**, da eseguire sul PC per inviare comandi ai dispositivi remoti 
e per ricevere informazioni sul loro stato.
Le due parti interagiscono via MQTT usando il broker di indirizzo ``tcp://broker.hivemq.com``.


.. _msenabler:

---------------------------------------------------
it.unibo.msenabler
---------------------------------------------------

Il progetto *it.unibo.msenabler*  è sviluppato in ``Java11`` e utilizza SpringBoot per fornire 
una WebGui alla porta ``8081`` che permette di comandare il Led e il Sonar. 

La GUI si presenta come segue:

.. image:: ./_static/img/Radar/msenablerGui.PNG
   :align: center
   :width: 60%

L'applicazione Spring alla base di *it.unibo.msenabler* potrebbe operare in due modi diversi:

#. *caso locale*: essere attivata su un Raspberry basato su **Buster**, che utilizza ``Java11`` ed 
   utlizzare l'applicazione **a** che fa riferimento ai dispositivi reali connessi al Raspberry. 
   Aprendo un browser su  ``http://<RaspberryIP>:8081``, un uente può inviare comandi al Led e ricevere i dati
   del Sonar in due modi diversi:

  - inviando al sonar il comando getDistance
  - utilizzando una websocket (con URI=/radarsocket). Per questa parte, si consiglia la lettura preliminare 
    di :ref:`WebSockets<WebSockets>`.   

#. caso remoto: essere attivata su un PC ed utlizzare l'applicazione **A** per inviare e ricevere informazione 
   via MQTT dalla parte applicativa ( **a**)  operante sul Raspberry.

++++++++++++++++++++++++++++++++++++++++++++++++
Caso locale 
++++++++++++++++++++++++++++++++++++++++++++++++

Come ogni applicazione SpringBoot, gli elementi salienti sono:

- Un controller (denominato ``HumanEnablerController``) che presenta all'end user una pagina 
- La pagina che utilillza Bootstrap è ``RadarSystemUserConsole.html``
- WebSocketConfiguration


++++++++++++++++++++++++++++++++++++++++++++++++
Caso remoto 
++++++++++++++++++++++++++++++++++++++++++++++++