package it.unibo.radarSystem22_4.appl.main;

import it.unibo.radarSystem22.domain.interfaces.*;
import it.unibo.radarSystem22_4.appl.RadarSystemConfig;
import it.unibo.radarSystem22_4.appl.proxy.LedProxyAsClient;
import it.unibo.radarSystem22_4.comm.ProtocolType;
import it.unibo.radarSystem22_4.comm.interfaces.IApplication;
import it.unibo.radarSystem22_4.comm.utils.BasicUtils;
import it.unibo.radarSystem22_4.comm.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.utils.CommSystemConfig;

public class UseLedFromPc implements IApplication{
 	private ILed  led ;
 	
	@Override
	public String getName() {
		return this.getClass().getName() ; 
	}
	@Override
	public void doJob(String domainConfig, String systemConfig ) {
		setup(domainConfig,systemConfig);
		configure();
		execute();		
	}
	
	public void setup( String domainConfig, String systemConfig )  {
		RadarSystemConfig.DLIMIT           = 80;
		RadarSystemConfig.ctxServerPort    = 8756;
		CommSystemConfig.protcolType = ProtocolType.udp;
	}
	
	protected void configure() {		
		String host           = RadarSystemConfig.raspAddr;
		ProtocolType protocol = CommSystemConfig.protcolType;
		String ctxport        = ""+RadarSystemConfig.ctxServerPort;
		led    		          = new LedProxyAsClient("ledPxy", host, ctxport, protocol );
 	}
	

	public void execute() {
		ColorsOut.out("turnOn");
		led.turnOn();
		BasicUtils.delay(1000);
		//BasicUtils.waitTheUser();
		boolean ledState = led.getState();
		ColorsOut.out("ledState after on="+ledState);
		BasicUtils.delay(1000);
		//BasicUtils.waitTheUser();
		ColorsOut.out("turnOff");
		led.turnOff();
		ledState = led.getState();
		ColorsOut.out("ledState after off="+ledState);
   	}

	public void terminate() {
  		System.exit(0);
	}	
	
	public static void main( String[] args) throws Exception {
		//ColorsOut.out("Please set RadarSystemConfig.pcHostAddr in RadarSystemConfig.json");
		new UseLedFromPc().doJob(null,null);
 	}
	
}
