/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reply Trans Switch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.ReplyTransSwitch#getMessage <em>Message</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getReplyTransSwitch()
 * @model
 * @generated
 */
public interface ReplyTransSwitch extends InputTransition
{
  /**
   * Returns the value of the '<em><b>Message</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Message</em>' reference.
   * @see #setMessage(Reply)
   * @see it.unibo.qactork.QactorkPackage#getReplyTransSwitch_Message()
   * @model
   * @generated
   */
  Reply getMessage();

  /**
   * Sets the value of the '{@link it.unibo.qactork.ReplyTransSwitch#getMessage <em>Message</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message</em>' reference.
   * @see #getMessage()
   * @generated
   */
  void setMessage(Reply value);

} // ReplyTransSwitch