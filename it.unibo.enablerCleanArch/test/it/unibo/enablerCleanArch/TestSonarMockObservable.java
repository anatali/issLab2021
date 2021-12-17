package it.unibo.enablerCleanArch;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;



public class TestSonarMockObservable {
	@Before
	public void up() {
		RadarSystemConfig.simulation      = true;
		RadarSystemConfig.testingDistance = 22;
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	//@Test 
	public void testSingleshotSonarObservableMock() {
 		RadarSystemConfig.testing = true;
		boolean oneShot           = true;			
		ISonarObservable sonar    = DeviceFactory.createSonarObservable();
		IObserver obs1            = new SonarObserverFortesting("obs1",sonar,oneShot) ;
		sonar.register( obs1 );	//add an observer
		sonar.activate();
		sonar.register( new SonarObserverFortesting("obs2",sonar,oneShot) );	 
		
		int v0 = sonar.getDistance().getVal();
 		System.out.println("testSingleshotSonarObservableMock v0=" + v0);
 		assertTrue(  v0 == RadarSystemConfig.testingDistance );
	}
	
	@Test 
	public void testSonarObservableMock() {
		RadarSystemConfig.testing    = false;
 		RadarSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta       = 1;
		boolean oneShot = false;
				
		ISonarObservable sonar = DeviceFactory.createSonarObservable();
		
		IObserver obs1          = new SonarObserverFortesting("obs1",sonar,oneShot);
		IObserver obs2          = new SonarObserverFortesting("obs2",sonar,oneShot);
		
		sonar.register( obs1 );	//add an observer
		sonar.register( obs2 );	//add an observer

		new SonarConsumerForTesting( sonar, delta ).start();  //consuma

		sonar.activate();
		while( sonar.isActive() ) { Utils.delay(100);}
		
/*		
		while( sonar.isActive() ) { 
			int d = sonar.getDistance().getVal(); //toglie elementi dalla coda
			Colors.out("test unit consumed " + d, Colors.ANSI_PURPLE);
			if( d >= 10 ) sonar.unregister( obs1 );
			if( d >= 50 ) sonar.unregister( obs2 );
			Utils.delay(100);
		}

		int v0 = sonar.getDistance().getVal();
 		//System.out.println("testSonarObservableMock v0=" + v0);
		while( sonar.isActive() ) {
			int d = sonar.getDistance().getVal();
	 		//System.out.println("sonar getVal=" + d);
			int vexpected = v0-delta;
			if( d == 50 ) sonar.unregister( obs2 );
			if( d == 10 ) sonar.unregister( obs1 );
			assertTrue(  d == vexpected );
			v0 = d;
		}	*/	
	}
	
 
}
