package it.unibo.enablerCleanArch;
import static org.junit.Assert.assertTrue;
import java.util.Observable;
import org.junit.*;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

class SonarObserverFortesting implements IObserver{
	private String name;
	private boolean oneShot = false;
	private int v0          = -1;
	private int delta       =  1;
	
	public SonarObserverFortesting(String name, boolean oneShot) {
		this.name    = name;
		this.oneShot = oneShot;
	}
	@Override
	public void update(Observable source, Object data) {
		 //Colors.out( name + " | update data=" + data ); //+ " from " + source	
		 update( data.toString() );
	}

	@Override
	public void update(String vs) {
 		 if(oneShot) {
 			 assertTrue(  vs.equals( ""+RadarSystemConfig.testingDistance) );	
 		 }else {
 			 int value = Integer.parseInt(vs);
 			 if( v0 == -1 ) {	//set the first value observed
 				v0 = value;
 				Colors.out( name + "| v0=" + v0);
 			 }else {
 				Colors.out( name + "| value=" + value );  
  				int vexpectedMin = v0-delta;
 				int vexpectedMax = v0+delta;
 				assertTrue(  value <= vexpectedMax && value >= vexpectedMin );
 				v0 = value;			 
 			 }
 		 }
	}
	
}
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
			
		ISonarObservable sonar = DeviceFactory.createSonarObservable();
		IObserver obs1         = new SonarObserverFortesting("obs1",true) ;
		sonar.register( obs1 );	//add an observer
		sonar.activate();
		sonar.register( new SonarObserverFortesting("obs2",true) );	//add an observer
		
		int v0 = sonar.getDistance().getVal();
 		System.out.println("testSingleshotSonarObservableMock v0=" + v0);
 		assertTrue(  v0 == RadarSystemConfig.testingDistance );
	}
	
	@Test 
	public void testSonarObservableMock() {
		RadarSystemConfig.testing    = false;
 		RadarSystemConfig.sonarDelay = 10;		//quite fast generation ...
		int delta = 1;
				
		ISonarObservable sonar = DeviceFactory.createSonarObservable();
		
		IObserver obs1          = new SonarObserverFortesting("obs1",false);
		IObserver obs2          = new SonarObserverFortesting("obs2",false);
		
		sonar.register( obs1 );	//add an observer
		sonar.register( obs2 );	//add an observer

		sonar.activate();

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
		}		
	}
	
 
}
