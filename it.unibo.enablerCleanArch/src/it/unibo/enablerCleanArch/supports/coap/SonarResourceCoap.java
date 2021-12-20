package it.unibo.enablerCleanArch.supports.coap;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarResourceCoap extends CoapDeviceResource  {
ISonar sonar;
String curVal="";

		public SonarResourceCoap(String name, ISonar sonar) {
			super(name, DeviceType.input);
			this.sonar = sonar;
			Colors.out( getName() + " |  CREATED"   );	
	 	}
  			
		private void getSonarValues() {
			new Thread() {
				public void run() {
					sonar.activate();
					while( sonar.isActive() ) {
						int v = sonar.getDistance().getVal();
						elaborateAndNotify(  v );
						//Colors.out("sonar value="+v);
					}
				}
			}.start();
		}
		
		 // CoapDeviceResource
			@Override
			protected String elaborateGet(String req) {
//				Colors.out( getName() + " | elaborateGet req=" + req, Colors.GREEN  );					
				if( req == null || req.equals("getDistance")) {
					String answer = curVal;  
					return  answer;
				}else if( req != null && req.equals("isActive")) return ""+sonar.isActive();
				else return "notUnderstood";
			}

			@Override
			protected void elaboratePut(String arg) {
	 			//Colors.out( getName() + " |  elaboratePut:" + arg, Colors.GREEN  );
	 			if( arg.equals("activate")) getSonarValues();
	 			else if( arg.equals("deactivate")) sonar.deactivate(); 	
	 			else if( arg.equals("setVal")) { //just for some test ...
		 			Colors.out( getName() + " |  elaboratePut:" + arg, Colors.GREEN  );
	 				curVal=""+22; 	
	 				changed();
	 			}
	 			//changed();	// notify all CoAp observers
			}
			
			protected void elaborateAndNotify(int arg) {
				curVal= ""+arg;
//				Colors.out( getName() + " | elaborateAndNotify:" + curVal , Colors.GREEN  );		
				changed();	// notify all CoAp observers
			}
		
}
