/**
 * IssObservable.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction
interface IssActorObservable {
    fun registerActor(obs: IJavaActor)
    fun removeActor(obs: IJavaActor)
}