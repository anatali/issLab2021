package it.unibo.enablerCleanArch.main;

import static org.junit.Assert.assertTrue;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IApplication;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedMockWithGui;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.mqtt.ContextMqttMsgHandler;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;


public class LedUsageMain  implements IApplication{

private EnablerAsServer ledServer;
private TcpContextServer ctxServer;
private ILed ledClient1, ledClient2;
private ILed led;
private String ctxTopic = "topicCtxMqtt";

	@Override //IApplication
	public String getName() {
		return "LedUsageMain";
	}
	
	public void setUp( String fName) {
		if( fName != null ) RadarSystemConfig.setTheConfiguration(fName);
		else {
			RadarSystemConfig.simulation  	= true;
	 		RadarSystemConfig.testing     	= false;
	 		RadarSystemConfig.tracing       = false;
	 		RadarSystemConfig.ledPort     	= 8015;
	 		RadarSystemConfig.ledGui      	= true;
	 		RadarSystemConfig.withContext 	= true;
	 		RadarSystemConfig.protcolType 	= ProtocolType.mqtt;
	 		RadarSystemConfig.pcHostAddr  	= "localhost";
			RadarSystemConfig.mqttBrokerAddr= "tcp://test.mosquitto.org";
		}
	}
 
	public void configure() {	
 		configureTheLedEnablerServer();
 		configureTheLedProxyClient();
 		System.out.println("LedUsageMain | configure done"  );
	}
	
	protected void configureTheLedEnablerServer() {
		led = DeviceFactory.createLed();
		ProtocolType protocol = RadarSystemConfig.protcolType;
		switch( protocol ) {
			case tcp : {				
				if( RadarSystemConfig.withContext ) {
					ctxServer                    = new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
				    IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led) ;
				    ctxServer.addComponent("led", ledHandler);
				    ctxServer.activate();
				}else {
					ledServer = new EnablerAsServer("LedServer", RadarSystemConfig.ledPort, 
							RadarSystemConfig.protcolType, new LedApplHandler("ledH",led) );
					ledServer.start(); 		
				}
				break;
			}
			case coap : { new LedResourceCoap("led", led); break; }
			case mqtt :
				LedApplHandler ledHandler   = new LedApplHandler( "ledH" );
				ledHandler.setTheDevice( led ); //Injection	
 				MqttSupport mqtt        = MqttSupport.getSupport();
				IContextMsgHandler ctxH = mqtt.getHandler();
 				ctxH.addComponent("led", ledHandler);
 				break;
		}
	}
	protected void configureTheLedProxyClient() {		 
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String entry          = "";
		switch( protocol ) {
			case tcp : {
				if( RadarSystemConfig.withContext ) entry =  ""+RadarSystemConfig.ctxServerPort;
				else entry = ""+RadarSystemConfig.ledPort; 
				break;
			}
			case coap: entry = CoapApplServer.lightsDeviceUri+"/led"; break;
			case mqtt: entry = ctxTopic; break;
		}
 		ledClient1      = new LedProxyAsClient("client1", host, entry, protocol );
		ledClient2      = new LedProxyAsClient("client2", host, entry, protocol );	
	}
	
	public void execute() {
		Utils.delay(1000);	//To see the startup
 		ledClient1.turnOn();	
 		Utils.delay(1000);
 		boolean curLedstate = ledClient1.getState();
 		ColorsOut.outappl("LedUsageMain | ledState from client1=" + curLedstate, ColorsOut.GREEN);
 		Utils.delay(200);
 		assertTrue( curLedstate);
		Utils.delay(1000);
		ledClient2.turnOff();
		Utils.delay(500);
		curLedstate = ledClient2.getState();
		ColorsOut.outappl("LedUsageMain | ledState from client2=" + curLedstate, ColorsOut.GREEN);
		assertTrue( ! curLedstate);
	}
	
	public void terminate() {
		ColorsOut.outappl("terminate",ColorsOut.BLUE);
		if( led instanceof LedMockWithGui ) { 
			((LedMockWithGui) led).destroyLedGui(  ); 
		}
		if( RadarSystemConfig.protcolType == ProtocolType.tcp ) {
			if( ! RadarSystemConfig.withContext ) ledServer.stop();
			else ctxServer.deactivate();
		}
		else {
			CoapApplServer.getTheServer().stop();
			CoapApplServer.getTheServer().destroy();
		}
		System.exit(0);
	}
	
	public void doJob( String fName ) {	
 		setUp(fName);
		configure();
		execute();
		Utils.delay(1500);
 		terminate();
	}
 	
	public static void main( String[] args)  {
		LedUsageMain  sys = new LedUsageMain();	
		sys.doJob(null); //"RadarSystemConfig.json"
	}
	
}
