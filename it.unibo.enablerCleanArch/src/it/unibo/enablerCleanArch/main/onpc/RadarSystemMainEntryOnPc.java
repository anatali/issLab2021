package it.unibo.enablerCleanArch.main.onpc;

import org.eclipse.californium.core.CoapClient;
import it.unibo.enablerCleanArch.domain.IApplicationFacade;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.mqtt.MqttConnection;

/*
 * Applicazione usata da Spring quando gira su PC
 */

public class RadarSystemMainEntryOnPc  implements IApplicationFacade{
	public static final String mqttAnswerTopic  = "pctopic";
	public static final String mqttCurClient    = "pc4";

	protected ISonar sonar;
	protected ILed  led ;
 	protected boolean ledblinking        = false;
	protected String serverHost = "";
	
	public RadarSystemMainEntryOnPc( String addr, String configFile){
		setUp(  configFile );
		RadarSystemConfig.raspHostAddr = addr;		
		configure();
	}
	
	@Override
	public void setUp(String configFile) {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
 		else {
		  RadarSystemConfig.simulation        = false;
	      RadarSystemConfig.protcolType       = ProtocolType.coap;
		  RadarSystemConfig.raspHostAddr      = "192.168.1.9"; //"192.168.1.9";
		  RadarSystemConfig.ctxServerPort     = 8018;
		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
		  RadarSystemConfig.DLIMIT            = 40;
		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.tracing           = true;
		  RadarSystemConfig.mqttBrokerAddr    = "tcp://broker.hivemq.com"; //: 1883  OPTIONAL  "tcp://localhost:1883" 	
 		}
	}	
	
	@Override
	public void activateObserver(IObserver h) {
		if( Utils.isCoap()) {
			CoapClient  cli  = new CoapClient( "coap://"+RadarSystemConfig.raspHostAddr+":5683/"+CoapApplServer.inputDeviceUri+"/sonar" );
			//CoapObserveRelation obsrelation = 
					cli.observe( new SonarObserverCoap("sonarObsCoap", h) );
			//cancelObserverRelation(obsrelation);		
		}else if( Utils.isMqtt() ) {
			
		}
	}

	protected void configure() {
		if(Utils.isCoap() ) { 
			serverHost       = RadarSystemConfig.raspHostAddr;
			String ledPath   = CoapApplServer.lightsDeviceUri+"/led"; 
			String sonarPath = CoapApplServer.inputDeviceUri+"/sonar"; 
			led              = new LedProxyAsClient("ledPxy", serverHost, ledPath );
			sonar            = new SonarProxyAsClient("sonarPxy",  serverHost, sonarPath  );
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

//	@Override
//	public String getName() {	 
//		return "RadarSystemMainEntryOnPc";
//	}
	
	
 	@Override
	public void ledActivate(boolean v) {
		//Colors.out("RadarSystemMainEntryOnPc ledActivate " + v );
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
		ColorsOut.out("RadarSystemMainEntryOnPc | sonarActivate");
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

//----------------------------------------------------------------------
	/*
	 * NON VIENE USATA DAL Controller STRING - è fuori da IApplicationFacade
	 */
	public void entryLocalTest(   ) {
 		
		ledActivate(true);
		Utils.delay(1000);
		ledActivate(false);
		
		sonar.activate();
		Utils.delay(2000);
		
		int d = sonar.getDistance().getVal();
		ColorsOut.out("RadarSystemMainEntryOnPc | d="+d);
		
		terminate();
	}	
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		System.exit(0);
	}

	public static void main( String[] args) throws Exception {
		new RadarSystemMainEntryOnPc("192.168.1.9", null).entryLocalTest( );
	}




}
