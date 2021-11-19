package it.unibo.radarWithEnabler;

import it.unibo.enabler.TcpEnabler;

/*
 * Used for PC
 */
public class RadarSystem {
 	
	public void setup() throws Exception {
		//TcpEnabler le,se,rge;
		if( DeviceEnablerActivator.simulation ) {
			 new LedEnabler(DeviceEnablerActivator.ledEnablerPort,     DeviceEnablerActivator.simulation);
			 new SonarEnabler(DeviceEnablerActivator.sonarEnablerPort, DeviceEnablerActivator.simulation);		 
		}
		new RadarGuiEnabler( DeviceEnablerActivator.radarGuiEnablerPort );
		
		//Controller ctrl = 
				new Controller( );
 	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystem().setup();
	}

}
