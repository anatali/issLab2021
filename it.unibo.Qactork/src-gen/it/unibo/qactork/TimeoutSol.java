/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Timeout Sol</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.TimeoutSol#getRefsoltime <em>Refsoltime</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getTimeoutSol()
 * @model
 * @generated
 */
public interface TimeoutSol extends Timeout
{
  /**
   * Returns the value of the '<em><b>Refsoltime</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Refsoltime</em>' containment reference.
   * @see #setRefsoltime(VarSolRef)
   * @see it.unibo.qactork.QactorkPackage#getTimeoutSol_Refsoltime()
   * @model containment="true"
   * @generated
   */
  VarSolRef getRefsoltime();

  /**
   * Sets the value of the '{@link it.unibo.qactork.TimeoutSol#getRefsoltime <em>Refsoltime</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Refsoltime</em>' containment reference.
   * @see #getRefsoltime()
   * @generated
   */
  void setRefsoltime(VarSolRef value);

} // TimeoutSol