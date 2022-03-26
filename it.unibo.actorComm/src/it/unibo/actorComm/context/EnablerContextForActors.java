package it.unibo.actorComm.context;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.IContext;
import it.unibo.actorComm.interfaces.IContextMsgHandler;
import it.unibo.actorComm.tcp.TcpServer;
import it.unibo.actorComm.udp.UdpServer;
import it.unibo.actorComm.utils.ColorsOut;

public class EnablerContextForActors  implements IContext {  
private IApplMsgHandler ctxMsgHandler;
 
protected String name;
protected ProtocolType protocol;
protected TcpServer serverTcp;
protected UdpServer serverUdp;
protected boolean isactive = false;

	public EnablerContextForActors( String name, int port, ProtocolType protocol  )   { 
		this(name,""+port,protocol, new ContextMsgHandler("ctxH") );
	}

	public EnablerContextForActors( String name, String port, ProtocolType protocol, IApplMsgHandler handler )   { 
 		try {
			this.name     			= name;
			this.protocol 			= protocol;
			ctxMsgHandler           = handler;
 			if( protocol != null ) {
				setServerSupport( port, protocol, handler  );
			}else ColorsOut.out(name+" |  CREATED no protocol"  );
		} catch (Exception e) {
			ColorsOut.outerr(name+" |  CREATE Error: " + e.getMessage()  );
		}
	}
	
 	protected void setServerSupport( String portStr, ProtocolType protocol, IApplMsgHandler handler   ) throws Exception{
		if( protocol == ProtocolType.tcp  ) {
			int port = Integer.parseInt(portStr);
			serverTcp = new TcpServer( name+"Tcp" , port,  handler );
		}
		else if( protocol == ProtocolType.udp ) {  
			int port = Integer.parseInt(portStr);
			serverUdp = new UdpServer(name+"Udp" ,port,handler);
		}
		else if( protocol == ProtocolType.coap ) {
			//CoapApplServer.getTheServer();	//Le risorse sono create alla configurazione del sistema
			ColorsOut.out(name+" |  CREATED  CoapApplServer", ColorsOut.BLUE  );
		}
		else if( protocol == ProtocolType.mqtt ) {  
			ColorsOut.out(name+" |  Do nothing for mqtt", ColorsOut.BLUE );
		}
	}	
 	
 	public String getName() {
 		return name;
	}
 	public boolean isActive() {
 		return isactive;
 	}
//	public void addComponent( String devname, IApplMsgHandler h) {
//		ctxMsgHandler.addComponent(devname, h);
//	}
//	public void removeComponent( String devname ) {
//		ctxMsgHandler.removeComponent( devname );
//	}
	@Override
	public void activate() {
		ColorsOut.out(name+" |  activate ...", ColorsOut.BLUE );
		switch( protocol ) {
	   		case tcp :  { serverTcp.activate();break;}
	   		case udp:   { serverUdp.activate();break;}
	   		default: break;
	    }
		isactive = true;		
	}

	@Override
	public void deactivate() {
 		//ColorsOut.out(name+" |  deactivate  "  );
		switch( protocol ) {
	   		case tcp :  { serverTcp.deactivate();break;}
	   		case udp:   { serverUdp.deactivate();break;}
	   		default: break;
	    }
		isactive = false;
	}
  	 
}
