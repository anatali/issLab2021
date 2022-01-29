package it.unibo.enablerCleanArch.domain;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public abstract class SonarModel  implements ISonar{  
protected  IDistance curVal = new Distance(90);	 
protected boolean stopped   = true;
 	
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
		Colors.out("SonarModel | sonarSetUp " );
		sonarSetUp();   
	}
	
	protected void updateDistance( int d ) {
		curVal = new Distance( d );
		Colors.out("SonarModel | updateDistance "+ d);
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
 		Colors.out("SonarModel | activate", Colors.GREEN);
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					//Colors.out("SonarModel | call produce", Colors.GREEN);
					sonarProduce(  );
					//Utils.delay(RadarSystemConfig.sonarDelay);
				}
				Colors.out("SonarModel | ENDS", Colors.GREEN);
		    }
		}.start();
	}
 	
	@Override
	public void deactivate() {
		Colors.out("SonarModel | deactivate", Colors.GREEN);
		stopped = true;
	}

}

  

