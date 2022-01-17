package it.unibo.enablerCleanArch.domain;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public abstract class SonarModel  implements ISonar{  
public final static int queueSize = 1;  //backpressure to the production
protected  IDistance curVal = new Distance(90);	 
protected BlockingQueue<IDistance> blockingQueue = new LinkedBlockingDeque<IDistance>(queueSize);
protected boolean stopped  = true;
	
	
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
	
	protected SonarModel() {//hidden costructor, to force setup
		sonarSetUp();  //Lo mettiamo anche in activate
	}
	
	protected void updateDistance( int d ) {
		try {
			curVal = new Distance( d );
			Colors.out("SonarModel | updateDistance "+ d);
			blockingQueue.put( curVal );
		} catch (InterruptedException e) {
			Colors.outerr("SonarModel | updateDistance ERROR:"+e.getMessage());
		}
	}	

	protected abstract void sonarSetUp() ;
 	protected abstract void sonarProduce() ;

	@Override
	public boolean isActive() {
		return ! stopped;
	}
	
	@Override
	public IDistance getDistance() {
		try {
			IDistance curVal = blockingQueue.take();
			//Colors.out("SonarModel | getDistance curVal="+curVal, Colors.ANSI_PURPLE);
			return curVal;
		} catch (InterruptedException e) {
			Colors.outerr("SonarModel | ERROR:"+e.getMessage());
			return null;
		}	
	}
	
	@Override
	public void activate() {
		curVal = new Distance( 90 );
		//sonarSetUp(); 
		Colors.out("SonarModel | activate", Colors.GREEN);
		stopped = false;
		blockingQueue.clear();
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					//Colors.out("SonarModel | call produce", Colors.GREEN);
					sonarProduce(  );
					Utils.delay(RadarSystemConfig.sonarDelay);
				}
				Colors.out("SonarModel | ENDS", Colors.GREEN);
		    }
		}.start();
	}
 	
	@Override
	public void deactivate() {
		Colors.out("SonarModel | deactivate", Colors.GREEN);
		stopped = true;
		blockingQueue.clear();  //IMPORTANT if queueSize > 1
	}

}

  

