/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Duration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.Duration#getStore <em>Store</em>}</li>
 *   <li>{@link it.unibo.qactork.Duration#getStart <em>Start</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getDuration()
 * @model
 * @generated
 */
public interface Duration extends StateAction
{
  /**
   * Returns the value of the '<em><b>Store</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Store</em>' attribute.
   * @see #setStore(String)
   * @see it.unibo.qactork.QactorkPackage#getDuration_Store()
   * @model
   * @generated
   */
  String getStore();

  /**
   * Sets the value of the '{@link it.unibo.qactork.Duration#getStore <em>Store</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Store</em>' attribute.
   * @see #getStore()
   * @generated
   */
  void setStore(String value);

  /**
   * Returns the value of the '<em><b>Start</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Start</em>' attribute.
   * @see #setStart(String)
   * @see it.unibo.qactork.QactorkPackage#getDuration_Start()
   * @model
   * @generated
   */
  String getStart();

  /**
   * Sets the value of the '{@link it.unibo.qactork.Duration#getStart <em>Start</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Start</em>' attribute.
   * @see #getStart()
   * @generated
   */
  void setStart(String value);

} // Duration
