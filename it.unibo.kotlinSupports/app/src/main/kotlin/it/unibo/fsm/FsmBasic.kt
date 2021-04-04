package fsm
/*
 FsmBasic.kt
 Custom DSL for Moore Finite State Machine behavior.
 By Antonio Natali - DISI - University of Bologna
 */
 
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.MsgUtil
import kotlinx.coroutines.*
import java.util.NoSuchElementException
import kotlinx.coroutines.channels.actor

var traceOn   = false
fun trace( msg: String ){ if(traceOn) println( "		TRACE $msg" ) }

/*
================================================================
 STATE
================================================================
 */
class State(val stateName : String, val scope: CoroutineScope ) {
    private val edgeList          = mutableListOf<Transition>()
    private val stateEnterAction  = mutableListOf< suspend (State) -> Unit>()
    private val myself : State    = this

    fun transition(edgeName: String, targetState: String, cond: Transition.() -> Unit) {
        val trans = Transition(edgeName, targetState)
        //println("      trans $name $targetState")
        trans.cond() //set eventHandler (given by user) See fireIf
        edgeList.add(trans)
    }
    //Add an action which will be called when the state is entered
    fun action(  a:  suspend (State) -> Unit) {
        //println("State $stateName    | add action  $a")
        stateEnterAction.add( a )
    }
    /*
    fun addAction (action:  suspend (State) -> Unit) {
        stateEnterAction.add(action)
    }
    */
    suspend fun enterState() {
        val myself = this
        scope.launch {
            //trace(" --- | State $stateName    | enterState ${myself.stateName} ")
            stateEnterAction.get(0)( myself )
        }.join()
        //trace(" --- | State $stateName    | enterState DONE ")
    }

    //Get the appropriate Edge for the Message
    fun getTransitionForMessage(msg: ApplMessage): Transition? {
        //println("State $name       | getTransitionForMessage  $msg  list=${edgeList.size} ")
        val first = edgeList.firstOrNull { it.canHandleMessage(msg) }
        return first
    }
}

/*
================================================================
 Transition
================================================================
 */
class Transition(val edgeName: String, val targetState: String) {

    lateinit var edgeEventHandler: ( ApplMessage ) -> Boolean  //MsgId previous: String
    private val actionList = mutableListOf<(Transition) -> Unit>()
    fun action(action: (Transition) -> Unit) { //MEALY?
        //println("Transition  | add ACTION:  $action")
        actionList.add(action)
    }

    //Invoke when you go down the transition to another state
    fun enterTransition(retrieveState: (String) -> State): State {
//        println("Transition  | enterEdge  retrieveState: ${retrieveState} actionList=$actionList")
        actionList.forEach { it(this) }         //MEALY?
        return retrieveState(targetState)
    }

    fun canHandleMessage(msg: ApplMessage): Boolean {
        //println("Transition  | canHandleMessage: ${msg}  ${msg is Message.Event}" )
        return edgeEventHandler( msg  ) //msg.msgId()
    }
}

/*
================================================================
 FSM
================================================================
 */
	//"tcp://mqtt.eclipse.org:1883"
	//mqtt.eclipse.org
	//tcp://test.mosquitto.org
	//mqtt.fluux.io
	//"tcp://broker.hivemq.com" 
val mqttbrokerAddr   =  "tcp://broker.hivemq.com"

