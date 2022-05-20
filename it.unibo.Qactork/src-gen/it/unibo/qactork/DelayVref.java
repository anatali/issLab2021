/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Delay Vref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.DelayVref#getReftime <em>Reftime</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getDelayVref()
 * @model
 * @generated
 */
public interface DelayVref extends Delay
{
  /**
   * Returns the value of the '<em><b>Reftime</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reftime</em>' containment reference.
   * @see #setReftime(VarRef)
   * @see it.unibo.qactork.QactorkPackage#getDelayVref_Reftime()
   * @model containment="true"
   * @generated
   */
  VarRef getReftime();

  /**
   * Sets the value of the '{@link it.unibo.qactork.DelayVref#getReftime <em>Reftime</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Reftime</em>' containment reference.
   * @see #getReftime()
   * @generated
   */
  void setReftime(VarRef value);

} // DelayVref
