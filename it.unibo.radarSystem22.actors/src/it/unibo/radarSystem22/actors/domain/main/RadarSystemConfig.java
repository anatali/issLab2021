package it.unibo.radarSystem22.actors.domain.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.json.JSONObject;
import org.json.JSONTokener;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.utils.ColorsOut;

public class RadarSystemConfig {
	public static  boolean withContext   = true;
	public static  boolean tracing       = false;
	public static  String pcHostAddr     = "192.168.1.132";
	public static  String raspHostAddr   = "192.168.1.4";
	
 	public static  int ctxServerPort     = 8048;
	public static  String mqttBrokerAddr = "tcp://localhost:1883"; //: 1883  OPTIONAL  tcp://broker.hivemq.com
 	
	public static int serverTimeOut       =  600000;  //10 minuti
	
 	public static ProtocolType protcolType = ProtocolType.tcp;

	public static void setTheConfiguration(  ) {
		setTheConfiguration("../CommSystemConfig.json");
	}
	public static void setTheConfiguration( String resourceName ) {
		//Nella distribuzione resourceName è in una dir che include la bin  
		FileInputStream fis = null;
		try {
			ColorsOut.out("%%% setTheConfiguration from file:" + resourceName);
			if(  fis == null ) {
 				 fis = new FileInputStream(new File(resourceName));
			}
	        JSONTokener tokener = new JSONTokener(fis);
	        JSONObject object   = new JSONObject(tokener);
	        
	        pcHostAddr       = object.getString("pcHostAddr");
	        raspHostAddr     = object.getString("raspHostAddr");

 	        
	        ctxServerPort    = object.getInt("ctxServerPort");
	        mqttBrokerAddr   = object.getString("mqttBrokerAddr");
	        
	        switch( object.getString("protocolType") ) {
		        case "tcp"  : protcolType = ProtocolType.tcp; break;
		        case "coap" : protcolType = ProtocolType.coap; break;
		        case "mqtt" : protcolType = ProtocolType.mqtt; break;
	        }
 	        
		} catch (FileNotFoundException e) {
 			ColorsOut.outerr("setTheConfiguration ERROR " + e.getMessage() );
		}

	}	

}
