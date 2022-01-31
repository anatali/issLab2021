package it.unibo.enablerCleanArch.supports.coap;

import java.net.InetAddress;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarResourceCoap extends CoapDeviceResource  {
ISonar sonar;
String curDistance="0";  //Initial state

		public SonarResourceCoap(String name, ISonar sonar) {
			super(name, DeviceType.input);
			this.sonar = sonar;
			ColorsOut.out( getName() + " |  SonarResourceCoap CREATED"   );	
	 	}
  			
		private void getSonarValues() {
			ColorsOut.out( getName() + " |  SonarResourceCoap getSonarValues"   );
			new Thread() {
				public void run() {
					if( ! sonar.isActive() ) sonar.activate();
					while( sonar.isActive() ) {
						int v = sonar.getDistance().getVal();
						elaborateAndNotify(  v );
						Utils.delay(RadarSystemConfig.sonarDelay);
						//Colors.out("SonarResourceCoap | sonar value="+v);
					}
				}
			}.start();
		}
		
		 // CoapDeviceResource
			@Override
			protected String elaborateGet(String req) {
 				ColorsOut.out( getName() + " | elaborateGet req=" + req + " curVal="+curDistance , ColorsOut.GREEN  );					
				if( req == null  ) {
					ColorsOut.outerr("getName() + \" | elaborateGet req NULL");
					return curDistance;
				}
				if( req.equals("getDistance")) {
					//String answer = curDistance;  
					String answer = ""+sonar.getDistance().getVal();
					return  answer;
				}
				if( req != null && req.isEmpty()) return curDistance; //for the observers
				if( req != null && req.equals("isActive")) return ""+sonar.isActive();
				return "SonarResourceCoap: request notUnderstood";
			}
			
			@Override
			protected String elaborateGet(String req, InetAddress callerAddr) {
				return elaborateGet(req);
			}

			@Override
			protected void elaboratePut(String arg) {
	 			ColorsOut.out( getName() + " |  elaboratePut:" + arg, ColorsOut.GREEN  );
	 			if( arg.equals("activate")) getSonarValues();
	 			else if( arg.equals("deactivate")) sonar.deactivate(); 	
	 			else if( arg.equals("setVal")) { //just for some test ...
		 			ColorsOut.out( getName() + " |  elaboratePut:" + arg, ColorsOut.GREEN  );
	 				curDistance=""+22; 	
	 				changed();
	 			}
	 			//changed();	// notify all CoAp observers
			}
			@Override
			protected void elaboratePut(String req, InetAddress callerAddr) {
				ColorsOut.out( getName() + " | before elaboratePut req:" + req + " callerAddr="  + callerAddr  );
				elaboratePut(req);
			}			
			
			
			protected void elaborateAndNotify(int arg) {
				curDistance= ""+arg;
 				ColorsOut.out( getName() + " | elaborateAndNotify:" + curDistance , ColorsOut.GREEN  );		
				changed();	// notify all CoAP observers
			}

		
}
