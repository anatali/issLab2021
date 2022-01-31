package it.unibo.enablerCleanArch.supports.coap.example;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.ColorsOut;

public class ObserverNaive  implements CoapHandler{
private String name;
	public ObserverNaive( String name ) {
		this.name = name;
	}
	@Override
	public void onLoad(CoapResponse response) {
 		ColorsOut.outappl(name + " | " + response.getResponseText(), ColorsOut.ANSI_PURPLE);
	}
	@Override
	public void onError() {
 		ColorsOut.outerr(name + " | error"  );	
	}	
}