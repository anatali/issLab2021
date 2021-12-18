package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.server.resources.Resource;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 

public class CoapSonarResourceMain   {  

 	 
	public static void main(String[] args) throws  Exception {
	//CONFIGURATION
	RadarSystemConfig.simulation = true;
	RadarSystemConfig.sonarDelay = 100;
	
	//Create sonar resource
	//Resource sonarRes = new CoapSonarResource("sonar", DeviceType.input) ;  //  
	Resource sonarRes = new SonarResourceCoap( "sonar", DeviceFactory.createSonar() ) ;
	//CoapApplObserver obs = 	
		new CoapApplObserver( "localhost", 
				CoapApplServer.inputDeviceUri+"/"+sonarRes.getName() ,
			    new SonarMessageHandler( "sonarH" ) 
		);	
			
 	//USAGE
	String uri = CoapApplServer.inputDeviceUri+"/"+sonarRes.getName();
	System.out.println("uri= " + uri );
	CoapSupport cps = new CoapSupport("localhost", uri );
 	
 
	for( int i= 1; i<=10; i++) {
		String vs = cps.readResource();		//invia GET	
		System.out.println("vs=" + vs );
		Thread.sleep(500);
	}	
	Thread.sleep(1000);
 
	cps.updateResource("stop");
	
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



 
