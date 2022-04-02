package unibo.actor22;

import java.util.HashMap;

import it.unibo.actorComm.proxy.ProxyAsClient;

public class Qak22Context {
	private static HashMap<String,QakActor22> ctxMap      = new HashMap<String,QakActor22>();
    private static HashMap<String,ProxyAsClient> proxyMap = new HashMap<String,ProxyAsClient>();

	public static void addActor(QakActor22 a) {	
        ctxMap.put(a.getName(), a);
    }
	
    public static QakActor22 getActor(String actorName) {
        return ctxMap.get(actorName);
    }
	
}
