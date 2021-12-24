package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarUsageMainWithContextTcp extends SonarUsageAbstractMain{
	protected ApplMessage sonarActivate = new ApplMessage("msg( sonarcmd, dispatch,main,sonar, activate,0)");
	protected ApplMessage sonarStop     = new ApplMessage("msg( sonarcmd, dispatch, main, sonar, deactivate, 4 )");
	protected ApplMessage getDistance   = new ApplMessage("msg( sonarcmd, request,  main, sonar, getDistance,   5 )");

	protected ISonar clientSonarProxy ;	
	private TcpContextServer ctxServer;
//	private ProxyAsClient sonarCaller;
	
	@Override
	public void setConfiguration() {	//Called by configure
		super.setConfiguration();
		RadarSystemConfig.sonarObservable = false;	
		RadarSystemConfig.ctxServerPort   = 8018;
	}
	
	@Override
	protected void configureTheServer() {
		ctxServer  = new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		ctxServer.addComponent("sonar", sonarHandler);
  		ctxServer.activate();
	}
	

	@Override
	public void execute() {
 		clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", 
 				RadarSystemConfig.pcHostAddr, ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp );
//		sonarCaller = 
//				new ProxyAsClient("proxyCtx",
//						"RadarSystemConfig.pcHostAddr", ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
		//contextServer.activate();
 		clientSonarProxy.activate();
		for( int i=1; i<=5; i++) {
			IDistance v = clientSonarProxy.getDistance();
			System.out.println("SonarUsageMain | executeTcp with context getDistance="+v);
			Utils.delay(RadarSystemConfig.sonarDelay+100);
		}	 
		if( RadarSystemConfig.sonarObservable) ((ISonarObservable) sonar).unregister(obsfortesting); 
		Utils.delay( 500  ); //The sonars works for a while, by putting data in the queue
		clientSonarProxy.deactivate();;			
	}
	
	public void terminate() {
		System.exit(0);
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMainWithContextTcp  sys = new SonarUsageMainWithContextTcp();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();

	}

}
