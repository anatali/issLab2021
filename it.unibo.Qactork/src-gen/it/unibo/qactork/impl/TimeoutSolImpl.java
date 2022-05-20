/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork.impl;

import it.unibo.qactork.QactorkPackage;
import it.unibo.qactork.TimeoutSol;
import it.unibo.qactork.VarSolRef;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Timeout Sol</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.impl.TimeoutSolImpl#getRefsoltime <em>Refsoltime</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TimeoutSolImpl extends TimeoutImpl implements TimeoutSol
{
  /**
   * The cached value of the '{@link #getRefsoltime() <em>Refsoltime</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRefsoltime()
   * @generated
   * @ordered
   */
  protected VarSolRef refsoltime;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TimeoutSolImpl()
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
    return QactorkPackage.Literals.TIMEOUT_SOL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public VarSolRef getRefsoltime()
  {
    return refsoltime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetRefsoltime(VarSolRef newRefsoltime, NotificationChain msgs)
  {
    VarSolRef oldRefsoltime = refsoltime;
    refsoltime = newRefsoltime;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QactorkPackage.TIMEOUT_SOL__REFSOLTIME, oldRefsoltime, newRefsoltime);
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
  public void setRefsoltime(VarSolRef newRefsoltime)
  {
    if (newRefsoltime != refsoltime)
    {
      NotificationChain msgs = null;
      if (refsoltime != null)
        msgs = ((InternalEObject)refsoltime).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.TIMEOUT_SOL__REFSOLTIME, null, msgs);
      if (newRefsoltime != null)
        msgs = ((InternalEObject)newRefsoltime).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QactorkPackage.TIMEOUT_SOL__REFSOLTIME, null, msgs);
      msgs = basicSetRefsoltime(newRefsoltime, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, QactorkPackage.TIMEOUT_SOL__REFSOLTIME, newRefsoltime, newRefsoltime));
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
      case QactorkPackage.TIMEOUT_SOL__REFSOLTIME:
        return basicSetRefsoltime(null, msgs);
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
      case QactorkPackage.TIMEOUT_SOL__REFSOLTIME:
        return getRefsoltime();
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
      case QactorkPackage.TIMEOUT_SOL__REFSOLTIME:
        setRefsoltime((VarSolRef)newValue);
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
      case QactorkPackage.TIMEOUT_SOL__REFSOLTIME:
        setRefsoltime((VarSolRef)null);
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
      case QactorkPackage.TIMEOUT_SOL__REFSOLTIME:
        return refsoltime != null;
    }
    return super.eIsSet(featureID);
  }

} //TimeoutSolImpl
