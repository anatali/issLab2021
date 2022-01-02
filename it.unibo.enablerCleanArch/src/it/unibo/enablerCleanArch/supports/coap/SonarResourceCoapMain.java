package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.Resource;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.NaiveMessageHandler;
 

public class SonarResourceCoapMain   {  

 	 
	public static void main(String[] args) throws  Exception {
	//CONFIGURATION
	RadarSystemConfig.simulation = true;
	RadarSystemConfig.sonarDelay = 100;
	
//	SonarObserverFortesting obsfortesting = new SonarObserverFortesting( "obs", false );	

	//Create sonar resource
 	ISonarObservable sonar = DeviceFactory.createSonarObservable();
//	sonar.register( obsfortesting );
	Resource sonarRes = new SonarResourceCoap( "sonar", sonar ) ;
	String sonaruri = CoapApplServer.inputDeviceUri+"/"+sonarRes.getName();
	String sonarAddr = "coap://localhost:5683/"+sonaruri;
	System.out.println("sonaruri= " + sonarAddr );
	
// 	CoapApplObserver obs = 	new CoapApplObserver( "localhost", sonaruri,new SonarMessageHandler( "sonarH" ) );	
 	CoapApplObserver obs = 	new CoapApplObserver( "localhost", sonaruri,new NaiveMessageHandler( "naiveH" ) );	

		 
 	//USAGE
	
//	CoapSupport cps = new CoapSupport("localhost", sonaruri );
	
//	cps.observeResource(obs);
	
	CoapClient client   = new CoapClient( sonarAddr );
	 
	//client.put("activate", MediaTypeRegistry.TEXT_PLAIN);
	CoapObserveRelation obsrelation =  client.observe(obs);
	//client.put("setVal", MediaTypeRegistry.TEXT_PLAIN);
	client.put("activate", MediaTypeRegistry.TEXT_PLAIN);
//	String vs = cps.request("getDistance");		//invia GET	
//	System.out.println("vs=" + vs );
	Utils.delay(500);
	client.put("deactivate", MediaTypeRegistry.TEXT_PLAIN);
  
/* 
	for( int i= 1; i<=1; i++) {
		String vs = cps.request("getDistance");		//invia GET	
		System.out.println("vs=" + vs );
		//Thread.sleep(200);
	}	*/
//	Utils.delay(1500);	
//	cps.forward("stop");

 
//	System.exit(0);
	
/*
	//Altro modo per leggere i dati con CoapClient
	CoapClient client  = new CoapClient( "coap://localhost:5683/"+ uri ); //+"?value=10"
	CoapResponse answer = client.get();
	System.out.println("answer=" + answer.getCode() );
	System.out.println("valore finale=" + answer.getResponseText() );
	String vs = cps.readResource();		//invia GET	
	System.out.println("\nvs final=" + vs );
*/ 
//TERMINATE	
	//obs.
	Utils.delay(1000);	//last observation
	Colors.outappl("TERMINATE", Colors.BLUE);
	//Proactive Observe cancellation: Cancel the observe relation by sending a GET with Observe=1.
	while( ! obsrelation.isCanceled()) {
		Colors.out("attempts to remove the observer ", Colors.BLUE);
		obsrelation.proactiveCancel(); 
		Utils.delay(1000);
	}
	CoapApplServer.stopTheServer();
 	client.shutdown();
 	 
	Colors.outappl("BYE", Colors.BLUE);
	System.exit(0);
	}

}



 
