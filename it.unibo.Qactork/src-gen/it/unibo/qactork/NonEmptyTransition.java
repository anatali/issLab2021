/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Non Empty Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.NonEmptyTransition#getName <em>Name</em>}</li>
 *   <li>{@link it.unibo.qactork.NonEmptyTransition#getDuration <em>Duration</em>}</li>
 *   <li>{@link it.unibo.qactork.NonEmptyTransition#getTrans <em>Trans</em>}</li>
 *   <li>{@link it.unibo.qactork.NonEmptyTransition#getElseempty <em>Elseempty</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getNonEmptyTransition()
 * @model
 * @generated
 */
public interface NonEmptyTransition extends Transition
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see it.unibo.qactork.QactorkPackage#getNonEmptyTransition_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link it.unibo.qactork.NonEmptyTransition#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Duration</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Duration</em>' containment reference.
   * @see #setDuration(Timeout)
   * @see it.unibo.qactork.QactorkPackage#getNonEmptyTransition_Duration()
   * @model containment="true"
   * @generated
   */
  Timeout getDuration();

  /**
   * Sets the value of the '{@link it.unibo.qactork.NonEmptyTransition#getDuration <em>Duration</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Duration</em>' containment reference.
   * @see #getDuration()
   * @generated
   */
  void setDuration(Timeout value);

  /**
   * Returns the value of the '<em><b>Trans</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.InputTransition}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trans</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getNonEmptyTransition_Trans()
   * @model containment="true"
   * @generated
   */
  EList<InputTransition> getTrans();

  /**
   * Returns the value of the '<em><b>Elseempty</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elseempty</em>' containment reference.
   * @see #setElseempty(EmptyTransition)
   * @see it.unibo.qactork.QactorkPackage#getNonEmptyTransition_Elseempty()
   * @model containment="true"
   * @generated
   */
  EmptyTransition getElseempty();

  /**
   * Sets the value of the '{@link it.unibo.qactork.NonEmptyTransition#getElseempty <em>Elseempty</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Elseempty</em>' containment reference.
   * @see #getElseempty()
   * @generated
   */
  void setElseempty(EmptyTransition value);

} // NonEmptyTransition
