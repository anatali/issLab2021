package it.unibo.enablerCleanArch.supports.coap;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;
import it.unibo.enablerCleanArch.supports.Colors;

public class CoapApplServer extends CoapServer{
	
	//private static CoapServer server = new CoapServer();
	private static CoapResource root      = new CoapResource("devices");
	private static CoapApplServer server  = null;
	
	public final static String outputDeviceUri = "devices/output";
	public final static String inputDeviceUri  = "devices/input";
	
	public static CoapApplServer getServer() {
		if( server == null ) server = new CoapApplServer();
		return server;
	}
	
	public CoapApplServer(){
		root.add(new CoapResource("output"));
		root.add(new CoapResource("input"));
		add( root );
		start();
	}
	
	public static Resource getResource( String uri ) {
		Collection<Resource> rootChilds = root.getChildren();
		Iterator<Resource> iter         = rootChilds.iterator();
		//System.out.println("child size:"+rootChilds.size());
		while( iter.hasNext() ) {
			Resource curRes = iter.next();
			String curUri   = curRes.getURI();
			//System.out.println("CoapApplServer | getResource curUri:"+curUri);
			if( curUri.equals(uri) ){
				Colors.out("CoapApplServer | getResource finds "+ curRes.getName() + " for " + curUri, Colors.ANSI_YELLOW);
				return  curRes;
			}
		}
 		return null;
	}
	public  void addCoapResourceAtRoot( CoapResource resource   ){
		Colors.out("CoapApplServer | added " + resource.getName(), Colors.ANSI_YELLOW );
       	root.add( resource );
	}
	public  void addCoapResource( CoapResource resource, String fatherUri  )   {
		Colors.out("CoapApplServer | added " + resource.getName(), Colors.ANSI_YELLOW );
		Resource res = getResource("/"+fatherUri);
		if( res != null ) res.add( resource );
	}
	public  void addOutputResource( CoapResource resource   )   {
		Resource outputDevice = getResource( "/"+outputDeviceUri );
		if( outputDevice != null )  outputDevice.add(resource);
	}
	public  void addInputResource( CoapResource resource   )   {
		Resource inputDevice = getResource( "/"+inputDeviceUri );
		if( inputDevice != null ) inputDevice.add(resource);
	}
	public  void stopServer() {
		stop();
	}

}
