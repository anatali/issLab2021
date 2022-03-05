package it.unibo.radarSystem22.actors.context;

import java.util.HashMap;

import it.unibo.kactor.ActorBasic;

public class ActorLocalContext {
	
	private static HashMap<String,ActorBasic> ctxMap = new HashMap<String,ActorBasic>();
	
	public static void addActor(ActorBasic a) {
		ctxMap.put(a.getName(), a);
	}
	public static ActorBasic getActor(String actorName) {
		return ctxMap.get(actorName);
	}
}
