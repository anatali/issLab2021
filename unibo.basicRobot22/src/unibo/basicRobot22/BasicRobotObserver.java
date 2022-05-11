package unibo.basicRobot22;

 

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import unibo.actor22comm.utils.ColorsOut;

public class BasicRobotObserver {
	private CoapObserveRelation relation = null;
	private CoapClient client = null;
	
	public BasicRobotObserver(String port, String actorName){
		client = new CoapClient("coap://localhost:"+port+"/actors/"+actorName);
		ColorsOut.outappl("BasicRobotObserver | CREATED client=" + client, ColorsOut.GREEN);
		observe();
	}
	
	public void  observe( ) {
		relation = client.observe(
				new CoapHandler() {
					@Override public void onLoad(CoapResponse response) {
						String content = response.getResponseText();
						ColorsOut.outappl("BasicRobotObserver | value=" + content, ColorsOut.GREEN);
					}					
					@Override public void onError() {
						ColorsOut.outerr("BasicRobotObserver FAILED (press enter to exit)");
					}
				});		
	}
	
 

}
