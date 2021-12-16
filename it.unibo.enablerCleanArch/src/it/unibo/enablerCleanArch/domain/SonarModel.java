package it.unibo.enablerCleanArch.domain;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarModel  implements ISonar{ //extends Observable
public final static int queueSize = 10;
//	protected  IDistance curVal ;
	
	protected boolean stopped  = false;
//	private boolean produced   = false;
	
	private BlockingQueue<IDistance> blockingQueue = new LinkedBlockingDeque<IDistance>(queueSize);
	
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
	
	protected SonarModel() {
		sonarSetUp();
	}
	protected abstract void sonarSetUp() ;
	//protected abstract void sonarProduce() ;
	protected abstract void sonarProduce(BlockingQueue<IDistance> queue) ;

	@Override
	public boolean isActive() {
		return ! stopped;
	}
	
	@Override
	public IDistance getDistance() {
		//Colors.out("SonarModel | getDistance curVal="+curVal, Colors.ANSI_PURPLE);
		//waitForUpdatedVal();		
		try {
			IDistance curVal = blockingQueue.take();
			return curVal;
		} catch (InterruptedException e) {
			Colors.outerr("SonarMock | ERROR:"+e.getMessage());
			return null;
		}	
	}
	
	@Override
	public void activate() {
		Colors.out("SonarModel | activate", Colors.GREEN);
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					sonarProduce( blockingQueue );
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

	/*
	protected synchronized void valueUpdated( ){
		produced = true;
		this.notify();
	}

	protected synchronized void waitForUpdatedVal() {
		try {
 			while( ! produced ) wait();
 			produced = false;
 		} catch (InterruptedException e) {
 			System.out.println("Sonar | waitForUpdatedVal ERROR " + e.getMessage() );
		}		
	}*/
}
