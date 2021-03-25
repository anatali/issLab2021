/**
 * IssCommActorSupport.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction

import it.unibo.supports2021.ActorBasicJava

interface IssCommActorSupport : IssOperations {
    fun registerActor( obs: ActorBasicJava)
    fun removeActor( obs: ActorBasicJava)
    fun isOpen() : Boolean
    fun close()
}