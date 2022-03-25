package it.unibo.radarSystem22.actors.businessLogic;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONObject;
import org.json.JSONTokener;

import it.unibo.radarSystem22.domain.utils.ColorsOut;


public class BusinessLogicConfig {
 	public static boolean tracing         = false;	
	public static boolean testing         = false;			
	public static int DLIMIT              = 70;     	
 	
	
	public static void setTheConfiguration(  ) {
		setTheConfiguration("../BusinessLogicConfig.json");
	}
	
	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName è in una dir che include la bin  
		FileInputStream fis = null;
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);
			if(  fis == null ) {
 				 fis = new FileInputStream(new File(resourceName));
			}
//	        JSONTokener tokener = new JSONTokener(fis);
			Reader reader       = new InputStreamReader(fis);
			JSONTokener tokener = new JSONTokener(reader);      
	        JSONObject object   = new JSONObject(tokener);
	 		
   	        tracing          = object.getBoolean("tracing");
	        testing          = object.getBoolean("testing");
 	        DLIMIT           = object.getInt("DLIMIT");	
 	        
		} catch (FileNotFoundException e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
		}

	}	
	 
}
