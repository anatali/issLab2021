package it.unibo.enabler;

public class DeviceEnablerActivator {
	
	public static void main( String[] args) throws Exception {
		LedEnabler led           = new LedEnabler(8010);
		RadarGuiEnabler radarGui = new RadarGuiEnabler(8020);
	}

}
