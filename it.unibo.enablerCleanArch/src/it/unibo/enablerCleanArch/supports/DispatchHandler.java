package it.unibo.enablerCleanArch.supports;

/*
 * 
 */
public abstract class DispatchHandler {
protected String name;

	public DispatchHandler( String name ) {
		this.name = name;
	}
 
	protected abstract void elaborate( String message ) ;
	
 	
	public String getName() {
		return name;
	}
	
 
	
	
}
