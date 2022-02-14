.. role:: red 
.. role:: blue 
.. role:: remark

.. _Applicazione web: https://it.wikipedia.org/wiki/Applicazione_web    

.. _WebappFrameworks: https://www.geeksforgeeks.org/top-10-frameworks-for-web-applications/

.. _Springio: https://spring.io/

.. _WebSocket: https://it.wikipedia.org/wiki/WebSocket

.. _Node.js: https://nodejs.org/it/

.. _Express: https://expressjs.com/it/

.. _CleanArchitecture: https://clevercoder.net/2018/09/08/clean-architecture-summary-review

==================================
RadarSystem WebGui
==================================  
.. Il progetto *it.unibo.msenabler*  è sviluppato in ``Java11`` e utilizza SpringBoot per fornire 

Vogliamo costruire una `Applicazione web`_ che soddisfi due requisiti funzionali principali:

#. (requisito :blue:`master`): permettere a un amministratore di sistema (master) di impostare attraverso una pagina web 
   (porta  ``8081``) la configurazione del'applicazione RadarSystem e di inviare comandi al Led e al Sonar;
#. (requisito :blue:`observe`): permettere a un utente di osservare lo stato dell'applicazione e dei dispositivi, 
   senza avare la possibilità di inviare comandi.
 
--------------------------------------------
Analisi del problema WebGui
--------------------------------------------

Lo sviluppo di applicazioni Web non può presceindere dall'uso di uno dei numerosi framework disponibili (si veda ad
esempio `WebappFrameworks`_). 

Per il nostro legame con Java, può essere opportuno, per ora, fare 
riferimento a `Springio`_. Tuttavia, una adeguata alternativa potrebbe essere l'uso di framework basati su 
`Node.js`_ ed `Express`_.

La WebGUI si potrebbe presentare, sia al master, sia a un utente observer, come segue:

.. image:: ./_static/img/Radar/msenablerGuiNoWebcam.PNG
   :align: center
   :width: 60%

L'utente potrebbe quindi vedere tutto ciò che vede il master, senza avere però la possibilità di inviare comandi.
Naturalmente, la struttura finale della pagina sarà stabilita in coooperazione con il committente.

++++++++++++++++++++++++++++++++++++
Architettura del sistema WebGui
++++++++++++++++++++++++++++++++++++

Dal punto di vista architetturale, il WebServer dell'applicazione Spring potrebbe eseere organizzato in due modi:

#. **caso locale**: essere attivato sul Raspberry (basato su **Buster**, che utilizza ``Java11``) ed interagire 
   con una applicazione Java locale;
      
#. **caso remoto**: essere attivato su un PC ed interagire con una applicazione Java remota operante sul Raspberry,
   come ad esempio :ref:`RadarSystemMainDevsOnRasp`.

In ogni caso, l'organizzazione interna del codice del WebServer dovrà essere ispirata ai principi della
`CleanArchitecture`_.

.. csv-table::  
    :align: center
    :widths: 50,50
    :width: 100% 
    
    .. image:: ./_static/img/Architectures/cleanArchCone.jpg,.. image:: ./_static/img/Architectures/cleanArch.jpg

++++++++++++++++++++++++++++++++++++
Architettura interna al server
++++++++++++++++++++++++++++++++++++

Nello sviluppo del RadarSystem, abbiamo già organizzato il software secondo il principio che  il 
flusso di controllo si origina dal **Controller** per passare poi allo **UseCase** e al **Presenter**
in accordo al principio della `inversione delle dipendenze <https://en.wikipedia.org/wiki/Dependency_inversion_principle>`_:

- :remark:`I componenti di alto livello non devono dipendere da componenti di livello più basso.`

Con queste premesse, il compito che ci attende è quello di realizzare la parte 
**Presenter** in modo da continuare a tenere separati i casi d'uso dall'interfaccia utente.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso remoto
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Abbiamo già osservato come l'applicazione :ref:`RadarSystemMainDevsOnRasp` possa essere
riusata come referente remoto di un WebServer allocato su PC. Il WebServer potrebbe dunque avvalersi
della applicazione :ref:`RadarSystemMainEntryOnPc` per inviare ed ottenere dal Raspberry 
(usando TCP, MQTT o CoAP) le informazioni legate alla attività del **Presenter**.

Il compito del Controller-Spring che si occupa della Human-Interaction (``HIController``) sarà quello di
utilizzare questa applicazione per inviare i comandi immessi dal master mediante la GUI (una pagina html)
e per introdurre nella pagina html le informazioni ricevute.

