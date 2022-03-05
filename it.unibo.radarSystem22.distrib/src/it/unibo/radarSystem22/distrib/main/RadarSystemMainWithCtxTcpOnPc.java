package it.unibo.radarSystem22.distrib.main;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.radarSystem22.distrib.IApplication;
import it.unibo.radarSystem22.distrib.proxy.LedProxyAsClient;
import it.unibo.radarSystem22.distrib.proxy.SonarProxyAsClient;
import it.unibo.radarSystem22.domain.ActionFunction;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.interfaces.*;
import it.unibo.radarSystem22.domain.DeviceFactory;

/*
 * Applicazione che va in coppia con RadarSystemMainDevsCtxOnRasp
 */

public class RadarSystemMainWithCtxTcpOnPc implements IApplication{

private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private ApplController controller;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainWithCtxTcpOnPc";
	}

	public void setup( String configFile )  {
		DomainSystemConfig.DLIMIT            = 40;  
 	    DomainSystemConfig.testing           = false;
		DomainSystemConfig.sonarDelay        = 500;
		DomainSystemConfig.tracing           = true;
 	      CommSystemConfig.protcolType       = ProtocolType.tcp;
		  CommSystemConfig.raspHostAddr      = "localhost"; //"192.168.1.9";
		  CommSystemConfig.ctxServerPort     = 8018;
		  CommSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
	
	}
	
	protected void configure() {
		String host           = CommSystemConfig.raspHostAddr;
		ProtocolType protocol = CommSystemConfig.protcolType;
		String ctxport        = ""+CommSystemConfig.ctxServerPort;
		led    		= new LedProxyAsClient("ledPxy",     host, ctxport, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
  		radar  		= DeviceFactory.createRadarGui();
  		controller 	= ApplController.create(led, sonar, radar);
	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		execute();
	}
	
	protected void useLed() {
	    led.turnOn();
	    BasicUtils.delay(1000);
	    boolean ledState = led.getState();
	    ColorsOut.outappl("led state=" + ledState, ColorsOut.MAGENTA);
 	    led.turnOff();
		ColorsOut.outappl("led state=" + led.getState(), ColorsOut.MAGENTA);
	}
	public void execute() {
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
	    //useLed();
		controller.start(endFun, 30);
	    //BasicUtils.delay(30000);
	}
 
	public void terminate() {
		sonar.deactivate();
		System.exit(0);
	}

 
 	public IRadarDisplay getRadarGui() {
		return radar;
	}

	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainWithCtxTcpOnPc().doJob(null);
 	}

}
