package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarMock extends SonarModel implements ISonar{
  
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
			//Colors.out("SonarMock | sonarProduce curVal="+curVal, Colors.ANSI_PURPLE);
			stopped = ( curVal == 0 );
		}
		valueUpdated(   ); 
		delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
 	}
 
}
