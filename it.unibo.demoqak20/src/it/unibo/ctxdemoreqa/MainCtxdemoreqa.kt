/* Generated by AN DISI Unibo */ 
package it.unibo.ctxdemoreqa
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "demo_req_a.pl", "sysRules.pl"
	)
}

