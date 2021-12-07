package it.unibo.enablerCleanArch.domain;

public interface ISonarObservable  extends ISonar{
	  void register( IObserver obs );
	  void unregister( IObserver obs );
}
