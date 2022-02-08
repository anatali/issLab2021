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

public static final String mqttAnswerTopic  = "pctopic";

private ISonar sonar;
private ILed  led ;
private Controller controller;

private String serverHost = "";
private String serverEntry = "";

	
	@Override
	public String getName() {	 
		return "RadarSystemMainUasgeOnPc";
	}

	public void setup(   )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.coap;
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
//		String host           = RadarSystemConfig.raspHostAddr;
//		ProtocolType protocol = RadarSystemConfig.protcolType;
//		  String serverhost  ="";
//		  String serverentry ="";
//
//		if(Utils.isCoap() ) {
//			serverhost      =  RadarSystemConfig.raspHostAddr;
//			String ledUri   =  CoapApplServer.lightsDeviceUri+"/led";
//			String sonarUri =  CoapApplServer.inputDeviceUri+"/sonar";		
//			led    		    = new LedProxyAsClient("ledPxy",     host, ledUri,   protocol );
// 			sonar  		    = new SonarProxyAsClient("sonarPxy", host, sonarUri, protocol );
//		}else if(Utils.isMqtt() ) {
//			serverhost      = RadarSystemConfig.raspHostAddr;  //dont'care
//			String clientId 		= "pc4";
//			String topicToSubscribe = "pctopic";
//			IContext ctx            = Context2021.create(clientId,topicToSubscribe);   //activates! 
//			led    		= new LedProxyAsClient("ledPxy",     host, topicToSubscribe, protocol );
//   		    sonar  		= new SonarProxyAsClient("sonarPxy", host, topicToSubscribe, protocol );
//		}else if(Utils.isTcp() ){ 
// 			String ctxport  = "" +RadarSystemConfig.ctxServerPort;
//			led    		= new LedProxyAsClient("ledPxy",     host, ctxport, protocol );
//   		    sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
// 		}
		
		
		
		
		
		if(Utils.isTcp() ) { 
			serverHost  = RadarSystemConfig.raspHostAddr;
			serverEntry = "" +RadarSystemConfig.ctxServerPort; 
		}
		if(Utils.isMqtt() ) { 
			serverHost  = RadarSystemConfig.mqttBrokerAddr;
			Context2021.create("pv4","pctopic");
			serverEntry = mqttAnswerTopic; 
		}		
		
		if(Utils.isCoap() ) { 
			serverHost  = RadarSystemConfig.raspHostAddr;
			serverEntry = CoapApplServer.lightsDeviceUri+"/led"; 
		}
   		led  = new LedProxyAsClient("ledPxy", serverHost, serverEntry );

 		if(Utils.isCoap() ) { 
 			serverEntry = CoapApplServer.inputDeviceUri+"/sonar"; 
 		}
  		sonar = new SonarProxyAsClient("sonarPxy",  serverHost, serverEntry  );
 		
  		
	}
	
 	
	
	protected void useLedAndSonar() {
	    led.turnOn();
	    Utils.delay(1000);
	    boolean ledState = led.getState();
	    ColorsOut.outappl("led state=" + ledState, ColorsOut.MAGENTA);
 	    led.turnOff();
		ColorsOut.outappl("led state=" + led.getState(), ColorsOut.MAGENTA);
		
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
	    useLedAndSonar();
//	    controller 	= Controller.create( led, sonar );
//		controller.start(endFun, 10);
	    
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
