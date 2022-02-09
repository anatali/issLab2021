package it.unibo.enablerCleanArch.supports.coap;

import java.net.InetAddress;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplInterpreter;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarResourceCoap extends ApplResourceCoap  {
String curDistance="0";  //Initial state
private IApplInterpreter sonarLogic;
 
		public SonarResourceCoap(String name, IApplInterpreter sonarLogic) {
			super(name, DeviceType.input);
			this.sonarLogic = sonarLogic;
			ColorsOut.out( getName() + " |  SonarResourceCoap CREATED"   );	
	 	}
		
		private boolean sonarActive() {
			return sonarLogic.elaborate("isActive").equals("true");
		}
  			
		private void getSonarValues() {
			ColorsOut.out( getName() + " |  SonarResourceCoap getSonarValues for observers"   );
			new Thread() {
				public void run() {
					if( ! sonarActive() ) sonarLogic.elaborate("activate");
					while( sonarActive() ) {
						String v = sonarLogic.elaborate("getDistance");
		 				//ColorsOut.out( getName() + " | SonarResourceCoap v=" + v , ColorsOut.BgYellow  );		
						elaborateAndNotify(  v );
						Utils.delay(RadarSystemConfig.sonarDelay);
					}
				}
			}.start();
		}
		
		 // CoapDeviceResource
			@Override
			protected String elaborateGet(String req) {
				String answer = "";
 				ColorsOut.out( getName() + " | elaborateGet req=" + req + " curDistance="+curDistance    );					
				if( req == null || req.isEmpty() ) { //query by observers
					ColorsOut.outerr(getName() +  " | elaborateGet req NULL or empty");
					answer = curDistance;
					return answer;
				}
				try {
					ApplMessage msg = new ApplMessage( req );
					answer = sonarLogic.elaborate( msg  );			
				}catch( Exception e) {
					answer = sonarLogic.elaborate( req  );
				}		
				return answer; //sonarLogic.elaborate(req);
				/*
				if( req.equals("getDistance")) {
					//String answer = curDistance;  
					curDistance = ""+sonar.getDistance().getVal();
					return  curDistance;
				}
				if( req != null && req.isEmpty()) return curDistance; //for the observers
				if( req != null && req.equals("isActive")) return ""+sonar.isActive();
				return "SonarResourceCoap: request notUnderstood";
				*/
			}
			
			@Override
			protected String elaborateGet(String req, InetAddress callerAddr) {
				return elaborateGet(req);
			}

			@Override
			protected void elaboratePut(String arg) {
	 			ColorsOut.out( getName() + " |  elaboratePut:" + arg, ColorsOut.GREEN  );
	 			
	 			String result = sonarLogic.elaborate(arg);
	 			if( result.equals("activate_done")) getSonarValues(); //per CoAP observers
	 			/*
	 			if( arg.equals("activate")) getSonarValues();
	 			else if( arg.equals("deactivate")) sonar.deactivate(); 	
//	 			else if( arg.equals("setVal")) { //just for some test ...
//		 			ColorsOut.out( getName() + " |  elaboratePut:" + arg, ColorsOut.GREEN  );
//	 				curDistance=""+22; 	
//	 				changed();
//	 			}
	 			//changed();	// notify all CoAp observers
	 			 * 
	 			 */
			}
			@Override
			protected void elaboratePut(String req, InetAddress callerAddr) {
				ColorsOut.out( getName() + " | before elaboratePut req:" + req + " callerAddr="  + callerAddr  );
				elaboratePut(req);
			}			
			
			
			protected void elaborateAndNotify(String arg) {
				curDistance=  arg;
 				ColorsOut.out( getName() + " | elaborateAndNotify:" + curDistance , ColorsOut.BgYellow  );		
 				changed();	// notify all CoAP observers
			}
 }
