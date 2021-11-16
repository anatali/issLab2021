package it.unibo.enabler;

public class DeviceEnablerActivator {
	public static final boolean simulation = false;
	public static final String  hostAddr  = "192.168.1.4";
	public static final int ledEnablerPort        = 8010;
	public static final int sonarEnablerPort      = 8012;
	public static final int radarGuiEnablerPort   = 8014;
	public static final int controllerEnablerPort = 8016;
	
	public static void main( String[] args) throws Exception {
		 new LedEnabler( ledEnablerPort,  simulation);
		 new SonarEnabler(sonarEnablerPort, simulation);
	}

}
