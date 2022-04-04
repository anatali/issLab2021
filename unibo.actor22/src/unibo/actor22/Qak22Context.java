package unibo.actor22;

import java.util.HashMap;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.proxy.ProxyAsClient;

 

public class Qak22Context {
	private static HashMap<String,QakActor22> ctxMap      = new HashMap<String,QakActor22>();
    private static HashMap<String,ProxyAsClient> proxyMap = new HashMap<String,ProxyAsClient>();

    public static final String actorReplyPrefix = "arp_";
    
    public static void setActorAsRemote(
    		String actorName, String entry, String host, ProtocolType protocol ) {
//    	if( ! proxyMap.containsKey(actorName)   ) { //defensive
    		if( ! proxyMap.containsKey(actorName+"Pxy")) { //un solo proxy per contesto remoto
	    		ProxyAsClient pxy = new ProxyAsClient(actorName+"Pxy", host, entry, protocol);
	    		proxyMap.put(actorName, pxy);
    		}
//    	}   	
    }
    
	public static void addActor(QakActor22 a) {	
        ctxMap.put(a.getName(), a);
    }
	public static void removeActor(QakActor22 a) {	
        ctxMap.remove( a.getName() );
    }	
    public static QakActor22 getActor(String actorName) {
        return ctxMap.get(actorName);
    }
    
//proxy
 
	public static ProxyAsClient getProxy(String actorName) {
        return proxyMap.get(actorName);
    }
    
    
}
