package it.unibo.enablerCleanArch.main;

import static org.junit.Assert.assertTrue;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedMockWithGui;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.LedApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.LedProxyAsClient;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;


public class LedUsageMain  {

private EnablerAsServer ledServer;
private ILed ledClient1, ledClient2;
private ILed led;

	public void setUp( String fName) {
		if( fName != null ) RadarSystemConfig.setTheConfiguration(fName);
		else {
			RadarSystemConfig.simulation  = true;
	 		RadarSystemConfig.testing     = false;
	 		RadarSystemConfig.ledPort     = 8015;
	 		RadarSystemConfig.protcolType = ProtocolType.coap;
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
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			ledServer = new EnablerAsServer("LedServer", RadarSystemConfig.ledPort, 
					RadarSystemConfig.protcolType, new LedApplHandler("ledH",led) );
			ledServer.start(); 
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap){		
			new LedResourceCoap("led", led);
		}
	}
	protected void configureTheLedProxyClient() {		 
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String portLedTcp     = ""+RadarSystemConfig.ledPort;
		String nameUri        = CoapApplServer.lightsDeviceUri+"/led"; 
		String entry    = protocol==ProtocolType.coap ? nameUri : portLedTcp;
		ledClient1      = new LedProxyAsClient("client1", host, entry, protocol );
		ledClient2      = new LedProxyAsClient("client2", host, entry, protocol );	
	}
	
	public void execute() {
 		ledClient1.turnOn();	
 		boolean curLedstate = ledClient2.getState();
 		System.out.println("LedProxyAsClientMain | ledState from client2=" + curLedstate);
 		assertTrue( curLedstate);
		Utils.delay(1500);
		ledClient2.turnOff();
		curLedstate = ledClient1.getState();
		System.out.println("LedProxyAsClientMain | ledState from client1=" + curLedstate);
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
	}
	public static void main( String[] args)  {
		LedUsageMain  sys = new LedUsageMain();	
		sys.setUp(null);
		sys.configure();
		sys.execute();
		Utils.delay(2500);
 		sys.terminate();
		//System.exit(0);
	}
	
}
