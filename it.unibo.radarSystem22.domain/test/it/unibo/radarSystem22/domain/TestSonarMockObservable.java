package it.unibo.radarSystem22.domain;
import static org.junit.Assert.assertTrue;
import org.junit.*;

import it.unibo.radarSystem22.domain.interfaces.IObserver;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;


public class TestSonarMockObservable {
	@Before
	public void up() {
		DomainSystemConfig.simulation      = true;
		DomainSystemConfig.testingDistance = 22;
	}
	
	@After
	public void down() {
		System.out.println("down");		
	}	
	
	//@Test 
	public void testSingleshotSonarObservableMock() {
 		DomainSystemConfig.testing = true;
		boolean oneShot           = true;			
		ISonarObservable sonar    = DeviceFactory.createSonarObservable();
		IObserver obs1            = new SonarObserverFortesting("obs1",sonar,oneShot) ;
		sonar.register( obs1 );	//add an observer
		sonar.activate();
		//sonar.register( new SonarObserverFortesting("obs2",sonar,oneShot) );	 
		BasicUtils.delay(500);  //setup
		int v0 = sonar.getDistance().getVal();
 		System.out.println("testSingleshotSonarObservableMock v0=" + v0);
 		assertTrue(  v0 == DomainSystemConfig.testingDistance );
	}
	
	@Test 
	public void testSonarObservableMock() {
		DomainSystemConfig.testing    = false;
 		DomainSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta       = 1;
		boolean oneShot = false;
				
		ISonarObservable sonar = DeviceFactory.createSonarObservable();
		
		IObserver obs1          = new SonarObserverFortesting("obs1",sonar,oneShot);
		IObserver obs2          = new SonarObserverFortesting("obs2",sonar,oneShot);
		
		sonar.register( obs1 );	//add an observer
		sonar.register( obs2 );	//add an observer

		new SonarConsumerForTesting( sonar, delta ).start();  //consuma

		sonar.activate();
		while( sonar.isActive() ) { BasicUtils.delay(100);}
		BasicUtils.delay(1000);

	}
	
 
}
