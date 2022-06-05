package it.unibo.generator;

import it.unibo.qactork.Context;
import it.unibo.qactork.QActor;
import it.unibo.qactork.QActorCoded;
import it.unibo.qactork.QActorDeclaration;
import it.unibo.qactork.QActorExternal;
import it.unibo.qactork.QActorSystemSpec;
import it.unibo.qactork.generator.common.SysKb;
import java.util.Arrays;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenSystemInfo {
  public void doGenerate(final QActorSystemSpec system, final boolean tracing, final boolean msglogging, final SysKb kb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object");
  }
  
  /**
   * SYSTEM DESCRIPTION FILE CONTENT
   */
  public CharSequence genCtx(final QActorSystemSpec system, final boolean tracing, final boolean msglog) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved."
      + "\nThe method or field context is undefined for the type QActorSystemSpec"
      + "\nThe method or field actor is undefined for the type QActorSystemSpec");
  }
  
  public CharSequence genContext(final Context ctx) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isMqtt = ctx.isMqtt();
      if (_isMqtt) {
        _builder.append("context(");
        String _lowerCase = ctx.getName().toLowerCase();
        _builder.append(_lowerCase);
        _builder.append(", \"");
        String _host = ctx.getIp().getHost();
        _builder.append(_host);
        _builder.append("\",  \"MQTT\", \"");
        int _port = ctx.getIp().getPort();
        _builder.append(_port);
        _builder.append("\").");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("context(");
        String _lowerCase_1 = ctx.getName().toLowerCase();
        _builder.append(_lowerCase_1);
        _builder.append(", \"");
        String _host_1 = ctx.getIp().getHost();
        _builder.append(_host_1);
        _builder.append("\",  \"TCP\", \"");
        int _port_1 = ctx.getIp().getPort();
        _builder.append(_port_1);
        _builder.append("\").");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genQactorDescr(final QActorDeclaration qa) {
    return null;
  }
  
  protected CharSequence _genQactorDescr(final QActorExternal qa) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("qactor( ");
    String _lowerCase = qa.getName().toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(", ");
    String _lowerCase_1 = qa.getContext().getName().toLowerCase();
    _builder.append(_lowerCase_1);
    _builder.append(", \"external\").");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genQactorDescr(final QActor qa) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field toFirstUpper is undefined for the type String");
  }
  
  protected CharSequence _genQactorDescr(final QActorCoded qa) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("qactor( ");
    String _lowerCase = qa.getName().toLowerCase();
    _builder.append(_lowerCase);
    _builder.append(", ");
    String _lowerCase_1 = qa.getContext().getName().toLowerCase();
    _builder.append(_lowerCase_1);
    _builder.append(", \"");
    String _className = qa.getClassName();
    _builder.append(_className);
    _builder.append("\").");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * SYSTEM RULES FILE CONTENT
   */
  public CharSequence genCtxRules() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("%==============================================");
    _builder.newLine();
    _builder.append("% CONTEXT HANDLING UTILTY");
    _builder.newLine();
    _builder.append("%==============================================");
    _builder.newLine();
    _builder.append("getCtxNames(CTXNAMES) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall( NAME, context( NAME, _, _, _ ), CTXNAMES).");
    _builder.newLine();
    _builder.append("getCtxPortNames(PORTNAMES) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall( PORT, context( _, _, _, PORT ), PORTNAMES).");
    _builder.newLine();
    _builder.append("getTheContexts(CTXS) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall( context( CTX, HOST, PROTOCOL, PORT ), context( CTX, HOST, PROTOCOL, PORT ), CTXS).");
    _builder.newLine();
    _builder.append("getTheActors(ACTORS) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall( qactor( A, CTX, CLASS ), qactor( A, CTX, CLASS ), ACTORS).");
    _builder.newLine();
    _builder.newLine();
    _builder.newLine();
    _builder.append("getOtherContexts(OTHERCTXS, MYSELF) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall( ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("context( CTX, HOST, PROTOCOL, PORT ), ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("(context( CTX, HOST, PROTOCOL, PORT ), CTX \\== MYSELF), \t ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("OTHERCTXS");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(").");
    _builder.newLine();
    _builder.append("getOtherContextNames(OTHERCTXS, MYSELF) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall(");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("CTX,");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("(context( CTX, HOST, PROTOCOL, PORT ), CTX \\== MYSELF),");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("OTHERCTXS");
    _builder.newLine();
    _builder.append("\t");
    _builder.append(").");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("getTheActors(ACTORS,CTX) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("findall( qactor( A, CTX, CLASS ), qactor( A, CTX, CLASS ),   ACTORS).");
    _builder.newLine();
    _builder.append("getActorNames(ACTORS,CTX) :-");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("findall( NAME, qactor( NAME, CTX, CLASS ),   ACTORS).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("getCtxHost( NAME, HOST )  :- context( NAME, HOST, PROTOCOL, PORT ).");
    _builder.newLine();
    _builder.append("getCtxPort( NAME,  PORT ) :- context( NAME, HOST, PROTOCOL, PORT ).");
    _builder.newLine();
    _builder.append("getCtxProtocol( NAME,  PROTOCOL ) :- context( NAME, HOST, PROTOCOL, PORT ).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("getMsgId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , MSGID  ).");
    _builder.newLine();
    _builder.append("getMsgType( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , MSGTYPE ).");
    _builder.newLine();
    _builder.append("getMsgSenderId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , SENDER ).");
    _builder.newLine();
    _builder.append("getMsgReceiverId( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , RECEIVER ).");
    _builder.newLine();
    _builder.append("getMsgContent( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , CONTENT ).");
    _builder.newLine();
    _builder.append("getMsgNum( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) , SEQNUM ).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("checkMsg( MSG, MSGLIST, PLANLIST, RES ) :- ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%%stdout <- println( checkMsg( MSG, MSGLIST, PLANLIST, RES ) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("checkTheMsg(MSG, MSGLIST, PLANLIST, RES).\t");
    _builder.newLine();
    _builder.append("checkTheMsg( MSG, [], _, dummyPlan ).");
    _builder.newLine();
    _builder.append("checkTheMsg( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ), [ MSGID | _ ], [ PLAN | _ ], PLAN ):-!.");
    _builder.newLine();
    _builder.append("checkTheMsg( MSG, [_|R], [_|P], RES ):- ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%%stdout <- println( checkMsg( MSG, R, P, RES ) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("checkTheMsg(MSG, R, P, RES).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("msgContentToPlan( MSG, CONTENTLIST,PLANLIST,RES ):-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%stdout <- println( msgContentToPlan( MSG, CONTENTLIST,PLANLIST,RES) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("msgContentToAPlan( MSG, CONTENTLIST,PLANLIST,RES ).");
    _builder.newLine();
    _builder.append("msgContentToAPlan( MSG, [], _, dummyPlan ).");
    _builder.newLine();
    _builder.append("msgContentToAPlan( msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ), [ CONTENT | _ ], [ PLAN | _ ], PLAN ):-!.");
    _builder.newLine();
    _builder.append("msgContentToAPlan( event(  CONTENT  ), [ CONTENT | _ ], [ PLAN | _ ], PLAN ):-!.");
    _builder.newLine();
    _builder.append("msgContentToAPlan( MSG, [_|R], [_|P], RES ):- ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%stdout <- println( msgContentToAPlan( MSG, R, P, RES ) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("msgContentToPlan(MSG, R, P, RES).\t");
    _builder.newLine();
    _builder.newLine();
    _builder.append("removeCtx( CtxName, HOST, PORT ) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%% stdout <- println( removeCtx(  CtxName ) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("retract( context( CtxName, HOST, _ , PORT ) ),!,");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("removeAllActors( CtxName ).");
    _builder.newLine();
    _builder.append("\t ");
    _builder.newLine();
    _builder.append("removeAllActors( CtxName ) :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("retract( qactor( NAME, CtxName, _ ) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("removeAllActors( CtxName ).");
    _builder.newLine();
    _builder.append("removeAllActors( CtxName ).  ");
    _builder.newLine();
    _builder.newLine();
    _builder.append("showSystemConfiguration :-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stdout <- println(\"&&&&&&&&&&&&&&&&&&SysRules&&&&&&&&&&&&&&&&&&&&\"),");
    _builder.newLine();
    _builder.append("  \t");
    _builder.append("getTheContexts(CTXS),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stdout <- println(\'CONTEXTS IN THE SYSTEM:\'),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("showElements(CTXS),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stdout <- println(\'ACTORS   IN THE SYSTEM:\'),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("getTheActors(ACTORS),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("showElements(ACTORS),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stdout <- println(\"&&&&&&&&&&&&&&&&&&SysRules&&&&&&&&&&&&&&&&&&&&\").");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("showElements(ElementListString):- ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("text_term( ElementListString, ElementList ),");
    _builder.newLine();
    _builder.append(" \t");
    _builder.append("showListOfElements(ElementList).");
    _builder.newLine();
    _builder.append("showListOfElements([]).");
    _builder.newLine();
    _builder.append("showListOfElements([C|R]):-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("stdout <- println( C ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("showElements(R).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("unify(A,B)    :-  A = B.");
    _builder.newLine();
    _builder.newLine();
    _builder.append("assign( I,V ) :-  retract( value(I,_) ),!, assert( value( I,V )).");
    _builder.newLine();
    _builder.append("assign( I,V ) :-  assert( value( I,V )).");
    _builder.newLine();
    _builder.append("getVal( I, V ):-  value(I,V), !.");
    _builder.newLine();
    _builder.append("getVal( I, fail ).");
    _builder.newLine();
    _builder.append("inc(I,K,N):- value( I,V ), N is V + K, assign( I,N ).");
    _builder.newLine();
    _builder.append("dec(I,K,N):- value( I,V ), N is V - K, assign( I,N ).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("addRule( Rule ):-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%%output( addRule( Rule ) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("assert( Rule ).");
    _builder.newLine();
    _builder.append("removeRule( Rule ):-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("retract( Rule ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%%output( removedFact(Rule) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("!.");
    _builder.newLine();
    _builder.append("removeRule( A  ):- ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("%%output( remove(A) ),");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("retract( A :- B ),!.");
    _builder.newLine();
    _builder.append("removeRule( _  ).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("replaceRule( Rule, NewRule ):-");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("removeRule( Rule ),addRule( NewRule ).");
    _builder.newLine();
    _builder.newLine();
    _builder.append("%==============================================");
    _builder.newLine();
    _builder.append("% MEMENTO");
    _builder.newLine();
    _builder.append("%==============================================");
    _builder.newLine();
    _builder.append("%%% :- stdout <- println( hello ).");
    _builder.newLine();
    _builder.append("%%% --------------------------------------------------");
    _builder.newLine();
    _builder.append("% context( NAME, HOST, PROTOCOL, PORT )");
    _builder.newLine();
    _builder.append("% PROTOCOL : \"TCP\" | \"UDP\" | \"SERIAL\" | \"PROCESS\" | ...");
    _builder.newLine();
    _builder.append("%%% --------------------------------------------------");
    _builder.newLine();
    _builder.newLine();
    _builder.append("%%% --------------------------------------------------");
    _builder.newLine();
    _builder.append("% msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )");
    _builder.newLine();
    _builder.append("% MSGTYPE : dispatch request answer");
    _builder.newLine();
    _builder.append("%%% --------------------------------------------------");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence genQactorDescr(final QActorDeclaration qa) {
    if (qa instanceof QActor) {
      return _genQactorDescr((QActor)qa);
    } else if (qa instanceof QActorCoded) {
      return _genQactorDescr((QActorCoded)qa);
    } else if (qa instanceof QActorExternal) {
      return _genQactorDescr((QActorExternal)qa);
    } else if (qa != null) {
      return _genQactorDescr(qa);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(qa).toString());
    }
  }
}
