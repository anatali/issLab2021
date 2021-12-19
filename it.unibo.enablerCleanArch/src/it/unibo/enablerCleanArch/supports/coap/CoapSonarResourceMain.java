package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.Resource;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Utils;
 

public class CoapSonarResourceMain   {  

 	 
	public static void main(String[] args) throws  Exception {
	//CONFIGURATION
	RadarSystemConfig.simulation = true;
	RadarSystemConfig.sonarDelay = 100;
	
	//Create sonar resource
	//Resource sonarRes = new CoapSonarResource("sonar", DeviceType.input) ;  //  
	Resource sonarRes = new SonarResourceCoap( "sonar", DeviceFactory.createSonar() ) ;
	
	String sonaruri = CoapApplServer.inputDeviceUri+"/"+sonarRes.getName();
	String sonarAddr = "coap://localhost:5683/"+sonaruri;
	System.out.println("sonaruri= " + sonarAddr );

	CoapApplObserver obs = 	new CoapApplObserver( "localhost", sonaruri,new SonarMessageHandler( "sonarH" ) );	
		 
 	//USAGE
	
//	CoapSupport cps = new CoapSupport("localhost", sonaruri );
	
//	cps.observeResource(obs);
	
	CoapClient client   = new CoapClient( sonarAddr );
	//client.put("activate", MediaTypeRegistry.TEXT_PLAIN);
	client.observe(obs);
	//client.put("setVal", MediaTypeRegistry.TEXT_PLAIN);
	client.put("activate", MediaTypeRegistry.TEXT_PLAIN);
//	String vs = cps.request("getDistance");		//invia GET	
//	System.out.println("vs=" + vs );
/* 
	for( int i= 1; i<=1; i++) {
		String vs = cps.request("getDistance");		//invia GET	
		System.out.println("vs=" + vs );
		//Thread.sleep(200);
	}	*/
	Utils.delay(1500);	
//	cps.forward("stop");

 
	System.exit(0);
	
/*
	//Altro modo per leggere i dati con CoapClient
	CoapClient client  = new CoapClient( "coap://localhost:5683/"+ uri ); //+"?value=10"
	CoapResponse answer = client.get();
	System.out.println("answer=" + answer.getCode() );
	System.out.println("valore finale=" + answer.getResponseText() );
	String vs = cps.readResource();		//invia GET	
	System.out.println("\nvs final=" + vs );
*/ 
	}

}



 
