package it.unibo.enablerCleanArch.supports.coap.example;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;



public class CaliforniumUsageExampleMain {
private CoapServer coapServer;
private ObserverNaive observer;
private CoapClient client;
private CoapResourceExample res;

	public void configure() {
		coapServer               = new CoapServer();
		CoapResource root        = new CoapResource("root");
	    res                      = new CoapResourceExample("example");
		observer				 = new ObserverNaive("obs1");
		
		root.add( res );
		coapServer.add( root );
		coapServer.start();

		String url               = "coap://localhost:5683/root/example" ;
		client                   = new CoapClient( url );
		ColorsOut.out(   "configure DONE "  );
		
	}
	public void execute() {
		ColorsOut.out("------------------------ execute", ColorsOut.RED);
 		showTheResource(client);
		modifyTheResource(client,"s1");		
		showTheResource(client);		
	}
	
	public void executeWithObserver() {
		ColorsOut.out("------------------------ executeWithObserver", ColorsOut.RED);
		CoapObserveRelation obsrelation = client.observe( observer );	//invia una richiesta di osservazione sulla risorsa
		//per ogni PUT (che modifica) vederemo venire eseguita una GET (per l'osservabilità)
		Utils.delay(1000);			//diamo tempo di vedere che l'observer mostra subito lo stato 

		// 		showTheResource(client);
// 		Utils.delay(1000);
		modifyTheResource(client,"sobs");	//modifichiamo la risorsa	
		Utils.delay(1000);	//diamo tempo all'observer di mostrare la modifica
		
 		cancelObserverRelation(obsrelation);
	}
	protected void cancelObserverRelation(CoapObserveRelation obsrelation) {
		obsrelation.proactiveCancel();
		Utils.delay(1000);	//diamo tempo ...
		ColorsOut.outappl( "nObsOn_res="+res.getObserverCount() + " obsrelation_isCanceled=" + obsrelation.isCanceled(), ColorsOut.ANSI_PURPLE);		
	}
	
	public void executeQuery() {
		ColorsOut.out("------------------------ executeQuery", ColorsOut.RED);
		String url             = "coap://localhost:5683/root/example/?q=time" ;
		ColorsOut.out(   "executeQuery url=" + url  );
		CoapClient myclient    = new CoapClient( url );
		CoapResponse answer    = myclient.get(  );
		System.out.println("executeQuery | get answer="+answer.getResponseText() + " code=" + answer.getCode());			
		modifyTheResource(myclient, "squery");
	}
	public void executeQuerySameclient() {
		ColorsOut.out("------------------------ executeQuerySameclient", ColorsOut.RED);
		String url           = "coap://localhost:5683/root/example/?q=time" ;
		ColorsOut.outappl(   "executeQuerySameclient: url=" + url, ColorsOut.GREEN  );
		client.setURI(url);
		CoapResponse answer    = client.get(  );
		ColorsOut.outappl("executeQuerySameclient: get answer="+answer.getResponseText() 
		 + " code=" + answer.getCode(), ColorsOut.GREEN);			
		modifyTheResource(client, "squery");
	}

	protected void showTheResource(CoapClient client) {
		CoapResponse answer  = client.get(  );
		ColorsOut.outappl("showTheResource | get answer="+answer.getResponseText() + " code=" + answer.getCode(), ColorsOut.ANSI_PURPLE);		
	}
	protected void modifyTheResource(CoapClient client, String newState) {
		CoapResponse answer  = client.put(newState, 0);
		ColorsOut.outappl("modifyTheResource: put answer="+answer.getResponseText()+ " code=" + answer.getCode(), ColorsOut.ANSI_PURPLE);		
	}
	
	
	public void terminate() {
		coapServer.stop();
		coapServer.destroy();
		ColorsOut.out(   "terminated "  );
	}
	public static void main(String[] args)  {
		CaliforniumUsageExampleMain sys = new CaliforniumUsageExampleMain();
		sys.configure();
		sys.execute();
		//sys.executeQuery();
		sys.executeWithObserver();
		sys.executeQuerySameclient();
		Utils.delay(10000);
		sys.terminate();
	}
}
