package it.unibo.enablerCleanArch.main;

public class RadarSystemConfig {
	public static final boolean simulation           = true;
	
	public static final boolean ledRemote            = false;			
	public static final boolean SonareRemote         = false;			
	public static final boolean RadarGuieRemote      = false;			
	
	public static final String pcHostAddr            = "192.168.1.132";
	public static final String raspHostAddr          = "192.168.1.4";
	
	public static final int radarGuiPort      = 8014;
	public static final int ledPort           = 8010;
	public static final int sonarPort         = 8012;
	public static final int controllerPort    = 8016;
	
	
	public static int serverTimeOut           =  600000;  //10 minuti

}
