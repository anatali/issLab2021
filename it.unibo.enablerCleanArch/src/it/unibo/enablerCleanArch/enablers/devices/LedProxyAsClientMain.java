package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapLedResource;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

public class LedProxyAsClientMain  {

	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
 		RadarSystemConfig.testing    = false;
		String ledPort    			 = "8015";
		ProtocolType protocol 		 = ProtocolType.coap;
		String host     			 = "localhost" ;            

		if( protocol == ProtocolType.tcp) {
			int port = Integer.parseInt(ledPort);
			EnablerAsServer ledServer = 
					new EnablerAsServer("LedEnablerAsServer", port, protocol, new LedApplHandler("ledH") );
			ledServer.activate();
		}else if( protocol == ProtocolType.coap){
			new CoapLedResource("led", DeviceType.input);
		}
		String nameUri = CoapApplServer.inputDeviceUri+"/led";
		String entry   = protocol==ProtocolType.coap ? nameUri : ledPort;
		ILed ledClient1 = new LedProxyAsClient("client1", host, entry, protocol );
		ILed ledClient2 = new LedProxyAsClient("client2", host, entry, protocol );
			
		
		ledClient1.turnOn();	
 		System.out.println("ledState from client2=" + ledClient2.getState());
		Thread.sleep(1500);
		ledClient2.turnOff();
		System.out.println("ledState from client1=" + ledClient1.getState());
		Thread.sleep(2500);
		/*	 */
		System.exit(0);
	}
	
}
