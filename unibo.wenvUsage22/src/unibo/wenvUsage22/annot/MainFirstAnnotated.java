package unibo.wenvUsage22.annot;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.Actor;
import unibo.actor22.annotations.AnnotUtil;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.actors.SysData;
import unibo.wenvUsage22.common.ApplData;


@Actor(name = "a1",      implement = FirstAnnotated.class)
public  class MainFirstAnnotated  {
  	 
	public MainFirstAnnotated() {
		configure( );
		doJob();
	}
	protected void configure(   ) {
		//new FirstAnnotated(name);
		AnnotUtil.handleRepeatableActorDeclaration(this);
		Qak22Context.showActorNames();
	}
	protected void doJob() {
		Qak22Util.sendAMsg( ApplData.moveCmd("main", "a1", "w" ) );
		Qak22Util.sendAMsg( SysData.haltSysCmd("main", "a1") );
		CommUtils.delay(1000);		
		
	}
	
	public static void main( String[] args) {
		CommSystemConfig.tracing = true;
		CommUtils.aboutThreads("Before start - ");
		new MainFirstAnnotated(  ) ;
 		CommUtils.aboutThreads("Before end - ");
	}
	
}
