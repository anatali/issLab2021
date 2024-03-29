package it.unibo.enablerCleanArch.main.context.tcp;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

/*
 * Applicazione che va in coppia con RadarSystemMainDevsCtxOnRasp
 */

public class RadarSystemMainWithCtxTcpOnPc implements IApplication{

private IRadarDisplay radar;
private ISonar sonar;
private ILed  led ;
private Controller controller;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainWithCtxTcpOnPc";
	}

	public void setup( String configFile )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.coap;
 		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.raspHostAddr      = "localhost"; //"192.168.1.9";
		  RadarSystemConfig.ctxServerPort     = 8018;
		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
		  RadarSystemConfig.tracing           = true;
	
	}
	
	protected void configure() {
		String host           = RadarSystemConfig.raspHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxport        = ""+RadarSystemConfig.ctxServerPort;
		led    		= new LedProxyAsClient("ledPxy",     host, ctxport, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
  		//radar  		= DeviceFactory.createRadarGui();
  		//controller 	= Controller.create(led, sonar, radar);
	}
	
 	
	@Override
	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		execute();
	}
	
	protected void useLed() {
	    led.turnOn();
	    Utils.delay(1000);
	    boolean ledState = led.getState();
	    ColorsOut.outappl("led state=" + ledState, ColorsOut.MAGENTA);
 	    led.turnOff();
		ColorsOut.outappl("led state=" + led.getState(), ColorsOut.MAGENTA);
	}
	public void execute() {
	    ActionFunction endFun = (n) -> { System.out.println(n); terminate(); };
	    useLed();
	    Utils.delay(30000);
		//controller.start(endFun, 30);
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
