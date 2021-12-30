package it.unibo.enablerCleanArch.main;

import static org.junit.Assert.assertTrue;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedMockWithGui;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.ContextMqttMsgHandler;
import it.unibo.enablerCleanArch.supports.ContextMsgHandler;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;


public class LedUsageSimpleMain  implements IApplication{

private EnablerAsServer ledServer;
private ILed ledClient1, ledClient2;
private ILed led;

	@Override //IApplication
	public String getName() {
		return "LedUsageMain";
	}
	
	public void setUp( String fName) {
		if( fName != null ) RadarSystemConfig.setTheConfiguration(fName);
		else {
			RadarSystemConfig.simulation  = true;
	 		RadarSystemConfig.testing     = false;
	 		RadarSystemConfig.ledPort     = 8015;
	 		RadarSystemConfig.ledGui      = true;
	 		RadarSystemConfig.protcolType = ProtocolType.mqtt;
	 		RadarSystemConfig.pcHostAddr  = "localhost";
		}
	}
 
	public void configure() {	
 		configureTheLedEnablerServer();
 		configureTheLedProxyClient();
 		System.out.println("LedProxyAsClientMain | configure done"  );
	}
	
	protected void configureTheLedEnablerServer() {
		led = DeviceFactory.createLed();
		ProtocolType protocol = RadarSystemConfig.protcolType;
		if( protocol == ProtocolType.tcp  ) {
			ledServer = new EnablerAsServer("LedServer", RadarSystemConfig.ledPort, 
					RadarSystemConfig.protcolType, new LedApplHandler("ledH",led) );
			ledServer.start(); 
		}else if( RadarSystemConfig.protcolType == ProtocolType.mqtt){		
//			ledServer = new EnablerAsServer("LedServerMqtt",   
//					"topicLedServerMqtt" , new LedApplHandler("ledHMqtt",led) );
			IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);
			IContextMsgHandler  ctxH     = new ContextMqttMsgHandler ( "ctxH" );
			ctxH.addComponent("led", ledHandler);
			EnablerAsServer ctxServer = new EnablerAsServer("CtxServerMqtt",   
					"topicCtxMqtt" , ctxH );			
			ctxServer.start(); 
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap){		
			new LedResourceCoap("led", led);
		}
	}
	protected void configureTheLedProxyClient() {		 
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String entry          = "";
		switch( protocol ) {
			case tcp : entry = ""+RadarSystemConfig.ledPort;
			case coap: entry = CoapApplServer.lightsDeviceUri+"/led";
			case mqtt: entry = "topicCtxMqtt"; 
						//entry = "topic"+ledServer.getName(); 
			
		}
//		String portLedTcp     = ""+RadarSystemConfig.ledPort;
//		String nameUri        = CoapApplServer.lightsDeviceUri+"/led"; 
//		String entry    = protocol==ProtocolType.coap ? nameUri : portLedTcp;
		ledClient1      = new LedProxyAsClient("client1", host, entry, protocol );
 	}
	
	public void execute() {
		Utils.delay(1000);	//To see the startup
    	ledClient1.turnOn();	
 		Utils.delay(1000);
 		boolean curLedstate = ledClient1.getState();
 		Colors.outappl("LedProxyAsClientMain | ledState from client1=" + curLedstate, Colors.GREEN);
 		assertTrue( curLedstate);
 		Utils.delay(1000);
		ledClient1.turnOff();
		Utils.delay(1000);
		curLedstate = ledClient1.getState();
		Colors.outappl("LedProxyAsClientMain | ledState from client1=" + curLedstate, Colors.GREEN);
 		assertTrue( ! curLedstate);
	}
	
	public void terminate() {
		if( led instanceof LedMockWithGui ) { 
			((LedMockWithGui) led).destroyLedGui(  ); 
		}
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) ledServer.stop();
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
		LedUsageSimpleMain  sys = new LedUsageSimpleMain();	
		sys.doJob(null); //"RadarSystemConfig.json"
	}
	
}
