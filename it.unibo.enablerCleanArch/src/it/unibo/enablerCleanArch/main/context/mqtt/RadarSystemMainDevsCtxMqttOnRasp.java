package it.unibo.enablerCleanArch.main.context.mqtt;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.Context2021;
import it.unibo.enablerCleanArch.supports.mqtt.MqttContextServer;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArch.supports.tcp.TcpContextServer;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;
 

/*
 * Applicazione che va in coppia con RadarSystemMainWithCtxOnPc
 */

public class RadarSystemMainDevsCtxMqttOnRasp implements IApplication{
	private ISonar sonar;
	private ILed  led ;
 	private IContext contextServer;

	@Override
	public String getName() {	 
		return "RadarSystemMainDevsCtxOnRasp";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
 			RadarSystemConfig.withContext       = true;
		    RadarSystemConfig.protcolType       = ProtocolType.mqtt;
   			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.sonarDelay        = 200;
 			RadarSystemConfig.simulation   		= false;
			RadarSystemConfig.DLIMIT      		= 12;  
			//RadarSystemConfig.mqttBrokerAddr  = "tcp://localhost:1883";  
			//RadarSystemConfig.mqttBrokerAddr  = "tcp://broker.hivemq.com:1883";
			//RadarSystemConfig.mqttBrokerAddr  = "tcp://mqtt.eclipse.org:1883";  //NO
			//RadarSystemConfig.mqttBrokerAddr  = "tcp://test.mosca.io:1883"; //NO
			//RadarSystemConfig.mqttBrokerAddr    = "tcp://test.mosquitto.org";
 
 	}
	
 	
	 	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
		//Creazione del server di contesto
		String clientId 		=  "rasp";
		String topicToSubscribe = MqttSupport.topicInput;
		IContext ctx          = Context2021.create(clientId,topicToSubscribe);   
		//Registrazione dei componenti presso il contesto
		  IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar); 
		  IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);		  
		  contextServer.addComponent("sonar", sonarHandler);	//sonar NAME mandatory
		  contextServer.addComponent("led",   ledHandler);		//led NAME mandatory
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		contextServer.deactivate();	
		System.exit(0);
	}

	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		contextServer.activate();		//dovrei attivare prima, alla configurazione
	}
	
	public static void main( String[] args) throws Exception {
		//ColorsOut.out("Please set RadarSystemConfig.pcHostAddr in RadarSystemConfig.json");
		new RadarSystemMainDevsCtxMqttOnRasp().doJob("RadarSystemConfig.json");
 	}

}
