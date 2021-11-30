package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class SonarMock extends SonarModel implements ISonar{
  
	@Override
	protected void sonarSetUp() {
		curVal = 90;		
	}
	
	@Override
	protected void sonarProduce() {
		curVal--;
		//System.out.println("SonarMock | curVal="+curVal);
		if( curVal < 0 ) stopped = true;
		setVal(   ); 
		delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
 	}
 
}
