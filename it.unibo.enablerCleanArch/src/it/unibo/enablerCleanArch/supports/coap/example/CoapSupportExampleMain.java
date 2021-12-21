package it.unibo.enablerCleanArch.supports.coap.example;

import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;

public class CoapSupportExampleMain {
private CoapSupport cps;
private CoapServer coapServer;
private CoapResourceExample res;

	public void configure() {
		coapServer               = new CoapServer();
 		
		//Create the resource
		Resource resource = new CoapResourceExample("example");

		CoapResource root = new CoapResource("root");
	    res               = new CoapResourceExample("example");
 		
		root.add( res );
		coapServer.add( root );
		coapServer.start();
 		
		//Create the CoapSupport for the resource
		String resourceuri = "root/"+resource.getName();
		cps = new CoapSupport("localhost", resourceuri );
		
	}
	public void execute() {
		Colors.out("------------------------ execute", Colors.RED);
		try {
			for( int i=1; i<=3; i++ ) {
				String vs = cps.request("");
				Colors.outappl("execute: current state="+vs, Colors.BLUE);
				cps.forward("s"+i);
				Utils.delay(200);
			}
			//cps.forward("deactivate");
		} catch (Exception e) {
			Colors.outerr("execute error:"+ e.getMessage());	 
 		}	
	}
	public void executeWithObserver() {
		Colors.out("------------------------ executeWithObserver", Colors.RED);
		try {
			CoapObserveRelation rel1 = cps.observeResource( new ObserverNaive("obs1") );
			CoapObserveRelation rel2 = cps.observeResource( new ObserverNaive("obs2") );
			//cps.forward("activate");
			for( int i=1; i<=3; i++ ) {
				String vs = cps.request("");
				Colors.outappl("executeWithObserver: state i=" + i + " vs="+vs, Colors.GREEN);
				cps.forward("s"+i);
				Utils.delay(200);
			}
			Utils.delay(300);
			cps.removeObserve(rel1);
			Utils.delay(200);
			cps.removeObserve(rel2);
			Utils.delay(200);
		} catch (Exception e) {
			Colors.outerr("executeWithObserver error"+ e.getMessage());	 
 		}	
	}
	
	public void terminate() {
		coapServer.stop();
		coapServer.destroy();
		cps.close();
 		Colors.outappl("terminate DONE", Colors.BLUE);
	}
	
	public static void main(String[] args) throws  Exception {
		CoapSupportExampleMain sys = new CoapSupportExampleMain();
		sys.configure();
		//sys.execute();
		sys.executeWithObserver();
		//sys.execute();	//Again, to see if client ...
		sys.terminate();
	}
}
