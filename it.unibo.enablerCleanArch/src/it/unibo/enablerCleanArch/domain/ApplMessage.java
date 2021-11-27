package it.unibo.enablerCleanArch.domain;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;

enum ApplMessageType{
    event, dispatch, request, reply, invitation
}

public class ApplMessage {
    protected String msgId       = "";
    protected String msgType     = null;
    protected String msgSender   = "";
    protected String msgReceiver = "";
    protected String msgContent  = "";
    protected int msgNum         = 0;
	
	public ApplMessage( 
			String MSGID, String MSGTYPE, String SENDER, String RECEIVER, String CONTENT, String SEQNUM ) {
        msgId 		= MSGID;
        msgType 	= MSGTYPE;
        msgSender 	= SENDER;
        msgReceiver = RECEIVER;
        msgContent 	= CONTENT;
        msgNum = Integer.parseInt(SEQNUM);		
	}
	
	
	
    public ApplMessage( String msg ) {
        //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
    	Struct msgStruct = (Struct) Term.createTerm(msg);
        setFields(msgStruct);
    }
	
    private void setFields( Struct msgStruct ) {
        msgId 		= msgStruct.getArg(0).toString();
        msgType 	= msgStruct.getArg(1).toString();
        msgSender 	= msgStruct.getArg(2).toString();
        msgReceiver = msgStruct.getArg(3).toString();
        msgContent 	= msgStruct.getArg(4).toString();
        msgNum 		= Integer.parseInt(msgStruct.getArg(5).toString());
    }

    public String msgId() {   return msgId; }
    public String msgType() { return msgType; }
    public String msgSender() { return msgSender; }
    public String msgReceiver() { return msgReceiver;  }
    public String msgContent() { return msgContent;  }
    public String msgNum() { return "" + msgNum; }
    
    
    public boolean isEvent(){
        return msgType == ApplMessageType.event.toString();
    }
    public boolean isDispatch(){
        return msgType == ApplMessageType.dispatch.toString();
    }
    public boolean isRequest(){
        return msgType == ApplMessageType.request.toString();
    }
    public boolean isInvitation(){
        return msgType == ApplMessageType.invitation.toString();
    }
    public boolean isReply(){
        return msgType == ApplMessageType.reply.toString();
    }   

}
