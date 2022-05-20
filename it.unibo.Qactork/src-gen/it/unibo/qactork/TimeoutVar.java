/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Timeout Var</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.TimeoutVar#getVariable <em>Variable</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getTimeoutVar()
 * @model
 * @generated
 */
public interface TimeoutVar extends Timeout
{
  /**
   * Returns the value of the '<em><b>Variable</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Variable</em>' containment reference.
   * @see #setVariable(Variable)
   * @see it.unibo.qactork.QactorkPackage#getTimeoutVar_Variable()
   * @model containment="true"
   * @generated
   */
  Variable getVariable();

  /**
   * Sets the value of the '{@link it.unibo.qactork.TimeoutVar#getVariable <em>Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Variable</em>' containment reference.
   * @see #getVariable()
   * @generated
   */
  void setVariable(Variable value);

} // TimeoutVar
