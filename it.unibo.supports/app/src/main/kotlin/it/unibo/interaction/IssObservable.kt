/**
 * IssObservable.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction
interface IssObservable {
    fun registerActor(obs: IJavaActor)
    fun removeActor(obs: IJavaActor)
}