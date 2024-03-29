package it.unibo.enablerCleanArch.supports.coap.example;

import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.server.resources.Resource;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;

public class CoapSupportExampleSonarMain {
private CoapSupport cps;

	public void configure() {
		RadarSystemConfig.simulation = true;
		RadarSystemConfig.sonarDelay = 100;
		
		//Create sonar resource
		Resource sonarRes  = new SonarResourceCoap( "sonar", DeviceFactory.createSonar() ) ;
		//Create the CoapSupport for the resource
		String sonaruri = CoapApplServer.inputDeviceUri +"/"+ sonarRes.getName(); 
 		cps = new CoapSupport("localhost", sonaruri );
		
//		CoapApplObserver obs = 	new CoapApplObserver( "localhost", sonaruri,new SonarMessageHandler( "sonarH" ) );	
		
	}
	public void execute() {
		ColorsOut.out("------------------------ execute", ColorsOut.RED);
		try {
			cps.forward("activate");
			Utils.delay(200);
			for( int i=1; i<=3; i++ ) {
				String vs = cps.request("getDistance");
				ColorsOut.outappl("execute: current distance="+vs, ColorsOut.BLUE);
				Utils.delay(200);
			}
			cps.forward("deactivate");
		} catch (Exception e) {
			ColorsOut.outerr("execute error"+ e.getMessage());	 
 		}	
	}
	public void executeWithObserver() {
		ColorsOut.out("------------------------ executeWithObserver", ColorsOut.RED);
		try {
			CoapObserveRelation relObs1 = cps.observeResource( new ObserverNaive("obs1") );
			CoapObserveRelation relObs2 = cps.observeResource( new ObserverNaive("obs2") );
			cps.forward("activate");
			for( int i=1; i<=3; i++ ) {
				String vs = cps.request("getDistance");
				ColorsOut.outappl("executeWithObserver: distance i=" + i + " vs="+vs, ColorsOut.BLUE);
				Utils.delay(200);
			}
			Utils.delay(300);
			cps.removeObserve(relObs1);
			Utils.delay(200);
			cps.removeObserve(relObs2);
			Utils.delay(200);
			cps.forward("deactivate");
		} catch (Exception e) {
			ColorsOut.outerr("executeWithObserver error"+ e.getMessage());	 
 		}	
	}
	
	public void terminate() {
		cps.close();
		CoapApplServer.getTheServer().stop(); 
		CoapApplServer.getTheServer().destroy();
		ColorsOut.outappl("terminate DONE", ColorsOut.BLUE);
		System.exit(0);
	}
	
	public static void main(String[] args) throws  Exception {
		CoapSupportExampleSonarMain sys = new CoapSupportExampleSonarMain();
		sys.configure();
		sys.execute();
		sys.executeWithObserver();
		//sys.execute();	//Again, to see if client ...
		sys.terminate();
	}
}
