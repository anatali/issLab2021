package it.unibo.comm2022.udp;
 
import java.net.DatagramSocket;
import java.net.InetAddress;

import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;


public class UdpClientSupport {
	protected final static SocketUdpSupport udpSupport = new SocketUdpSupport();
	
	public static Interaction2021 connect(String host, int port, int nattempts ) throws Exception {		 
		for( int i=1; i<=nattempts; i++ ) {
			try {
				DatagramSocket socket =  udpSupport.connectAsClient(host,port);
				InetAddress ia        = InetAddress.getByName( host );
				Interaction2021 conn  =  new UdpConnection( socket,port,ia );
				ColorsOut.outappl("UdpClient | connected with host:" + host + " socket=" + socket, ColorsOut.BLUE);
				return conn;
			}catch(Exception e) {
				ColorsOut.out("UdpClient | Another attempt to connect with host:" + host + " port=" + port);
				Thread.sleep(500);
			}
		}//for
		throw new Exception("UdpClient | Unable to connect to host:" + host);
		 
 	}
 
}
