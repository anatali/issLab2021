package it.unibo.enablerCleanArch.main.onpc;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Context2021;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;
 

/*
 * Applicazione che va in coppia con RadarSystemMainWithCtxOnPc
 */

public class RadarSystemMainDevsOnPc implements IApplication{
	private ISonar sonar;
	private ILed  led ;
 	private IContext ctx;

	@Override
	public String getName() {	 
		return "RadarSystemMainDevsOnPc";
	}

	public void setup(   )  {
 			RadarSystemConfig.ctxServerPort     = 8018;
			RadarSystemConfig.withContext       = true;
		    RadarSystemConfig.protcolType       = ProtocolType.coap;
 			RadarSystemConfig.sonarDelay        = 200;
 			RadarSystemConfig.simulation   		= true;
			RadarSystemConfig.DLIMIT      		= 55;  
			RadarSystemConfig.ledGui            = true;
			RadarSystemConfig.tracing           = true;
			RadarSystemConfig.testing           = false;
 	}
	
 	
	 	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    led.turnOff();
		//Creazione del server di contesto
	    ctx  = Context2021.create();
  		//Registrazione dei componenti presso il contesto
		  IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar); 
		  IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);		  
		  ctx.addComponent("sonar", sonarHandler);	//sonar NAME mandatory
		  ctx.addComponent("led",   ledHandler);		//led NAME mandatory
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		ctx.deactivate();
		System.exit(0);
	}

	public void doJob( String configFileName ) {
		setup( );
		configure();
 	}
	
	public static void main( String[] args) throws Exception {
 		new RadarSystemMainDevsOnPc().doJob( null );
 	}

}
