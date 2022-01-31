package it.unibo.enablerCleanArch.supports.coap.example;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import it.unibo.enablerCleanArch.supports.ColorsOut;

class CoapResourceExample extends CoapResource{
String state = "s0";

	public CoapResourceExample(String name) {
		super(name);
		setObservable(true); 
	}
	@Override
	public void handleGET(CoapExchange exchange) {
		ColorsOut.out( getName() + " handleGET exchange=" + exchange);
		String query = exchange.getQueryParameter("q");
		if( query == null ) {
			ColorsOut.out( getName() + " handleGET request=" + exchange.getRequestText() );
			exchange.respond( state );
		}else{
			ColorsOut.out( getName() + " handleGET query=" + query);
			if( query.equals("time")) exchange.respond( state + " at " + System.currentTimeMillis() );
		}
		
	}
	@Override
	public void handlePUT(CoapExchange exchange) {
		ColorsOut.out( getName() + " handlePUT request=" + exchange.getRequestText() );
		state = state+"_"+exchange.getRequestText();
		changed();
		exchange.respond(CHANGED);
 	}
	@Override
	public void handlePOST(CoapExchange exchange) {
		ColorsOut.out( getName() + " handlePOST request=" + exchange.getRequestText() );
		exchange.respond(CHANGED);
 	}
	@Override
	public void handleDELETE(CoapExchange exchange) {
		ColorsOut.out( getName() + " handleDELETE request= " + exchange.getRequestText() );
		delete();
		exchange.respond(DELETED);
	}	
}

