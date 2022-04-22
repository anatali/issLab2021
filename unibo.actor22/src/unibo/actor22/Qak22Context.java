package unibo.actor22;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.annotations.Actor22;
import unibo.actor22.annotations.ActorLocal;
import unibo.actor22.annotations.ActorRemote;
import unibo.actor22.annotations.Context22;
import unibo.actor22comm.ProtocolInfo;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.context.EnablerContextForActors;
import unibo.actor22comm.events.EventMsgHandler;
import unibo.actor22comm.proxy.ProxyAsClient;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

 

public class Qak22Context {
	private static HashMap<String,QakActor22> ctxMap      = new HashMap<String,QakActor22>();
    private static HashMap<String,ProxyAsClient> proxyMap = new HashMap<String,ProxyAsClient>();

    public static final String actorReplyPrefix = "arply_";
    
    public static void setActorAsRemote(String actorName, 
    				String entry, String host, ProtocolType protocol ) {
			ColorsOut.out("Qak22Context | setActorAsRemote entry=" + entry+ " at " + host + " since:" + actorName, ColorsOut.MAGENTA);
    	    ProxyAsClient pxy = proxyMap.get(host+"Pxy");
     		if( pxy == null ) { //un solo proxy per contesto remoto
	    		pxy = new ProxyAsClient(host+"Pxy", host, entry, protocol);
	    		ColorsOut.out("Qak22Context | CREATED proxy for " + host + " since:" + actorName, ColorsOut.MAGENTA);
	    		proxyMap.put(host+"Pxy", pxy);
    		}else {
	    		ColorsOut.out("Qak22Context | EXISTS proxy for " + host + " since:" + actorName, ColorsOut.MAGENTA);  			
    		}
    		proxyMap.put(actorName, pxy);
    }
    

	public static void addActor(QakActor22 a) {	
        ctxMap.put(a.getName(), a);
    }
	public static void removeActor(QakActor22 a) {	
        ctxMap.remove( a.getName() );
    }	
	public static void showActorNames( ) {
		ColorsOut.outappl("CURRENT ACTORS in Context:", ColorsOut.MAGENTA);
		ctxMap.forEach( (  v,x) ->  
    		ColorsOut.outappl("" + v , ColorsOut.MAGENTA)
		 );
	}
    public static QakActor22 getActor(String actorName) {
        return ctxMap.get(actorName);
    }
    
//proxy
 
	public static ProxyAsClient getProxy(String actorName) {
        return proxyMap.get(actorName);
    }

	
	
//Annotations
	
    public static void handleLocalActorDecl(Object element) {   	
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
         for (Annotation annot : annotations) {
        	 if (annot instanceof ActorLocal) {
        		 ActorLocal a = (ActorLocal) annot;
        		 for( int i=0; i<a.name().length; i++) {
        			 String name     = a.name()[i];
        			 Class  impl     = a.implement()[i];
            		 try {
						impl.getConstructor( String.class ).newInstance( name );
	            		 ColorsOut.outappl( "Qak22Context | CREATED LOCAL ACTOR: "+ name, ColorsOut.MAGENTA );
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
 						e.printStackTrace();
					}
         		 }
        	 }
         }
    }
    public static void handleRemoteActorDecl(Object element) {
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
        for (Annotation annot : annotations) {
        	 if (annot instanceof ActorRemote) {
        		 ActorRemote a = (ActorRemote) annot;
        		 for( int i=0; i<a.name().length;i++) {
        			 String name     = a.name()[i];
        			 String host     = a.host()[i];
        			 String port     = a.port()[i];
        			 String protocol = a.protocol()[i];        			 
        			 setActorAsRemote(name, port, host, ProtocolInfo.getProtocol(protocol));
//            		 ColorsOut.outappl(
//            				 "Qak22Context | CREATE REMOTE ACTOR PROXY:"+ name + " host:" + host + " port:"+port
//            						 + " protocol:" + protocol, ColorsOut.MAGENTA);        			 
        		 }
        	 }
        }
    }
    public static void handleActorDeclaration(Object element) {
    	handleLocalActorDecl(element);
    	handleRemoteActorDecl(element);
   }  
    
//Final Annotations 
    public static Map<String, Context22> setRemoteContexts22(Object element) {
        Map<String, Context22> out  = new HashMap<>();
        Class<?> clazz              = element.getClass();
        Annotation[] annotations    = clazz.getAnnotations();
        Context22[] contexts        = element.getClass().getAnnotationsByType(Context22.class);
        for (Context22 rc : contexts) {
            String name     = rc.name();
            String host     = rc.host();
            int port        = Integer.parseInt(rc.port());
            ProtocolType protocol = rc.protocol();
            out.put(name, rc);
            if( host.equals("localhost")) {
            	EnablerContextForActors ctx = new EnablerContextForActors( "ctx", port, protocol);
            	ctx.activate();
            }
            ColorsOut.outappl("Registered context: " + name+ " at "
                            + String.format("%s//%s:%s", protocol, host, port), ColorsOut.YELLOW);
        }
        return out;
    }
    
    public static void configureTheSystem(Object element) {
        Class<?> clazz             = element.getClass();
        Map<String, Context22> contextsMap = setRemoteContexts22(element);
        Actor22[] actorAnnotations = clazz.getAnnotationsByType(Actor22.class);
        for (Actor22 actor : actorAnnotations) {
                String actorName = actor.name();
                String actorCtx  = actor.contextName();
                //ColorsOut.out( "Qak22Context | handling actor: "+ actorName + " in " + actorCtx , ColorsOut.BLUE );
                Context22 refCtx = contextsMap.get(actorCtx);
                if( refCtx == null ) {
                	ColorsOut.outerr("No context found for: " + actorName  ); 
                	return;
                }
                if( refCtx.host().equals("localhost")) {
                    Class implement = actor.implement();
                    if (implement.equals(void.class))
                        throw new IllegalArgumentException("@Actor: Local actor needs a class specification");
                    try {
                         implement.getConstructor(String.class).newInstance(actorName);
                         ColorsOut.out( "Qak22Context | CREATED LOCAL ACTOR: "+ actorName, ColorsOut.MAGENTA );
                         //attivo l'attore
                         Qak22Util.sendAMsg( SystemData.activateActor(refCtx.name(),actorName) );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }              	
                }else { //Actor remote
                	setActorAsRemote( actorName, refCtx.port(), refCtx.host(), refCtx.protocol() );
                	//String actorName, String entry, String host, ProtocolType protocol
                }
        }//for         
     }

    
   
//Events
    
    public static final String registerForEvent   = "registerForEvent";
    public static final String unregisterForEvent = "unregisterForEvent";
    
	
	public static void registerAsEventObserver(String observer, String evId) {
		QakActor22 a = getActor(EventMsgHandler.myName);
		if( a == null ) new EventMsgHandler();
		IApplMessage m =
				CommUtils.buildDispatch(observer, registerForEvent, evId, EventMsgHandler.myName);
		ColorsOut.out( "Qak22Context | registerAsEventObserver m="+ m, ColorsOut.MAGENTA );
		Qak22Util.sendAMsg( m, EventMsgHandler.myName );  //Redirection to store
	}
	
	public static void unregisterAsEventObserver(String observer, String evId) {
		IApplMessage m =
				CommUtils.buildDispatch(observer, unregisterForEvent, evId, EventMsgHandler.myName);
		Qak22Util.sendAMsg( m, EventMsgHandler.myName );  //Redirection to store
	}
	

    
}
