package it.unibo.radarWithEnabler;

public class DeviceEnablerActivator {
	public static final boolean simulation        = true;
	public static final String  hostAddr          = "localhost";
	public static final int ledEnablerPort        = 8010;
	public static final int sonarEnablerPort      = 8012;
	public static final int radarGuiEnablerPort   = 8014;
	public static final int controllerEnablerPort = 8016;
	
	/*
	 * Used for RaspberryPi 
	 */
	public static void main( String[] args) throws Exception {
		 new LedEnabler( ledEnablerPort,  simulation);
		 new SonarEnabler(sonarEnablerPort, simulation);
	}

}
