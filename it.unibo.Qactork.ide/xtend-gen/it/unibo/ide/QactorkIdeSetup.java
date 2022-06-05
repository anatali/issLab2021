/**
 * generated by Xtext 2.16.0
 */
package it.unibo.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import it.unibo.QactorkRuntimeModule;
import it.unibo.QactorkStandaloneSetup;
import it.unibo.ide.QactorkIdeModule;
import org.eclipse.xtext.util.Modules2;

/**
 * Initialization support for running Xtext languages as language servers.
 */
@SuppressWarnings("all")
public class QactorkIdeSetup extends QactorkStandaloneSetup {
  @Override
  public Injector createInjector() {
    QactorkRuntimeModule _qactorkRuntimeModule = new QactorkRuntimeModule();
    QactorkIdeModule _qactorkIdeModule = new QactorkIdeModule();
    return Guice.createInjector(Modules2.mixin(_qactorkRuntimeModule, _qactorkIdeModule));
  }
}
