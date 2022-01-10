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

- Selezionando **a** si esegue la parte di applicazione che attiva i dispositivi Led e Sonar sul Raspberry.
  A queta parte corrisponde la parte di applicazione  **A** da eseguire sul PC per inviare comandi al Led.
  Le due parti interagiscono via MQTT usando il broker di indirizzo ``tcp://broker.hivemq.com``.


.. _msenabler:

---------------------------------------------------
it.unibo.msenabler
---------------------------------------------------

Il progetto *it.unibo.msenabler*  è sviluppato in ``Java11`` e utilizza SpringBoot per fornire 
una WebGui alla porta ``8081`` che permette di comandare il Led usando l'applicazione **A**.

Si consiglia la lettura preliminare di :ref:`WebSockets<WebSockets>`. 

L'applicazione SpringBoot potrebbe anche attivare la parte applicativa **a** ed essere eseguita 
su un Raspberry basato su **Buster**, che utilizza ``Java11``.

Come ogni applicazione SpringBoot, gli elementi salienti sono:

- Un controller (denominato ``HumanEnablerController``) che presenta all'end user una pagina 
- La pagina che utilillza Bootstrap è ``RadarSystemUserConsole.html``
- WebSocketConfiguration