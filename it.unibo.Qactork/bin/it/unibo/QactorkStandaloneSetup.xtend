/*
 * generated by Xtext 2.16.0
 */
package it.unibo


/**
 * Initialization support for running Xtext languages without Equinox extension registry.
 */
class QactorkStandaloneSetup extends QactorkStandaloneSetupGenerated {

	def static void doSetup() {
		new QactorkStandaloneSetup().createInjectorAndDoEMFRegistration()
	}
}
