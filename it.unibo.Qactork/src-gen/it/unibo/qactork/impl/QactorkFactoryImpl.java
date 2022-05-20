/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QactorkFactoryImpl extends EFactoryImpl implements QactorkFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static QactorkFactory init()
  {
    try
    {
      QactorkFactory theQactorkFactory = (QactorkFactory)EPackage.Registry.INSTANCE.getEFactory(QactorkPackage.eNS_URI);
      if (theQactorkFactory != null)
      {
        return theQactorkFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new QactorkFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QactorkFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case QactorkPackage.QACTOR_SYSTEM: return createQActorSystem();
      case QactorkPackage.QACTOR_SYSTEM_SPEC: return createQActorSystemSpec();
      case QactorkPackage.BROKER_SPEC: return createBrokerSpec();
      case QactorkPackage.MESSAGE: return createMessage();
      case QactorkPackage.OUT_ONLY_MESSAGE: return createOutOnlyMessage();
      case QactorkPackage.OUT_IN_MESSAGE: return createOutInMessage();
      case QactorkPackage.EVENT: return createEvent();
      case QactorkPackage.SIGNAL: return createSignal();
      case QactorkPackage.TOKEN: return createToken();
      case QactorkPackage.DISPATCH: return createDispatch();
      case QactorkPackage.REQUEST: return createRequest();
      case QactorkPackage.REPLY: return createReply();
      case QactorkPackage.INVITATION: return createInvitation();
      case QactorkPackage.CONTEXT: return createContext();
      case QactorkPackage.COMPONENT_IP: return createComponentIP();
      case QactorkPackage.QACTOR_DECLARATION: return createQActorDeclaration();
      case QactorkPackage.QACTOR_EXTERNAL: return createQActorExternal();
      case QactorkPackage.QACTOR_CODED: return createQActorCoded();
      case QactorkPackage.QACTOR: return createQActor();
      case QactorkPackage.STATE: return createState();
      case QactorkPackage.STATE_ACTION: return createStateAction();
      case QactorkPackage.IF_SOLVED_ACTION: return createIfSolvedAction();
      case QactorkPackage.GUARDED_STATE_ACTION: return createGuardedStateAction();
      case QactorkPackage.PRINT_CUR_MSG: return createPrintCurMsg();
      case QactorkPackage.PRINT: return createPrint();
      case QactorkPackage.SOLVE_GOAL: return createSolveGoal();
      case QactorkPackage.DISCARD_MSG: return createDiscardMsg();
      case QactorkPackage.MEMO_TIME: return createMemoTime();
      case QactorkPackage.DURATION: return createDuration();
      case QactorkPackage.FORWARD: return createForward();
      case QactorkPackage.EMIT: return createEmit();
      case QactorkPackage.DEMAND: return createDemand();
      case QactorkPackage.ANSWER: return createAnswer();
      case QactorkPackage.REPLY_REQ: return createReplyReq();
      case QactorkPackage.DELAY: return createDelay();
      case QactorkPackage.DELAY_INT: return createDelayInt();
      case QactorkPackage.DELAY_VAR: return createDelayVar();
      case QactorkPackage.DELAY_VREF: return createDelayVref();
      case QactorkPackage.DELAY_SOL: return createDelaySol();
      case QactorkPackage.MSG_COND: return createMsgCond();
      case QactorkPackage.END_ACTOR: return createEndActor();
      case QactorkPackage.UPDATE_RESOURCE: return createUpdateResource();
      case QactorkPackage.NO_MSG_COND: return createNoMsgCond();
      case QactorkPackage.ANY_ACTION: return createAnyAction();
      case QactorkPackage.CODE_RUN: return createCodeRun();
      case QactorkPackage.CODE_RUN_ACTOR: return createCodeRunActor();
      case QactorkPackage.CODE_RUN_SIMPLE: return createCodeRunSimple();
      case QactorkPackage.EXEC: return createExec();
      case QactorkPackage.TRANSITION: return createTransition();
      case QactorkPackage.EMPTY_TRANSITION: return createEmptyTransition();
      case QactorkPackage.NON_EMPTY_TRANSITION: return createNonEmptyTransition();
      case QactorkPackage.TIMEOUT: return createTimeout();
      case QactorkPackage.TIMEOUT_INT: return createTimeoutInt();
      case QactorkPackage.TIMEOUT_VAR: return createTimeoutVar();
      case QactorkPackage.TIMEOUT_VAR_REF: return createTimeoutVarRef();
      case QactorkPackage.TIMEOUT_SOL: return createTimeoutSol();
      case QactorkPackage.INPUT_TRANSITION: return createInputTransition();
      case QactorkPackage.EVENT_TRANS_SWITCH: return createEventTransSwitch();
      case QactorkPackage.MSG_TRANS_SWITCH: return createMsgTransSwitch();
      case QactorkPackage.REQUEST_TRANS_SWITCH: return createRequestTransSwitch();
      case QactorkPackage.REPLY_TRANS_SWITCH: return createReplyTransSwitch();
      case QactorkPackage.PHEAD: return createPHead();
      case QactorkPackage.PATOM: return createPAtom();
      case QactorkPackage.PATOM_STRING: return createPAtomString();
      case QactorkPackage.PATOMIC: return createPAtomic();
      case QactorkPackage.PATOM_NUM: return createPAtomNum();
      case QactorkPackage.PSTRUCT_REF: return createPStructRef();
      case QactorkPackage.PSTRUCT: return createPStruct();
      case QactorkPackage.VARIABLE: return createVariable();
      case QactorkPackage.VAR_REF: return createVarRef();
      case QactorkPackage.VAR_REF_IN_STR: return createVarRefInStr();
      case QactorkPackage.VAR_SOL_REF: return createVarSolRef();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QActorSystem createQActorSystem()
  {
    QActorSystemImpl qActorSystem = new QActorSystemImpl();
    return qActorSystem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QActorSystemSpec createQActorSystemSpec()
  {
    QActorSystemSpecImpl qActorSystemSpec = new QActorSystemSpecImpl();
    return qActorSystemSpec;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BrokerSpec createBrokerSpec()
  {
    BrokerSpecImpl brokerSpec = new BrokerSpecImpl();
    return brokerSpec;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Message createMessage()
  {
    MessageImpl message = new MessageImpl();
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public OutOnlyMessage createOutOnlyMessage()
  {
    OutOnlyMessageImpl outOnlyMessage = new OutOnlyMessageImpl();
    return outOnlyMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public OutInMessage createOutInMessage()
  {
    OutInMessageImpl outInMessage = new OutInMessageImpl();
    return outInMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Event createEvent()
  {
    EventImpl event = new EventImpl();
    return event;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Signal createSignal()
  {
    SignalImpl signal = new SignalImpl();
    return signal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Token createToken()
  {
    TokenImpl token = new TokenImpl();
    return token;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Dispatch createDispatch()
  {
    DispatchImpl dispatch = new DispatchImpl();
    return dispatch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Request createRequest()
  {
    RequestImpl request = new RequestImpl();
    return request;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Reply createReply()
  {
    ReplyImpl reply = new ReplyImpl();
    return reply;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Invitation createInvitation()
  {
    InvitationImpl invitation = new InvitationImpl();
    return invitation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Context createContext()
  {
    ContextImpl context = new ContextImpl();
    return context;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ComponentIP createComponentIP()
  {
    ComponentIPImpl componentIP = new ComponentIPImpl();
    return componentIP;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QActorDeclaration createQActorDeclaration()
  {
    QActorDeclarationImpl qActorDeclaration = new QActorDeclarationImpl();
    return qActorDeclaration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QActorExternal createQActorExternal()
  {
    QActorExternalImpl qActorExternal = new QActorExternalImpl();
    return qActorExternal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QActorCoded createQActorCoded()
  {
    QActorCodedImpl qActorCoded = new QActorCodedImpl();
    return qActorCoded;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QActor createQActor()
  {
    QActorImpl qActor = new QActorImpl();
    return qActor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public State createState()
  {
    StateImpl state = new StateImpl();
    return state;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StateAction createStateAction()
  {
    StateActionImpl stateAction = new StateActionImpl();
    return stateAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public IfSolvedAction createIfSolvedAction()
  {
    IfSolvedActionImpl ifSolvedAction = new IfSolvedActionImpl();
    return ifSolvedAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GuardedStateAction createGuardedStateAction()
  {
    GuardedStateActionImpl guardedStateAction = new GuardedStateActionImpl();
    return guardedStateAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PrintCurMsg createPrintCurMsg()
  {
    PrintCurMsgImpl printCurMsg = new PrintCurMsgImpl();
    return printCurMsg;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Print createPrint()
  {
    PrintImpl print = new PrintImpl();
    return print;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SolveGoal createSolveGoal()
  {
    SolveGoalImpl solveGoal = new SolveGoalImpl();
    return solveGoal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DiscardMsg createDiscardMsg()
  {
    DiscardMsgImpl discardMsg = new DiscardMsgImpl();
    return discardMsg;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MemoTime createMemoTime()
  {
    MemoTimeImpl memoTime = new MemoTimeImpl();
    return memoTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Duration createDuration()
  {
    DurationImpl duration = new DurationImpl();
    return duration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Forward createForward()
  {
    ForwardImpl forward = new ForwardImpl();
    return forward;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Emit createEmit()
  {
    EmitImpl emit = new EmitImpl();
    return emit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Demand createDemand()
  {
    DemandImpl demand = new DemandImpl();
    return demand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Answer createAnswer()
  {
    AnswerImpl answer = new AnswerImpl();
    return answer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ReplyReq createReplyReq()
  {
    ReplyReqImpl replyReq = new ReplyReqImpl();
    return replyReq;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Delay createDelay()
  {
    DelayImpl delay = new DelayImpl();
    return delay;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DelayInt createDelayInt()
  {
    DelayIntImpl delayInt = new DelayIntImpl();
    return delayInt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DelayVar createDelayVar()
  {
    DelayVarImpl delayVar = new DelayVarImpl();
    return delayVar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DelayVref createDelayVref()
  {
    DelayVrefImpl delayVref = new DelayVrefImpl();
    return delayVref;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public DelaySol createDelaySol()
  {
    DelaySolImpl delaySol = new DelaySolImpl();
    return delaySol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MsgCond createMsgCond()
  {
    MsgCondImpl msgCond = new MsgCondImpl();
    return msgCond;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EndActor createEndActor()
  {
    EndActorImpl endActor = new EndActorImpl();
    return endActor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public UpdateResource createUpdateResource()
  {
    UpdateResourceImpl updateResource = new UpdateResourceImpl();
    return updateResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NoMsgCond createNoMsgCond()
  {
    NoMsgCondImpl noMsgCond = new NoMsgCondImpl();
    return noMsgCond;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public AnyAction createAnyAction()
  {
    AnyActionImpl anyAction = new AnyActionImpl();
    return anyAction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CodeRun createCodeRun()
  {
    CodeRunImpl codeRun = new CodeRunImpl();
    return codeRun;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CodeRunActor createCodeRunActor()
  {
    CodeRunActorImpl codeRunActor = new CodeRunActorImpl();
    return codeRunActor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CodeRunSimple createCodeRunSimple()
  {
    CodeRunSimpleImpl codeRunSimple = new CodeRunSimpleImpl();
    return codeRunSimple;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Exec createExec()
  {
    ExecImpl exec = new ExecImpl();
    return exec;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Transition createTransition()
  {
    TransitionImpl transition = new TransitionImpl();
    return transition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EmptyTransition createEmptyTransition()
  {
    EmptyTransitionImpl emptyTransition = new EmptyTransitionImpl();
    return emptyTransition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NonEmptyTransition createNonEmptyTransition()
  {
    NonEmptyTransitionImpl nonEmptyTransition = new NonEmptyTransitionImpl();
    return nonEmptyTransition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Timeout createTimeout()
  {
    TimeoutImpl timeout = new TimeoutImpl();
    return timeout;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeoutInt createTimeoutInt()
  {
    TimeoutIntImpl timeoutInt = new TimeoutIntImpl();
    return timeoutInt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeoutVar createTimeoutVar()
  {
    TimeoutVarImpl timeoutVar = new TimeoutVarImpl();
    return timeoutVar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeoutVarRef createTimeoutVarRef()
  {
    TimeoutVarRefImpl timeoutVarRef = new TimeoutVarRefImpl();
    return timeoutVarRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TimeoutSol createTimeoutSol()
  {
    TimeoutSolImpl timeoutSol = new TimeoutSolImpl();
    return timeoutSol;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public InputTransition createInputTransition()
  {
    InputTransitionImpl inputTransition = new InputTransitionImpl();
    return inputTransition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EventTransSwitch createEventTransSwitch()
  {
    EventTransSwitchImpl eventTransSwitch = new EventTransSwitchImpl();
    return eventTransSwitch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MsgTransSwitch createMsgTransSwitch()
  {
    MsgTransSwitchImpl msgTransSwitch = new MsgTransSwitchImpl();
    return msgTransSwitch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RequestTransSwitch createRequestTransSwitch()
  {
    RequestTransSwitchImpl requestTransSwitch = new RequestTransSwitchImpl();
    return requestTransSwitch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ReplyTransSwitch createReplyTransSwitch()
  {
    ReplyTransSwitchImpl replyTransSwitch = new ReplyTransSwitchImpl();
    return replyTransSwitch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PHead createPHead()
  {
    PHeadImpl pHead = new PHeadImpl();
    return pHead;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PAtom createPAtom()
  {
    PAtomImpl pAtom = new PAtomImpl();
    return pAtom;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PAtomString createPAtomString()
  {
    PAtomStringImpl pAtomString = new PAtomStringImpl();
    return pAtomString;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PAtomic createPAtomic()
  {
    PAtomicImpl pAtomic = new PAtomicImpl();
    return pAtomic;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PAtomNum createPAtomNum()
  {
    PAtomNumImpl pAtomNum = new PAtomNumImpl();
    return pAtomNum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PStructRef createPStructRef()
  {
    PStructRefImpl pStructRef = new PStructRefImpl();
    return pStructRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PStruct createPStruct()
  {
    PStructImpl pStruct = new PStructImpl();
    return pStruct;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable createVariable()
  {
    VariableImpl variable = new VariableImpl();
    return variable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarRef createVarRef()
  {
    VarRefImpl varRef = new VarRefImpl();
    return varRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarRefInStr createVarRefInStr()
  {
    VarRefInStrImpl varRefInStr = new VarRefInStrImpl();
    return varRefInStr;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarSolRef createVarSolRef()
  {
    VarSolRefImpl varSolRef = new VarSolRefImpl();
    return varSolRef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public QactorkPackage getQactorkPackage()
  {
    return (QactorkPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static QactorkPackage getPackage()
  {
    return QactorkPackage.eINSTANCE;
  }

} //QactorkFactoryImpl
