/**
 * IssObservable.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction
interface IssActorObservable {
    fun registerActor(obs: IUniboActor)
    fun removeActor(obs: IUniboActor)
}