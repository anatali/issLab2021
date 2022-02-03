package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArch.supports.mqtt.SonarDataObserverHandler;
import it.unibo.enablerCleanArch.useCases.LedAlarmUsecase;
import it.unibo.enablerCleanArch.useCases.RadarGuiUsecase;


/*
 * Applicazione che va in coppia con RadarSystemDevicesOnRaspMqtt
 */

public class RadarSystemMainOnPcMqtt implements IApplication{
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
	
	

	protected void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		//else {
			RadarSystemConfig.simulation   		= false;
			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.DLIMIT      		= 12; //55
			RadarSystemConfig.mqttBrokerAddr    = "tcp://test.mosquitto.org"; //: 1883  OPTIONAL  "tcp://localhost:1883"; // tcp://broker.hivemq.com
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;
			//RadarSystemConfig.withContext       = true;
		//}
 	}
	
	public void configure()  {					
		mqtt                  = MqttSupport.getSupport("pc2", "pctopic");
		
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxTopic       = MqttSupport.topicOut;
 		ledClient             = new LedProxyAsClient("clientLed", host, ctxTopic, protocol );
  		sonarClient           = new SonarProxyAsClient("clientSonar", host, ctxTopic, protocol );
 		
	} 
	
	protected void configureWithSonarObservable() {
  		mqtt.subscribe("sonarDataTopic", new SonarDataObserverHandler("sonarDataHOnPc", ledClient) );		
	}
	protected void configureForController() {
		
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
	
	
	protected void workWithLed() {
  		ledActivate(true);		
 		ColorsOut.outappl("Led state="+ledState(), ColorsOut.GREEN);
    	Utils.delay(500);
  		ledActivate(false);
 		ColorsOut.outappl("Led state="+ledState(), ColorsOut.GREEN);
  		Utils.delay(500);		
	}
	
	protected void workAsTheController() {
		configureForController();
		IRadarDisplay radar  = DeviceFactory.createRadarGui();	
		Utils.delay(2000);
  		ColorsOut.outappl("ACTIVATE THE SONAR", ColorsOut.BLACK); 
		sonarClient.activate();			
		for( int i=1; i<=10; i++) {
			int d = sonarClient.getDistance().getVal();
			IDistance distanceAmpl = new Distance(d*5);  //Amplifico il dato ...
			IDistance distance     = new Distance( d );  //Per il controllo del led ...
			ColorsOut.outappl("Sonar distance i=" + i + " -> "+d, ColorsOut.GREEN);
			RadarGuiUsecase.doUseCase( radar, distanceAmpl  );	//
			LedAlarmUsecase.doUseCase( ledClient,  distance  );  //Meglio inviare un msg su una coda
			//Utils.delay(200);   //Con QoS = 2 sono 4 messaggi scambiati
			//TODO: passare a uno schema di sonar observable  
		}
		ColorsOut.outappl("Sonar deactivate ", ColorsOut.GREEN);
		ledActivate(false);	 
		sonarClient.deactivate();		
	}
	protected void workWithSonarObservable() {
		configureWithSonarObservable();
		boolean b = sonarClient.isActive();			
		ColorsOut.outappl("Sonar active="+b, ColorsOut.GREEN);		
		
  		ColorsOut.outappl("ACTIVATE THE SONAR", ColorsOut.BLACK); 
		sonarClient.activate();			
		b = sonarClient.isActive();			
		ColorsOut.outappl("Sonar active="+b, ColorsOut.GREEN);			
		Utils.delay(3000);
		ColorsOut.outappl("Sonar deactivate ", ColorsOut.GREEN);
		sonarClient.deactivate();
	}
	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();    	
		execute();
	}


	public void execute() {
		//workWithLed();
		//workWithSonarObservable();
		workAsTheController();
		Utils.delay(1000);
  		terminate();
	}
  		
 
//		doLedBlink();
//		stopLedBlink();
 



 
// 	public IRadarDisplay getRadarGui() {
//		return radar;
//	}

	public void terminate() {
		try {
			MqttSupport.getSupport().close();
			ColorsOut.outappl("BYE BYE ", ColorsOut.GREEN);
		} catch (Exception e) {
			ColorsOut.outerr("terminate ERROR:" + e.getMessage());
 		}
		//System.exit(0);
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcMqtt().doJob(null);
 	}

 
}
