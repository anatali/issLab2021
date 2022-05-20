package it.unibo.generator

import it.unibo.qactork.*
import it.unibo.qactork.generator.common.SysKb
import it.unibo.qactork.generator.common.GenUtils
import org.eclipse.emf.common.util.EList

class GenQActor {
	var count = 0
	//def void doGenerate(QActor actor, SysKb kb) {
	def dispatch void doGenerate(QActorDeclaration actor, SysKb kb) {}
	def dispatch void doGenerate(QActorExternal actor, SysKb kb) {
		println(" *** GenQActor external: "  + actor.name )
	}
	def dispatch void doGenerate(QActorCoded actor, SysKb kb) {
		println(" *** GenQActor already coded: "  + actor.name )
	}
	def dispatch void doGenerate(QActor actor, SysKb kb) {
 		println(" *** GenQActor starts for regular actor " + actor.name )
//		if( actor.className !== null) println(" *** GenQActor already coded: "  + actor.name )
//		else{
			val actorClassName  = actor.name.toFirstUpper 		 
			//GenUtils.genFile(  GenUtils.packageName,  actorClassName,  "kt", genQActor( actor, actorClassName, ""  ) )	
			GenUtils.genFileDir( "../src/", GenUtils.packageName,  actorClassName,  "kt", genQActor( actor, actorClassName, ""  ) )	
//		}
	}
	
	def  genQActor( QActor actor, String actorClassName, String extensions)'''
	«GenUtils.logo»
	package «GenUtils.packageName»
	
	import it.unibo.kactor.*
	import alice.tuprolog.*
	import kotlinx.coroutines.CoroutineScope
	import kotlinx.coroutines.delay
	import kotlinx.coroutines.launch
	import kotlinx.coroutines.runBlocking
		
	class «actorClassName» ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){
	
		override fun getInitialState() : String{
			return "«genInitialStateName(actor)»"
		}
		override fun getBody() : (ActorBasicFsm.() -> Unit){
			«IF actor.start !== null»«genAction( actor.start ) »«ENDIF»
			return { //this:ActionBasciFsm
					«genStates(actor)»
				}
			}
	}
	'''
	def genInitialStateName( QActor actor ){
		val StringBuilder sb = new StringBuilder();
		actor.states.forEach[ state | if( state.normal ) sb.append( state.name )]
		return sb.toString()
	}
	def genStates( QActor actor ){
		val StringBuilder sb = new StringBuilder();
		actor.states.forEach[ state | sb.append( genState( actor, state ) ) ]
		return sb.toString()
	}
	
	def genState( QActor actor, State  state )'''
	 state("«state.name»") { //this:State
	 	action { //it:State
	 		«genActions( state )»
	 		«genTimer( actor, state )»
	 	}
	 	«IF (state.transition !== null) » «genTransition(actor, state, state.transition)»«ENDIF»	 	 
	 }	 
	'''
	
	def genActions( State state ){
		val StringBuilder sb = new StringBuilder();
		for( action : state.actions ){			 
			 sb.append( genAction( action )	)	 
		}
		return sb.toString()
	}
	//If there is a whenTime, generate a Timer actor
	def genTimer( QActor actor,State  state){ if( state.transition !== null ) genTheTimer( actor,state.name, state.transition ) }
	def dispatch genTheTimer( QActor actor,String stateName, Transition  tr )''''''
	def dispatch genTheTimer( QActor actor,String stateName, NonEmptyTransition  tr ){
		if( tr.duration !== null ) return'''
		stateTimer = TimerActor("timer_«stateName»", 
			scope, context!!, "local_tout_«actor.name»_«stateName»", «genTimeToWait(tr.duration)» )
		'''
	}
	
	def dispatch genTimeToWait(Timeout tout){}
	def dispatch genTimeToWait(TimeoutInt tout)'''«tout.msec».toLong()'''
 	def dispatch genTimeToWait(TimeoutVar tout){
		var d = genPHead( tout.variable)
		return d
	}
	def dispatch genTimeToWait(TimeoutSol tout){
		var d = genPHead( tout.refsoltime )
		return '''«d».toLong()''' 
	}
	
/*
 * ACTIONS
 */	
    def dispatch genAction( IfSolvedAction a ){	
    	var actionStr = genActionSequence( a.solvedactions ) 
    	var guardStr = '''if( currentSolution.isSuccess() ) ''' 
 		var elseStr   = ""
 		if( a.notsolvedactions !== null ) elseStr = '''else
«genActionSequence( a.notsolvedactions )»
 		'''
    	return '''«guardStr»«actionStr»«elseStr»'''
    }
    
