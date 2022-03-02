package it.unibo.radarSystem22.distrib.main;

import it.unibo.comm2022.ProtocolType;
import it.unibo.comm2022.context.TcpContextServer;
import it.unibo.comm2022.interfaces.IApplMsgHandler;
import it.unibo.radarSystem22.distrib.IApplication;
import it.unibo.radarSystem22.distrib.handlers.LedApplHandler;
import it.unibo.radarSystem22.distrib.handlers.SonarApplHandler;
import it.unibo.radarSystem22.interfaces.*;
import it.unibo.comm2022.utils.CommSystemConfig;
import it.unibo.radarSystem22.domain.DeviceFactory;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

/*
 * Applicazione che va in coppia con RadarSystemMainWithCtxOnPc
 */

public class 	RadarSystemMainDevsCtxTcpOnRasp implements IApplication{
	private ISonar sonar;
	private ILed  led ;
 	private TcpContextServer contextServer;

	@Override
	public String getName() {	 
		return "RadarSystemMainDevsCtxOnRasp";
	}

	public void setup( String configFile )  {
		if( configFile != null ) CommSystemConfig.setTheConfiguration(configFile);
			DomainSystemConfig.simulation  = true;
	    	DomainSystemConfig.testing     = false;			
			DomainSystemConfig.sonarDelay  = 200;
	    	DomainSystemConfig.tracing     = false;			
	    	DomainSystemConfig.ledGui      = true;			

			CommSystemConfig.ctxServerPort     = 8018;
			CommSystemConfig.withContext       = true;
		    CommSystemConfig.protcolType       = ProtocolType.tcp; 
 	}
	
 	
	 	
	protected void configure() {
		//Dispositivi di Input
	    sonar      = DeviceFactory.createSonar();
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
		//Creazione del server di contesto
	    contextServer  = new TcpContextServer("TcpCtxServer",CommSystemConfig.ctxServerPort);
		//Registrazione dei componenti presso il contesto
		  IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar); 
		  IApplMsgHandler ledHandler   = new LedApplHandler("ledH",led);		  
		  contextServer.addComponent("sonar", sonarHandler);	//sonar NAME mandatory
		  contextServer.addComponent("led",   ledHandler);		//led NAME mandatory
		led.turnOff();
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
		//ColorsOut.out("Please set CommSystemConfig.pcHostAddr in CommSystemConfig.json");
		new RadarSystemMainDevsCtxTcpOnRasp().doJob(null);
 	}

}
