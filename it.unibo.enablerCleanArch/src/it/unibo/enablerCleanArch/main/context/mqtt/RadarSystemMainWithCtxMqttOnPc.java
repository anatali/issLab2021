package it.unibo.enablerCleanArch.main.context.mqtt;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Context2021;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;

/*
 * Applicazione che va in coppia con RadarSystemMainDevsCtxOnRasp
 */

public class RadarSystemMainWithCtxMqttOnPc implements IApplication{

private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainWithCtxMqttOnPc";
	}

	public void setup( String configFile )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.mqtt;
 		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.raspHostAddr      = "192.168.1.9";
//		  RadarSystemConfig.ctxServerPort     = 8018;
//		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
		  RadarSystemConfig.tracing           = true;
		  RadarSystemConfig.mqttBrokerAddr    = "tcp://test.mosquitto.org";
	
	}
	
	protected void configure() {
		String clientId 		=  "pc4";
		String topicToSubscribe = "pctopic";
		IContext ctx          = Context2021.create(clientId,topicToSubscribe);   
		//Context2021.create();
		String host           = RadarSystemConfig.raspHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
//		String ctxport        = MqttSupport.topicInput;
		led    		= new LedProxyAsClient("ledPxy",     host, topicToSubscribe, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, topicToSubscribe, protocol );
  		radar  		= DeviceFactory.createRadarGui();
  		controller 	= Controller.create(led, sonar, radar);
	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		execute();
	}
	
	public void execute() {
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
		controller.start(endFun, 30);
 	}
	
 
	public void terminate() {
		sonar.deactivate();
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainWithCtxMqttOnPc().doJob(null);
 	}

}
