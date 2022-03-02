package it.unibo.radarSystem22;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;

import it.unibo.radarSystem22.domain.ActionFunction;
import it.unibo.radarSystem22.domain.RadarDisplay;
import it.unibo.radarSystem22.main.local.RadarSystemMainLocal;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.BasicUtils; 

public class TestBehaviorLocal {
private RadarSystemMainLocal sys;

	@Before
	public void setUp() {
		System.out.println("setUp");
		try {
			sys = new RadarSystemMainLocal();
			sys.setup( null );
			DomainSystemConfig.testing    		= true;   
			DomainSystemConfig.tracing    		= true; 
			//sys = new RadarSystemMainOnPc();
//			
//			//Set system configuration (we don't read DomainSystemConfig.json)
//			DomainSystemConfig.simulation 		= true;    
//			//we must do testing work with mock devices (local or remote) ???
//			//mock devices are 'almost identical' to concrete devices 
//			DomainSystemConfig.testing    		= true;    		
//			DomainSystemConfig.ControllerRemote	= false;    		
//			DomainSystemConfig.LedRemote  		= false;    		
//			DomainSystemConfig.SonareRemote  	= false;    		
//			DomainSystemConfig.RadarGuiRemote  	= false;    	
//			DomainSystemConfig.pcHostAddr        = "localhost";
//			DomainSystemConfig.protcolType       = ProtocolType.tcp;
//			//sys.buildAndRun(DomainSystemConfig.protcolType);
//			sys.doJob("DomainSystemConfig.json");
//			Utils.delay(2000);
		} catch (Exception e) {
			fail("setup ERROR " + e.getMessage() );
 		}
	}
	
	@After
	public void resetAll() {
		System.out.println("resetAll");		
	}	
	
	
	@Test 
	public void testFarDistance() {
		DomainSystemConfig.testingDistance = DomainSystemConfig.DLIMIT +20;
		testTheDistance( false );
 	}	
	
	@Test 
	public void testNearDistance( ) {
 		DomainSystemConfig.testingDistance = DomainSystemConfig.DLIMIT - 1;
		testTheDistance( true );
	}	
	
	protected void testTheDistance( boolean ledStateExpected ) {
		System.out.println("testDistance " + DomainSystemConfig.testingDistance );
		RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
		
		ActionFunction endFun = (n) -> { 
			System.out.println(n);
			boolean ledState = sys.getLed().getState();
			int radarDisplayedDistance = radar.getCurDistance();
			
			ColorsOut.out("ledState=" + ledState + " radarDisplayedDistance=" + radarDisplayedDistance, ColorsOut.MAGENTA);
	 	    assertTrue(  ledState == ledStateExpected
	 	    		&& radarDisplayedDistance == DomainSystemConfig.testingDistance);
		};
		
		sys.getController().start( endFun, 1 ); //one-shot
  	    BasicUtils.delay(1000) ; //give time to work ... 		
	}
 
}
