package it.unibo.actorSystem22.context;

import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.tcp.TcpConnection;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;

/*
 * Il server deve andare in suo Thread perchè si sospende
 */
public class TcpCtxServer extends Thread{
private	String name;
private int port;
private boolean stopped = true;
private int nConn = 0;
private ServerSocket serversock;

	public TcpCtxServer(String name, int port ) {
		this.name = name;
		this.port = port;
		start();
  	}

	@Override
	public void run( ) {
		ColorsOut.out(name + " | run port=" + port   );	 
		stopped = false;
		try {
		    serversock            = new ServerSocket( port );
		    serversock.setSoTimeout(CommSystemConfig.serverTimeOut);
 			while( ! stopped ) {
				//Accept a connection				 
				ColorsOut.out(name + " | waits on server port=" + port + " serversock=" + serversock );	 
		 		Socket sock          = serversock.accept();	
				ColorsOut.outappl(name + " | accepted connection  ", ColorsOut.BLUE   ); 
 		 		Interaction2021 conn = new TcpConnection(sock);
 		 		nConn += 1;
 		 		//Attivo un thread perchè si sospende in attesa di msg
				new ConnHandler("connH"+nConn, conn);
			}//while
		}catch (Exception e) { 
		   ColorsOut.outerr(name + " | ERROR: " + e.getMessage());
		}
		
	}

}
