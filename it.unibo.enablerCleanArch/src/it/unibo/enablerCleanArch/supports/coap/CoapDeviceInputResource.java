package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONObject;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;


public class CoapDeviceInputResource extends CoapResource {
private String model         = "unkown";
private int limitDistance    = 0;
private ApplMessageHandler handler;

	public CoapDeviceInputResource(String name,  ApplMessageHandler handler)  {
		super(name);
		this.handler=handler;
		//this.addObserver(handler);
 		LOGGER.info("CoapDeviceInputResource " + name + " | created  ");
	}
 
 	
	@Override
	public void handleGET(CoapExchange exchange) {
		String req = exchange.getRequestText();
		System.out.println("CoapDeviceInputResource " + getName() +
				" | handleGET from:" + exchange.getSourceAddress() + " arg:" + req  );
		exchange.respond( "model=" + model + " limit=" + limitDistance);
	}

/*
 * POST is NOT idempotent.	Use POST when you want to add a child resource
 */
	@Override
	public void handlePOST(CoapExchange exchange) {
		exchange.accept();		
		if (exchange.getRequestOptions().isContentFormat(MediaTypeRegistry.TEXT_PLAIN)) {
			String xml = exchange.getRequestText();
			exchange.respond(CREATED, xml.toUpperCase());
			
		} else {
			// ...
			exchange.respond(CREATED);
		}
	}
 	@Override
	public void handlePUT(CoapExchange exchange) {
		String arg = exchange.getRequestText() ;
		elaborate( arg );
 		exchange.respond(CHANGED);
	}
 	
	protected void elaborate(String arg) {
		//System.out.println(getName() + " | elaborate arg=" + arg );
		try {
			JSONObject jsonObj = new JSONObject( arg );
			String model = jsonObj.getString("model");
			int    limit = jsonObj.getInt("limit");
			//System.out.println(getName() + " | moodel=" + model + " limit="+limit+" from " + exchange.getRequestCode() );
			this.model    = model;
			limitDistance = limit;
		}catch( Exception e) {
			limitDistance = Integer.parseInt(arg);
		}
		handler.elaborate(arg);
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete();
		exchange.respond(DELETED);
	}

}
