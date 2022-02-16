.. role:: red 
.. role:: blue 
.. role:: remark

=====================================================
Enablers per i componenti del RadarSystems (SPRINT2)
=====================================================

------------------------------------------
Enabler e proxy per il Sonar
------------------------------------------

.. image::  ./_static/img/Radar/EnablerProxySonar.PNG
         :align: center 
         :width: 60%


++++++++++++++++++++++++++++++++++++++++
Enabler per il Sonar
++++++++++++++++++++++++++++++++++++++++
.. list-table::
  :widths: 30,70
  :width: 100%

  * - .. image::  ./_static/img/Radar/EnablerAsServerSonar.PNG
         :align: center 
         :width: 80%
    - L'*enabler tipo server* per il Sonar è un ``EnablerAsServer`` connesso un gestore 
      applicativo  di tipo ``IApplMsgHandler`` che elabora:

      - i comandi: ridirigendoli al sonar locale 
      - le richieste:  ridirigendole al sonar locale e inviando la risposta al client 

.. _SonarApplHandler:

.. code:: java

  public class SonarApplHandler extends ApplMsgHandler  {
  ISonar sonar;
    public SonarApplHandler(String name, ISonar sonar) {
      super(name);
      this.sonar=sonar;
    }
    @Override
    public void elaborate(String message, Interaction2021 conn) {
      if( message.equals("getDistance")) {
        String vs = ""+sonar.getDistance().getVal();
        sendMsgToClient(vs, conn);
      }else if( message.equals("activate")) {
        sonar.activate();
      }else if( message.equals("activate")) {
        sonar.deactivate();
      }else if( message.equals("isActive")) {
        String sonarState = ""+sonar.isActive();
        sendMsgToClient(sonarState, conn);
      }
    }
  }

++++++++++++++++++++++++++++++++++++++++
Proxy per il Sonar
++++++++++++++++++++++++++++++++++++++++

.. list-table::
  :widths: 30,70
  :width: 100%

  * - .. image::  ./_static/img/Radar/SonarProxyAsClient.PNG
         :align: center 
         :width: 70%
    - Il '*proxy tipo client* per il Sonar è una specializzazione di  ``ProxyAsClient`` che implementa i 
      metodi di ``ISonar`` inviando dispatch o request all'*enabler tipo server* sulla connessione:


.. SonarProxyAsClient   NON QUI: vedi ContextServer

.. code:: java

  public class SonarProxyAsClient extends ProxyAsClient implements ISonar{
    public SonarProxyAsClient( 
         String name, String host, String entry, ProtocolType protocol ) {
    super( name,  host,  entry, protocol );
    }
    @Override
    public void activate() { sendCommandOnConnection("activate"); }
    @Override
    public void deactivate() { sendCommandOnConnection("deactivate"); }
    @Override
    public IDistance getDistance() {
      String answer = sendRequestOnConnection("getDistance");
      return new Distance( Integer.parseInt(answer) );
    }
    @Override
    public boolean isActive() {
      String answer = sendRequestOnConnection("isActive");
      return answer.equals( "true" );
    }
  }

 

-----------------------------------------
Enabler e proxy per il Led
-----------------------------------------

.. image::  ./_static/img/Radar/EnablerProxyLed.PNG
         :align: center 
         :width: 60%

L'enabler server per il Led usa un gestore di messaggi ``LedApplHandler`` che riceve comandi
e richieste da un ``LedProxyAsClient``. 
Entrambe queste classi sono simili a quanto visto per i sonar.
 

 
-----------------------------------------
Testing degli enabler
-----------------------------------------

La configurazione crea gli elementi della architettura di figura:

.. image::  ./_static/img/Radar/TestEnablers.PNG
         :align: center 
         :width: 50%


.. code::  java

    @Before
    public void setup() {
      RadarSystemConfig.simulation = true;
      RadarSystemConfig.ledPort    = 8015;
      RadarSystemConfig.sonarPort  = 8011;
      RadarSystemConfig.sonarDelay = 100;
      RadarSystemConfig.testing    = false;

    sonar = DeviceFactory.createSonar();
    led   = DeviceFactory.createLed();
    host  = "localhot";
		
    //I server
    sonarServer = new EnablerAsServer("sonarSrv",RadarSystemConfig.sonarPort, 
                              protocol,new SonarApplHandler("sonarH", sonar));
    ledServer   = new EnablerAsServer("ledSrv",  RadarSystemConfig.ledPort,   
                              protocol,new LedApplHandler("ledH", led)  );
		
    //I client
    String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
    String entrySonar= 
       protocol==ProtocolType.coap ? sonarUri : ""+RadarSystemConfig.sonarPort;
    sonarClient=new SonarProxyAsClient("sonarClient",host,entrySonar,protocol);
		
    String ledUri  = CoapApplServer.outputDeviceUri+"/led";
    String entryLed= 
      protocol==ProtocolType.coap ? ledUri : ""+RadarSystemConfig.ledPort;
    ledClient = new LedProxyAsClient("ledClient", host, entryLed, protocol);	
	}

Il test simula il comportamento del Controller, senza RadarDisplay:

.. code::  java

    @Test 
    public void testEnablers() {
      sonar.activate();
      sonarServer.activate();
      ledServer.activate();
		
      RadarSystemConfig.testing=false; //true => oneshot
      RadarSystemConfig.sonarDelay=100;
      RadarSystemConfig.DLIMIT=30;
		
      //Simulo il Controller
      Utils.delay(500);		
      while( sonarClient.isActive() ) {
        int v = sonarClient.getDistance().getVal();
        if( v < RadarSystemConfig.DLIMIT ){
          ledClient.turnOn();
          boolean ledState = ledClient.getState();
          assertTrue( ledState );	
        }else{
         ledClient.turnOff();
         boolean ledState = ledClient.getState();
         assertTrue( ! ledState );	
        }
    }		
  }




 
 

  

