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
	public final static String lightsDeviceUri = outputDeviceUri+"/lights";
	public final static String inputDeviceUri  = "devices/input";
	
	public static CoapApplServer getServer() {
		if( server == null ) server = new CoapApplServer();
		return server;
	}
	
	private CoapApplServer(){
		CoapResource outputRes= new CoapResource("output");
		outputRes.add( new CoapResource("lights"));
		root.add(outputRes);
		root.add(new CoapResource("input"));
		add( root );
		start();
	}
	
	public static Resource getResource( String uri ) {
		return getResource( root, uri );		
	}
	
	//Depth-first research
	private static Resource getResource(Resource root, String uri) {
		if( root != null ) {
			//Colors.out("getResource checks in: "+root.getName() + " for uri=" + uri);
			Collection<Resource> rootChilds = root.getChildren();
			Iterator<Resource> iter         = rootChilds.iterator();
			//Colors.out("child size:"+rootChilds.size());
			while( iter.hasNext() ) {
				Resource curRes = iter.next();
				String curUri   = curRes.getURI();
				//Colors.out("getResource curUri:"+curUri);
				if( curUri.equals(uri) ){
					//Colors.out("getResource finds "+ curRes.getName() + " for " + curUri, Colors.ANSI_YELLOW);
					return  curRes;
				}else { 
					//Colors.out("getResource restart from:"+curRes.getName());
					Resource subRes = getResource(curRes,uri); 
					if( subRes != null ) return subRes;					
				}
			}//while  (all sons explored)
		}
		return null;
	}
 
	public  void addCoapResource( CoapResource resource, String fatherUri  )   {
		Resource res = getResource("/"+fatherUri);
		if( res != null ) {
			res.add( resource );
			Colors.out("CoapApplServer | added " + resource.getName(), Colors.ANSI_YELLOW );
		}else {
			Colors.outerr("addCoapResource FAILS for " + fatherUri);
		}
	}
 
	public  void stopServer() {
		stop();
	}

}
