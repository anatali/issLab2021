package it.unibo.enablerCleanArch.domain;
import java.util.Observable;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

/*
 * There is no more getVal
 * The observers are called when a new value is produced
 */
public abstract class SonarModelObservableOld extends Observable implements ISonarObservable{
	protected  int curVal = 0;
	protected boolean stopped  = false;

	
	public static ISonarObservable create() {
		//if( RadarSystemConfig.simulation )  
			return createSonarMockObservable();
		//else  return createSonarConcreteObservable();		
	}

	public static ISonarObservable createSonarMockObservable() {
		Colors.out("createSonarMockObservable");
		return new SonarMockObservable();
	}	
	/*
	public static ISonar createSonarConcrete() {
		Colors.out("createSonarConcrete");
		return new SonarConcrete();
	}	*/
	

	protected abstract void sonarSetUp() ;
	protected abstract void sonarProduce() ;

	@Override
	public boolean isActive() {
		return ! stopped;
	}
	
	@Override
	public void activate() {
		sonarSetUp();
		Colors.out("Sonar | activate");
		stopped = false;
		new Thread() {
			public void run() {
				while( ! stopped  ) {
					sonarProduce();
					//Observable
			        setChanged();
			        notifyObservers(curVal);
				}
				Colors.out("Sonar | ENDS");
		    }
		}.start();
	}
 	
	@Override
	public void deactivate() {
		Colors.out("Sonar | deactivate");
		stopped = true;
	}
 	
	protected void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}
	
//Observable	
	@Override
	public void register( IObserver obs ) {
		this.addObserver( obs );
	}
	@Override
	public void unregister( IObserver obs ) {
		this.deleteObserver( obs );
	}	
	
	
	
}
