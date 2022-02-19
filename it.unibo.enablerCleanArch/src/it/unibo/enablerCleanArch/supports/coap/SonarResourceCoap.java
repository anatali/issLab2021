package it.unibo.enablerCleanArch.supports.coap;

import java.net.InetAddress;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplInterpreter;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarResourceCoap extends ApplResourceCoap  {
private String curDistance="0";  //Initial state
private IApplInterpreter sonarIntrprt;
 
		public SonarResourceCoap(String name, IApplInterpreter sonarIntrprt) {
			super(name, DeviceType.input);
			this.sonarIntrprt = sonarIntrprt;
			ColorsOut.out( getName() + " |  SonarResourceCoap CREATED"   );	
	 	}
		
		private boolean sonarActive() {
			return sonarIntrprt.elaborate("isActive").equals("true");
		}
  			
		private void getSonarValues() { //SonarResourceCoap r
			ColorsOut.out( getName() + " |  SonarResourceCoap getSonarValues for observers"   );
			new Thread() {
				public void run() {
					if( ! sonarActive() ) sonarIntrprt.elaborate("activate");
					while( sonarActive() ) {
						String v = sonarIntrprt.elaborate("getDistance");
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
					answer = sonarIntrprt.elaborate( msg  );			
				}catch( Exception e) {
					answer = sonarIntrprt.elaborate( req  );
				}		
				return answer;  
			}
			
			@Override
			protected String elaborateGet(String req, InetAddress callerAddr) {
				return elaborateGet(req);
			}

			@Override
			protected void elaboratePut(String arg) {
	 			ColorsOut.out( getName() + " |  elaboratePut:" + arg, ColorsOut.GREEN  );
	 			
	 			String result = sonarIntrprt.elaborate(arg);
	 			if( result.equals("activate_done")) getSonarValues(); //per CoAP observers
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
