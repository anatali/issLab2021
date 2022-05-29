/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.MemoTime;
import it.unibo.qactork.QactorkPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Memo Time</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.impl.MemoTimeImpl#getStore <em>Store</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MemoTimeImpl extends StateActionImpl implements MemoTime
{
  /**
   * The default value of the '{@link #getStore() <em>Store</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStore()
   * @generated
   * @ordered
   */
  protected static final String STORE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStore() <em>Store</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStore()
   * @generated
   * @ordered
   */
  protected String store = STORE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MemoTimeImpl()
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
    return QactorkPackage.Literals.MEMO_TIME;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getStore()
  {
    return store;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setStore(String newStore)
  {
    String oldStore = store;
    store = newStore;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.MEMO_TIME__STORE, oldStore, store));
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
      case QactorkPackage.MEMO_TIME__STORE:
        return getStore();
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
      case QactorkPackage.MEMO_TIME__STORE:
        setStore((String)newValue);
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
      case QactorkPackage.MEMO_TIME__STORE:
        setStore(STORE_EDEFAULT);
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
      case QactorkPackage.MEMO_TIME__STORE:
        return STORE_EDEFAULT == null ? store != null : !STORE_EDEFAULT.equals(store);
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
    result.append(" (store: ");
    result.append(store);
    result.append(')');
    return result.toString();
  }

} //MemoTimeImpl