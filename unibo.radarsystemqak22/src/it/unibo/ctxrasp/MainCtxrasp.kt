/* Generated by AN DISI Unibo */ 
package it.unibo.ctxrasp
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.1.xxx", this, "radarsystem22analisi.pl", "sysRules.pl"
	)
}
