package it.unibo.enablerCleanArch.main;

public class RadarSystemConfig {
	public static final boolean simulation           = true;
	
	public static final boolean ledRemote            = false;			
	public static final boolean SonareRemote         = false;			
	public static final boolean RadarGuieRemote      = false;			
	
	public static final String radarGuiHostAddr      = "localhost";
	public static final int radarGuiEnablerPort      = 8014;
	public static final int ledEnablerPort           = 8010;
	public static final int sonarEnablerPort         = 8012;
	public static final int controllerEnablerPort    = 8016;

}
