/**
 * IssCommSupport.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction

import it.unibo.supports2021.ActorBasicJava

interface IssCommSupport : IssOperations {
    fun registerObserver(obs: IssObserver)
    fun removeObserver(obs: IssObserver)
    fun close()
}