package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class LedAdapterEnablerAsClientMain  {

	public static void main( String[] args) throws Exception {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.ledPort    = 8015;
 		RadarSystemConfig.testing    = false;
  		
		new EnablerAsServer("LedEnablerAsServer",RadarSystemConfig.ledPort, 
				ProtocolType.tcp,  new LedApplHandler("ledH") );

		ILed ledClient1 = new LedAdapterEnablerAsClient(
				"client1", "localhost",RadarSystemConfig.ledPort, ProtocolType.tcp );
		ILed ledClient2 = new LedAdapterEnablerAsClient(
				"client2", "localhost",RadarSystemConfig.ledPort, ProtocolType.tcp );
			
		ledClient1.turnOn();		 
		Thread.sleep(500);
		ledClient2.turnOff();
		Thread.sleep(500);
		/*	 */
		System.exit(0);
	}
	
}
