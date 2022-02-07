package it.unibo.enablerCleanArch.supports.coap;

import java.net.InetAddress;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplLogic;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarResourceCoap extends ApplResourceCoap  {
//ISonar sonar;
String curDistance="0";  //Initial state
private IApplLogic sonarLogic;

 
		public SonarResourceCoap(String name, IApplLogic sonarLogic) {
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
				String answer = "";
 				ColorsOut.out( getName() + " | elaborateGet req=" + req + " curVal="+curDistance , ColorsOut.GREEN  );					
				if( req == null  ) {
					ColorsOut.outerr(getName() +  " | elaborateGet req NULL");
					answer = curDistance;
				}
				try {
					ApplMessage msg = new ApplMessage( req );
					answer = sonarLogic.elaborate( msg  );			
				}catch( Exception e) {
					answer = sonarLogic.elaborate( req  );
				}		
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
				return answer; //sonarLogic.elaborate(req);
			}
			
			@Override
			protected String elaborateGet(String req, InetAddress callerAddr) {
				return elaborateGet(req);
			}

			@Override
			protected void elaboratePut(String arg) {
	 			ColorsOut.out( getName() + " |  elaboratePut:" + arg, ColorsOut.GREEN  );
	 			
	 			sonarLogic.elaborate(arg);
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
 				ColorsOut.out( getName() + " | elaborateAndNotify:" + curDistance , ColorsOut.GREEN  );		
				changed();	// notify all CoAP observers
			}
 }
