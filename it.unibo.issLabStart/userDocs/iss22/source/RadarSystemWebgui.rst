.. role:: red 
.. role:: blue 
.. role:: remark

.. _Applicazione web: https://it.wikipedia.org/wiki/Applicazione_web    

.. _WebappFrameworks: https://www.geeksforgeeks.org/top-10-frameworks-for-web-applications/

.. _Springio: https://spring.io/

.. _WebSocket: https://it.wikipedia.org/wiki/WebSocket

.. _Node.js: https://nodejs.org/it/

.. _Express: https://expressjs.com/it/

==================================
RadarSystem WebGui
==================================  
.. Il progetto *it.unibo.msenabler*  è sviluppato in ``Java11`` e utilizza SpringBoot per fornire 

Vogliamo costruire una `Applicazione web`_ che soddisfi due requisiti funzionali principali:

#. (requisito :blue:`master`): permettere a un amministratore di sistema (master) di impostare attraverso una pagina web 
   (con )porta  ``8081``) l'applicazione RadarSystem e inviare comandi al Led e al Sonar;
#. (requisito :blue:`observe`): permettere a un utente di osservare lo stato dell'applicazione e dei dispositivi 
   senza poter inviare comandi
 
--------------------------------------------
Analisi del problema WebGui
--------------------------------------------

Lo sviluppo di applicazioni Web è oggi basata sull'uso di uno dei numerosi framework, come quelli riportati in 
`WebappFrameworks`_. 

Per la nostra abitudine ad utilizzare Java come linguaggio di programmazione, faremo qui
riferimento a `Springio`_. Una adeguata alternativa consiste nell'utilizzare framework basati su 
`Node.js`_ ed `Express`_.

La WebGUI si potrebbe presentare, sia al master, sia a un utente observer, come segue:

.. image:: ./_static/img/Radar/msenablerGuiNoWebcam.PNG
   :align: center
   :width: 60%

L'utente potrebbe quindi vedere tutto ciò che vede il master, senza avere però la possibilità di inviare comandi.

 
L'applicazione Spring potrebbe operare in due modi diversi:

#. **caso locale**: essere attivata su un Raspberry basato su **Buster**, che utilizza ``Java11`` ed 
   utlizzare una versione dell'applicazione (:ref:`RadarSystemMainEntryOnRasp`) che usa i dispositivi 
   reali connessi al Raspberry. 
 
   .. image:: ./_static/img/Radar/ArchWebGuiOnRasp.PNG
      :align: center
      :width: 40%
      

#. **caso remoto**: essere attivata su un PC ed utlizzare l'applicazione :ref:`RadarSystemMainEntryOnPc` per interagire 
   con la parte applicativa :ref:`RadarSystemMainDevsOnRasp` operante sul Raspberry.


Per non variare il codice del controller Spring (HIController) in relazione al caso locale o remoto,
l'applicazione cui il controller fa riferimento dovrà rispettare una unica interfaccia, che potremmo
definire come segue:

++++++++++++++++++++++++++++++++++++
IApplicationFacade
++++++++++++++++++++++++++++++++++++


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


In entrambi i casi, il requisito :blue:`observe` può essere ottenuto in due modi diversi:

  - inviando una richiesta al dispositivo (ad esempio ``getDistance`` o ``getState``)
  - utilizzando il meccanismo delle `WebSocket`_, che permette l'aggiornamento automatico della pagina da parte
    di un observer (del Sonar) 



il master può inviare comandi al Led e ricevere i dati del Sonar 
  
Per ricevere dati senza polling può essere conveniente utilizzare il meccanismo delle `WebSocket`_.

.. (si veda come lettura preliminare :ref:`WebSockets<wsintro>` in ``iss2Technologies``)  



.. una WebGui alla porta ``8081`` che permette di comandare il Led e il Sonar. 


.. image:: ./_static/img/Radar/ArchWebGui0.PNG
   :align: center
   :width: 60%




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


#. Costruiamo una risorsa accessibile via rete mediante CoaP che aggiorna uno stato
#. Costruiamo una applicazione RadarGuiCoap che osserva le variazioni di stato della risorsa e modifica il RadarDisplay
#. Costruiamo radarGui rendendo accessibile RadarGuiCoap via rete con SpringBoot / HTTP in modo RESTFUL 
#. Facciamo il deployment su docker rendendo accessibile sia la risorsa sia la radarGui


----------------------------------------------------------
Progetto e realizzazione della parte applicativa
----------------------------------------------------------

++++++++++++++++++++++++++++++++++++++
RadarSystemMainEntryOnRasp
++++++++++++++++++++++++++++++++++++++

.. code:: java 

    public class RadarSystemMainEntryOnRasp  implements IApplicationFacade{
    protected ISonar sonar;
    protected ILed  led ;
    protected boolean ledblinking   = false;

        @Override
        public void setUp(String configFile) {
            if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
            configure();
        }	

        protected void configure() {
            if( RadarSystemConfig.sonarObservable ) {
                sonar = DeviceFactory.createSonarObservable();		
            }else{
                sonar = DeviceFactory.createSonar();
            }
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
