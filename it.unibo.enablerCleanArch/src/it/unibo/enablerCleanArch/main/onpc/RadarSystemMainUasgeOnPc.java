package it.unibo.enablerCleanArch.main.onpc;

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

public class RadarSystemMainUasgeOnPc implements IApplication{

private ISonar sonar;
private ILed  led ;
private Controller controller;

	
	@Override
	public String getName() {	 
		return "RadarSystemMainWithCtxTcpOnPc";
	}

	public void setup(   )  {
 	      RadarSystemConfig.protcolType       = ProtocolType.coap;
 		  RadarSystemConfig.testing           = false;
		  RadarSystemConfig.raspHostAddr      = "localhost"; //"192.168.1.9";
		  RadarSystemConfig.ctxServerPort     = 8018;
		  RadarSystemConfig.sonarDelay        = 1500;
		  RadarSystemConfig.withContext       = true; //MANDATORY: to use ApplMessage
		  RadarSystemConfig.DLIMIT            = 40;
		  RadarSystemConfig.tracing           = true;
	
	}
	
	protected void configure() {
		String host           = RadarSystemConfig.raspHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String ctxport        = ""+RadarSystemConfig.ctxServerPort;
		led    		= new LedProxyAsClient("ledPxy",     host, ctxport, protocol );
  		sonar  		= new SonarProxyAsClient("sonarPxy", host, ctxport, protocol );
   		//controller 	= Controller.create( led, sonar );
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
		//controller.start(endFun, 10);
 	}
 
	public void terminate() {
		led.turnOff();
	    //Utils.delay(1000); //give the time for last actions ...
		sonar.deactivate();
		System.exit(0);
	}

 
 

	@Override
	public void doJob(String configFileName) {
		setup( );
		configure();
		execute();
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainUasgeOnPc().doJob(null);
 	}

}
