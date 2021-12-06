package it.unibo.enablerCleanArch.domain;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class SonarMockObservable extends SonarModelObservable {

	@Override
	protected void sonarSetUp() {
		curVal = 90;		
		System.out.println("SonarMock | sonarSetUp curVal="+curVal);
	}

	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			curVal = RadarSystemConfig.testingDistance;
			stopped = true;  //one shot
		}else {
			curVal--;
			//System.out.println("SonarMock | sonarProduce curVal="+curVal);
			stopped = ( curVal == 0 );
		}
 		delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
	}

 	
 
 
 
}
