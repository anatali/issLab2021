package it.unibo.enablerCleanArch.domain;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public abstract class SonarModelWithQueue  implements ISonar{  
public final static int queueSize = 1;  //backpressure to the production
protected  IDistance curVal = new Distance(90);	 
protected BlockingQueue<IDistance> blockingQueue = new LinkedBlockingDeque<IDistance>(queueSize);
protected boolean stopped  = true;
	
	
	public static ISonar create() {
		if( RadarSystemConfig.simulation )  return createSonarMock();
		else  return createSonarConcrete();		
	}

	public static ISonar createSonarMock() {
		ColorsOut.out("createSonarMock");
		return new SonarMock();
	}	
	public static ISonar createSonarConcrete() {
		ColorsOut.out("createSonarConcrete");
		return new SonarConcrete();
	}	
	
	protected SonarModelWithQueue() {//hidden costructor, to force setup
		sonarSetUp();  //Lo mettiamo anche in activate
	}
	
	protected void updateDistance( int d ) {
		try {
			curVal = new Distance( d );
			ColorsOut.out("SonarModel | updateDistance "+ d);
			blockingQueue.put( curVal );
		} catch (InterruptedException e) {
			ColorsOut.outerr("SonarModel | updateDistance ERROR:"+e.getMessage());
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
			ColorsOut.outerr("SonarModel | ERROR:"+e.getMessage());
			return null;
		}	
	}
	
	@Override
	public void activate() {
		curVal = new Distance( 90 );
		//sonarSetUp(); 
		ColorsOut.out("SonarModel | activate", ColorsOut.GREEN);
		stopped = false;
		blockingQueue.clear();
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					//Colors.out("SonarModel | call produce", Colors.GREEN);
					sonarProduce(  );
					//Utils.delay(RadarSystemConfig.sonarDelay);
				}
				ColorsOut.out("SonarModel | ENDS", ColorsOut.GREEN);
		    }
		}.start();
	}
 	
	@Override
	public void deactivate() {
		ColorsOut.out("SonarModel | deactivate", ColorsOut.GREEN);
		stopped = true;
		blockingQueue.clear();  //IMPORTANT if queueSize > 1
	}

}

  

