package it.unibo.enablerCleanArch.supports.coap;

import it.unibo.enablerCleanArch.domain.WebCamRasp;
import it.unibo.enablerCleanArch.supports.Colors;

public class WebCamRaspResourceCoap extends CoapDeviceResource {
 	
	public WebCamRaspResourceCoap( String name  ) {
		super(name, DeviceType.input);  //add the resource
		//WebCamRasp.startWebCamStream();  //Forse non prende foto ???
  	}

	@Override
	protected String elaborateGet(String req) {
		Colors.out( getName() + " | before elaborateGet req:" + req   );
		if( req.startsWith("getImage-")) {
			String fname=req.substring( req.indexOf('-')+1, req.length());
			String imgBase64 = WebCamRasp.getImage(fname);
			Colors.out(getName() + " | imgBase64:" + imgBase64.length());
			return imgBase64;
		}
  		return "nothing to to" ;
	}

	@Override
	protected void elaboratePut(String req) {
		Colors.out( getName() + " | before elaboratePut req:" + req   );
		 if( req.startsWith("getPhoto-") ){
			String fname=req.substring( req.indexOf('-')+1, req.length());
			Colors.out( getName() + " | getPhoto fname:" + fname   );
			WebCamRasp.getPhoto(fname);
		}else if( req.startsWith("startWebCamStream") ){
			WebCamRasp.startWebCamStream();
		}
 		 
 	}  

}
