package it.unibo.enablerCleanArch.supports.coap;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarResourceCoap extends CoapDeviceResource  {
ISonar sonar;
//private String curVal = "-1";

		public SonarResourceCoap(String name, ISonar sonar) {
			super(name, DeviceType.input);
			this.sonar = sonar;
			//sonar = it.unibo.enablerCleanArch.domain.SonarModel.createSonarMock();
			//getSonarValues();
			Colors.out( getName() + " |  CREATED"   );	
	 	}
  			
		private void getSonarValues() {
			new Thread() {
				public void run() {
					sonar.activate();
					while( sonar.isActive() ) {
						int v = sonar.getDistance().getVal();
						elaborateAndNotify(  v );
					}
				}
			}.start();
		}
		
		 // CoapDeviceResource
			@Override
			protected String elaborateGet(String req) {
				Colors.out( getName() + " | elaborateGet req=" + req, Colors.GREEN  );	
				if( req.equals("isActive")) return ""+sonar.isActive();
				else if( req.equals("getDistance")) {
					Colors.out( getName() + " |  elaborateGet getDistance="+sonar, Colors.ANSI_YELLOW  );	
					String answer = ""+(sonar.getDistance().getVal());
					Colors.out( getName() + " |  elaborateGet answer=" + answer, Colors.ANSI_YELLOW  );	
					return  answer;
				}
				else return "notUnderstood";
			}

			@Override
			protected void elaboratePut(String arg) {
	 			Colors.out( getName() + " |  elaboratePut:" + arg, Colors.ANSI_YELLOW  );
	 			if( arg.equals("activate")) getSonarValues();
	 			else if( arg.equals("deactivate")) sonar.deactivate(); 	
	 			changed();	// notify all CoAp observers
			}
			
			protected void elaborateAndNotify(int arg) {
				Colors.out( getName() + " |  elaborateAndNotify:" + arg, Colors.ANSI_YELLOW  );		
				//curVal= ""+arg;
				changed();	// notify all CoAp observers
			}
		
}
