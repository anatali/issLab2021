package it.unibo.radarSystem22.interfaces;

public interface ISonarObservable  extends ISonar{
	  void register( IObserver obs );
	  void unregister( IObserver obs );
}
