package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Interaction2021;


public abstract class EnablerAsClient {
private Interaction2021 conn; 
protected String name ;	

	public EnablerAsClient( String name, String host, int port ) {
		try {
			this.name = name;
			 conn = setProtocolClient(host,  port);
		} catch (Exception e) {
			System.out.println( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected abstract Interaction2021 setProtocolClient( String host, int port  ) throws Exception;
	
	protected void sendValueOnConnection( String val ) throws Exception{
		conn.forward(val);
	}
	
	public Interaction2021 getConn() {
		return conn;
	}
}
