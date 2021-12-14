package it.unibo.enablerCleanArch.supports.coap;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarModel;
import it.unibo.enablerCleanArch.supports.Colors;
 

public class CoapSonarResource extends CoapDeviceResource {  
private ISonar sonar; 
private String curVal = "-1";

	public CoapSonarResource( String name, DeviceType devtype  ) {  
		super( name, devtype )  ;
		sonar = SonarModel.create();
		getSonarValues();
		Colors.out( getName() + " |  CREATED", Colors.RED   );	
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
			Colors.out( getName() + " |  elaborateGet:" + req  );		
			return  curVal;
		}

		@Override
		protected void elaboratePut(String arg) {
			Colors.out( getName() + " |  elaboratePut:" + arg  );		
			if( arg.equals("stop")) sonar.deactivate(); 	
 			changed();	// notify all CoAp observers
		}
		
		protected void elaborateAndNotify(int arg) {
			Colors.out( getName() + " |  elaborateAndNotify:" + arg  );		
			curVal= ""+arg;
			changed();	// notify all CoAp observers
		}
}
 


 
