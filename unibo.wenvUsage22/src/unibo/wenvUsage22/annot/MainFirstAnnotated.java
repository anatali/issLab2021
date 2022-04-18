package unibo.wenvUsage22.annot;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;



public  class MainFirstAnnotated  {
  	
	public MainFirstAnnotated( ) {
  	}
  	 
 	
	public void doJob( String name ) {
		new FirstAnnotated(name);
	}
	
	public static void main( String[] args) {
		CommSystemConfig.tracing = true;
		CommUtils.aboutThreads("Before start - ");
		new MainFirstAnnotated(  ).doJob( "a1" );
		Qak22Context.showActorNames();
// 		Qak22Util.sendAMsg( startCmd );
		Qak22Util.sendAMsg( ApplData.moveCmd("main", "a1", "w" ) );
		CommUtils.delay(1000);		
		CommUtils.aboutThreads("Before end - ");
	}
	
}
