package it.unibo.generator;

import it.unibo.qactork.Answer;
import it.unibo.qactork.AnyAction;
import it.unibo.qactork.CodeRun;
import it.unibo.qactork.CodeRunActor;
import it.unibo.qactork.CodeRunSimple;
import it.unibo.qactork.Delay;
import it.unibo.qactork.DelayInt;
import it.unibo.qactork.DelaySol;
import it.unibo.qactork.DelayVar;
import it.unibo.qactork.DelayVref;
import it.unibo.qactork.Demand;
import it.unibo.qactork.DiscardMsg;
import it.unibo.qactork.Duration;
import it.unibo.qactork.Emit;
import it.unibo.qactork.EmptyTransition;
import it.unibo.qactork.EndActor;
import it.unibo.qactork.EventTransSwitch;
import it.unibo.qactork.Exec;
import it.unibo.qactork.Forward;
import it.unibo.qactork.GuardedStateAction;
import it.unibo.qactork.IfSolvedAction;
import it.unibo.qactork.InputTransition;
import it.unibo.qactork.MemoTime;
import it.unibo.qactork.MsgCond;
import it.unibo.qactork.MsgTransSwitch;
import it.unibo.qactork.NonEmptyTransition;
import it.unibo.qactork.PAtomNum;
import it.unibo.qactork.PAtomString;
import it.unibo.qactork.PAtomic;
import it.unibo.qactork.PHead;
import it.unibo.qactork.PStruct;
import it.unibo.qactork.PStructRef;
import it.unibo.qactork.Print;
import it.unibo.qactork.PrintCurMsg;
import it.unibo.qactork.QActor;
import it.unibo.qactork.QActorCoded;
import it.unibo.qactork.QActorDeclaration;
import it.unibo.qactork.QActorExternal;
import it.unibo.qactork.ReplyReq;
import it.unibo.qactork.ReplyTransSwitch;
import it.unibo.qactork.RequestTransSwitch;
import it.unibo.qactork.SolveGoal;
import it.unibo.qactork.State;
import it.unibo.qactork.StateAction;
import it.unibo.qactork.Timeout;
import it.unibo.qactork.TimeoutInt;
import it.unibo.qactork.TimeoutSol;
import it.unibo.qactork.TimeoutVar;
import it.unibo.qactork.Transition;
import it.unibo.qactork.UpdateResource;
import it.unibo.qactork.VarRef;
import it.unibo.qactork.VarRefInStr;
import it.unibo.qactork.VarSolRef;
import it.unibo.qactork.Variable;
import it.unibo.qactork.generator.common.SysKb;
import java.util.Arrays;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenQActor {
  private int count = 0;
  
  protected void _doGenerate(final QActorDeclaration actor, final SysKb kb) {
  }
  
  protected void _doGenerate(final QActorExternal actor, final SysKb kb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method println(Object) is undefined"
      + "\n+ cannot be resolved.");
  }
  
  protected void _doGenerate(final QActorCoded actor, final SysKb kb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method println(Object) is undefined"
      + "\n+ cannot be resolved.");
  }
  
  protected void _doGenerate(final QActor actor, final SysKb kb) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method println(Object) is undefined"
      + "\n+ cannot be resolved."
      + "\nThe method or field toFirstUpper is undefined for the type String"
      + "\nThe method genFileDir(String, String, String, String, CharSequence) from the type GenUtils refers to the missing type Object");
  }
  
  public CharSequence genQActor(final QActor actor, final String actorClassName, final String extensions) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved.");
  }
  
  public String genInitialStateName(final QActor actor) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field states is undefined for the type QActor"
      + "\nforEach cannot be resolved"
      + "\nnormal cannot be resolved"
      + "\nname cannot be resolved");
  }
  
  public String genStates(final QActor actor) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field states is undefined for the type QActor"
      + "\nforEach cannot be resolved");
  }
  
  public CharSequence genState(final QActor actor, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved.");
  }
  
  public String genActions(final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field actions is undefined for the type State");
  }
  
  public CharSequence genTimer(final QActor actor, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved.");
  }
  
  protected CharSequence _genTheTimer(final QActor actor, final String stateName, final Transition tr) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _genTheTimer(final QActor actor, final String stateName, final NonEmptyTransition tr) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved.");
  }
  
  protected CharSequence _genTimeToWait(final Timeout tout) {
    return null;
  }
  
  protected CharSequence _genTimeToWait(final TimeoutInt tout) {
    StringConcatenation _builder = new StringConcatenation();
    int _msec = tout.getMsec();
    _builder.append(_msec);
    _builder.append(".toLong()");
    return _builder;
  }
  
  protected CharSequence _genTimeToWait(final TimeoutVar tout) {
    CharSequence d = this.genPHead(tout.getVariable());
    return d;
  }
  
  protected CharSequence _genTimeToWait(final TimeoutSol tout) {
    CharSequence d = this.genPHead(tout.getRefsoltime());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(d);
    _builder.append(".toLong()");
    return _builder.toString();
  }
  
  /**
   * ACTIONS
   */
  protected CharSequence _genAction(final IfSolvedAction a) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field solvedactions is undefined for the type IfSolvedAction"
      + "\nThe method or field notsolvedactions is undefined for the type IfSolvedAction"
      + "\nThe method or field notsolvedactions is undefined for the type IfSolvedAction"
      + "\nThe method genActionSequence(EList) from the type GenQActor refers to the missing type EList"
      + "\nThe method genActionSequence(EList) from the type GenQActor refers to the missing type EList"
      + "\n!== cannot be resolved");
  }
  
  protected CharSequence _genAction(final GuardedStateAction a) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field okactions is undefined for the type GuardedStateAction"
      + "\nThe method or field koactions is undefined for the type GuardedStateAction"
      + "\nThe method or field koactions is undefined for the type GuardedStateAction"
      + "\nThe method genActionSequence(EList) from the type GenQActor refers to the missing type EList"
      + "\nThe method genActionSequence(EList) from the type GenQActor refers to the missing type EList"
      + "\nlength cannot be resolved"
      + "\n> cannot be resolved");
  }
  
  public String genActionSequence(final /* EList<StateAction> */Object a) {
    final StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (final EList action : a) {
      sb.append(this.genAction(action));
    }
    sb.append("}\n");
    return sb.toString();
  }
  
  protected CharSequence _genAction(final StateAction a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here StateAction ");
    _builder.append(a);
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final AnyAction a) {
    StringConcatenation _builder = new StringConcatenation();
    String _replace = a.getBody().toString().replace("#", "");
    _builder.append(_replace);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Print a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("println(");
    CharSequence _genPHead = this.genPHead(a.getArgs());
    _builder.append(_genPHead);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Forward a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("forward(\"");
    String _name = a.getMsgref().getName();
    _builder.append(_name);
    _builder.append("\", \"");
    CharSequence _genPHead = this.genPHead(a.getVal());
    _builder.append(_genPHead);
    _builder.append("\" ,\"");
    String _name_1 = a.getDest().getName();
    _builder.append(_name_1);
    _builder.append("\" ) ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Emit a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("emit(\"");
    String _name = a.getMsgref().getName();
    _builder.append(_name);
    _builder.append("\", \"");
    CharSequence _genPHead = this.genPHead(a.getVal());
    _builder.append(_genPHead);
    _builder.append("\" ) ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Demand a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("request(\"");
    String _name = a.getMsgref().getName();
    _builder.append(_name);
    _builder.append("\", \"");
    CharSequence _genPHead = this.genPHead(a.getVal());
    _builder.append(_genPHead);
    _builder.append("\" ,\"");
    String _name_1 = a.getDest().getName();
    _builder.append(_name_1);
    _builder.append("\" )  ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Answer a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("answer(\"");
    String _name = a.getReqref().getName();
    _builder.append(_name);
    _builder.append("\", \"");
    String _name_1 = a.getMsgref().getName();
    _builder.append(_name_1);
    _builder.append("\", \"");
    CharSequence _genPHead = this.genPHead(a.getVal());
    _builder.append(_genPHead);
    _builder.append("\"   )  ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final ReplyReq a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("replyreq(\"");
    String _name = a.getReqref().getName();
    _builder.append(_name);
    _builder.append("\", \"");
    String _name_1 = a.getMsgref().getName();
    _builder.append(_name_1);
    _builder.append("\", \"");
    CharSequence _genPHead = this.genPHead(a.getVal());
    _builder.append(_genPHead);
    _builder.append("\"   )  ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Delay a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here dlay");
    return _builder;
  }
  
  protected CharSequence _genAction(final DelayInt a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("delay(");
    int _time = a.getTime();
    _builder.append(_time);
    _builder.append(") ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final DelayVar a) {
    CharSequence d = this.genPHead(a.getRefvar());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("delay(");
    _builder.append(d);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected CharSequence _genAction(final DelayVref a) {
    CharSequence d = this.genPHead(a.getReftime());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("delay(Integer.parseInt(");
    _builder.append(d);
    _builder.append(").toLong())");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected CharSequence _genAction(final DelaySol a) {
    CharSequence d = this.genPHead(a.getRefsoltime());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("delay(Integer.parseInt(");
    _builder.append(d);
    _builder.append(").toLong())");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected CharSequence _genAction(final SolveGoal m) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved.");
  }
  
  protected CharSequence _genAction(final MsgCond m) {
    throw new Error("Unresolved compilation problems:"
      + "\n+ cannot be resolved."
      + "\n+ cannot be resolved."
      + "\nThe method or field condactions is undefined for the type MsgCond"
      + "\n!== cannot be resolved."
      + "\nThe method or field notcondactions is undefined for the type NoMsgCond"
      + "\nThe method genTestMsgActions(EList) from the type GenQActor refers to the missing type EList"
      + "\nThe method genTestMsgActions(EList) from the type GenQActor refers to the missing type EList");
  }
  
  protected CharSequence _genAction(final EndActor m) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("terminate(");
    int _arg = m.getArg();
    _builder.append(_arg);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public String genTestMsgActions(final /* EList<StateAction> */Object a) {
    final StringBuilder sb = new StringBuilder();
    for (final EList action : a) {
      sb.append(this.genAction(action));
    }
    return sb.toString();
  }
  
  protected CharSequence _genAction(final DiscardMsg a) {
    CharSequence _xifexpression = null;
    boolean _isDiscard = a.isDiscard();
    if (_isDiscard) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("discardMessages = true");
      _builder.newLine();
      _xifexpression = _builder;
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("discardMessages = false");
      _builder_1.newLine();
      _xifexpression = _builder_1;
    }
    return _xifexpression;
  }
  
  protected CharSequence _genAction(final UpdateResource a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("updateResourceRep(");
    String _string = this.genAction(a.getVal()).toString();
    _builder.append(_string);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final MemoTime a) {
    StringConcatenation _builder = new StringConcatenation();
    String _store = a.getStore();
    _builder.append(_store);
    _builder.append(" = getCurrentTime()");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final Duration a) {
    StringConcatenation _builder = new StringConcatenation();
    String _store = a.getStore();
    _builder.append(_store);
    _builder.append(" = getDuration(");
    String _start = a.getStart();
    _builder.append(_start);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected CharSequence _genAction(final PrintCurMsg a) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("println(\"$name in ${currentState.stateName} | $currentMsg\")");
    _builder.newLine();
    return _builder;
  }
  
  protected CharSequence _genAction(final CodeRun move) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here \'CodeRun");
    return _builder;
  }
  
  protected CharSequence _genAction(final CodeRunSimple move) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field args is undefined for the type CodeRunSimple");
  }
  
  protected CharSequence _genAction(final CodeRunActor move) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field args is undefined for the type CodeRunActor"
      + "\nThe method or field args is undefined for the type CodeRunActor"
      + "\nlength cannot be resolved"
      + "\n> cannot be resolved");
  }
  
  protected CharSequence _genAction(final Exec move) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("currentProcess=machineExec(");
    String _action = move.getAction();
    _builder.append(_action);
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  /**
   * TRANSITIONS
   */
  protected CharSequence _genTransition(final QActor actor, final State curstate, final Transition tr) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here Transition");
    return _builder;
  }
  
  protected CharSequence _genTransition(final QActor actor, final State curstate, final EmptyTransition tr) {
    throw new Error("Unresolved compilation problems:"
      + "\n=== cannot be resolved.");
  }
  
  private String curActorNameForTransition = "";
  
  protected CharSequence _genTransition(final QActor actor, final State curstate, final NonEmptyTransition tr) {
    throw new Error("Unresolved compilation problems:"
      + "\n!== cannot be resolved."
      + "\nThe method or field trans is undefined for the type NonEmptyTransition"
      + "\n!== cannot be resolved."
      + "\ntargetState cannot be resolved");
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final InputTransition tr, final State state) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here InputTransition");
    return _builder;
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final EventTransSwitch tr, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n=== cannot be resolved."
      + "\n++ cannot be resolved."
      + "\n++ cannot be resolved.");
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final MsgTransSwitch tr, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n=== cannot be resolved."
      + "\n++ cannot be resolved."
      + "\n++ cannot be resolved.");
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final RequestTransSwitch tr, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n=== cannot be resolved."
      + "\n++ cannot be resolved."
      + "\n++ cannot be resolved.");
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final ReplyTransSwitch tr, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n=== cannot be resolved."
      + "\n++ cannot be resolved."
      + "\n++ cannot be resolved.");
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final Timeout tr, final State state) {
    throw new Error("Unresolved compilation problems:"
      + "\n++ cannot be resolved.");
  }
  
  /**
   * PHead
   */
  protected CharSequence _genPHead(final PHead ph) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here genPHead");
    return _builder;
  }
  
  protected CharSequence _genPHead(final PAtomString ph) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\"");
    String _val = ph.getVal();
    _builder.append(_val);
    _builder.append("\"");
    return _builder;
  }
  
  protected CharSequence _genPHead(final PAtomic ph) {
    StringConcatenation _builder = new StringConcatenation();
    String _val = ph.getVal();
    _builder.append(_val);
    return _builder;
  }
  
  protected CharSequence _genPHead(final Variable pt) {
    StringConcatenation _builder = new StringConcatenation();
    String _varName = pt.getVarName();
    _builder.append(_varName);
    return _builder;
  }
  
  protected CharSequence _genPHead(final VarRef pt) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("$");
    String _varName = pt.getVarName();
    _builder.append(_varName);
    return _builder;
  }
  
  protected CharSequence _genPHead(final VarRefInStr pt) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("${getCurSol(\"");
    String _varName = pt.getVarName();
    _builder.append(_varName);
    _builder.append("\").toString()}");
    return _builder;
  }
  
  protected CharSequence _genPHead(final VarSolRef pt) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("getCurSol(\"");
    String _varName = pt.getVarName();
    _builder.append(_varName);
    _builder.append("\").toString()");
    return _builder;
  }
  
  protected CharSequence _genPHead(final PStructRef ps) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field msgArg is undefined for the type PStruct");
  }
  
  protected CharSequence _genPHead(final PStruct ps) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field msgArg is undefined for the type PStruct");
  }
  
  protected CharSequence _genPHead(final PAtomNum ph) {
    StringConcatenation _builder = new StringConcatenation();
    int _val = ph.getVal();
    _builder.append(_val);
    return _builder;
  }
  
  public void doGenerate(final QActorDeclaration actor, final SysKb kb) {
    if (actor instanceof QActor) {
      _doGenerate((QActor)actor, kb);
      return;
    } else if (actor instanceof QActorCoded) {
      _doGenerate((QActorCoded)actor, kb);
      return;
    } else if (actor instanceof QActorExternal) {
      _doGenerate((QActorExternal)actor, kb);
      return;
    } else if (actor != null) {
      _doGenerate(actor, kb);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(actor, kb).toString());
    }
  }
  
  public CharSequence genTheTimer(final QActor actor, final String stateName, final Transition tr) {
    if (tr instanceof NonEmptyTransition) {
      return _genTheTimer(actor, stateName, (NonEmptyTransition)tr);
    } else if (tr != null) {
      return _genTheTimer(actor, stateName, tr);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(actor, stateName, tr).toString());
    }
  }
  
  public CharSequence genTimeToWait(final Timeout tout) {
    if (tout instanceof TimeoutInt) {
      return _genTimeToWait((TimeoutInt)tout);
    } else if (tout instanceof TimeoutSol) {
      return _genTimeToWait((TimeoutSol)tout);
    } else if (tout instanceof TimeoutVar) {
      return _genTimeToWait((TimeoutVar)tout);
    } else if (tout != null) {
      return _genTimeToWait(tout);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(tout).toString());
    }
  }
  
  public CharSequence genAction(final StateAction move) {
    if (move instanceof CodeRunActor) {
      return _genAction((CodeRunActor)move);
    } else if (move instanceof CodeRunSimple) {
      return _genAction((CodeRunSimple)move);
    } else if (move instanceof DelayInt) {
      return _genAction((DelayInt)move);
    } else if (move instanceof DelaySol) {
      return _genAction((DelaySol)move);
    } else if (move instanceof DelayVar) {
      return _genAction((DelayVar)move);
    } else if (move instanceof DelayVref) {
      return _genAction((DelayVref)move);
    } else if (move instanceof Answer) {
      return _genAction((Answer)move);
    } else if (move instanceof AnyAction) {
      return _genAction((AnyAction)move);
    } else if (move instanceof CodeRun) {
      return _genAction((CodeRun)move);
    } else if (move instanceof Delay) {
      return _genAction((Delay)move);
    } else if (move instanceof Demand) {
      return _genAction((Demand)move);
    } else if (move instanceof DiscardMsg) {
      return _genAction((DiscardMsg)move);
    } else if (move instanceof Duration) {
      return _genAction((Duration)move);
    } else if (move instanceof Emit) {
      return _genAction((Emit)move);
    } else if (move instanceof EndActor) {
      return _genAction((EndActor)move);
    } else if (move instanceof Exec) {
      return _genAction((Exec)move);
    } else if (move instanceof Forward) {
      return _genAction((Forward)move);
    } else if (move instanceof GuardedStateAction) {
      return _genAction((GuardedStateAction)move);
    } else if (move instanceof IfSolvedAction) {
      return _genAction((IfSolvedAction)move);
    } else if (move instanceof MemoTime) {
      return _genAction((MemoTime)move);
    } else if (move instanceof MsgCond) {
      return _genAction((MsgCond)move);
    } else if (move instanceof Print) {
      return _genAction((Print)move);
    } else if (move instanceof PrintCurMsg) {
      return _genAction((PrintCurMsg)move);
    } else if (move instanceof ReplyReq) {
      return _genAction((ReplyReq)move);
    } else if (move instanceof SolveGoal) {
      return _genAction((SolveGoal)move);
    } else if (move instanceof UpdateResource) {
      return _genAction((UpdateResource)move);
    } else if (move != null) {
      return _genAction(move);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(move).toString());
    }
  }
  
  public CharSequence genTransition(final QActor actor, final State curstate, final Transition tr) {
    if (tr instanceof EmptyTransition) {
      return _genTransition(actor, curstate, (EmptyTransition)tr);
    } else if (tr instanceof NonEmptyTransition) {
      return _genTransition(actor, curstate, (NonEmptyTransition)tr);
    } else if (tr != null) {
      return _genTransition(actor, curstate, tr);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(actor, curstate, tr).toString());
    }
  }
  
  public CharSequence genTransition(final String tname, final State curstate, final Object tr, final State state) {
    if (tr instanceof EventTransSwitch) {
      return _genTransition(tname, curstate, (EventTransSwitch)tr, state);
    } else if (tr instanceof MsgTransSwitch) {
      return _genTransition(tname, curstate, (MsgTransSwitch)tr, state);
    } else if (tr instanceof ReplyTransSwitch) {
      return _genTransition(tname, curstate, (ReplyTransSwitch)tr, state);
    } else if (tr instanceof RequestTransSwitch) {
      return _genTransition(tname, curstate, (RequestTransSwitch)tr, state);
    } else if (tr instanceof InputTransition) {
      return _genTransition(tname, curstate, (InputTransition)tr, state);
    } else if (tr instanceof Timeout) {
      return _genTransition(tname, curstate, (Timeout)tr, state);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(tname, curstate, tr, state).toString());
    }
  }
  
  public CharSequence genPHead(final PHead ph) {
    if (ph instanceof PAtomNum) {
      return _genPHead((PAtomNum)ph);
    } else if (ph instanceof PAtomString) {
      return _genPHead((PAtomString)ph);
    } else if (ph instanceof PAtomic) {
      return _genPHead((PAtomic)ph);
    } else if (ph instanceof VarRef) {
      return _genPHead((VarRef)ph);
    } else if (ph instanceof VarRefInStr) {
      return _genPHead((VarRefInStr)ph);
    } else if (ph instanceof VarSolRef) {
      return _genPHead((VarSolRef)ph);
    } else if (ph instanceof Variable) {
      return _genPHead((Variable)ph);
    } else if (ph instanceof PStruct) {
      return _genPHead((PStruct)ph);
    } else if (ph instanceof PStructRef) {
      return _genPHead((PStructRef)ph);
    } else if (ph != null) {
      return _genPHead(ph);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ph).toString());
    }
  }
}
