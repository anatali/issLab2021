package it.unibo.enablerCleanArch.local;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.*;

import it.unibo.enablerCleanArch.domain.ActionFunction;
import it.unibo.enablerCleanArch.domain.RadarDisplay;
import it.unibo.enablerCleanArch.local.main.RadarSystemMainLocal;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;


public class TestBehaviorLocal {
private RadarSystemMainLocal sys;

	@Before
	public void setUp() {
		System.out.println("setUp");
		try {
			sys = new RadarSystemMainLocal();
			sys.setup( null );
			RadarSystemConfig.testing    		= true;  //oneshot
			RadarSystemConfig.tracing    		= true; 
			//sys = new RadarSystemMainOnPc();
//			
//			//Set system configuration (we don't read RadarSystemConfig.json)
//			RadarSystemConfig.simulation 		= true;    
//			//we must do testing work with mock devices (local or remote) ???
//			//mock devices are 'almost identical' to concrete devices 
//			RadarSystemConfig.testing    		= true;    		
//			RadarSystemConfig.ControllerRemote	= false;    		
//			RadarSystemConfig.LedRemote  		= false;    		
//			RadarSystemConfig.SonareRemote  	= false;    		
//			RadarSystemConfig.RadarGuiRemote  	= false;    	
//			RadarSystemConfig.pcHostAddr        = "localhost";
//			RadarSystemConfig.protcolType       = ProtocolType.tcp;
//			//sys.buildAndRun(RadarSystemConfig.protcolType);
//			sys.doJob("RadarSystemConfig.json");
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
		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT +20;
		testTheDistance( false );
 	}	
	
	@Test 
	public void testNearDistance( ) {
 		RadarSystemConfig.testingDistance = RadarSystemConfig.DLIMIT - 1;
		testTheDistance( true );
	}	
	
	protected void testTheDistance( boolean ledStateExpected ) {
		System.out.println("testDistance " + RadarSystemConfig.testingDistance );
		RadarDisplay radar = RadarDisplay.getRadarDisplay();  //singleton
		
		ActionFunction endFun = (n) -> { 
			System.out.println(n);
			boolean ledState = sys.getLed().getState();
			int radarDisplayedDistance = radar.getCurDistance();
			
			ColorsOut.out("ledState=" + ledState + " radarDisplayedDistance=" + radarDisplayedDistance, ColorsOut.MAGENTA);
	 	    assertTrue(  ledState == ledStateExpected
	 	    		&& radarDisplayedDistance == RadarSystemConfig.testingDistance);
		};
		
		sys.getController().start( endFun, 1 );
  	    Utils.delay(1000) ; //give time to work ... 		
	}
 
}
