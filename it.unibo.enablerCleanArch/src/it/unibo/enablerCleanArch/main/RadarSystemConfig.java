package it.unibo.enablerCleanArch.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONObject;
import org.json.JSONTokener;

public class RadarSystemConfig {
	public static  boolean simulation           = true;
	
	public static  boolean ControllerRemote     = true;
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

	public static int sonarDelay          =  100;     
	public static int DLIMIT              =  15;     

	public static boolean testing         = false;			

	public static void setTheConfiguration(  ) {
		setTheConfiguration("../RadarSystemConfig.json");
	}
	
	public static void setTheConfiguration( String resourceName ) {
		FileInputStream fis = null;
 		System.out.println("setTheConfiguration " + resourceName ); 
 		try {
			//String resourceName = "../RadarSystemConfig.json";	
			//Nella distribuzione il file è in una dir che include la bin  
			fis                 = new FileInputStream(new File(resourceName));
 		} catch (FileNotFoundException e) {
 			//System.out.println("setTheConfiguration configuration file not found yet" );
		}
		try {
			if(  fis == null ) {
				 //String resourceName = "./RadarSystemConfig.json";
				 fis = new FileInputStream(new File(resourceName));
			}
	        JSONTokener tokener = new JSONTokener(fis);
	        JSONObject object   = new JSONObject(tokener);
	 		
	        simulation          = object.getBoolean("simulation");
	        
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
	        testing          = object.getBoolean("testing");
	        
		} catch (FileNotFoundException e) {
 			System.out.println("setTheConfiguration ERROR " + e.getMessage() );
		}

	}	
	 
}
