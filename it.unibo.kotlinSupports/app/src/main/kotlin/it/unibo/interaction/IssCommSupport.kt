/**
 * IssCommSupport.java
 * ==========================================================================
 *
 * ==========================================================================
 */
package it.unibo.interaction

interface IssCommSupport : IssOperations {
    fun isOpen() : Boolean
    fun close()
}