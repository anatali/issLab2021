/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reply Req</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.ReplyReq#getReqref <em>Reqref</em>}</li>
 *   <li>{@link it.unibo.qactork.ReplyReq#getMsgref <em>Msgref</em>}</li>
 *   <li>{@link it.unibo.qactork.ReplyReq#getVal <em>Val</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getReplyReq()
 * @model
 * @generated
 */
public interface ReplyReq extends StateAction
{
  /**
   * Returns the value of the '<em><b>Reqref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Reqref</em>' reference.
   * @see #setReqref(Request)
   * @see it.unibo.qactork.QactorkPackage#getReplyReq_Reqref()
   * @model
   * @generated
   */
  Request getReqref();

  /**
   * Sets the value of the '{@link it.unibo.qactork.ReplyReq#getReqref <em>Reqref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Reqref</em>' reference.
   * @see #getReqref()
   * @generated
   */
  void setReqref(Request value);

  /**
   * Returns the value of the '<em><b>Msgref</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Msgref</em>' reference.
   * @see #setMsgref(Request)
   * @see it.unibo.qactork.QactorkPackage#getReplyReq_Msgref()
   * @model
   * @generated
   */
  Request getMsgref();

  /**
   * Sets the value of the '{@link it.unibo.qactork.ReplyReq#getMsgref <em>Msgref</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Msgref</em>' reference.
   * @see #getMsgref()
   * @generated
   */
  void setMsgref(Request value);

  /**
   * Returns the value of the '<em><b>Val</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Val</em>' containment reference.
   * @see #setVal(PHead)
   * @see it.unibo.qactork.QactorkPackage#getReplyReq_Val()
   * @model containment="true"
   * @generated
   */
  PHead getVal();

  /**
   * Sets the value of the '{@link it.unibo.qactork.ReplyReq#getVal <em>Val</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Val</em>' containment reference.
   * @see #getVal()
   * @generated
   */
  void setVal(PHead value);

} // ReplyReq
