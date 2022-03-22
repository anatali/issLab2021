package it.unibo.comm2022.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/*
* A receiver connection is activate with ia==null
* Each receiver connection object ha  its own firstReceivedPacket
* 
* https://www.ionos.it/digitalguide/server/know-how/udp-user-datagram-protocol/
*/

public class UdpConnection implements Interaction2021{
	private DatagramSocket socket;
	private int portNum;
	private InetAddress ia ;
	private DatagramPacket firstReceivedPacket;
	
	public UdpConnection( DatagramSocket socket, int portNum, InetAddress ia ) {
		this.socket = socket;
		this.portNum = portNum;
		this.ia      = ia;
		ColorsOut.outappl( "UdpConnection | CREATED socket= " + socket, ColorsOut.ANSI_YELLOW );
	}
	
	@Override
	public void forward(String msg) throws Exception {
		send(socket, ia, portNum, msg.getBytes());	
		ColorsOut.outappl( "UdpConnection | forward " + msg + " socket=" + socket, ColorsOut.ANSI_YELLOW );
	}

	@Override
	public String request(String msg) throws Exception {
		forward(  msg );
		ColorsOut.out( "UdpConnection | request=" + msg, ColorsOut.ANSI_YELLOW );
		String answer = receiveMsg();
		ColorsOut.out( "UdpConnection | answer=" + answer, ColorsOut.ANSI_YELLOW );
		return answer;
	}

	@Override
	public void reply(String msg) throws Exception {
		//forward(msg);
		sendAReplyLine(msg);
	}

	@Override
	public String receiveMsg() throws Exception {
		int timeOut = 3000;
		DatagramPacket rp = receiveAPacket( timeOut ); //Time out  
		if( firstReceivedPacket  == null ) firstReceivedPacket = rp;
		String s  = new String(rp.getData(),0,rp.getLength());
 		ColorsOut.outappl("received " + s + " host=" + rp.getSocketAddress()+ "  port=" + rp.getPort(), ColorsOut.GREEN);
		return s;
	}

	@Override
	public void close() throws Exception {
		try {
			socket.close();
			ColorsOut.out( "UdpConnection | CLOSED  " );
		} catch (Exception e) {
			ColorsOut.outerr( "UdpConnection | close ERROR " + e.getMessage());	
		}		
	}

//-------------------------------------------------
	protected synchronized void send(
			DatagramSocket ds, InetAddress ia, int portNum, byte[] buffer) throws Exception{
		if( buffer.length > 64900) throw new IOException();
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length, ia, portNum);
		ds.send(dp);
 	}
	
	public  DatagramPacket receiveAPacket(  int timeout ) throws Exception {
 		byte[] 	buffer = new byte[65507];
 		DatagramPacket curPacket  = new DatagramPacket(buffer,buffer.length);
//			System.out.println("*** receiveAPacket " + socket   );
		socket.setSoTimeout(timeout);	//blocks for no more than 0.2 sec	    	
		socket.receive(curPacket);
		ColorsOut.out( "UdpConnection | received packet ...  curPacket=" + curPacket.getSocketAddress() );
		return curPacket;
 	}	
	
	
	protected void sendALine(InetAddress ia, int destPort, String msg) throws Exception {
		ColorsOut.outappl("UdpConnection |  sendALine destPort=" + destPort + "  port=" + destPort, ColorsOut.GREEN);
		send(socket, ia, destPort, msg.getBytes() );
	}
	
	public void sendAReplyLine( String msg )throws Exception{
		if( firstReceivedPacket != null )
			sendALine( firstReceivedPacket.getAddress(), firstReceivedPacket.getPort(), msg );
		else throw new Exception("UdpConnection |  ERROR: reply without caller");
	}
	
	
}
