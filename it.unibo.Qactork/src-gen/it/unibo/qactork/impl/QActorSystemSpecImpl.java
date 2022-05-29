/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.BrokerSpec;
import it.unibo.qactork.Context;
import it.unibo.qactork.Message;
import it.unibo.qactork.QActorDeclaration;
import it.unibo.qactork.QActorSystemSpec;
import it.unibo.qactork.QactorkPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>QActor System Spec</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.impl.QActorSystemSpecImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.QActorSystemSpecImpl#getMqttBroker <em>Mqtt Broker</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.QActorSystemSpecImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.QActorSystemSpecImpl#getContext <em>Context</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.QActorSystemSpecImpl#getActor <em>Actor</em>}</li>
 * </ul>
 *
 * @generated
 */
public class QActorSystemSpecImpl extends MinimalEObjectImpl.Container implements QActorSystemSpec
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getMqttBroker() <em>Mqtt Broker</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMqttBroker()
   * @generated
   * @ordered
   */
  protected BrokerSpec mqttBroker;

  /**
   * The cached value of the '{@link #getMessage() <em>Message</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessage()
   * @generated
   * @ordered
   */
  protected EList<Message> message;

  /**
   * The cached value of the '{@link #getContext() <em>Context</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContext()
   * @generated
   * @ordered
   */
  protected EList<Context> context;

  /**
   * The cached value of the '{@link #getActor() <em>Actor</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActor()
   * @generated
   * @ordered
   */
  protected EList<QActorDeclaration> actor;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected QActorSystemSpecImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return QactorkPackage.Literals.QACTOR_SYSTEM_SPEC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.QACTOR_SYSTEM_SPEC__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public BrokerSpec getMqttBroker()
  {
    return mqttBroker;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMqttBroker(BrokerSpec newMqttBroker, NotificationChain msgs)
  {
    BrokerSpec oldMqttBroker = mqttBroker;
    mqttBroker = newMqttBroker;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER, oldMqttBroker, newMqttBroker);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMqttBroker(BrokerSpec newMqttBroker)
  {
    if (newMqttBroker != mqttBroker)
    {
      NotificationChain msgs = null;
      if (mqttBroker != null)
        msgs = ((InternalEObject)mqttBroker).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER, null, msgs);
      if (newMqttBroker != null)
        msgs = ((InternalEObject)newMqttBroker).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER, null, msgs);
      msgs = basicSetMqttBroker(newMqttBroker, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER, newMqttBroker, newMqttBroker));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Message> getMessage()
  {
    if (message == null)
    {
      message = new EObjectContainmentEList<Message>(Message.class, this, QactorkPackage.QACTOR_SYSTEM_SPEC__MESSAGE);
    }
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Context> getContext()
  {
    if (context == null)
    {
      context = new EObjectContainmentEList<Context>(Context.class, this, QactorkPackage.QACTOR_SYSTEM_SPEC__CONTEXT);
    }
    return context;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<QActorDeclaration> getActor()
  {
    if (actor == null)
    {
      actor = new EObjectContainmentEList<QActorDeclaration>(QActorDeclaration.class, this, QactorkPackage.QACTOR_SYSTEM_SPEC__ACTOR);
    }
    return actor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER:
        return basicSetMqttBroker(null, msgs);
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MESSAGE:
        return ((InternalEList<?>)getMessage()).basicRemove(otherEnd, msgs);
      case QactorkPackage.QACTOR_SYSTEM_SPEC__CONTEXT:
        return ((InternalEList<?>)getContext()).basicRemove(otherEnd, msgs);
      case QactorkPackage.QACTOR_SYSTEM_SPEC__ACTOR:
        return ((InternalEList<?>)getActor()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case QactorkPackage.QACTOR_SYSTEM_SPEC__NAME:
        return getName();
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER:
        return getMqttBroker();
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MESSAGE:
        return getMessage();
      case QactorkPackage.QACTOR_SYSTEM_SPEC__CONTEXT:
        return getContext();
      case QactorkPackage.QACTOR_SYSTEM_SPEC__ACTOR:
        return getActor();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case QactorkPackage.QACTOR_SYSTEM_SPEC__NAME:
        setName((String)newValue);
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER:
        setMqttBroker((BrokerSpec)newValue);
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MESSAGE:
        getMessage().clear();
        getMessage().addAll((Collection<? extends Message>)newValue);
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__CONTEXT:
        getContext().clear();
        getContext().addAll((Collection<? extends Context>)newValue);
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__ACTOR:
        getActor().clear();
        getActor().addAll((Collection<? extends QActorDeclaration>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case QactorkPackage.QACTOR_SYSTEM_SPEC__NAME:
        setName(NAME_EDEFAULT);
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER:
        setMqttBroker((BrokerSpec)null);
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MESSAGE:
        getMessage().clear();
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__CONTEXT:
        getContext().clear();
        return;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__ACTOR:
        getActor().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case QactorkPackage.QACTOR_SYSTEM_SPEC__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MQTT_BROKER:
        return mqttBroker != null;
      case QactorkPackage.QACTOR_SYSTEM_SPEC__MESSAGE:
        return message != null && !message.isEmpty();
      case QactorkPackage.QACTOR_SYSTEM_SPEC__CONTEXT:
        return context != null && !context.isEmpty();
      case QactorkPackage.QACTOR_SYSTEM_SPEC__ACTOR:
        return actor != null && !actor.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //QActorSystemSpecImpl