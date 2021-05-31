 package coap;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.json.JSONObject;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.kactor.ApplMessage;

class DataHandler implements CoapHandler {
	
	protected String handleAsApplMessage(String content) { //retrun values not used
		System.out.println("DataHandler | observes_0: " + content  );
		if( ! content.contains("sonarvalue") ) return "0";
		/*
		//content = msg(sonar,event,sonarOnRaspCoap,none,sonar(V),N)
		ApplMessage msg = new ApplMessage(content);
		System.out.println("ResourceObserverRadar | observes_1: " + msg.msgContent()  );
		Struct data     = (Struct) Term.createTerm( msg.msgContent() );
		String value    = data.getArg(0).toString();
		System.out.println("ResourceObserverRadar | observes_2: " + value );
		*/
		JSONObject jsonContent = new JSONObject(content);
		String distance        = jsonContent.getString("sonarvalue");
		System.out.println("DataHandler | observes: " + distance );
		radarPojo.radarSupport.update(distance, "90");
		return content;
	}

	@Override public void onLoad(CoapResponse response) {
		String content  = handleAsApplMessage(response.getResponseText());	  
		//content= {"sonarvalue":"D" } or {"info":"..." }		 
		JSONObject jsonContent = new JSONObject(content);
		if( jsonContent.has("sonarvalue")) {
			String distance        = jsonContent.getString("sonarvalue");
			System.out.println("DataHandler | observes: " + distance );
			radarPojo.radarSupport.update(distance, "90");
		}
	}					
	@Override public void onError() {
		System.out.println("DataHandler |  FAILED (press enter to exit)");
	}
}

public class SonarresourceObserverRadar {
	private CoapSupport coapSupport = 
			new CoapSupport("coap://localhost:8028","ctxsonarresource/sonarresource");
	
	public SonarresourceObserverRadar(){
		radarPojo.radarSupport.setUpRadarGui();
		System.out.println("   ResourceObserverRadar | STARTS ");
		coapSupport.observeResource( new DataHandler() );
	}
	
	public static void main(String[] args) {
  		SonarresourceObserverRadar rco = new SonarresourceObserverRadar();
	}

}
