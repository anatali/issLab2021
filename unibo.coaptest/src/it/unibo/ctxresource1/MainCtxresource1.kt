/* Generated by AN DISI Unibo */ 
package it.unibo.ctxresource1
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "resource1.pl", "sysRules.pl","ctxresource1"
	)
}
