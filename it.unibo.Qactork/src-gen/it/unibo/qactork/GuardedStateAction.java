/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Guarded State Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.GuardedStateAction#getGuard <em>Guard</em>}</li>
 *   <li>{@link it.unibo.qactork.GuardedStateAction#getOkactions <em>Okactions</em>}</li>
 *   <li>{@link it.unibo.qactork.GuardedStateAction#getKoactions <em>Koactions</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getGuardedStateAction()
 * @model
 * @generated
 */
public interface GuardedStateAction extends StateAction
{
  /**
   * Returns the value of the '<em><b>Guard</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Guard</em>' containment reference.
   * @see #setGuard(AnyAction)
   * @see it.unibo.qactork.QactorkPackage#getGuardedStateAction_Guard()
   * @model containment="true"
   * @generated
   */
  AnyAction getGuard();

  /**
   * Sets the value of the '{@link it.unibo.qactork.GuardedStateAction#getGuard <em>Guard</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Guard</em>' containment reference.
   * @see #getGuard()
   * @generated
   */
  void setGuard(AnyAction value);

  /**
   * Returns the value of the '<em><b>Okactions</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.StateAction}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Okactions</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getGuardedStateAction_Okactions()
   * @model containment="true"
   * @generated
   */
  EList<StateAction> getOkactions();

  /**
   * Returns the value of the '<em><b>Koactions</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.StateAction}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Koactions</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getGuardedStateAction_Koactions()
   * @model containment="true"
   * @generated
   */
  EList<StateAction> getKoactions();

} // GuardedStateAction
