/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.ComponentIP;
import it.unibo.qactork.QactorkPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component IP</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.impl.ComponentIPImpl#getHost <em>Host</em>}</li>
 *   <li>{@link it.unibo.qactork.impl.ComponentIPImpl#getPort <em>Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentIPImpl extends MinimalEObjectImpl.Container implements ComponentIP
{
  /**
   * The default value of the '{@link #getHost() <em>Host</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHost()
   * @generated
   * @ordered
   */
  protected static final String HOST_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHost() <em>Host</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHost()
   * @generated
   * @ordered
   */
  protected String host = HOST_EDEFAULT;

  /**
   * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected static final int PORT_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected int port = PORT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComponentIPImpl()
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
    return QactorkPackage.Literals.COMPONENT_IP;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getHost()
  {
    return host;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setHost(String newHost)
  {
    String oldHost = host;
    host = newHost;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.COMPONENT_IP__HOST, oldHost, host));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getPort()
  {
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setPort(int newPort)
  {
    int oldPort = port;
    port = newPort;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.COMPONENT_IP__PORT, oldPort, port));
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
      case QactorkPackage.COMPONENT_IP__HOST:
        return getHost();
      case QactorkPackage.COMPONENT_IP__PORT:
        return getPort();
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
      case QactorkPackage.COMPONENT_IP__HOST:
        setHost((String)newValue);
        return;
      case QactorkPackage.COMPONENT_IP__PORT:
        setPort((Integer)newValue);
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
      case QactorkPackage.COMPONENT_IP__HOST:
        setHost(HOST_EDEFAULT);
        return;
      case QactorkPackage.COMPONENT_IP__PORT:
        setPort(PORT_EDEFAULT);
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
      case QactorkPackage.COMPONENT_IP__HOST:
        return HOST_EDEFAULT == null ? host != null : !HOST_EDEFAULT.equals(host);
      case QactorkPackage.COMPONENT_IP__PORT:
        return port != PORT_EDEFAULT;
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
    result.append(" (host: ");
    result.append(host);
    result.append(", port: ");
    result.append(port);
    result.append(')');
    return result.toString();
  }

} //ComponentIPImpl
