package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ILed;
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
 
	public void configure() {
		RadarSystemConfig.simulation  = true;
 		RadarSystemConfig.testing     = false;
 		RadarSystemConfig.ledPort     = 8015;
 		RadarSystemConfig.protcolType = ProtocolType.coap;
 		RadarSystemConfig.pcHostAddr  = "localhost";
 		
 		configureTheLedEnablerServer();
 		configureTheLedProxyClients();
 		System.out.println("LedProxyAsClientMain | configure done"  );
	}
	
	protected void configureTheLedEnablerServer() {
		ILed led = DeviceFactory.createLed();
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			ledServer = new EnablerAsServer("LedServer", RadarSystemConfig.ledPort, 
					RadarSystemConfig.protcolType, new LedApplHandler("ledH",led) );
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap){		
			new LedResourceCoap("led", led);
		}
	}
	protected void configureTheLedProxyClients() {		 
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String nameUri        = CoapApplServer.outputDeviceUri+"/led";
		String entry    = protocol==ProtocolType.coap ? nameUri : ""+RadarSystemConfig.ledPort;
		ledClient1      = new LedProxyAsClient("client1", host, entry, protocol );
		ledClient2      = new LedProxyAsClient("client2", host, entry, protocol );	
	}
	
	public void execute() {
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) { ledServer.activate(); }
		ledClient1.turnOn();	
 		System.out.println("LedProxyAsClientMain | ledState from client2=" + ledClient2.getState());
		Utils.delay(1500);
		ledClient2.turnOff();
		System.out.println("LedProxyAsClientMain | ledState from client1=" + ledClient1.getState());
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) ledServer.deactivate();
	}
	public static void main( String[] args)  {
		LedUsageMain  sys = new LedUsageMain();	
		sys.configure();
		sys.execute();
		Utils.delay(2500);
		/*	 */
		System.exit(0);
	}
	
}
