package it.unibo.radarGui21;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONObject;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;


public class OutputDevice extends CoapResource {
private String model         = "unkown";
private int limitDistance    = 0;

	public OutputDevice(String name )  {
		super(name);
 		LOGGER.info("OutputDevice " + name + " | created  ");
	}
	public OutputDevice(String name, String model, int limitDistance )  {
		super(name);
		this.model = model;
		this.limitDistance = limitDistance;
		LOGGER.info("OutputDevice " + name + " | created with model and limitDistance ");
	}
 	
	@Override
	public void handleGET(CoapExchange exchange) {
		String req = exchange.getRequestText();
		System.out.println("OutputDevice " + getName() +
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
		JSONObject jsonObj = new JSONObject( arg );
		String model = jsonObj.getString("model");
		int    limit = jsonObj.getInt("limit");
		//System.out.println(getName() + " | moodel=" + moodel + " from " + exchange.getRequestCode() );
		this.model    = model;
		limitDistance = limit;
 		exchange.respond(CHANGED);
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete();
		exchange.respond(DELETED);
	}
 

	public static void testWithSupport( String resourceUri ) throws  Exception {
		CoapSupport support    = new CoapSupport("coap://localhost:5683", resourceUri);
		String vs              = support.readResource();
 		System.out.println("state init: " + vs);

		JSONObject jsonObj = new JSONObject("{\"model\" : \"HC-SR04\", \"limit\" : 90 }");
		support.updateResource(jsonObj.toString());

 		System.out.println("state end: " + support.readResource());
 
 	}
		public static void main(String[] args) throws  Exception {
		/*
		Create the CoapApplServer
		 */
		CoapApplServer.init();

		/*
		Create distance as the resource of a sonar output-device
		 */
		CoapResource sonar   = new OutputDevice("sonar") ;
		CoapApplServer.addCoapResource( sonar, "/devices/output"  );

		String uri            = "devices/output/sonar";
 		OutputDevice.testWithSupport(uri);

	}

}
