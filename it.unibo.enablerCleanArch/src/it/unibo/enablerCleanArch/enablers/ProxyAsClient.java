package it.unibo.enablerCleanArch.enablers;
import java.net.Socket;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapConnection;
import it.unibo.enablerCleanArch.supports.mqtt.MqttConnection;
import it.unibo.enablerCleanArch.supports.tcp.TcpClientSupport;
import it.unibo.enablerCleanArch.supports.tcp.TcpConnection;

public class ProxyAsClient {
private Interaction2021 conn; 
protected String name ;		//could be a uri
protected ProtocolType protocol ;

/*
 * Realizza la connessione di tipo Interaction2021 (concetto astratto)
 * in modo diverso, a seconda del protocollo indicato (tecnologia specifica)
 */
 
	public ProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		try {
			ColorsOut.out(name+"  | CREATING entry= "+entry+" protocol=" + protocol, ColorsOut.BLUE );
			this.name     = name;
			this.protocol = protocol;			 
			setConnection(host,  entry,  protocol);
			ColorsOut.out(name+"  | CREATED entry= "+entry+" conn=" + conn, ColorsOut.BLUE );
		} catch (Exception e) {
			ColorsOut.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
 	
	protected void setConnection( String host, String entry, ProtocolType protocol  ) throws Exception {
		switch( protocol ) {
			case tcp : {
				int port = Integer.parseInt(entry);
				//conn = new TcpConnection( new Socket( host, port ) ) ; //non fa attempts
				conn = TcpClientSupport.connect(host,  port, 10); //10 = num of attempts
				ColorsOut.out(name + " |  setConnection "  + conn, ColorsOut.BLUE );		
				break;
			}
			case coap : {
				//conn = new CoapConnection("CoapSupport_"+name, host,  entry);  //entry is uri path
				conn = new CoapConnection( host,  entry );
				break;
			}
			case mqtt : {
				//La connessione col Broker viene stabilita in fase di configurazione
				//La entry è quella definita per ricevere risposte;
				//ColorsOut.out(name+"  | ProxyAsClient connect MQTT entry=" + entry );
				conn = MqttConnection.getSupport();					
 				break;
			}	
			default :{
				ColorsOut.outerr(name + " | Protocol unknown");
			}
		}
	}
  	
	public void sendCommandOnConnection( String cmd )  {
 		//ColorsOut.out( name+"  | sendCommandOnConnection " + cmd + " conn=" + conn, ColorsOut.BLUE );
		try {
			conn.forward(cmd);
		} catch (Exception e) {
			ColorsOut.outerr( name+"  | sendCommandOnConnection ERROR=" + e.getMessage()  );
		}
	}
	public String sendRequestOnConnection( String request )  {
 		ColorsOut.out( name+"  | sendRequestOnConnection request=" + request + " conn=" + conn );
		try {
			String answer = conn.request(request);
			ColorsOut.out( name+"  | sendRequestOnConnection-answer=" + answer  );
			return Utils.getContent( answer );
 		
		} catch (Exception e) {
			ColorsOut.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );
			return null;
		}
 	}	
	public Interaction2021 getConn() {
		return conn;
	}
	
	public void close() {
		try {
			conn.close();
			ColorsOut.out(name + " |  CLOSED " + conn  );
		} catch (Exception e) {
			ColorsOut.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );		}
	}
}
