package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;

public class CoapApplObserver implements CoapHandler{
	protected IApplMsgHandler applHandler;
	
	public CoapApplObserver(String hostAddr, String resourceUri, IApplMsgHandler applHandler) {
		this.applHandler = applHandler;
		Colors.outappl("CoapApplObserver | resourceUri= " + resourceUri, Colors.BLUE );
	}
	

	@Override
	public void onLoad(CoapResponse response) {
		Colors.outappl("CoapApplObserver | value=" +  response.getResponseText(), Colors.BLUE);
		if( response.getResponseText().equals("BYE")) {
		try {
			Colors.outappl("CoapApplObserver | finalize=" +  response.getResponseText(), Colors.BLUE);
			this.onError();
			//this.finalize();
		} catch (Throwable e) {
 			e.printStackTrace();
		}}else
		applHandler.elaborate(response.getResponseText(), null);
	}
	@Override
	public void onError() {
		//If a request timeouts or the server rejects it
		Colors.outerr("CoapApplObserver | ERROR " );	
		 
	}
}
