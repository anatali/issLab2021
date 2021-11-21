package it.unibo.enablerCleanArch.supports;

/*
 * 
 */
public abstract class ApplMessageHandler { //implements ApplMessageHandler
protected Interaction2021 conn;		//Injected
protected String name;

	public ApplMessageHandler( String name ) {
		this.name = name;
	}
 
	public abstract void elaborate( String message ) ;
	
	/* Used in case of reply */
	public void setConn( Interaction2021 conn) {
		System.out.println("ApplMessageHandler | setConn:" + conn);
		this.conn = conn;
	}
	
	public String getName() {
		return name;
	}
	
	public Interaction2021 getConn(  ) {
		return conn;
	}
 	
}
