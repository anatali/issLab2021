package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unibo.enablerCleanArch.supports.Colors;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;


public abstract class CoapDeviceResource extends CoapResource {

	public CoapDeviceResource(String name, DeviceType dtype)  {
		super(name);
		setObservable(true); 
		CoapApplServer coapServer = CoapApplServer.getServer(); //SINGLETION
 		if( dtype==DeviceType.input )        coapServer.addCoapResource( this, CoapApplServer.inputDeviceUri);
 		else if( dtype==DeviceType.output )  coapServer.addCoapResource( this, CoapApplServer.outputDeviceUri);
 		//LOGGER.info("CoapDeviceResource " + name + " | created  ");
	}
 
 	
	@Override
	public void handleGET(CoapExchange exchange) {
		Colors.out(getName() + " | handleGET arg=" + exchange.getRequestText() + " param=" + exchange.getQueryParameter("q"));
  		String answer = elaborateGet( exchange.getQueryParameter("q") );
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
