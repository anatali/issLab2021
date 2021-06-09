package connQak

import org.json.JSONObject
import java.io.File
 
  

object configurator{
//Page
	@JvmStatic public var pageTemplate		= "robotGuiSocket"

//MQTT broker	
	@JvmStatic var mqtthostAddr    	= "broker.hivemq.com"
	@JvmStatic var mqttport    		= "1883"
//
	@JvmStatic var stepsize			= "350" 
	
//Basicrobot application
	@JvmStatic var hostAddr   	    = "192.168.1.65";  //"192.168.1.5";		
	@JvmStatic var port    			= "8020";
	@JvmStatic var qakdest	     	= "basicrobot";
	@JvmStatic var ctxqadest 		= "ctxbasicrobot";
	
	@JvmStatic	//to be used by Java
	fun configure(){
		val configfile =   File("pageConfig.json")
		val config     =   configfile.readText()	//charset: Charset = Charsets.UTF_8
		//println( "		--- configurator | config=$config" )
		val jsonObject	=  JSONObject( config )
		pageTemplate 	=  jsonObject.getString("page") 
		hostAddr    	= jsonObject.getString("host") 
		port    		= jsonObject.getString("port")
		qakdest         = jsonObject.getString("qakdest")
		ctxqadest		= jsonObject.getString("ctxqadest")
		stepsize		= jsonObject.getString("stepsize")
		
		System.out.println( "		--- configurator | pageTemplate=$pageTemplate hostAddr=$hostAddr port=$port stepsize=$stepsize" )
		
	}
}

fun main(){
	println("configurator")
}