package it.unibo.enablerCleanArch.supports.coap;

import it.unibo.enablerCleanArch.domain.WebCamRasp;
import it.unibo.enablerCleanArch.supports.Colors;

public class WebCamRaspResourceCoap extends CoapDeviceResource {
 	
	public WebCamRaspResourceCoap( String name  ) {
		super(name, DeviceType.input);  //add the resource
  	}

	@Override
	protected String elaborateGet(String req) {
		Colors.out( getName() + " |  before elaborateGet req:" + req   );
		WebCamRasp.getPhoto("xxx.jpg");
 		return "done xxx.jpg" ;
	}

	@Override
	protected void elaboratePut(String req) {
		Colors.out( getName() + " |  before elaboratePut req:" + req   );
 	}  

}
