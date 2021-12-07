package it.unibo.enablerCleanArch.domain;

import java.util.Observable;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarModel extends Observable implements ISonar{
	protected  int curVal = 0;
	protected boolean stopped  = false;
	private boolean produced   = false;
	
	public static ISonar create() {
		if( RadarSystemConfig.simulation )  return createSonarMock();
		else  return createSonarConcrete();		
	}

	public static ISonar createSonarMock() {
		Colors.out("createSonarMock");
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete() {
		Colors.out("createSonarConcrete");
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
		Colors.out("SonarModel | activate", Colors.GREEN);
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					sonarProduce();
				}
				Colors.out("SonarModel | ENDS", Colors.GREEN);
		    }
		}.start();
	}
 	
	@Override
	public void deactivate() {
		Colors.out("Sonar | deactivate");
		stopped = true;
	}

	protected synchronized void valueUpdated( ){
		produced = true;
		this.notify();
	}

	@Override
	public  int getVal() {
		waitForUpdatedVal();
		return curVal;
	}

	protected synchronized void waitForUpdatedVal() {
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

 
	
}
