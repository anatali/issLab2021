package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public abstract class SonarAbstract implements ISonar{
	protected  static int curVal = 0;
	protected boolean stopped    = false;
	protected boolean produced   = false;

	public static ISonar createSonarMock() {
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete() {
		return new SonarConcrete();
	}	
	
	protected abstract void sonarSetUp() ;
	protected abstract void sonarBehavior() ;

	@Override
	public void activate() {
		sonarSetUp();
		if( RadarSystemConfig.testing )  return;
		
		System.out.println("Sonar | STARTS");
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					sonarBehavior();
				}
				System.out.println("Sonar | ENDS");
		    }
		}.start();
	}

	
	
	@Override
	public void deactivate() {
		stopped = true;
	}


	protected synchronized void setVal( ){
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
 			System.out.println("Sonar | waitForUpdatedVal ERROR " + e.getMessage() );
		}		
	}
	
	protected void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}

	//ADDED for testing
	//-------------------------------------------------
	
	public void oneShot( int d ) {
		curVal = d;
		setVal();
	}	
	
}