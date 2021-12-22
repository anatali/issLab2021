package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
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
		applHandler.elaborate(response.getResponseText(), null);
	}
	@Override
	public void onError() {
		Colors.outerr("CoapApplObserver | ERROR " );	
	}
}
