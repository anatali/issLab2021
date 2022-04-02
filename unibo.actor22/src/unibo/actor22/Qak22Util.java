package unibo.actor22;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.events.EventMsgHandler;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.*;
import unibo.actor22.annotations.AnnotUtil;
import java.util.HashMap;

public  class Qak22Util   {
//    private static HashMap<String,ProxyAsClient> proxyMap = new HashMap<String,ProxyAsClient>();

//    public static final String registerForEvent   = "registerForEvent";
//    public static final String unregisterForEvent = "unregisterForEvent";
    
//	public static void registerAsEventObserver(String observer, String evId) {
//		IApplMessage m =
//				CommUtils.buildDispatch(observer, registerForEvent, evId, EventMsgHandler.myName);
//		sendAMsg( m, EventMsgHandler.myName );
//	}
//	
//	
//    public static void emit(IApplMessage msg) {
//    	if( msg.isEvent() ) {
//    		ColorsOut.outappl( "Actors22Util | emit=" + msg  , ColorsOut.GREEN);
//    		sendAMsg( msg, EventMsgHandler.myName);
//    	}   	
//    }

    
    //TODO: mettere in Actor22
//    protected static HashMap<String,String> eventObserverMap = new HashMap<String,String>();  
//	 
//	public static void handleEvent(IApplMessage msg) {
//		try {
//		ColorsOut.outappl( "Actors22Util handleEvent:" + msg, ColorsOut.MAGENTA);
//		if( msg.isDispatch() && msg.msgId().equals(Qak22Util.registerForEvent)) {
//			eventObserverMap.put(msg.msgSender(), msg.msgContent());
//		}else if( msg.isEvent()) {
//			eventObserverMap.forEach(
//					( actorName,  evName) -> {
//						System.out.println(actorName + " " + evName); 
//						if( evName.equals(msg.msgId()) ) {
//							Qak22Util.sendAMsg(msg, actorName );
//						}
//			} ) ;
//		}else {
//			ColorsOut.outerr( "Actors22Util handleEvent: msg unknown");
//		}
//		}catch( Exception e) {
//			ColorsOut.outerr( "Actors22Util handleEvent ERROR:" + e.getMessage());
//		}
//	}    
//---------------------------------------------------------------    
//    public static void handleLocalActorDecl(Object element) {
//    	AnnotUtil.createActorLocal(element);
//    }
//    public static void handleRemoteActorDecl(Object element) {
//     	AnnotUtil.createProxyForRemoteActors(element);
//    }

//    public static void handleActorDeclaration(Object element) {
//    	AnnotUtil.createActorLocal(element);
//    	AnnotUtil.createProxyForRemoteActors(element);
//    }
   
//    public static void setActorAsRemote(String actorName, String entry, String host, ProtocolType protocol ) {
//    	if( ! proxyMap.containsKey(actorName)   ) { //defensive
//    		ProxyAsClient pxy = new ProxyAsClient(actorName+"Pxy", host, entry, protocol);
//    		proxyMap.put(actorName, pxy);
//    	}   	
//    }
    


    public static void showActors22(){
    	Qak22Util.showActors22();
    }
    
 
    //Rimpiazzabile da sendAMsg(String msgId,  String msg, Actor22 destActor )
//    public static void sendAMsg(String msgId,  String msg, Actor22 destActor ){
//        //null for kotlin.coroutines.Continuation<? super Unit> $completion
////        if( destActor instanceof Actor22 ){
////            destActor.forward(msgId,    msg,   destActor.getName(), destActor).getmycompletion());
////        }
////        else destActor.forward(msgId,    msg,   destActor.getName(),null);
//    }
 
 
//    //Rimpiazzabile da sendAMsg(msg, destActor ) in Actor22
//    public static void sendAMsg(IApplMessage msg, Actor22 destActor ){
////       destActor.autoMsg( destActor,msg ).getmycompletion());
//       //System.out.println("Actor22 | sendMsg " + msg + " dest=" + destActor.getName());
// 
//    }
    
    //autoMsg( IApplMessage msg ) in Actor22
//    protected static void autoMsg(Actor22 a, IApplMessage msg) {
//		try {
//			a.autoMsg(msg,  a.getmycompletion() );
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	
//    }
    
    
    
   
    
    //Usabile da Java: Distingue tra locale e remoto
    public static void sendAMsg( IApplMessage msg ){
    	sendAMsg( msg, msg.msgReceiver() );
    }
   
    protected static void sendAMsg(IApplMessage msg, String destActorName){
		//ColorsOut.out("Qak22Util | sendAMsg " + msg  , ColorsOut.GREEN);	  
        QakActor22 dest = Qak22Context.getActor(destActorName);  
        if( dest != null ) { //attore locale
    		ColorsOut.out("Qak22Util | sendAMsg " + msg + " to:" + dest.getName() , ColorsOut.GREEN);
    		dest.queueMsg(msg);
        }else{ //invio di un msg ad un attore non locale : cerco in proxyMap
        	ColorsOut.outerr("Qak22Util | send to a non local actor not yet implmented " + msg  );
//			ProxyAsClient pxy = proxyMap.get(destActorName);
//    		ColorsOut.out("Qak22Util | sendAMsg " + msg + " using:" + pxy , ColorsOut.GREEN);
//			if( pxy != null ) {
//				if( msg.isRequest() ) {
//					String answerMsg  = pxy.sendRequestOnConnection( msg.toString()) ;
//					IApplMessage reply= new ApplMessage( answerMsg );
//					ColorsOut.out("Qak22Util | answer=" + reply.msgContent() , ColorsOut.GREEN);
//					QakActor22 sender = Qak22Context.getActor(msg.msgSender());
//					sender.elabMsg(reply); //WARNING: the sender must handle the reply as msg
//				}else {
//					pxy.sendCommandOnConnection(msg.toString());
//				}
//			}
        }
	}

    //sendAnswer(IApplMessage msg, IApplMessage reply) in Actor22
//    public static void sendReply(IApplMessage msg, IApplMessage reply) {
//        //System.out.println(   "Actor22 sendReply | reply= " + reply );
//        Actor22 dest = Actor22.getActor(msg.msgSender());
//        if(dest != null)  sendAMsg(reply, dest );
//        else {
//        	Actor22 ar = Actor22.getActor("ar"+msg.msgSender());
//            if(ar !=null) ar.sendAMsg(reply );
//            else {
//                System.out.println("Actor22 sendReply | Reply IMPOSSIBLE");
//             }
//        }
//    }
    
 }
