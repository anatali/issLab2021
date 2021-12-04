package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class SonarMock extends SonarModel implements ISonar{
  
	@Override
	protected void sonarSetUp() {
		curVal = 90;		
	}
	
	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			curVal = RadarSystemConfig.testingDistance;
			stopped = true;  //one shot
		}else {
			curVal--;
			//System.out.println("SonarMock | curVal="+curVal);
			stopped = ( curVal == 0 );
		}
		setVal(   ); 
		delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
 	}
 
}
