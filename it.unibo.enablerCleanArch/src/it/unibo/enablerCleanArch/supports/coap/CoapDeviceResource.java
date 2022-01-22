package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unibo.enablerCleanArch.supports.Colors;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;


public abstract class CoapDeviceResource extends CoapResource {

	public CoapDeviceResource(String name, DeviceType dtype)  {
		super(name);
		setObservable(true); 
		CoapApplServer coapServer = CoapApplServer.getTheServer();  //SINGLETION
 		if( dtype==DeviceType.input )        coapServer.addCoapResource( this, CoapApplServer.inputDeviceUri);
 		else if( dtype==DeviceType.output )  coapServer.addCoapResource( this, CoapApplServer.lightsDeviceUri);
 		//LOGGER.info("CoapDeviceResource " + name + " | created  ");
	}
 
 
	protected boolean examineGETOptions(CoapExchange exchange) {
		boolean observe1Found = false;
			List<Option> opts = exchange.getRequestOptions().asSortedList();
			//Colors.out( getName() + "handleGET options size=" + opts.size() );
			Iterator<Option> iter = opts.iterator();
			while( iter.hasNext() ) {
				Option option = iter.next();
				Colors.out( getName() + "handleGET options=" + option );
				//if( option.toString().contains("Observe: 1")) exchange.respond("BYE");
				observe1Found =  true;
			}		
			return observe1Found;
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
 		//examineGETOptions(exchange);
		String query = exchange.getQueryParameter("q"); 
		if( query == null ) {
			Colors.out( getName() + "handleGET request=" + exchange.getRequestText() );
			String answer = elaborateGet( exchange.getRequestText() );
			if( answer.length() < 500000 ) {
				Colors.out( getName() + "handleGET request answer=" + answer , Colors.GREEN );
				exchange.respond(answer);
			}
			else {
				Colors.out( getName() + "handleGET  long answer=" + answer.length() , Colors.RED ); 
				exchange.respond("long answer");
			}
			
		}else{ 
 			Colors.out( getName() + "handleGET query=" + query);
 			String answer = elaborateGet( exchange.getQueryParameter("q") );
 			if( answer.length() < 500000 ) {
 				if( answer.length() < 1000) Colors.out( getName() + "handleGET q-query answer=" + answer , Colors.GREEN );
 				else Colors.out( getName() + "handleGET q-long answer=" + answer.length() , Colors.RED );
 				exchange.respond(answer);
 			}
			else {
				Colors.out( getName() + "handleGET q-long answer=" + answer.length() , Colors.RED ); 
	  			exchange.respond("too long");
	  		}

		}		
	}

/*
 * POST is NOT idempotent.	Use POST when you want to add a child resource
 */
	@Override
	public void handlePOST(CoapExchange exchange) {
 	}
 	@Override
	public void handlePUT(CoapExchange exchange) {
 		Colors.out(getName() + " | handlePUT addr=" + exchange.getSourceAddress(), Colors.BgYellow );
 		String arg = exchange.getRequestText() ;
		elaboratePut( arg, exchange.getSourceAddress() );
		//changed();
		Colors.out(getName() + " | handlePUT arg=" + arg + " CHANGED="+ CHANGED );
		exchange.respond(""+CHANGED);
	}

 	protected abstract String elaborateGet(String req);
 	protected abstract void elaboratePut(String req);	
 	protected abstract void elaboratePut(String req, InetAddress callerAddr);


	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete();
		exchange.respond(DELETED);
	}

}
