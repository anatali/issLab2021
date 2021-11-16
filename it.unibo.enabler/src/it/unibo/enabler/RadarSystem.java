package it.unibo.enabler;

public class RadarSystem {
 	
	public void setup() throws Exception {
		IEnabler le,se,rge;
		if( ! DeviceEnablerActivator.simulation ) {
			 le  = new LedEnabler(DeviceEnablerActivator.ledEnablerPort,     DeviceEnablerActivator.simulation);
			 se  = new SonarEnabler(DeviceEnablerActivator.sonarEnablerPort, DeviceEnablerActivator.simulation);		 
		}
		rge  = new RadarGuiEnabler( DeviceEnablerActivator.radarGuiEnablerPort );
		
		//Controller ctrl = 
				new Controller( );
 	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystem().setup();
	}

}
