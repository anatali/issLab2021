package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

public abstract class SonarModel implements ISonar{
	protected  static int curVal = 0;
	protected boolean stopped    = false;
	private boolean produced   = false;
	
	public static ISonar create() {
		if( RadarSystemConfig.simulation )  return createSonarMock();
		else  return createSonarConcrete();		
	}

	public static ISonar createSonarMock() {
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete() {
		return new SonarConcrete();
	}	
	
	protected abstract void sonarSetUp() ;
	protected abstract void sonarProduce() ;

	@Override
	public boolean isActive() {
		return ! stopped;
	}
	
	@Override
	public void activate() {
		sonarSetUp();
		//if( RadarSystemConfig.testing )  return;
		
		System.out.println("		Sonar | STARTS");
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					sonarProduce();
				}
				System.out.println("		Sonar | ENDS");
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
