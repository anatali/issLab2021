package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMock extends SonarModel implements ISonar{
	protected  ISonarState curVal ;
  
	@Override
	protected void sonarSetUp() {
		curVal = new SonarState(90);		
		Colors.out("SonarMock | sonarSetUp curVal="+curVal);
	}
	
	@Override
	public int getVal() {
		//Colors.out("SonarMock | getVal curVal="+curVal, Colors.ANSI_PURPLE);
		waitForUpdatedVal();
		return curVal.getVal();
	}

	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			curVal.setVal(  RadarSystemConfig.testingDistance );
			stopped = true;  //one shot
		}else {
			curVal.setVal( curVal.getVal() - 1 );
			//Colors.out("SonarMock | sonarProduce curVal="+curVal, Colors.ANSI_PURPLE);
			stopped = ( curVal.getVal() == 0 );
		}
		valueUpdated(   ); 
		Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation 
 	}
 
}
