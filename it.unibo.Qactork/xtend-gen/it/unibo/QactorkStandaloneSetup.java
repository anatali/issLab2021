/**
 * generated by Xtext 2.16.0
 */
package it.unibo;

import it.unibo.QactorkStandaloneSetupGenerated;

/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
@SuppressWarnings("all")
public class QactorkStandaloneSetup extends QactorkStandaloneSetupGenerated {
  public static void doSetup() {
    new QactorkStandaloneSetup().createInjectorAndDoEMFRegistration();
  }
}
