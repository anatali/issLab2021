/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PStruct</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.PStruct#getFunctor <em>Functor</em>}</li>
 *   <li>{@link it.unibo.qactork.PStruct#getMsgArg <em>Msg Arg</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getPStruct()
 * @model
 * @generated
 */
public interface PStruct extends PHead
{
  /**
   * Returns the value of the '<em><b>Functor</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Functor</em>' attribute.
   * @see #setFunctor(String)
   * @see it.unibo.qactork.QactorkPackage#getPStruct_Functor()
   * @model
   * @generated
   */
  String getFunctor();

  /**
   * Sets the value of the '{@link it.unibo.qactork.PStruct#getFunctor <em>Functor</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Functor</em>' attribute.
   * @see #getFunctor()
   * @generated
   */
  void setFunctor(String value);

  /**
   * Returns the value of the '<em><b>Msg Arg</b></em>' containment reference list.
   * The list contents are of type {@link it.unibo.qactork.PHead}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Msg Arg</em>' containment reference list.
   * @see it.unibo.qactork.QactorkPackage#getPStruct_MsgArg()
   * @model containment="true"
   * @generated
   */
  EList<PHead> getMsgArg();

} // PStruct
