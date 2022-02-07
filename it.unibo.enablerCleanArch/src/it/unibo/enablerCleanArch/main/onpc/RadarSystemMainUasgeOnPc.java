package it.unibo.enablerCleanArch.main.onpc;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.context.Context2021;

/*
 * Applicazione che va in coppia con RadarSystemMainDevsCtxOnRasp
 */

public class RadarSystemMainUasgeOnPc implements IApplication{

private ISonar sonar;
private ILed  led ;
private Controller controller;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainUasgeOnPc";
	}

	public void setup(   )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.mqtt;
 		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.raspHostAddr      = "localhost"; //"192.168.1.9";
		  RadarSystemConfig.ctxServerPort     = 8018;
		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
		  RadarSystemConfig.DLIMIT            = 40;
		  RadarSystemConfig.tracing           = true;
		  RadarSystemConfig.mqttBrokerAddr    = "tcp://broker.hivemq.com"; //: 1883  OPTIONAL  "tcp://localhost:1883" 	
	}
	
	protected void configure() {
		String host           = RadarSystemConfig.raspHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		if(Utils.isCoap() ) {
			String ledUri   =  CoapApplServer.lightsDeviceUri+"/led";
			String sonarUri =  CoapApplServer.inputDeviceUri+"/sonar";		
			led    		    = new LedProxyAsClient("ledPxy",     host, ledUri,   protocol );
 			sonar  		    = new SonarProxyAsClient("sonarPxy", host, sonarUri, protocol );
		}else if(Utils.isMqtt() ) {
			String clientId 		= "pc4";
			String topicToSubscribe = "pctopic";
			IContext ctx            = Context2021.create(clientId,topicToSubscribe);   //activates! 
			led    		= new LedProxyAsClient("ledPxy",     host, topicToSubscribe, protocol );
   		    sonar  		= new SonarProxyAsClient("sonarPxy", host, topicToSubscribe, protocol );
		}else { 
 			String ctxport  = "" +RadarSystemConfig.ctxServerPort;
			led    		= new LedProxyAsClient("ledPxy",     host, ctxport, protocol );
   		    sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
 		}
  		//controller 	= Controller.create( led, sonar );
	}
	
 	
	
	protected void useLed() {
//	    led.turnOn();
//	    Utils.delay(1000);
//	    boolean ledState = led.getState();
//	    ColorsOut.outappl("led state=" + ledState, ColorsOut.MAGENTA);
// 	    led.turnOff();
//		ColorsOut.outappl("led state=" + led.getState(), ColorsOut.MAGENTA);
		
		boolean sonarState = sonar.isActive();
		ColorsOut.outappl("sonar state=" + sonarState, ColorsOut.GREEN);
		
		sonar.activate(); 
		ColorsOut.outappl("sonar state=" + sonar.isActive(), ColorsOut.GREEN);
		Utils.delay(1000);
		int d = sonar.getDistance().getVal();
		ColorsOut.outappl("sonar distance=" + d, ColorsOut.GREEN);
		Utils.delay(1000);
		 
		terminate();
		
	}
	public void execute() {
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
	    useLed();
		//controller.start(endFun, 10);
	    
 	}
 
	public void terminate() {
		led.turnOff();
	    //Utils.delay(1000); //give the time for last actions ...
		sonar.deactivate();
		System.exit(0);
	}

 
 

	@Override
	public void doJob(String configFileName) {
		setup( );
		configure();
		execute();
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainUasgeOnPc().doJob(null);
 	}

}
