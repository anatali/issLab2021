package it.unibo.enablerCleanArch.domain;

public interface ISonar {
	public void activate();		 
	public void deactivate();
	public IDistance getDistance();	
	public boolean isActive();
}
