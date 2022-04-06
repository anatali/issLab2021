package unibo.radarSystem22.actors.main;


import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.ActorRemote;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
 
import unibo.radarSystem22.actors.*;

 
/*
 * Questo sistema ipotizza che led e sonar siano attori 
 * con cui interagire  a scambio di messaggi
 */
@ActorLocal(name =     {"controller" }, 
           implement = {unibo.radarSystem22.actors.ControllerActorAsObserver.class })
@ActorRemote(name =   {"led","sonar"}, 
             host=    {ApplData.raspAddr, ApplData.raspAddr}, 
             port=    { ""+ApplData.ctxRaspPort, ""+ApplData.ctxRaspPort}, 
             protocol={ "TCP" , "TCP" })
public class ControllerActorAsObserverOnPc {
	
 
	public void doJob() {
		ColorsOut.outappl("UsingActorsWithAnnotOnPc | Start", ColorsOut.BLUE);
		configure();
		CommUtils.aboutThreads("Before execute - ");
		//CommUtils.waitTheUser();
		execute();
		//terminate();
	}
	
	protected void configure() {
		DomainSystemConfig.tracing      = false;			
 		CommSystemConfig.protcolType    = ProtocolType.tcp;
		CommSystemConfig.tracing        = false;
		ProtocolType protocol 		    = CommSystemConfig.protcolType;
		RadarSystemConfig.DLIMIT        = 65;
		
		//TODO: sarebbe meglio automatizzare
		new EnablerContextForActors( "ctxPc",ApplData.ctxPcPort,ApplData.protocol).activate();

		//WARNING: observerName deve essere dichiarato come actor remoto dalla sorgente
		//PRO:    protezione
		//CONTRO: sorgente consapevole degli observer
		
//		new EventObserver(ApplData.observerName);
//		Qak22Context.registerAsEventObserver(ApplData.observerName, ApplData.evEndWork);
	
		Qak22Context.handleLocalActorDecl(this);
 		Qak22Context.handleRemoteActorDecl(this);
 	}
	
	protected void execute() {
		ColorsOut.outappl("UsingActorsWithAnnotOnPc | execute", ColorsOut.MAGENTA);
		Qak22Util.sendAMsg( ApplData.activateCrtl );
	} 
	
	public void terminate() {
		System.exit(0);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new ControllerActorAsObserverOnPc().doJob();
		CommUtils.aboutThreads("Before end - ");
	}

}

/*
 * Threads:
 */
