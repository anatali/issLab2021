package it.unibo.enablerCleanArch.main.onpc;

import org.eclipse.californium.core.CoapClient;
import it.unibo.enablerCleanArch.domain.IApplicationFacade;
import it.unibo.enablerCleanArch.domain.ILed;
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

//	@Override
//	public String getName() {	 
//		return "RadarSystemMainEntryOnPc";
//	}
	
	
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
