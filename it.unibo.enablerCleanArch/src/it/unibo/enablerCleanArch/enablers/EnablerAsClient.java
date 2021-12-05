package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;


public abstract class EnablerAsClient {
private Interaction2021 conn; 
protected String name ;	

	public EnablerAsClient( String name, String host, int port ) {
		try {
			this.name = name;
			conn = setConnection(host,  port);
			Colors.out(name+"  | STARTS conn=" + conn);
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected abstract Interaction2021 setConnection( String host, int port  ) throws Exception;
	
	protected void sendValueOnConnection( String val ) throws Exception{
		//Colors.out( name+"  | sendValueOnConnection " + val  );
		conn.forward(val);
	}
	
	public Interaction2021 getConn() {
		return conn;
	}
}
