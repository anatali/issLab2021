package it.unibo.enablerCleanArch.domain;

public interface ISonarObservable  {
	  void register( IObserver obs );
	  void unregister( IObserver obs );
		public void activate();		 
		public void deactivate();
		//public int getVal();			//no more
		public boolean isActive();
}
