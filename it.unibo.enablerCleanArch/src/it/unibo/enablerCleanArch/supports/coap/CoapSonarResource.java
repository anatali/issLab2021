package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapResource;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarMock;


public class CoapSonarResource extends CoapDeviceInputResource implements ISonar{
private ISonar sonar; 

	public CoapSonarResource( String name  ) {
		 super(name, new SonarMessageHandler( "cmh") )  ;
		/*
			1) Activate the CoapApplServer (SINGLETON)
		 */
		CoapApplServer.init();
		/*
			2) Create sonar as input-device Coap resource
		 */
		//CoapResource sonar   = new CoapSonarResource( name ) ;
		CoapApplServer.addInputResource( this  );
		sonar = new SonarMock();		
 	}

	@Override
	public void activate() {
		 sonar.activate();		
	}

	@Override
	public void deactivate() {
		sonar.deactivate();		
	}

	@Override
	public int getVal() {
		int v = sonar.getVal();
		elaborate( ""+v);
 		return v;
	}

	@Override
	public boolean isActive() {
		return sonar.isActive();
	}	

	/*
	private void useTheSonarDevice() {
		 new Thread() {
			 public void run() {
				 while( sonar.isActive() ) {
					 int v = sonar.getVal();	//bloccante
					 elaborate( ""+v);
				 }				 
			 }
		 }.start();
	}
    */
 

	public static void main(String[] args) throws  Exception {
	//CONFIGURATION
	/*
		1) Activate the CoapApplServer (SINGLETON)
	 */
	CoapApplServer.init();
	/*
		2) Create sonar as input-device Coap resource
	 */
	ISonar sonar   = new CoapSonarResource("sonar" ) ;
 	sonar.activate();
 	
	while( sonar.isActive() ) {
		int v = sonar.getVal();	//esiste un observer=>il controller non deve updatare gui
		//System.out.println(" %%% v= " + v );
	}
	//USAGE
	/*
	String resourceUri     = CoapApplServer.inputDeviceUri+"/sonar";
	CoapSupport support    = new CoapSupport("coap://localhost:5683", resourceUri);
	String vs              = support.readResource();
	System.out.println(" %%% state init: " + vs);
 
	support.updateResource("50");
		
	System.out.println(" %%% state end: " + support.readResource());
*/
}



}