Il requisito :blue:`observe` può essere ottenuto in due modi diversi:

  - ``HIController`` può inviare **richieste** di informazione al Raspberry (ad esempio ``getDistance`` o ``getState``)
    e presentare all'utente un nuova pagina con le risposte ottenute
  - utilizzando il meccanismo delle `WebSocket`_, che permette l'aggiornamento automatico della pagina attraverso
    la introduzione di un observer. In questo caso, l'uso di CoAP o MQTT può rendere il compito più agevole rispetto
    a TCP, in quanto in precedenza abiamo introdotto solo observer locali. Con CoAP o MQTT invece non è complicato
    introdurre presso il WebServer Spring un observer che riceve dati emessi dal Raspberry.


.. image:: ./_static/img/Architectures/portAdapterArch.png
   :width: 70% 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Caso locale
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

Nel **caso locale**, si può pensare che il Presenter possa avvalersi di una applicazione ad-hoc
(:ref:`RadarSystemMainEntryOnRasp`) che acceda ai dispositivi reali connessi al Raspberry.
 
   .. image:: ./_static/img/Radar/ArchWebGuiOnRasp.PNG
      :align: center
      :width: 40%
      
Prima di realizzare questa nuova applicazione, conviene però fare in modo che sia ``HIController`` a indicare
l'insieme di operazioni di cui necessita.


++++++++++++++++++++++++++++++++++++
IApplicationFacade
++++++++++++++++++++++++++++++++++++

In quanto componente applicativo primario, 
il Controller-Spring realtivo alla Human-interaction (``HIController``) e anche quello
relativo a una probabile futura Machine-interaction (``MIController``) impone che 
il componente di cui farà uso per realizzare i suoi use-cases obbedisca una interfaccia 
definita come segue:

.. code:: java 

   public interface IApplicationFacade {  
      public void setUp( String configFile );
      public void ledActivate( boolean v );	
      public String ledState(   );
      public void sonarActivate(   );
      public boolean sonarIsactive(   );
      public void sonarDectivate(   );
      public String sonarDistance(   );	
      public void doLedBlink();
      public void stopLedBlink();
   }


++++++++++++++++++++++++++++++++++++++
RadarSystemMainEntryOnRasp
++++++++++++++++++++++++++++++++++++++
La realizzazione della nuova risorsa necessaria per il :ref:`Caso locale` è facilmente definibile:

.. code:: java 

    public class RadarSystemMainEntryOnRasp  implements IApplicationFacade{
    protected ISonar sonar;
    protected ILed  led ;
    protected boolean ledblinking   = false;

        @Override
        public void setUp(String configFile) {
            RadarSystemConfig.setTheConfiguration(configFile);
            if( RadarSystemConfig.sonarObservable ) 
                sonar = DeviceFactory.createSonarObservable();		
            else sonar = DeviceFactory.createSonar();
            led  = DeviceFactory.createLed();
        }
        @Override
        public String getName() { return "RadarSystemMainEntryOnPc"; }
        @Override
        public void ledActivate(boolean v) {if( v ) led.turnOn();else led.turnOff();} 	
        @Override
        public String ledState() { return ""+led.getState(); }
        @Override
        public void stopLedBlink() {ledblinking = false;}	
        @Override
        public void sonarActivate() { sonar.activate();	}
        @Override
        public boolean sonarIsactive() { return sonar.isActive(); }
        @Override
        public void sonarDectivate() { sonar.deactivate(); }
        @Override
        public String sonarDistance() { return ""+sonar.getDistance().getVal(); }
    }//RadarSystemMainEntryOnRasp



 





.. il master può inviare comandi al Led e ricevere i dati del Sonar 
  
.. Per ricevere dati senza polling può essere conveniente utilizzare il meccanismo delle `WebSocket`_.

.. (si veda come lettura preliminare :ref:`WebSockets<wsintro>` in ``iss2Technologies``)  



.. una WebGui alla porta ``8081`` che permette di comandare il Led e il Sonar. 


.. image:: ./_static/img/Radar/ArchWebGui0.PNG
   :align: center
   :width: 60%




 

Come ogni applicazione SpringBoot, gli elementi salienti sono:

- Un controller (denominato ``HumanEnablerController``) che presenta all'end user una pagina 
- La pagina che utilillza Bootstrap è ``RadarSystemUserConsole.html``
- WebSocketConfiguration

Sembra molto lento, in particolare quando si attiva la webcam.

 

Su Raspberry, attiviamo 7 (RadarSystemDevicesOnRasp) e su PC 9 (RadarSystemMainOnPcCoap)
all'interno di una applicazione SpringBoot.


#. Costruiamo una risorsa accessibile via rete mediante CoaP che aggiorna uno stato
#. Costruiamo una applicazione RadarGuiCoap che osserva le variazioni di stato della risorsa e modifica il RadarDisplay
#. Costruiamo radarGui rendendo accessibile RadarGuiCoap via rete con SpringBoot / HTTP in modo RESTFUL 
#. Facciamo il deployment su docker rendendo accessibile sia la risorsa sia la radarGui


