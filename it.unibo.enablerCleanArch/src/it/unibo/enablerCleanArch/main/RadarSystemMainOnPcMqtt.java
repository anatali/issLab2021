package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArch.supports.mqtt.SonarObserverHandlerNaive;


/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRaspMqtt
 */

public class RadarSystemMainOnPcMqtt implements IApplication{
private IRadarDisplay radar = null;
private ILed   ledClient;
private ISonar sonarClient;
private boolean ledblinking = false;
private MqttSupport mqtt;

    public RadarSystemMainOnPcMqtt(){
    }
    
	@Override
	public String getName() {	 
		return "RadarSystemMainOnPcMqtt";
	}
	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();    	
		execute();
	}

	protected void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		//else {
			RadarSystemConfig.simulation   		= false;
			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 12; //55
			RadarSystemConfig.mqttBrokerAddr    = "tcp://test.mosquitto.org"; //: 1883  OPTIONAL  tcp://broker.hivemq.com
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;
			//RadarSystemConfig.withContext       = true;
		//}
 	}
	
	public void configure()  {			
 		//radar  = DeviceFactory.createRadarGui();	
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		mqtt = MqttSupport.getSupport("pc", "pctopic");
		mqtt.subscribe("sonarDataTopic", new SonarObserverHandlerNaive("sonarDataHOnPc") );
		
		String ctxTopic       = MqttSupport.topicOut;
 		ledClient             = new LedProxyAsClient("clientLed", host, ctxTopic, protocol );
 		sonarClient           = new SonarProxyAsClient("clientSonar", host, ctxTopic, protocol );
	} 
 	
	public void ledActivate( boolean v ) {
		if( v ) ledClient.turnOn();
		else ledClient.turnOff();
	}
	
	public String ledState(   ) {
		return ""+ledClient.getState();
	}
	public String sonarDistance(   ) {
		return ""+sonarClient.getDistance();
	}
	
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
	public void stopLedBlink() {
		ledblinking = false;
	}
	
	public void execute() {
	//Segnale di vita ...	
  		ledActivate(true);		
// 		ColorsOut.outappl("Led state="+ledState(), ColorsOut.GREEN);
// 
    	Utils.delay(500);
  		ledActivate(false);
// 		ColorsOut.outappl("Led state="+ledState(), ColorsOut.GREEN);
 
 		//Utils.delay(5000);
/*
//		doLedBlink();
//		Utils.delay(3000);
//		stopLedBlink();
		
//		String ledstate = ledState(   );
//		ColorsOut.outappl("Led state="+ledstate, ColorsOut.GREEN);
		
		try {
			ColorsOut.outappl("Please hit to restart ", ColorsOut.ANSI_PURPLE);
			int v = System.in.read();
		} catch (IOException e) {
				e.printStackTrace();
		}
 
		
*/ 
		
//			ColorsOut.outappl("Led state="+ledState(), ColorsOut.GREEN);
//			ColorsOut.outappl("sonar active="+sonarClient.isActive(), ColorsOut.GREEN);

		boolean b = sonarClient.isActive();			
		ColorsOut.outappl("Sonar active="+b, ColorsOut.GREEN);	
		
		
  		ColorsOut.outappl("ACTIVATE THE SONAR", ColorsOut.BLACK); 
			sonarClient.activate();			
			b = sonarClient.isActive();			
			ColorsOut.outappl("Sonar active="+b, ColorsOut.GREEN);			
			/*
			 	while( ! b ) {
				ColorsOut.outappl("Sonar not active .. =", ColorsOut.GREEN);
				Utils.delay(2500);
				b = sonarClient.isActive();
			}
 			*/
				for( int i=1; i<=15; i++) {
	 				int d = sonarClient.getDistance().getVal();
					ColorsOut.outappl("Sonar state i=" + i + " -> "+d, ColorsOut.GREEN);
					if( d < 10 ) ledActivate(true);	//RadarSystemConfig.DLIMIT
					else ledActivate(false);	
//					Utils.delay(200);   //Con QoS = 2 sono 4 messaggi scambiati
					//TODO: passare a uno schema di sonar observable  
				}
		
		
		Utils.delay(1000);			
		sonarClient.deactivate();
		ColorsOut.outappl("Sonar deactivate ", ColorsOut.GREEN);
 		terminate();
	}

	public void terminate() {
		try {
			MqttSupport.getSupport().close();
			ColorsOut.outappl("BYE BYE ", ColorsOut.GREEN);
		} catch (Exception e) {
			ColorsOut.outerr("terminate ERROR:" + e.getMessage());
 		}
		//System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcMqtt().doJob(null);
 	}

 
}
