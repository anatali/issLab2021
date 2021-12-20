package it.unibo.enablerCleanArch.supports.coap;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

class CoapResourceExample extends CoapResource{
String state = "s0";

	public CoapResourceExample(String name) {
		super(name);
		setObservable(true); 
	}
	@Override
	public void handleGET(CoapExchange exchange) {
		String query = exchange.getQueryParameter("q");
		if( query == null ) {
			Colors.out( getName() + "handleGET request=" + exchange.getRequestText() );
			exchange.respond( state );
		}else{
			Colors.out( getName() + "handleGET query  =" + query);
			if( query.equals("time")) exchange.respond( state + " at " + System.currentTimeMillis() );
		}
		
	}
	@Override
	public void handlePUT(CoapExchange exchange) {
		Colors.out( getName() + "handlePUT request=" + exchange.getRequestText() );
		state = state+"_"+exchange.getRequestText();
		changed();
		exchange.respond(CHANGED);
 	}
	@Override
	public void handlePOST(CoapExchange exchange) {
		Colors.out( getName() + "handlePOST request=" + exchange.getRequestText() );
		exchange.respond(CHANGED);
 	}
	@Override
	public void handleDELETE(CoapExchange exchange) {
		Colors.out( getName() + "handleDELETE request= " + exchange.getRequestText() );
		delete();
		exchange.respond(DELETED);
	}	
}


public class CaliforniumUsageExample {
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
		Colors.out(   "configure DONE "  );
		
	}
	public void execute() {
		Colors.out("------------------------ execute", Colors.RED);
 		showTheResource(client);
		modifyTheResource(client,"s1");		
		showTheResource(client);		
	}
	
	public void executeWithObserver() {
		Colors.out("------------------------ executeWithObserver", Colors.RED);
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
		Colors.outappl( "nObsOn_res="+res.getObserverCount() + " obsrelation_isCanceled=" + obsrelation.isCanceled(), Colors.ANSI_PURPLE);		
	}
	
	public void executeQuery() {
		Colors.out("------------------------ executeQuery", Colors.RED);
		String url             = "coap://localhost:5683/root/example/?q=time" ;
		Colors.out(   "executeQuery url=" + url  );
		CoapClient myclient    = new CoapClient( url );
		CoapResponse answer    = myclient.get(  );
		System.out.println("executeQuery | get answer="+answer.getResponseText() + " code=" + answer.getCode());			
		modifyTheResource(myclient, "squery");
	}
	public void executeQuerySameclient() {
		Colors.out("------------------------ executeQuerySameclient", Colors.RED);
		String url           = "coap://localhost:5683/root/example/?q=time" ;
		Colors.outappl(   "executeQuerySameclient: url=" + url, Colors.GREEN  );
		client.setURI(url);
		CoapResponse answer    = client.get(  );
		Colors.outappl("executeQuerySameclient: get answer="+answer.getResponseText() 
		 + " code=" + answer.getCode(), Colors.GREEN);			
		modifyTheResource(client, "squery");
	}

	protected void showTheResource(CoapClient client) {
		CoapResponse answer  = client.get(  );
		Colors.outappl("showTheResource | get answer="+answer.getResponseText() + " code=" + answer.getCode(), Colors.ANSI_PURPLE);		
	}
	protected void modifyTheResource(CoapClient client, String newState) {
		CoapResponse answer  = client.put(newState, 0);
		Colors.outappl("modifyTheResource: put answer="+answer.getResponseText()+ " code=" + answer.getCode(), Colors.ANSI_PURPLE);		
	}
	
	
	public void terminate() {
		coapServer.stop();
		coapServer.destroy();
		Colors.out(   "terminated "  );
	}
	public static void main(String[] args)  {
		CaliforniumUsageExample sys = new CaliforniumUsageExample();
		sys.configure();
		sys.execute();
		//sys.executeQuery();
		sys.executeWithObserver();
		sys.executeQuerySameclient();
		Utils.delay(10000);
		sys.terminate();
	}
}
