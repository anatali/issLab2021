.. role:: red 
.. role:: blue 
.. role:: remark

==================================
WebGui
==================================  

---------------------------------------
IApplicationFacade
---------------------------------------


.. code:: java 

   public interface IApplicationFacade { //extends IApplication
      //public void doJob(String configFileName);
      public String getName();
      public void setUp( String configFile );
      public void ledActivate( boolean v );	
      public String ledState(   );
      public void sonarActivate(   );
      public boolean sonarIsactive(   );
      public void sonarDectivate(   );
      public String sonarDistance(   );	
      public void takePhoto( String fName  );	
      public void sendCurrentPhoto();
      public void startWebCamStream(   );	
      public void stopWebCamStream(   );	
      public String getImage(String fName);
      public void storeImage(String encodedString, String fName);

      public void doLedBlink();
      public void stopLedBlink();
   }


---------------------------------------
RadarSystemMainEntryOnPc
---------------------------------------



.. code:: java 

   public class RadarSystemMainEntryOnPc  implements IApplicationFacade{
	public static final String mqttAnswerTopic  = "pctopic";
	public static final String mqttCurClient    = "pc4";

	protected ISonar sonar;
	protected ILed  led ;
	protected final int ampl             = 3;
	protected boolean ledblinking        = false;
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
		  RadarSystemConfig.mqttBrokerAddr    = "tcp://broker.hivemq.com"; //: 1883  OPTIONAL  "tcp://localhost:1883" 	
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
 
 	}

	@Override
	public String getName() {	 
		return "RadarSystemMainEntryOnPc";
	}
	
	
 	@Override
	public void ledActivate(boolean v) {
		//Colors.out("RadarSystemMainOnPcCoapBase ledActivate " + v );
 		if( v ) led.turnOn();else led.turnOff();
	}
 	
	@Override
	public String ledState() {
 		return ""+led.getState();//coapLedSup.request("ledState"); //payload don't care
	}
	
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
	public void stopLedBlink() {
		ledblinking = false;		
	}
	
	@Override
	public void sonarActivate() {
		ColorsOut.out("RadarSystemMainOnPcCoapBase | sonarActivate");
 		sonar.activate();
		
	}
	@Override
	public boolean sonarIsactive() {
		return sonar.isActive();
	}
	@Override
	public void sonarDectivate() {
		sonar.deactivate();
	}
	@Override
	public String sonarDistance() {
 		return ""+sonar.getDistance().getVal();
	}

 	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainEntryOnPc("").doJob(null);
	}

   }

---------------------------------------------------
it.unibo.msenabler
---------------------------------------------------

Il progetto *it.unibo.msenabler*  è sviluppato in ``Java11`` e utilizza SpringBoot per fornire 
una WebGui alla porta ``8081`` che permette di comandare il Led e il Sonar. 

La GUI si presenta come segue:

.. image:: ./_static/img/Radar/msenablerGuiNoWebcam.PNG
   :align: center
   :width: 60%

L'applicazione Spring alla base di *it.unibo.msenabler* potrebbe operare in due modi diversi:

#. **caso locale**: essere attivata su un Raspberry basato su **Buster**, che utilizza ``Java11`` ed 
   utlizzare l'applicazione **a** che fa riferimento ai dispositivi reali connessi al Raspberry. 
   Aprendo un browser su  ``http://<RaspberryIP>:8081``, un uente può inviare comandi al Led e ricevere i dati
   del Sonar in due modi diversi:

  - inviando al sonar il comando getDistance
  - utilizzando una websocket (con URI=/radarsocket). Per questa parte, si consiglia la lettura preliminare 
    di :ref:`WebSockets<WebSockets>`.   

#. **caso remoto**: essere attivata su un PC ed utlizzare l'applicazione :ref:`RadarSystemMainEntryOnPc` per interagire 
   con la parte applicativa :ref:`RadarSystemMainDevsOnRasp` operante sul Raspberry.

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