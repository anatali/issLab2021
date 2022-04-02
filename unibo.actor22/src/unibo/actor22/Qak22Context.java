package unibo.actor22;

import java.util.HashMap;

public class Qak22Context {
	private static HashMap<String,QakActor22> ctxMap = new HashMap<String,QakActor22>();

	public static void addActor(QakActor22 a) {	
        ctxMap.put(a.getName(), a);
    }
	
    public static QakActor22 getActor(String actorName) {
        return ctxMap.get(actorName);
    }
	
}
