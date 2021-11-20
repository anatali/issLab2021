package it.unibo.enabler;

public abstract class ApplMessageHandler {
protected Interaction2021 conn;
 	
	protected abstract void elaborate( String message ) ;
	
	/* Used when reply */
	public void setConn( Interaction2021 conn) {
		System.out.println("ApplMessageHandler | setConn:" + conn);
		this.conn = conn;
	}
}
