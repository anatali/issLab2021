/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.ComponentIP;
import it.unibo.qactork.Context;
import it.unibo.qactork.QactorkPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Context</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.impl.ContextImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.ContextImpl#getIp <em>Ip</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.ContextImpl#isMqtt <em>Mqtt</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContextImpl extends MinimalEObjectImpl.Container implements Context
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
   * The cached value of the '{@link #getIp() <em>Ip</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIp()
   * @generated
   * @ordered
   */
  protected ComponentIP ip;

  /**
   * The default value of the '{@link #isMqtt() <em>Mqtt</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isMqtt()
   * @generated
   * @ordered
   */
  protected static final boolean MQTT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isMqtt() <em>Mqtt</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isMqtt()
   * @generated
   * @ordered
   */
  protected boolean mqtt = MQTT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ContextImpl()
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
    return QactorkPackage.Literals.CONTEXT;
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
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.CONTEXT__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ComponentIP getIp()
  {
    return ip;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetIp(ComponentIP newIp, NotificationChain msgs)
  {
    ComponentIP oldIp = ip;
    ip = newIp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QactorkPackage.CONTEXT__IP, oldIp, newIp);
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
  public void setIp(ComponentIP newIp)
  {
    if (newIp != ip)
    {
      NotificationChain msgs = null;
      if (ip != null)
        msgs = ((InternalEObject)ip).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.CONTEXT__IP, null, msgs);
      if (newIp != null)
        msgs = ((InternalEObject)newIp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.CONTEXT__IP, null, msgs);
      msgs = basicSetIp(newIp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.CONTEXT__IP, newIp, newIp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isMqtt()
  {
    return mqtt;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setMqtt(boolean newMqtt)
  {
    boolean oldMqtt = mqtt;
    mqtt = newMqtt;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.CONTEXT__MQTT, oldMqtt, mqtt));
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
      case QactorkPackage.CONTEXT__IP:
        return basicSetIp(null, msgs);
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
      case QactorkPackage.CONTEXT__NAME:
        return getName();
      case QactorkPackage.CONTEXT__IP:
        return getIp();
      case QactorkPackage.CONTEXT__MQTT:
        return isMqtt();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case QactorkPackage.CONTEXT__NAME:
        setName((String)newValue);
        return;
      case QactorkPackage.CONTEXT__IP:
        setIp((ComponentIP)newValue);
        return;
      case QactorkPackage.CONTEXT__MQTT:
        setMqtt((Boolean)newValue);
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
      case QactorkPackage.CONTEXT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case QactorkPackage.CONTEXT__IP:
        setIp((ComponentIP)null);
        return;
      case QactorkPackage.CONTEXT__MQTT:
        setMqtt(MQTT_EDEFAULT);
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
      case QactorkPackage.CONTEXT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case QactorkPackage.CONTEXT__IP:
        return ip != null;
      case QactorkPackage.CONTEXT__MQTT:
        return mqtt != MQTT_EDEFAULT;
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
    result.append(", mqtt: ");
    result.append(mqtt);
    result.append(')');
    return result.toString();
  }

} //ContextImpl