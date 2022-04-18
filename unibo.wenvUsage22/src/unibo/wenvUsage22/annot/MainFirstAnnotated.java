package unibo.wenvUsage22.annot;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;



public  class MainFirstAnnotated  {
  	
	public MainFirstAnnotated( ) {
  	}
  	 
 	
	public void doJob( String name ) {
		CommSystemConfig.tracing = false;
		new FirstAnnotated(name);
 		CommUtils.delay(2000);
	}
	
	public static void main( String[] args) {
		CommUtils.aboutThreads("Before start - ");
		new MainFirstAnnotated(  ).doJob( "a1" );
		CommUtils.aboutThreads("Before end - ");
		
	}
	
}
