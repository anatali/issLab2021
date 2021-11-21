package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;

/*
 * Adapter (working as a client) for an output device 
 */
public class LedAdapterClient implements ILed {
private Interaction2021 conn;
private boolean ledStateMirror = false;

	public LedAdapterClient(   ) {
		try {
			conn = TcpClient.connect( RadarSystemConfig.raspHostAddr, RadarSystemConfig.ledPort);
		} catch (Exception e) {
			System.out.println( "LedAdapterClient |  ERROR " + e.getMessage());
		}
 	}
	@Override
	public void turnOn() { 
		try {
			conn.forward("on");
			ledStateMirror = true;
		} catch (Exception e) {
			System.out.println( "LedAdapterClient |  ERROR " + e.getMessage());
		}  
	}

	@Override
	public void turnOff() {   
		try {
			conn.forward("off");
			ledStateMirror = false;
		} catch (Exception e) {
			System.out.println( "LedAdapterClient |  ERROR " + e.getMessage());
		}  		
	}

	@Override
	public boolean getState() {   
		return ledStateMirror;
	}

}
