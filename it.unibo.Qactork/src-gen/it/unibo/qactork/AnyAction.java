/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Any Action</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.AnyAction#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getAnyAction()
 * @model
 * @generated
 */
public interface AnyAction extends StateAction
{
  /**
   * Returns the value of the '<em><b>Body</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' attribute.
   * @see #setBody(String)
   * @see it.unibo.qactork.QactorkPackage#getAnyAction_Body()
   * @model
   * @generated
   */
  String getBody();

  /**
   * Sets the value of the '{@link it.unibo.qactork.AnyAction#getBody <em>Body</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' attribute.
   * @see #getBody()
   * @generated
   */
  void setBody(String value);

} // AnyAction
