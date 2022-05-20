/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Empty Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.EmptyTransition#getTargetState <em>Target State</em>}</li>
 *   <li>{@link it.unibo.qactork.EmptyTransition#getEguard <em>Eguard</em>}</li>
 *   <li>{@link it.unibo.qactork.EmptyTransition#getOthertargetState <em>Othertarget State</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getEmptyTransition()
 * @model
 * @generated
 */
public interface EmptyTransition extends Transition
{
  /**
   * Returns the value of the '<em><b>Target State</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target State</em>' reference.
   * @see #setTargetState(State)
   * @see it.unibo.qactork.QactorkPackage#getEmptyTransition_TargetState()
   * @model
   * @generated
   */
  State getTargetState();

  /**
   * Sets the value of the '{@link it.unibo.qactork.EmptyTransition#getTargetState <em>Target State</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target State</em>' reference.
   * @see #getTargetState()
   * @generated
   */
  void setTargetState(State value);

  /**
   * Returns the value of the '<em><b>Eguard</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Eguard</em>' containment reference.
   * @see #setEguard(AnyAction)
   * @see it.unibo.qactork.QactorkPackage#getEmptyTransition_Eguard()
   * @model containment="true"
   * @generated
   */
  AnyAction getEguard();

  /**
   * Sets the value of the '{@link it.unibo.qactork.EmptyTransition#getEguard <em>Eguard</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Eguard</em>' containment reference.
   * @see #getEguard()
   * @generated
   */
  void setEguard(AnyAction value);

  /**
   * Returns the value of the '<em><b>Othertarget State</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Othertarget State</em>' reference.
   * @see #setOthertargetState(State)
   * @see it.unibo.qactork.QactorkPackage#getEmptyTransition_OthertargetState()
   * @model
   * @generated
   */
  State getOthertargetState();

  /**
   * Sets the value of the '{@link it.unibo.qactork.EmptyTransition#getOthertargetState <em>Othertarget State</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Othertarget State</em>' reference.
   * @see #getOthertargetState()
   * @generated
   */
  void setOthertargetState(State value);

} // EmptyTransition
