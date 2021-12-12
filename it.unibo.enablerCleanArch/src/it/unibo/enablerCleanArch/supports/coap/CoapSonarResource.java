package it.unibo.enablerCleanArch.supports.coap;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarModel;
 

public class CoapSonarResource extends CoapDeviceResource {  
private ISonar sonar; 
private String curVal = "-1";

	public CoapSonarResource( String name, DeviceType devtype  ) {  
		super( name, devtype )  ;
		sonar = SonarModel.create();
		getSonarValues();
		System.out.println( getName() + " |  CREATED"   );	
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
			//System.out.println( getName() + " |  elaborateGet:" + req  );		
			return  curVal;
		}

		@Override
		protected void elaboratePut(String arg) {
 			//System.out.println( getName() + " |  elaboratePut:" + arg  );		
			if( arg.equals("stop")) sonar.deactivate(); 	
 			changed();	// notify all CoAp observers
		}
		
		protected void elaborateAndNotify(int arg) {
 			//System.out.println( getName() + " |  elaborateAndNotify:" + arg  );		
			curVal= ""+arg;
			changed();	// notify all CoAp observers
		}

	 
 
}
 


 
