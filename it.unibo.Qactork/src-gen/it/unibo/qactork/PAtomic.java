/**
 * generated by Xtext 2.22.0
 */
package it.unibo.qactork;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>PAtomic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link it.unibo.qactork.PAtomic#getVal <em>Val</em>}</li>
 * </ul>
 *
 * @see it.unibo.qactork.QactorkPackage#getPAtomic()
 * @model
 * @generated
 */
public interface PAtomic extends PAtom
{
  /**
   * Returns the value of the '<em><b>Val</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Val</em>' attribute.
   * @see #setVal(String)
   * @see it.unibo.qactork.QactorkPackage#getPAtomic_Val()
   * @model
   * @generated
   */
  String getVal();

  /**
   * Sets the value of the '{@link it.unibo.qactork.PAtomic#getVal <em>Val</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Val</em>' attribute.
   * @see #getVal()
   * @generated
   */
  void setVal(String value);

} // PAtomic
