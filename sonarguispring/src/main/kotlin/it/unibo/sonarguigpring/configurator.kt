package it.unibo.sonarguigpring

import org.json.JSONObject
import java.io.File
import java.net.InetAddress
//import java.nio.charset.Charset
//import org.apache.commons.io.Charsets
  

object configurator{
//Page
	@JvmStatic public var pageTemplate		= "robotGuiSocket"

//MQTT broker	
//	@JvmStatic var mqtthostAddr    	= "broker.hivemq.com"
	@JvmStatic var mqtthostAddr    	= "localhost"
	@JvmStatic var mqttport    		= "1883"
//
	@JvmStatic var stepsize			= "350" 
	
 //Domains application
 	@JvmStatic var hostAddr   	    = "localhost";  //"192.168.1.5";		
 	@JvmStatic var port    			= "8028";
 	@JvmStatic var qakdest	     	= "sonarresource";
 	@JvmStatic var ctxqadest 		= "ctxsonarresource";

}

