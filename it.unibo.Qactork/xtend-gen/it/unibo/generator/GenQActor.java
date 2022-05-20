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
import it.unibo.qactork.NoMsgCond;
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
import it.unibo.qactork.generator.common.GenUtils;
import it.unibo.qactork.generator.common.SysKb;
import java.util.Arrays;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class GenQActor {
  private int count = 0;
  
  protected void _doGenerate(final QActorDeclaration actor, final SysKb kb) {
  }
  
  protected void _doGenerate(final QActorExternal actor, final SysKb kb) {
    String _name = actor.getName();
    String _plus = (" *** GenQActor external: " + _name);
    InputOutput.<String>println(_plus);
  }
  
  protected void _doGenerate(final QActorCoded actor, final SysKb kb) {
    String _name = actor.getName();
    String _plus = (" *** GenQActor already coded: " + _name);
    InputOutput.<String>println(_plus);
  }
  
  protected void _doGenerate(final QActor actor, final SysKb kb) {
    String _name = actor.getName();
    String _plus = (" *** GenQActor starts for regular actor " + _name);
    InputOutput.<String>println(_plus);
    final String actorClassName = StringExtensions.toFirstUpper(actor.getName());
    GenUtils.genFileDir("../src/", GenUtils.packageName, actorClassName, "kt", this.genQActor(actor, actorClassName, ""));
  }
  
  public CharSequence genQActor(final QActor actor, final String actorClassName, final String extensions) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(GenUtils.logo);
    _builder.newLineIfNotEmpty();
    _builder.append("package ");
    _builder.append(GenUtils.packageName);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import it.unibo.kactor.*");
    _builder.newLine();
    _builder.append("import alice.tuprolog.*");
    _builder.newLine();
    _builder.append("import kotlinx.coroutines.CoroutineScope");
    _builder.newLine();
    _builder.append("import kotlinx.coroutines.delay");
    _builder.newLine();
    _builder.append("import kotlinx.coroutines.launch");
    _builder.newLine();
    _builder.append("import kotlinx.coroutines.runBlocking");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("class ");
    _builder.append(actorClassName);
    _builder.append(" ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("override fun getInitialState() : String{");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return \"");
    String _genInitialStateName = this.genInitialStateName(actor);
    _builder.append(_genInitialStateName, "\t\t");
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("override fun getBody() : (ActorBasicFsm.() -> Unit){");
    _builder.newLine();
    _builder.append("\t\t");
    {
      AnyAction _start = actor.getStart();
      boolean _tripleNotEquals = (_start != null);
      if (_tripleNotEquals) {
        CharSequence _genAction = this.genAction(actor.getStart());
        _builder.append(_genAction, "\t\t");
      }
    }
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("return { //this:ActionBasciFsm");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    String _genStates = this.genStates(actor);
    _builder.append(_genStates, "\t\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String genInitialStateName(final QActor actor) {
    final StringBuilder sb = new StringBuilder();
    final Consumer<State> _function = (State state) -> {
      boolean _isNormal = state.isNormal();
      if (_isNormal) {
        sb.append(state.getName());
      }
    };
    actor.getStates().forEach(_function);
    return sb.toString();
  }
  
  public String genStates(final QActor actor) {
    final StringBuilder sb = new StringBuilder();
    final Consumer<State> _function = (State state) -> {
      sb.append(this.genState(actor, state));
    };
    actor.getStates().forEach(_function);
    return sb.toString();
  }
  
  public CharSequence genState(final QActor actor, final State state) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("state(\"");
    String _name = state.getName();
    _builder.append(_name);
    _builder.append("\") { //this:State");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("action { //it:State");
    _builder.newLine();
    _builder.append("\t\t");
    String _genActions = this.genActions(state);
    _builder.append(_genActions, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    CharSequence _genTimer = this.genTimer(actor, state);
    _builder.append(_genTimer, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    {
      Transition _transition = state.getTransition();
      boolean _tripleNotEquals = (_transition != null);
      if (_tripleNotEquals) {
        _builder.append(" ");
        CharSequence _genTransition = this.genTransition(actor, state, state.getTransition());
        _builder.append(_genTransition, "\t");
      }
    }
    _builder.append("\t \t ");
    _builder.newLineIfNotEmpty();
    _builder.append("}\t ");
    _builder.newLine();
    return _builder;
  }
  
  public String genActions(final State state) {
    final StringBuilder sb = new StringBuilder();
    EList<StateAction> _actions = state.getActions();
    for (final StateAction action : _actions) {
      sb.append(this.genAction(action));
    }
    return sb.toString();
  }
  
  public CharSequence genTimer(final QActor actor, final State state) {
    CharSequence _xifexpression = null;
    Transition _transition = state.getTransition();
    boolean _tripleNotEquals = (_transition != null);
    if (_tripleNotEquals) {
      _xifexpression = this.genTheTimer(actor, state.getName(), state.getTransition());
    }
    return _xifexpression;
  }
  
  protected CharSequence _genTheTimer(final QActor actor, final String stateName, final Transition tr) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  protected CharSequence _genTheTimer(final QActor actor, final String stateName, final NonEmptyTransition tr) {
    Timeout _duration = tr.getDuration();
    boolean _tripleNotEquals = (_duration != null);
    if (_tripleNotEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("stateTimer = TimerActor(\"timer_");
      _builder.append(stateName);
      _builder.append("\", ");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("scope, context!!, \"local_tout_");
      String _name = actor.getName();
      _builder.append(_name, "\t");
      _builder.append("_");
      _builder.append(stateName, "\t");
      _builder.append("\", ");
      CharSequence _genTimeToWait = this.genTimeToWait(tr.getDuration());
      _builder.append(_genTimeToWait, "\t");
      _builder.append(" )");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    }
    return null;
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
    String actionStr = this.genActionSequence(a.getSolvedactions());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if( currentSolution.isSuccess() ) ");
    String guardStr = _builder.toString();
    String elseStr = "";
    EList<StateAction> _notsolvedactions = a.getNotsolvedactions();
    boolean _tripleNotEquals = (_notsolvedactions != null);
    if (_tripleNotEquals) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("else");
      _builder_1.newLine();
      String _genActionSequence = this.genActionSequence(a.getNotsolvedactions());
      _builder_1.append(_genActionSequence);
      _builder_1.newLineIfNotEmpty();
      elseStr = _builder_1.toString();
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append(guardStr);
    _builder_2.append(actionStr);
    _builder_2.append(elseStr);
    return _builder_2.toString();
  }
  
  protected CharSequence _genAction(final GuardedStateAction a) {
    String actionStr = this.genActionSequence(a.getOkactions());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if( ");
    Object _genAction = this.genAction(a.getGuard());
    _builder.append(_genAction);
    _builder.append(" )");
    String guardStr = _builder.toString();
    String elseStr = "";
    int _length = ((Object[])Conversions.unwrapArray(a.getKoactions(), Object.class)).length;
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("else");
      _builder_1.newLine();
      _builder_1.append(" ");
      String _genActionSequence = this.genActionSequence(a.getKoactions());
      _builder_1.append(_genActionSequence, " ");
      _builder_1.newLineIfNotEmpty();
      elseStr = _builder_1.toString();
    }
    StringConcatenation _builder_2 = new StringConcatenation();
    _builder_2.append(guardStr);
    _builder_2.append(actionStr);
    _builder_2.append(elseStr);
    return _builder_2.toString();
  }
  
  public String genActionSequence(final EList<StateAction> a) {
    final StringBuilder sb = new StringBuilder();
    sb.append("{");
    for (final StateAction action : a) {
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
    final String g = this.genPHead(m.getGoal()).toString().replace("\"", "\'");
    String r = "";
    Variable _resVar = m.getResVar();
    boolean _tripleNotEquals = (_resVar != null);
    if (_tripleNotEquals) {
      r = this.genPHead(m.getResVar()).toString();
    }
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("solve(\"");
    _builder.append(g);
    _builder.append("\",\"");
    _builder.append(r);
    _builder.append("\") //set resVar\t");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected CharSequence _genAction(final MsgCond m) {
    CharSequence _genPHead = this.genPHead(m.getMsg());
    final String msgUserTemplate = ("" + _genPHead);
    CharSequence _genPHead_1 = this.genPHead(m.getMessage().getMsg());
    final String msgTemplate = ("" + _genPHead_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("if( checkMsgContent( Term.createTerm(\"");
    _builder.append(msgTemplate);
    _builder.append("\"), Term.createTerm(\"");
    _builder.append(msgUserTemplate);
    _builder.append("\"), ");
    _builder.newLineIfNotEmpty();
    _builder.append("                        ");
    _builder.append("currentMsg.msgContent()) ) { //set msgArgList");
    _builder.newLine();
    _builder.append("\t\t");
    String _genTestMsgActions = this.genTestMsgActions(m.getCondactions());
    _builder.append(_genTestMsgActions, "\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    {
      NoMsgCond _ifnot = m.getIfnot();
      boolean _tripleNotEquals = (_ifnot != null);
      if (_tripleNotEquals) {
        _builder.append("else{");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _genTestMsgActions_1 = this.genTestMsgActions(m.getIfnot().getNotcondactions());
        _builder.append(_genTestMsgActions_1, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder.toString();
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
  
  public String genTestMsgActions(final EList<StateAction> a) {
    final StringBuilder sb = new StringBuilder();
    for (final StateAction action : a) {
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
    StringConcatenation _builder = new StringConcatenation();
    String _bitem = move.getBitem();
    _builder.append(_bitem);
    _builder.append("( ");
    {
      EList<PHead> _args = move.getArgs();
      boolean _hasElements = false;
      for(final PHead ms : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _genPHead = this.genPHead(ms);
        _builder.append(_genPHead);
        _builder.append(" ");
      }
    }
    _builder.append(" )");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  protected CharSequence _genAction(final CodeRunActor move) {
    String actorRef = "myself";
    int _length = ((Object[])Conversions.unwrapArray(move.getArgs(), Object.class)).length;
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      actorRef = "myself ,";
    }
    StringConcatenation _builder = new StringConcatenation();
    String _aitem = move.getAitem();
    _builder.append(_aitem);
    _builder.append("(");
    _builder.append(actorRef);
    {
      EList<PHead> _args = move.getArgs();
      boolean _hasElements = false;
      for(final PHead ms : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        CharSequence _genPHead = this.genPHead(ms);
        _builder.append(_genPHead);
        _builder.append(" ");
      }
    }
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    return _builder.toString();
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
    AnyAction _eguard = tr.getEguard();
    boolean _tripleEquals = (_eguard == null);
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("transition( edgeName=\"goto\",targetState=\"");
      String _name = tr.getTargetState().getName();
      _builder.append(_name);
      _builder.append("\", cond=doswitch() )");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("transition( edgeName=\"goto\",targetState=\"");
      String _name_1 = tr.getTargetState().getName();
      _builder_1.append(_name_1);
      _builder_1.append("\", cond=doswitchGuarded({");
      CharSequence _genAction = this.genAction(tr.getEguard());
      _builder_1.append(_genAction);
      _builder_1.append("}) )");
      _builder_1.newLineIfNotEmpty();
      _builder_1.append("transition( edgeName=\"goto\",targetState=\"");
      String _name_2 = tr.getOthertargetState().getName();
      _builder_1.append(_name_2);
      _builder_1.append("\", cond=doswitchGuarded({! (");
      CharSequence _genAction_1 = this.genAction(tr.getEguard());
      _builder_1.append(_genAction_1);
      _builder_1.append(") }) )");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
  }
  
  private String curActorNameForTransition = "";
  
  protected CharSequence _genTransition(final QActor actor, final State curstate, final NonEmptyTransition tr) {
    this.curActorNameForTransition = actor.getName();
    final StringBuilder sb = new StringBuilder();
    Timeout _duration = tr.getDuration();
    boolean _tripleNotEquals = (_duration != null);
    if (_tripleNotEquals) {
      sb.append(this.genTransition(tr.getName(), curstate, tr.getDuration(), tr.getDuration().getTargetState()));
    }
    EList<InputTransition> _trans = tr.getTrans();
    for (final InputTransition t : _trans) {
      sb.append(this.genTransition(tr.getName(), curstate, t, t.getTargetState()));
    }
    EmptyTransition _elseempty = tr.getElseempty();
    boolean _tripleNotEquals_1 = (_elseempty != null);
    if (_tripleNotEquals_1) {
      sb.append(this.genTransition(actor, curstate, tr.getElseempty()));
    }
    return sb.toString();
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final InputTransition tr, final State state) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("not here InputTransition");
    return _builder;
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final EventTransSwitch tr, final State state) {
    AnyAction _guard = tr.getGuard();
    boolean _tripleEquals = (_guard == null);
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("transition(edgeName=\"");
      _builder.append(tname);
      int _plusPlus = this.count++;
      _builder.append(_plusPlus);
      _builder.append("\",targetState=\"");
      String _name = state.getName();
      _builder.append(_name);
      _builder.append("\",cond=whenEvent(\"");
      String _name_1 = tr.getMessage().getName();
      _builder.append(_name_1);
      _builder.append("\"))");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("transition(edgeName=\"");
      _builder_1.append(tname);
      int _plusPlus_1 = this.count++;
      _builder_1.append(_plusPlus_1);
      _builder_1.append("\",targetState=\"");
      String _name_2 = state.getName();
      _builder_1.append(_name_2);
      _builder_1.append("\",cond=whenEventGuarded(\"");
      String _name_3 = tr.getMessage().getName();
      _builder_1.append(_name_3);
      _builder_1.append("\",{");
      CharSequence _genAction = this.genAction(tr.getGuard());
      _builder_1.append(_genAction);
      _builder_1.append("}))");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final MsgTransSwitch tr, final State state) {
    AnyAction _guard = tr.getGuard();
    boolean _tripleEquals = (_guard == null);
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("transition(edgeName=\"");
      _builder.append(tname);
      int _plusPlus = this.count++;
      _builder.append(_plusPlus);
      _builder.append("\",targetState=\"");
      String _name = state.getName();
      _builder.append(_name);
      _builder.append("\",cond=whenDispatch(\"");
      String _name_1 = tr.getMessage().getName();
      _builder.append(_name_1);
      _builder.append("\"))");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("transition(edgeName=\"");
      _builder_1.append(tname);
      int _plusPlus_1 = this.count++;
      _builder_1.append(_plusPlus_1);
      _builder_1.append("\",targetState=\"");
      String _name_2 = state.getName();
      _builder_1.append(_name_2);
      _builder_1.append("\",cond=whenDispatchGuarded(\"");
      String _name_3 = tr.getMessage().getName();
      _builder_1.append(_name_3);
      _builder_1.append("\",{");
      CharSequence _genAction = this.genAction(tr.getGuard());
      _builder_1.append(_genAction);
      _builder_1.append("}))");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final RequestTransSwitch tr, final State state) {
    AnyAction _guard = tr.getGuard();
    boolean _tripleEquals = (_guard == null);
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("transition(edgeName=\"");
      _builder.append(tname);
      int _plusPlus = this.count++;
      _builder.append(_plusPlus);
      _builder.append("\",targetState=\"");
      String _name = state.getName();
      _builder.append(_name);
      _builder.append("\",cond=whenRequest(\"");
      String _name_1 = tr.getMessage().getName();
      _builder.append(_name_1);
      _builder.append("\"))");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("transition(edgeName=\"");
      _builder_1.append(tname);
      int _plusPlus_1 = this.count++;
      _builder_1.append(_plusPlus_1);
      _builder_1.append("\",targetState=\"");
      String _name_2 = state.getName();
      _builder_1.append(_name_2);
      _builder_1.append("\",cond=whenRequestGuarded(\"");
      String _name_3 = tr.getMessage().getName();
      _builder_1.append(_name_3);
      _builder_1.append("\",{");
      CharSequence _genAction = this.genAction(tr.getGuard());
      _builder_1.append(_genAction);
      _builder_1.append("}))");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final ReplyTransSwitch tr, final State state) {
    AnyAction _guard = tr.getGuard();
    boolean _tripleEquals = (_guard == null);
    if (_tripleEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("transition(edgeName=\"");
      _builder.append(tname);
      int _plusPlus = this.count++;
      _builder.append(_plusPlus);
      _builder.append("\",targetState=\"");
      String _name = state.getName();
      _builder.append(_name);
      _builder.append("\",cond=whenReply(\"");
      String _name_1 = tr.getMessage().getName();
      _builder.append(_name_1);
      _builder.append("\"))");
      _builder.newLineIfNotEmpty();
      return _builder.toString();
    } else {
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("transition(edgeName=\"");
      _builder_1.append(tname);
      int _plusPlus_1 = this.count++;
      _builder_1.append(_plusPlus_1);
      _builder_1.append("\",targetState=\"");
      String _name_2 = state.getName();
      _builder_1.append(_name_2);
      _builder_1.append("\",cond=whenReplyGuarded(\"");
      String _name_3 = tr.getMessage().getName();
      _builder_1.append(_name_3);
      _builder_1.append("\",{");
      CharSequence _genAction = this.genAction(tr.getGuard());
      _builder_1.append(_genAction);
      _builder_1.append("}))");
      _builder_1.newLineIfNotEmpty();
      return _builder_1.toString();
    }
  }
  
  protected CharSequence _genTransition(final String tname, final State curstate, final Timeout tr, final State state) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("transition(edgeName=\"");
    _builder.append(tname);
    int _plusPlus = this.count++;
    _builder.append(_plusPlus);
    _builder.append("\",targetState=\"");
    String _name = state.getName();
    _builder.append(_name);
    _builder.append("\",cond=whenTimeout(\"local_tout_");
    _builder.append(this.curActorNameForTransition);
    _builder.append("_");
    String _name_1 = curstate.getName();
    _builder.append(_name_1);
    _builder.append("\"))   ");
    _builder.newLineIfNotEmpty();
    return _builder;
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
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("${");
    String _functor = ps.getStruct().getFunctor();
    _builder.append(_functor);
    _builder.append("(");
    {
      EList<PHead> _msgArg = ps.getStruct().getMsgArg();
      boolean _hasElements = false;
      for(final PHead term : _msgArg) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        Object _genPHead = this.genPHead(term);
        _builder.append(_genPHead);
      }
    }
    _builder.append(")}");
    return _builder;
  }
  
  protected CharSequence _genPHead(final PStruct ps) {
    StringConcatenation _builder = new StringConcatenation();
    String _functor = ps.getFunctor();
    _builder.append(_functor);
    _builder.append("(");
    {
      EList<PHead> _msgArg = ps.getMsgArg();
      boolean _hasElements = false;
      for(final PHead term : _msgArg) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        Object _genPHead = this.genPHead(term);
        _builder.append(_genPHead);
      }
    }
    _builder.append(")");
    return _builder;
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
  
  public CharSequence genTransition(final String tname, final State curstate, final EObject tr, final State state) {
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
