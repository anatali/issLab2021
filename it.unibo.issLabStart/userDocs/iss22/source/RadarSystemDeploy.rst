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

++++++++++++++++++++++++++++++++++++++++++++++++++++++++
I diversi scenari
++++++++++++++++++++++++++++++++++++++++++++++++++++++++


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso 8 uso di Coap - sistema tutto su Raspberry
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Il sistema su Raspberry attiva un unico server (**CoapApplServer**) e aggiunge come risorse 
il Led ( devices/output/lights/led ) e il Sonar (devices/input/sonar). 

.. code:: 

   "simulation"       : "false",
   "ControllerRemote" : "false",
   "LedRemote"        : "false",
   "SonareRemote"     : "false",
   "RadarGuiRemote"   : "false",
   "protocolType"     : "coap",
   "withContext"      : "false",
   "sonarDelay"       : "200",
   ........................................
   "pcHostAddr"       : "192.168.1.9",
   "raspHostAddr"     : "192.168.1.24",
   "radarGuiPort"     : "8014",
   "ledPort"          : "8010",
   "ledGui"           : "true",
   "sonarPort"        : "8012",
   "sonarObservable"  : "false",
   "controllerPort"   : "8016",
   "serverTimeOut"    : "600000",
   "applStartdelay"   : "3000",
   "sonarDistanceMax" : "150",
   "DLIMIT"           : "12",
   "ctxServerPort"    : "8018",
   "mqttBrokerAddr"   : "tcp://broker.hivemq.com",
   "testing"          : "false"

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso 8 uso di Coap - dispositivi su Raspberry
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Condifguriamo il sistema su Raspberry specificando che il controller è remoto.

.. code:: 

   "simulation"       : "false",
   "ControllerRemote" : "true",
   ...


A questo punto attiviamo il programma 9 su PC

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso 9 uso di Coap - Controller su PC
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Questo programma nasce per usare CoAP e quindi fissa in modo diretto i parametri di configurazione 
che gli interessano:

.. code:: 

   	RadarSystemConfig.raspHostAddr = "192.168.1.xxx";
		RadarSystemConfig.DLIMIT       = 12;
		RadarSystemConfig.simulation   = false;
		RadarSystemConfig.withContext  = false;
		RadarSystemConfig.sonarDelay   = 200;     //come quello del Raspberry

Il programma può operare anche definendo il Controller come un observer della risorsa Sonar,
ponendo 

.. code:: 

   useProxyClient = false

In caso contrario, il Controller opera con un convenzionale ciclo **read-eval-print**.

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

#. caso remoto: essere attivata su un PC ed utlizzare l'applicazione **A** (o 9) per inviare e ricevere informazione 
   via MQTT dalla parte applicativa ( **a** o 7)  operante sul Raspberry.

++++++++++++++++++++++++++++++++++++++++++++++++
Caso locale 
++++++++++++++++++++++++++++++++++++++++++++++++

Come ogni applicazione SpringBoot, gli elementi salienti sono:

- Un controller (denominato ``HumanEnablerController``) che presenta all'end user una pagina 
- La pagina che utilillza Bootstrap è ``RadarSystemUserConsole.html``
- WebSocketConfiguration

Sembra molto lento, in particolare quando si attiva la webcam.

++++++++++++++++++++++++++++++++++++++++++++++++
Caso remoto 
++++++++++++++++++++++++++++++++++++++++++++++++

Su Raspberry, attiviamo 7 (RadarSystemDevicesOnRasp) e su PC 9 (RadarSystemMainOnPcCoap)
all'interno di una applicazione SpringBoot.