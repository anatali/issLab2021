/**
 * IssObservable.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction
interface IssObservable {
    fun registerActor(obs: IssObserver)
    fun removeActor(obs: IssObserver)
}