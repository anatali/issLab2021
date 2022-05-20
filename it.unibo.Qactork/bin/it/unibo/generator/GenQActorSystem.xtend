package it.unibo.generator

import it.unibo.qactork.QActorSystemSpec
import it.unibo.qactork.generator.common.SysKb
import it.unibo.qactork.generator.common.GenUtils
import it.unibo.qactork.QActorSystem

class GenQActorSystem {
	val genQActorCtxSystem = new GenQActorCtxSystem
	val genQActor          = new GenQActor
	val genSystemInfo      = new GenSystemInfo
	var tracing            = false
	var msglogging         = false
	
	def void doGenerate(QActorSystem system, SysKb kb) {
		tracing    = system.trace
		msglogging = system.logmsg
		doGenerate(system.spec,kb)		
	}
	def void doGenerate(QActorSystemSpec system, SysKb kb) {
		
		//Generate the system info
		genSystemInfo.doGenerate(system,tracing, msglogging, kb)
		
		for( context : system.context ){
			GenUtils.setPackageName( context.name )
			//Generate the contexts
			genQActorCtxSystem.doGenerate(system,context,kb)
			
		}
		for( actor : system.actor ){
			GenUtils.setPackageName( actor.name )
 			genQActor.doGenerate(actor, kb)	
		}
	}
}