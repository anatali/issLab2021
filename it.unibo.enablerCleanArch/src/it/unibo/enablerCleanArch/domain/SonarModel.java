package it.unibo.enablerCleanArch.domain;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public abstract class SonarModel  implements ISonar{  
protected  IDistance curVal = new Distance(90);	 
protected boolean stopped   = true;
 	
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
	
	protected SonarModel() {//hidden costructor, to force setup
		ColorsOut.out("SonarModel | sonarSetUp " );
		sonarSetUp();   
	}
	
	protected void updateDistance( int d ) {
		curVal = new Distance( d );
		ColorsOut.out("SonarModel | updateDistance "+ d);
	}	

	protected abstract void sonarSetUp() ;
 	protected abstract void sonarProduce() ;

	@Override
	public boolean isActive() {
		return ! stopped;
	}
	
	@Override
	public IDistance getDistance() {
			return curVal;
	}
	
	@Override
	public void activate() {
		curVal = new Distance( 90 );
 		ColorsOut.out("SonarModel | activate", ColorsOut.GREEN);
		stopped = false;
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
	}

}

  

