/**
 * IssCommSupport.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction

import it.unibo.supports.IssWsHttpKotlinSupport
import kotlinx.coroutines.CoroutineScope

interface IssCommSupport : IssOperations {
    fun getConnectionWs(scope: CoroutineScope, addr: String) : IssWsHttpKotlinSupport
    fun isOpen() : Boolean
    fun close()
}