package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public class SonarMock implements ISonar{
private static int curVal = 90;
private boolean stopped   = false;
private boolean produced  = false;

	public static ISonar create() {
		SonarMock sonar = new SonarMock();
		//sonar.activate();
		return sonar;
	}
	
	public void deactivate() {
		stopped = true;
	}
	public void activate() {
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					curVal--;
					//System.out.println("SonarMock | curVal="+curVal);
					if( curVal < 0 ) break;
					setVal(   );
					delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
				}//
				System.out.println("SonarMock | ENDS");
			}
		}.start();
	}

	synchronized void setVal( ){
		produced = true;
		this.notify();
	}

	@Override
	public  int getVal() {
		waitForUpdatedVal();
		delay(800);		//simulate network delay
		return curVal;
	}

	private synchronized void waitForUpdatedVal() {
		try {
 			while( ! produced ) wait();
 			produced = false;
 		} catch (InterruptedException e) {
 			System.out.println("SonarMock | waitForUpdatedVal ERROR " + e.getMessage() );
		}		
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}

}
