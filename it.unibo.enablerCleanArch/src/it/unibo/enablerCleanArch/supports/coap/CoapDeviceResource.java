package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;

enum deviceType{
	input, output
}
public abstract class CoapDeviceResource extends CoapResource {

	public CoapDeviceResource(String name, deviceType dtype)  {
		super(name);
		setObservable(true); 
 		CoapApplServer.init();
 		if( dtype==deviceType.input ) CoapApplServer.addCoapResource( this, CoapApplServer.inputDeviceUri);
 		else if( dtype==deviceType.output )  CoapApplServer.addCoapResource( this, CoapApplServer.outputDeviceUri);
 		//LOGGER.info("CoapDeviceResource " + name + " | created  ");
	}
 
 	
	@Override
	public void handleGET(CoapExchange exchange) {
		//System.out.println(getName() + " | handleGET arg=" + exchange.getRequestText() );
  		String answer = elaborateGet( exchange.getRequestText() );
  		exchange.respond(answer);
	}

/*
 * POST is NOT idempotent.	Use POST when you want to add a child resource
 */
	@Override
	public void handlePOST(CoapExchange exchange) {
 	}
 	@Override
	public void handlePUT(CoapExchange exchange) {
 		String arg = exchange.getRequestText() ;
 		elaboratePut( arg );
		exchange.respond(CHANGED);
	}

 	protected abstract String elaborateGet(String req);
 	protected abstract void elaboratePut(String req);	
 	


	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete();
		exchange.respond(DELETED);
	}

}
