package it.unibo.radarGui21;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.Resource;

public class CoapApplServer {
	
	private static CoapServer server = new CoapServer();
	private static CoapResource root = new CoapResource("sonar");

	public static void init() {
		server.start();
		//root=root.add( new Distance("distance") );
 		server.add( root );		
	}
	
	public static CoapResource getResource( String path ) {
		Collection<Resource> rootChilds = root.getChildren();
		Iterator<Resource> iter         = rootChilds.iterator();
		System.out.println("child size:"+rootChilds.size());
		while( iter.hasNext() ) {
			System.out.println("child:"+iter.next().getURI());
		}
		//System.out.println(""+root.getChildren());
		return null;
	}
 	
	public static void addCoapResource( CoapResource resource, CoapResource father  )   {
		if( father == null ) root.add( resource );
		else father.add(resource);
	}
	/*
	public static void addCoapResource(CoapResource resource, String path) throws Exception {
		String[] items = path.split("/");
		CoapResource root  = new Distance(items[0]);
		CoapResource r = root;
		for( int i=1; i < items.length; i++) {
			r = r.add( new Distance(items[i]) );			
		}
		server.add( r );   
		server.start();	
	}
	*/
	public static void stopServer() {
		server.stop();
	}

}
