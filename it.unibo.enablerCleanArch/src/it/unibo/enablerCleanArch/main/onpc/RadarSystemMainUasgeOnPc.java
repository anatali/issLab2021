package it.unibo.enablerCleanArch.main.onpc;

import org.eclipse.californium.core.CoapClient;

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
import it.unibo.enablerCleanArch.supports.mqtt.MqttConnection;

/*
 * Applicazione che va in coppia con RadarSystemMainDevsCtxOnRasp
 */

public class RadarSystemMainUasgeOnPc implements IApplication{

public static final String mqttAnswerTopic  = "pctopic";
public static final String mqttCurClient    = "pc4";

private ISonar sonar;
private ILed  led ;
private Controller controller;

private String serverHost = "";


	
	@Override
	public String getName() {	 
		return "RadarSystemMainUasgeOnPc";
	}

	public void setup(   )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.mqtt;
		  RadarSystemConfig.raspHostAddr      = "localhost"; //"192.168.1.9";
 		  RadarSystemConfig.ctxServerPort     = 8018;
		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
		  RadarSystemConfig.DLIMIT            = 40;
 		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.tracing           = true;
		  RadarSystemConfig.mqttBrokerAddr    = "tcp://broker.hivemq.com"; //: 1883  OPTIONAL  "tcp://localhost:1883" 	
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
 	    controller 	= Controller.create( led, sonar );
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
//		int d = sonar.getDistance().getVal();
//		ColorsOut.outappl("sonar distance=" + d, ColorsOut.GREEN);
		//Utils.delay(1000);
		 
		terminate();
		
	}
	public void execute() {
//	    useLedAndSonar();    
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
 		controller.start(endFun, 10);
 	}
 
	public void terminate() {
		led.turnOff();
 		sonar.deactivate();
		ColorsOut.outappl("BYE", ColorsOut.GREEN);
		Utils.delay(3000);
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
