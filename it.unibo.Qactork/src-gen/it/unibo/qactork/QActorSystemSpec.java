/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>QActor System Spec</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.QActorSystemSpec#getName <em>Name</em>}</li>
 *   <li>{@link it.unibo.qactork.QActorSystemSpec#getMqttBroker <em>Mqtt Broker</em>}</li>
 *   <li>{@link it.unibo.qactork.QActorSystemSpec#getMessage <em>Message</em>}</li>
 *   <li>{@link it.unibo.qactork.QActorSystemSpec#getContext <em>Context</em>}</li>
 *   <li>{@link it.unibo.qactork.QActorSystemSpec#getActor <em>Actor</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getQActorSystemSpec()
 * @model
 * @generated
 */
public interface QActorSystemSpec extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see it.unibo.qactork.QactorkPackage#getQActorSystemSpec_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link it.unibo.qactork.QActorSystemSpec#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Mqtt Broker</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mqtt Broker</em>' containment reference.
   * @see #setMqttBroker(BrokerSpec)
   * @see it.unibo.qactork.QactorkPackage#getQActorSystemSpec_MqttBroker()
   * @model containment="true"
   * @generated
   */
  BrokerSpec getMqttBroker();

  /**
   * Sets the value of the '{@link it.unibo.qactork.QActorSystemSpec#getMqttBroker <em>Mqtt Broker</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mqtt Broker</em>' containment reference.
   * @see #getMqttBroker()
   * @generated
   */
  void setMqttBroker(BrokerSpec value);

  /**
   * Returns the value of the '<em><b>Message</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.Message}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getQActorSystemSpec_Message()
   * @model containment="true"
   * @generated
   */
  EList<Message> getMessage();

  /**
   * Returns the value of the '<em><b>Context</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.Context}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Context</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getQActorSystemSpec_Context()
   * @model containment="true"
   * @generated
   */
  EList<Context> getContext();

  /**
   * Returns the value of the '<em><b>Actor</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.QActorDeclaration}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Actor</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getQActorSystemSpec_Actor()
   * @model containment="true"
   * @generated
   */
  EList<QActorDeclaration> getActor();

} // QActorSystemSpec