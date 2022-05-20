/**
 * generated by Xtext 2.16.0
 */
package it.unibo.ui.labeling;

import com.google.inject.Inject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

/**
 * Provides labels for EObjects.
 * 
 * See https://www.eclipse.org/Xtext/documentation/304_ide_concepts.html#label-provider
 */
@SuppressWarnings("all")
public class QactorkLabelProvider extends DefaultEObjectLabelProvider {
  @Inject
  public QactorkLabelProvider(final AdapterFactoryLabelProvider delegate) {
    super(delegate);
  }
}
