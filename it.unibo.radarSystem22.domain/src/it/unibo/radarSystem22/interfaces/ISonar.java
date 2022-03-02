package it.unibo.radarSystem22.interfaces;

public interface ISonar extends IDevice{
	public void activate();		 
	public void deactivate();
	public IDistance getDistance();	
	public boolean isActive();
}