  	def dispatch genAction( GuardedStateAction a ) {
 		var actionStr = genActionSequence( a.okactions ) 
 		var guardStr  = '''if( «genAction(a.guard )» )''' 		 
 		var elseStr   = ""
 		if( a.koactions.length>0 ) elseStr = '''else
 «genActionSequence( a.koactions )»
 		'''
 		return '''«guardStr»«actionStr»«elseStr»''' 
 	}
 
	def genActionSequence( EList<StateAction> a ){
		val StringBuilder sb = new StringBuilder();
		sb.append("{")
		for( action : a  ){ 
 			sb.append( genAction( action ))
		}
		sb.append("}\n")
		return sb.toString()
	}
	
	def dispatch genAction( StateAction a )'''not here StateAction «a»
	'''
	def dispatch genAction( AnyAction a )'''«a.body.toString().replace("#","")» 
	''' 
	def dispatch genAction( Print a )'''println(«genPHead(a.args)»)
	'''
	def dispatch genAction( Forward a )'''forward("«a.msgref.name»", "«genPHead(a.^val)»" ,"«a.dest.name»" ) 
	'''
	def dispatch genAction( Emit a )'''emit("«a.msgref.name»", "«genPHead(a.^val)»" ) 
	'''
	def dispatch genAction( Demand a )'''request("«a.msgref.name»", "«genPHead(a.^val)»" ,"«a.dest.name»" )  
	'''
	def dispatch genAction( Answer a )'''answer("«a.reqref.name»", "«a.msgref.name»", "«genPHead(a.^val)»"   )  
	'''

	def dispatch genAction( ReplyReq a )'''replyreq("«a.reqref.name»", "«a.msgref.name»", "«genPHead(a.^val)»"   )  
	'''
	
	def dispatch genAction( Delay    a )'''not here dlay''' 
	def dispatch genAction( DelayInt a )'''delay(«a.time») 
	'''		
	def dispatch genAction( DelayVar a ){ var d = genPHead( a.refvar ); return '''delay(«d»)
	''' }
	def dispatch genAction( DelayVref a ){ var d = genPHead( a.reftime ); return '''delay(Integer.parseInt(«d»).toLong())
	''' }
	def dispatch genAction( DelaySol a ){ var d = genPHead( a.refsoltime ); return'''delay(Integer.parseInt(«d»).toLong())
	''' }
	
	def dispatch genAction( SolveGoal m ){
		val g = genPHead(m.goal).toString.replace("\"","'")
		var r = ""
		if( m.resVar !== null ) r = genPHead(m.resVar).toString()  
	return '''solve("«g»","«r»") //set resVar	
	'''
	}
	
 	def dispatch genAction( MsgCond m ){
 	val msgUserTemplate = ""+genPHead(m.msg)
	val msgTemplate     = ""+genPHead(m.message.msg)
	return 
'''if( checkMsgContent( Term.createTerm("«msgTemplate»"), Term.createTerm("«msgUserTemplate»"), 
                        currentMsg.msgContent()) ) { //set msgArgList
		«genTestMsgActions( m.condactions )»
}«IF m.ifnot !== null»else{
	«genTestMsgActions(  m.ifnot.notcondactions )»
}«ENDIF»
'''
}

def dispatch genAction( EndActor m )'''
	terminate(«m.arg»)
'''
	
 
	def genTestMsgActions( EList<StateAction> a ){
		val StringBuilder sb = new StringBuilder();
		for( action : a  ){ 
			//sb.append( genActionsInState( action ))
			sb.append( genAction( action ))
		}
		return sb.toString()
	}

 	def dispatch genAction( DiscardMsg a ){
 	if( a.discard )'''discardMessages = true
 	'''
 	else'''discardMessages = false
 	'''
 	}
 	def dispatch genAction( UpdateResource a ) '''updateResourceRep(«genAction(a.^val).toString()»)
 	'''
  
  	def dispatch genAction( MemoTime a )'''«a.store» = getCurrentTime()
  	''' 
  	def dispatch genAction( Duration a )'''«a.store» = getDuration(«a.start»)
  	''' 
 	def dispatch genAction( PrintCurMsg a )'''println("$name in ${currentState.stateName} | $currentMsg")
	'''
	def dispatch genAction( CodeRun move )'''not here 'CodeRun'''
	def dispatch genAction( CodeRunSimple move ){
		return '''«move.bitem»( «FOR ms:move.args SEPARATOR ","»«genPHead(ms)» «ENDFOR» )
		'''
	}
 	def dispatch genAction( CodeRunActor move ){
	var actorRef = "myself"
	if( (move.args.length >  0) ) actorRef="myself ,"
	return '''«move.aitem»(«actorRef»«FOR ms:move.args SEPARATOR ","»«genPHead(ms)» «ENDFOR»)
	'''
 	}
	def dispatch genAction( Exec move )'''currentProcess=machineExec(«move.action»)
	''' 
	
/*
 * TRANSITIONS
 */	
	
