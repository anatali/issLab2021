package it.unibo.enablerCleanArch.supports.coap;
 
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;

/*
 * Adapter for an output device 
 */
public class LedAdapterCoap implements ILed {
private boolean ledStateMirror = false;
//private CoapClient client;
private CoapSupport cps;
//private IApplMsgHandler ledHandler;

	public LedAdapterCoap( String hostAddr, String resourceUri ) {
 		ColorsOut.out( "LedAdapterCoap |  STARTS for " + hostAddr +" resourceName=>"+resourceUri, ColorsOut.BLUE);
		//client  = new CoapClient( "coap://localhost:5683/"+ resourceUri ); //+"?value=10"
		cps = new CoapSupport(hostAddr, resourceUri );
		//CoapResponse answer = client.get();
	}
 	
	@Override
	public void turnOn() { 
 		try {
 			cps.forward( "on" );
			ledStateMirror = true;
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}

	@Override
	public void turnOff() {   
 		try {
 			cps.forward( "off" );
			ledStateMirror = false;
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}

	@Override
	public boolean getState() {   
		return ledStateMirror;
		/*
		try {
			return cps.readResource(   ).equals("true");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

}
