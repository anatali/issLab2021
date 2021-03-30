/**
 * IssOperations.kt
 * ==========================================================================
 * Defines high-level interaction operation
 * These operations are introduced with reference to message-passing
 * rather than procedure-call.
 * Thus, forward is just 'fire and forget', while
 * request assumes that the called will execute a reply related to that request
 *
 * requestSynch is introduced to help the transition to the new paradigm,
 *
 * ==========================================================================
 */
package it.unibo.interaction

interface IssOperations {
    fun forward(msg: String) //String related to cril, aril, AppMsg
    fun request(msg: String)
    fun reply(msg: String)
    fun requestSynch(msg: String): String
}