	def dispatch genTransition( QActor actor, State  curstate,  Transition  tr )'''not here Transition'''
	def dispatch genTransition( QActor actor, State  curstate,  EmptyTransition  tr ){		 
		if( tr.eguard === null) return
'''transition( edgeName="goto",targetState="«tr.targetState.name»", cond=doswitch() )
'''	
		else return
'''transition( edgeName="goto",targetState="«tr.targetState.name»", cond=doswitchGuarded({«genAction(tr.eguard)»}) )
transition( edgeName="goto",targetState="«tr.othertargetState.name»", cond=doswitchGuarded({! («genAction(tr.eguard)») }) )
	'''	
	}
	var curActorNameForTransition = "" //to be removed by propagating actor 
	def dispatch genTransition( QActor actor, State  curstate,  NonEmptyTransition  tr ){
		curActorNameForTransition = actor.name
		val sb = new StringBuilder()
		if( tr.duration !== null ) sb.append(  genTransition(tr.name, curstate, tr.duration, tr.duration.targetState) )
		for( t : tr.trans ){			
			sb.append(  genTransition(tr.name, curstate, t, t.targetState) )
		}
  		if( tr.elseempty !== null  ) sb.append( genTransition(actor,curstate,tr.elseempty) )
		return sb.toString()		
	} 
	def dispatch genTransition(  String tname, State  curstate, InputTransition  tr, State state )'''not here InputTransition'''
	
	def dispatch genTransition(  String tname, State  curstate, EventTransSwitch  tr, State state  ){
		if( tr.guard === null ) return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenEvent("«tr.message.name»"))
	'''
		else return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenEventGuarded("«tr.message.name»",{«genAction(tr.guard)»}))
	'''		
	} 
	
	def dispatch genTransition(  String tname, State  curstate, MsgTransSwitch  tr, State state  ){
		if( tr.guard === null ) return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenDispatch("«tr.message.name»"))
	'''
		else return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenDispatchGuarded("«tr.message.name»",{«genAction(tr.guard)»}))
	'''		
	}
	def dispatch genTransition(  String tname, State  curstate, RequestTransSwitch  tr, State state  ){
		if( tr.guard === null ) return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenRequest("«tr.message.name»"))
	'''
		else return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenRequestGuarded("«tr.message.name»",{«genAction(tr.guard)»}))
	'''		
	} 
	def dispatch genTransition(  String tname, State  curstate, ReplyTransSwitch  tr, State state  ){
		if( tr.guard === null ) return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenReply("«tr.message.name»"))
	'''
		else return
	'''transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenReplyGuarded("«tr.message.name»",{«genAction(tr.guard)»}))
	'''		
	}
	def dispatch genTransition(  String tname, State  curstate, Timeout  tr, State state  )'''
	transition(edgeName="«tname»«count++»",targetState="«state.name»",cond=whenTimeout("local_tout_«curActorNameForTransition»_«curstate.name»"))   
 	'''	
	
	
/*
 * PHead
 */

 def dispatch genPHead( PHead ph )'''not here genPHead'''
 def dispatch genPHead( PAtomString ph )'''"«ph.^val»"'''
 def dispatch genPHead( PAtomic ph )'''«ph.^val»'''
 def dispatch genPHead( Variable pt )'''«pt.varName»'''
 def dispatch genPHead( VarRef pt )'''$«pt.varName»'''
 def dispatch genPHead( VarRefInStr pt )'''${getCurSol("«pt.varName»").toString()}'''
 def dispatch genPHead( VarSolRef pt )'''getCurSol("«pt.varName»").toString()'''
 def dispatch genPHead( PStructRef ps ) 
 '''${«ps.struct.functor»(«FOR term: ps.struct.msgArg SEPARATOR ','»«genPHead(term)»«ENDFOR»)}'''
 def dispatch genPHead( PStruct ps )
 '''«ps.functor»(«FOR term: ps.msgArg SEPARATOR ','»«genPHead(term)»«ENDFOR»)'''


 def dispatch genPHead( PAtomNum ph )'''«ph.^val»'''
 
}