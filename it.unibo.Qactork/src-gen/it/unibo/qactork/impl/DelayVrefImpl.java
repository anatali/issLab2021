/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.DelayVref;
import it.unibo.qactork.QactorkPackage;
import it.unibo.qactork.VarRef;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Delay Vref</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.impl.DelayVrefImpl#getReftime <em>Reftime</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DelayVrefImpl extends DelayImpl implements DelayVref
{
  /**
   * The cached value of the '{@link #getReftime() <em>Reftime</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReftime()
   * @generated
   * @ordered
   */
  protected VarRef reftime;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DelayVrefImpl()
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
    return QactorkPackage.Literals.DELAY_VREF;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarRef getReftime()
  {
    return reftime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetReftime(VarRef newReftime, NotificationChain msgs)
  {
    VarRef oldReftime = reftime;
    reftime = newReftime;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QactorkPackage.DELAY_VREF__REFTIME, oldReftime, newReftime);
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
  public void setReftime(VarRef newReftime)
  {
    if (newReftime != reftime)
    {
      NotificationChain msgs = null;
      if (reftime != null)
        msgs = ((InternalEObject)reftime).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.DELAY_VREF__REFTIME, null, msgs);
      if (newReftime != null)
        msgs = ((InternalEObject)newReftime).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.DELAY_VREF__REFTIME, null, msgs);
      msgs = basicSetReftime(newReftime, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.DELAY_VREF__REFTIME, newReftime, newReftime));
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
      case QactorkPackage.DELAY_VREF__REFTIME:
        return basicSetReftime(null, msgs);
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
      case QactorkPackage.DELAY_VREF__REFTIME:
        return getReftime();
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
      case QactorkPackage.DELAY_VREF__REFTIME:
        setReftime((VarRef)newValue);
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
      case QactorkPackage.DELAY_VREF__REFTIME:
        setReftime((VarRef)null);
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
      case QactorkPackage.DELAY_VREF__REFTIME:
        return reftime != null;
    }
    return super.eIsSet(featureID);
  }

} //DelayVrefImpl
