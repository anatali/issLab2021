package it.unibo.enablerCleanArch.domain;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class SonarObservableMock extends SonarObservableModel {

	@Override
	protected void sonarSetUp() {
		curVal = 90;		
		System.out.println("SonarMockObservable | sonarSetUp curVal="+curVal);
	}

	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			curVal = RadarSystemConfig.testingDistance;
			stopped = true;  //one shot
		}else {
			curVal--;
			//System.out.println("SonarMockObservable | sonarProduce curVal="+curVal);
			stopped = ( curVal == 0 );
		}
		valueUpdated(   ); 
 		delay(RadarSystemConfig.sonarDelay);  //avoid too fast generation 
	}
 
}
