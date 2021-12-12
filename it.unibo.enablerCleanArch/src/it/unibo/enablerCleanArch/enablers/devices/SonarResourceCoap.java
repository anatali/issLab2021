package it.unibo.enablerCleanArch.enablers.devices;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.coap.CoapDeviceResource;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

public class SonarResourceCoap extends CoapDeviceResource  {
ISonar sonar;
private String curVal = "-1";

		public SonarResourceCoap(String name) {
			super(name, DeviceType.input);
			sonar = it.unibo.enablerCleanArch.domain.SonarModel.createSonarMock();
			//getSonarValues();
			Colors.out( getName() + " |  CREATED"   );	
	 	}
  			
		private void getSonarValues() {
			new Thread() {
				public void run() {
					sonar.activate();
					while( sonar.isActive() ) {
						int v = sonar.getVal();
						elaborateAndNotify(  v );
					}
				}
			}.start();
		}
		
		 // CoapDeviceResource
			@Override
			protected String elaborateGet(String req) {
				Colors.out( getName() + " |  elaborateGet req=" + req, Colors.ANSI_YELLOW  );	
				if( req.equals("isActive")) return ""+sonar.isActive();
				else if( req.equals("getVal"))  return  curVal;
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
				curVal= ""+arg;
				changed();	// notify all CoAp observers
			}
		
}