abstract class  FsmBasic(val name:  String,
                         val scope: CoroutineScope = GlobalScope,
                         val discardMessages : Boolean = false,
                         val usemqtt  :    Boolean = false,
                         val confined :    Boolean = false,
                         val ioBound :     Boolean = false,
                         val channelSize : Int = 50 ) {
	
	protected lateinit var currentState: State
	val NoMsg        = MsgUtil.buildEvent( name, "local_noMsg", "noMsg")
	val autoStartMsg = MsgUtil.buildDispatch(name, "autoStartSysMsg", "start", name)
	
	private val stateList          = mutableListOf<State>()
	protected var currentMsg       = NoMsg
	protected var myself : FsmBasic
	private var isStarted          = false
	private val msgQueueStore      = mutableListOf<ApplMessage>()
 
	internal val requestMap : MutableMap<String, ApplMessage> = mutableMapOf<String,ApplMessage>()
	
	companion object{
		var connectNameCounter = 0
	}
/* 
*/
    init {
		val initialState = getInitialState()
        myself  = this
        setBody( getBody(), initialState )
        //println("Fsm $name | INIT setBody in state=${initialState}")
    }
	
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	protected val dispatcher =
        if( confined ) newSingleThreadContext("fsmsingle")
        else  if( ioBound ) newFixedThreadPoolContext(64, "fsmiopool")
              else Dispatchers.Default 

 
	
	
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun waitTermination(){
		(fsmactor as Job).join()
	}
    //================================== STRUCTURAL =======================================
    fun state(stateName: String, build: State.() -> Unit) {
        val state = State(stateName, scope)
        state.build()
        stateList.add(state)
    }

    private fun getStateByName(name: String): State {
        return stateList.firstOrNull { it.stateName == name } ?: throw NoSuchElementException(name)
    }
    //===========================================================================================
	
	fun handleCurrentMessage(msg: ApplMessage, nextState: State?, memo: Boolean = true): Boolean {
        if (nextState is State) {
            trace("Fsm $name | handleCurrentMessage in ${currentState.stateName} " +
                    "msg=${msg.msgId} nextState=${nextState.stateName}")
            currentMsg   = msg
            if( currentMsg.isRequest() ){ requestMap.put(currentMsg.msgId, currentMsg) }  //Request
            currentState = nextState
            return true
        } else { //no nextState EXCLUDE EVENTS FROM msgQueueStore.  
            if (!memo) return false
            if (!(msg.isEvent())  && ! discardMessages) {
                msgQueueStore.add(msg)
                println("		*** Fsm $name |  state=${currentState.stateName} added $msg in msgQueueStore")
            }
            else trace("		*** Fsm $name | DISCARDING : ${msg.msgId} " +
                    "in state=${currentState.stateName}")
            return false
        }
	}
	
	suspend fun checkDoEmptyMove() {
        var nextState = checkTransition(NoMsg) //EMPTY MOVE
        while (nextState is State) {
            currentMsg = NoMsg
            currentState = nextState
            currentState.enterState()
            nextState = checkTransition(NoMsg) //EMPTY MOVE
        }		
	}

	private fun lookAtMsgQueueStore(): State? {
        trace("Fsm $name | lookAtMsgQueueStore :${msgQueueStore.size}")
        msgQueueStore.forEach {
            val state = checkTransition(it)
            if (state is State) {
                currentMsg = msgQueueStore.get( msgQueueStore.indexOf(it) )
                println("		*** Fsm $name | lookAtMsgQueueStore state=${currentState.stateName} " +
                        "FOUND $currentMsg")
                msgQueueStore.remove(it)
                return state
            }
        }
        return null
	}
	
	
	private fun checkTransition(msg: ApplMessage): State? {
        val trans = currentState.getTransitionForMessage(msg)
        //println("		*** Fsm $name | checkTransition $trans, $msg, curState=${currentState.stateName}")
        return if (trans != null) {
           trans.enterTransition { getStateByName(it) }
        } else {
            //println("		*** Fsm $name | checkTransition in ${currentState.stateName} NO next State for $msg !!!")
            null
        }
    }
	

	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun terminate(){
		println("		*** Fsm $name | terminates")
 
		fsmactor.close()
	}
	
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun autoMsg(  msg : ApplMessage ) {
     	//println("		*** Fsm $name | autoMsg $msg actor=${actor}")
     	fsmactor.send( msg )
    }
	
	fun doswitch(): Transition.() -> Unit { 
        return { edgeEventHandler = { true } }
    }
    fun doswitchGuarded( guard:()->Boolean ): Transition.() -> Unit {
        return { edgeEventHandler = { guard() } }
    }

	
    abstract fun getBody(): (FsmBasic.() -> Unit)
    abstract fun getInitialState(): String

	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    fun setBody(buildbody: FsmBasic.() -> Unit, initialStateName: String ) {
        buildbody()            //Build the structural part
        currentState = getStateByName(initialStateName)
        trace("Fsm $name | setBody send ${autoStartMsg}")
        scope.launch { fsmactor.send( autoStartMsg )    }  //auto-start
  	}

	suspend fun fsmStartWork() {
        isStarted = true
        trace("		*** Fsm $name | fsmStartWork in STATE ${currentState.stateName}")

        currentState.enterState()
        checkDoEmptyMove()
    }
		
	suspend fun fsmwork(msg : ApplMessage) {
//        println("		*** Fsm $name | fsmwork in STATE ${currentState.stateName} msg=$msg")
        var nextState = checkTransition(msg)
        var b = handleCurrentMessage( msg, nextState )
        while(  b  ){ //handle previous messages
            currentState.enterState()
            checkDoEmptyMove()
            val nextState1 = lookAtMsgQueueStore()
			if( nextState1 == null ){
				b = handleCurrentMessage( msg, nextState1, memo=false )
			}else{
				b = handleCurrentMessage( currentMsg, nextState1, memo=false )
			}
         }
	}

    suspend fun actorBody(msg: ApplMessage) {
        trace("Fsm $name | actorBody msg=$msg")
        if (msg.msgId == autoStartMsg.msgId && !isStarted) {
             fsmStartWork()
         } else {
            fsmwork(msg)
         }
    }
/*
 				
*/				
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    val fsmactor = scope.actor<ApplMessage>( dispatcher, capacity=channelSize ) {
        trace("Fsm $name | fsmactor RUNNING IN $dispatcher"  )
        for( msg in channel ) {  //msg-driven
            //println("		*** Fsm $name | fsmactor msg-driven msg= $msg   "  )
            if( msg.msgContent == "stopTheActor") { channel.close() }
            else{ actorBody( msg ) }
        }
    }

	
//-----------------------------------------------------------------------
	fun whenDispatch(msgName: String): Transition.() -> Unit {
            return {
                edgeEventHandler = {
                    //println("		*** Fsm $name | ${currentState.stateName} whenDispatch $it  $msgName")
                    it.isDispatch() && it.msgId == msgName }
             }
    }
	fun whenEvent(evName: String): Transition.() -> Unit {
            return {
                edgeEventHandler = {
                    //println("		*** Fsm $name | ${currentState.stateName} whenEvent $it  $msgName")
                    it.isEvent() && it.msgId == evName }
             }
    }
	
    fun whenDispatchGuarded(msgName: String, guard:()->Boolean ): Transition.() -> Unit {
        return {
            edgeEventHandler = {
                //println("whenDispatchGuarded $it - $evName");
                it.isDispatch() && it.msgId == msgName && guard()  }
          }
    }
	
	
	
/*
INTERACTION	
 */	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forward(  msgId : String, payload: String, dest : FsmBasic ){
		val msg = MsgUtil.buildDispatch(actor=name, msgId=msgId , content=payload, dest=dest.name)
		forward( msg, dest )
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun forward(  msg : ApplMessage, dest : FsmBasic ){
	 	//println("		*** Fsm $name | forward  msg: ${msg} ")
		 if( ! dest.fsmactor.isClosedForSend) dest.fsmactor.send( msg  )
		else println("		*** Fsm $name | WARNING: Messages.forward attempts to send ${msg} to closed ${dest.name} ")
	}
	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun emit(  msgId : String, payload: String ){
		val msg = MsgUtil.buildEvent(actor=name, msgId=msgId , content=payload )
		emit( msg )
	}	
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi
	suspend fun emit(  msg : ApplMessage ){
	 	//trace("		*** Fsm $name | emit  msg: ${msg} usemqtt=$usemqtt")
        trace("		*** Fsm $name | WARNING: Messages.emit NOT SUPPORTED without MQTT")
	}
			
  	
/*
machineExec and TIMING
*/
private var timeAtStart: Long = 0	
    fun machineExec(cmd: String) : Process {
        try {
            return Runtime.getRuntime().exec(cmd)
        } catch (e: Exception) {
            println("		*** Fsm $name | machineExec ERROR $e ")
            throw e
        }
    }

    fun memoTime() {
        timeAtStart = System.currentTimeMillis()
    }

    fun getDuration() : Int{
        val duration = (System.currentTimeMillis() - timeAtStart).toInt()
        //println("		*** Fsm $name | DURATION = $duration")
        return duration
    }

}//Fsm