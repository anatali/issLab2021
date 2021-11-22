package it.unibo.enablerCleanArch.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RadarSystemConfig {
	public static  boolean simulation           = true;
	
	public static  boolean ControllerRemote     = false;
	public static  boolean LedRemote            = false;			
	public static  boolean SonareRemote         = false;			
	public static  boolean RadarGuieRemote      = false;			
	
	public static  String pcHostAddr            = "192.168.1.132";
	public static  String raspHostAddr          = "192.168.1.4";
	
	public static  int radarGuiPort      = 8014;
	public static  int ledPort           = 8010;
	public static  int sonarPort         = 8012;
	public static  int controllerPort    = 8016;
 	
	public static int serverTimeOut       =  600000;  //10 minuti
	public static int applStartdelay      =  5000;     

	public static int sonarDelay          =  500;  
	
	public static int DLIMIT              =  15;     
	
	public static void setTheConfiguration(  ) {
 		String resourceName = "./RadarSystemConfig.json";
 		/*
 		System.out.println(""+ mainclass);
        InputStream is      = mainclass.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("RadarSystemConfig | Cannot find resource file " + resourceName);
        }
        */
         
 
		try {
			FileInputStream fis = new FileInputStream(new File(resourceName));
	        JSONTokener tokener = new JSONTokener(fis);
	        JSONObject object   = new JSONObject(tokener);
	 		
	        simulation       = object.getBoolean("simulation");
	        
	        ControllerRemote = object.getBoolean("ControllerRemote");
	        LedRemote        = object.getBoolean("LedRemote");
	        SonareRemote     = object.getBoolean("SonareRemote");
	        RadarGuieRemote  = object.getBoolean("RadarGuieRemote");
	        
	        pcHostAddr       = object.getString("pcHostAddr");
	        raspHostAddr     = object.getString("raspHostAddr");

	        ledPort          = object.getInt("ledPort");
	        radarGuiPort     = object.getInt("radarGuiPort");
	        sonarPort        = object.getInt("sonarPort");
	        controllerPort   = object.getInt("controllerPort");		
	        
	        applStartdelay   = object.getInt("applStartdelay");		        
	        sonarDelay       = object.getInt("sonarDelay");	
	        
	        DLIMIT           = object.getInt("DLIMIT");	
	        
	        
	        
		} catch (FileNotFoundException e) {
 			e.printStackTrace();
		}

	}	
	 
}
