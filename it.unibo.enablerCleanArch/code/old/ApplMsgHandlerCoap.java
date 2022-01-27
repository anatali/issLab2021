package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.supports.coap.CoapDeviceResource;
import it.unibo.enablerCleanArch.supports.coap.DeviceType;

public abstract class ApplMsgHandlerCoap extends CoapDeviceResource  {

	public ApplMsgHandlerCoap(String name, DeviceType dtype) {
		super(name, dtype);
		//The resource is added to CoapApplServer
	}
 
}
