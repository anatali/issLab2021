/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>QActor System</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.QActorSystem#isTrace <em>Trace</em>}</li>
 *   <li>{@link it.unibo.qactork.QActorSystem#isLogmsg <em>Logmsg</em>}</li>
 *   <li>{@link it.unibo.qactork.QActorSystem#getSpec <em>Spec</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getQActorSystem()
 * @model
 * @generated
 */
public interface QActorSystem extends EObject
{
  /**
   * Returns the value of the '<em><b>Trace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Trace</em>' attribute.
   * @see #setTrace(boolean)
   * @see it.unibo.qactork.QactorkPackage#getQActorSystem_Trace()
   * @model
   * @generated
   */
  boolean isTrace();

  /**
   * Sets the value of the '{@link it.unibo.qactork.QActorSystem#isTrace <em>Trace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Trace</em>' attribute.
   * @see #isTrace()
   * @generated
   */
  void setTrace(boolean value);

  /**
   * Returns the value of the '<em><b>Logmsg</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Logmsg</em>' attribute.
   * @see #setLogmsg(boolean)
   * @see it.unibo.qactork.QactorkPackage#getQActorSystem_Logmsg()
   * @model
   * @generated
   */
  boolean isLogmsg();

  /**
   * Sets the value of the '{@link it.unibo.qactork.QActorSystem#isLogmsg <em>Logmsg</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Logmsg</em>' attribute.
   * @see #isLogmsg()
   * @generated
   */
  void setLogmsg(boolean value);

  /**
   * Returns the value of the '<em><b>Spec</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Spec</em>' containment reference.
   * @see #setSpec(QActorSystemSpec)
   * @see it.unibo.qactork.QactorkPackage#getQActorSystem_Spec()
   * @model containment="true"
   * @generated
   */
  QActorSystemSpec getSpec();

  /**
   * Sets the value of the '{@link it.unibo.qactork.QActorSystem#getSpec <em>Spec</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Spec</em>' containment reference.
   * @see #getSpec()
   * @generated
   */
  void setSpec(QActorSystemSpec value);

} // QActorSystem
