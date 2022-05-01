package  unibo.Robots;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.IApplMsgHandler;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;


public class CoapApplObserver implements CoapHandler{
	protected IApplMsgHandler applHandler;
	
	public CoapApplObserver(String hostAddr, String resourceUri, IApplMsgHandler applHandler) {
		this.applHandler = applHandler;
		ColorsOut.outappl("CoapApplObserver | resourceUri= " + resourceUri, ColorsOut.BLUE );
	}
	

	@Override
	public void onLoad(CoapResponse response) {
		ColorsOut.outappl("CoapApplObserver | value=" +  response.getResponseText(), ColorsOut.BLUE);
		if( response.getResponseText().equals("BYE")) {
		try {
			ColorsOut.outappl("CoapApplObserver | finalize=" +  response.getResponseText(), ColorsOut.BLUE);
			this.onError();
			//this.finalize();
		} catch (Throwable e) {
 			e.printStackTrace();
		}}else {
			//applHandler.elaborate(response.getResponseText(), null);
			IApplMessage msg = CommUtils.buildDispatch("coap", "id", response.getResponseText(), "any" );
			if( applHandler != null ) applHandler.elaborate(msg, null);
		}
	}
	@Override
	public void onError() {
		//If a request timeouts or the server rejects it
		ColorsOut.outerr("CoapApplObserver | ERROR " );	
		 
	}
}