----------------------------------------------------------
Progetto e realizzazione della parte applicativa
----------------------------------------------------------



++++++++++++++++++++++++++++++++++++++
RadarSystemMainEntryOnPc
++++++++++++++++++++++++++++++++++++++



.. code:: java 

    public class RadarSystemMainEntryOnPc  implements IApplicationFacade{
    public static final String mqttAnswerTopic  = "pctopic";
    public static final String mqttCurClient    = "pc4";

    protected ISonar sonar;
    protected ILed  led ;
    protected boolean ledblinking   = false;
    protected String serverHost = "";
	
    public RadarSystemMainEntryOnPc( String addr){
        RadarSystemConfig.raspHostAddr = addr;		
    }
    public void doJob(String configFileName) {
        setUp( configFileName );
        configure();
    }
    @Override
    public void setUp(String configFile) {
        if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
        else {
            RadarSystemConfig.protcolType       = ProtocolType.tcp;
            RadarSystemConfig.raspHostAddr      = "localhost"; //"192.168.1.9";
            RadarSystemConfig.ctxServerPort     = 8018;
            RadarSystemConfig.sonarDelay        = 1500;
            RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
            RadarSystemConfig.DLIMIT            = 40;
            RadarSystemConfig.testing           = false;
            RadarSystemConfig.tracing           = true;
            RadarSystemConfig.mqttBrokerAddr    = "tcp://broker.hivemq.com"; 
                                    //: 1883  or  "tcp://localhost:1883" 	
        }
    }	

    protected void configure() {
        if(Utils.isCoap() ) { 
            serverHost       = RadarSystemConfig.raspHostAddr;
            String ledPath   = CoapApplServer.lightsDeviceUri+"/led"; 
            String sonarPath = CoapApplServer.inputDeviceUri+"/sonar"; 
            led              = new LedProxyAsClient("ledPxy", serverHost, ledPath );
            sonar            = new SonarProxyAsClient("sonarPxy",  serverHost, sonarPath  );
            CoapClient  client = new CoapClient( "coap://localhost:5683/"+CoapApplServer.inputDeviceUri+"/sonar" );
            //CoapObserveRelation obsrelation = 
            client.observe( new SonarObserverCoap("sonarObs") );
            //cancelObserverRelation(obsrelation);
        }else {
            String serverEntry = "";
            if(Utils.isTcp() ) { 
                serverHost  = RadarSystemConfig.raspHostAddr;
                serverEntry = "" +RadarSystemConfig.ctxServerPort; 
            }
            if(Utils.isMqtt() ) { 
                MqttConnection conn = MqttConnection.createSupport( mqttCurClient ); //,mqttAnswerTopic
                conn.subscribe( mqttCurClient, mqttAnswerTopic );
                serverHost  = RadarSystemConfig.mqttBrokerAddr;  //dont'care
                serverEntry = mqttAnswerTopic; 
			}				
            led   = new LedProxyAsClient("ledPxy", serverHost, serverEntry );
        sonar = new SonarProxyAsClient("sonarPxy",  serverHost, serverEntry  );
        }
    }//configure

	
	
	
    @Override
    public void doLedBlink() {
    new Thread() {
        public void run() {
            ledblinking = true;
            while( ledblinking ) {
                ledActivate(true);
                Utils.delay(500);
                ledActivate(false);
                Utils.delay(500);
             }
        }
    }.start();			
    }
    @Override
    public String getName() { return "RadarSystemMainEntryOnPc"; }
    @Override
    public void ledActivate(boolean v) {if( v ) led.turnOn();else led.turnOff();} 	
    @Override
    public String ledState() { return ""+led.getState(); }
    @Override
    public void stopLedBlink() {ledblinking = false;}	
    @Override
    public void sonarActivate() { sonar.activate();	}
    @Override
    public boolean sonarIsactive() { return sonar.isActive(); }
    @Override
    public void sonarDectivate() { sonar.deactivate(); }
    @Override
    public String sonarDistance() { return ""+sonar.getDistance().getVal(); }
  	
     }//RadarSystemMainEntryOnPc 


--------------------------------------------------------
L'applicazione Spring
--------------------------------------------------------



---------------------------------------
IApplicationFacadeWithWebcam
---------------------------------------

.. code:: java 

   public interface IApplicationFacadeWithWebcam extends IApplicationFacade{  
      public void takePhoto( String fName  );	
      public void sendCurrentPhoto();
      public void startWebCamStream(   );	
      public void stopWebCamStream(   );	
      public String getImage(String fName);
      public void storeImage(String encodedString, String fName);
    }
