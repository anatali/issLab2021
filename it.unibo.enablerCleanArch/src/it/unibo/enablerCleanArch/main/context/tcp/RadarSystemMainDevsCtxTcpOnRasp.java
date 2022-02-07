package it.unibo.enablerCleanArch.main.context.tcp;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.context.TcpContextServer;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;
 

/*
 * Applicazione che va in coppia con RadarSystemMainWithCtxOnPc
 */

public class RadarSystemMainDevsCtxTcpOnRasp implements IApplication{
	private ISonar sonar;
	private ILed  led ;
 	private TcpContextServer contextServer;

	@Override
	public String getName() {	 
		return "RadarSystemMainDevsCtxOnRasp";
	}

	public void setup( String configFile )  {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
			RadarSystemConfig.ctxServerPort     = 8018;
			RadarSystemConfig.withContext       = true;
		    RadarSystemConfig.protcolType       = ProtocolType.tcp;
   			RadarSystemConfig.testing      		= false;			
			RadarSystemConfig.sonarDelay        = 200;
 			RadarSystemConfig.simulation   		= false;
			RadarSystemConfig.DLIMIT      		= 12;  
 
 	}
	
 	
	 	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
		//Creazione del server di contesto
	    contextServer  = new TcpContextServer("TcpCtxServer",RadarSystemConfig.ctxServerPort);
		//Registrazione dei componenti presso il contesto
		  IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar); 
		  IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);		  
		  contextServer.addComponent("sonar", sonarHandler);	//sonar NAME mandatory
		  contextServer.addComponent("led",   ledHandler);		//led NAME mandatory
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		contextServer.deactivate();
		System.exit(0);
	}

	public void doJob(String configFileName) {
		setup(configFileName);
		configure();
		contextServer.activate();
	}
	
	public static void main( String[] args) throws Exception {
		//ColorsOut.out("Please set RadarSystemConfig.pcHostAddr in RadarSystemConfig.json");
		new RadarSystemMainDevsCtxTcpOnRasp().doJob(null);
 	}

}
