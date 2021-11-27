package it.unibo.enablerCleanArch.supports.coap;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

public class CoapApplServer {
	
	private static CoapServer server = new CoapServer();
	private static CoapResource root = new CoapResource("devices");
	private static boolean started = false;
	
	public final static String outputDeviceUri = "devices/output";
	public final static String inputDeviceUri  = "devices/input";
	
	public static void init() {
		if( ! started ) {		//SINGLETON
			started = true;
			server.start();
			root.add(new CoapResource("output"));
			root.add(new CoapResource("input"));
			server.add( root );
		}
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
				System.out.println("CoapApplServer | getResource finds "+ curRes );
				return  curRes;
			}
		}
 		return null;
	}
	public static void addCoapResourceAtRoot( CoapResource resource   )   {
       	root.add( resource );
	}
	public static void addCoapResource( CoapResource resource, String fatherUri  )   {
		Resource res = getResource(fatherUri);
		if( res != null ) res.add( resource );
	}
	public static void addOutputResource( CoapResource resource   )   {
		Resource outputDevice = getResource( "/"+outputDeviceUri );
		if( outputDevice != null )  outputDevice.add(resource);
	}
	public static void addInputResource( CoapResource resource   )   {
		Resource inputDevice = getResource( "/"+inputDeviceUri );
		if( inputDevice != null ) inputDevice.add(resource);
	}
	public static void stopServer() {
		server.stop();
	}

}
