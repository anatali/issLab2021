/**
 * generated by Xtext 2.16.0
 */
package it.unibo.generator;

import com.google.common.collect.Iterables;
import it.unibo.qactork.QActorSystem;
import it.unibo.qactork.generator.common.GenUtils;
import it.unibo.qactork.generator.common.SysKb;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.AbstractGenerator;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

/**
 * Generates code from your model files on save.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#code-generation
 */
@SuppressWarnings("all")
public class QactorkGenerator extends AbstractGenerator {
  @Override
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context) {
    URI _uRI = resource.getURI();
    String _plus = (" *** QactorGenerator resource.URI=" + _uRI);
    InputOutput.<String>println(_plus);
    String _fileExtension = resource.getURI().fileExtension();
    String _plus_1 = (" *** QactorGenerator resource.URI.fileExtension=" + _fileExtension);
    InputOutput.<String>println(_plus_1);
    String _property = System.getProperty("user.dir");
    String _plus_2 = (" *** user.dir " + _property);
    InputOutput.<String>println(_plus_2);
    String _property_1 = System.getProperty("user.home");
    String _plus_3 = (" *** user.home " + _property_1);
    InputOutput.<String>println(_plus_3);
    this.mygenerate(resource, fsa);
  }
  
  public void mygenerate(final Resource resource, final IFileSystemAccess fsa) {
    final GenQActorSystem genQActorSystem = new GenQActorSystem();
    final SysKb kb = new SysKb();
    GenUtils.setFsa(fsa);
    GenUtils.setFileExtension(resource.getURI().fileExtension());
    Iterable<QActorSystem> _filter = Iterables.<QActorSystem>filter(IteratorExtensions.<EObject>toIterable(resource.getAllContents()), QActorSystem.class);
    for (final QActorSystem system : _filter) {
      {
        kb.setDomainModel(system.getSpec());
        genQActorSystem.doGenerate(system, kb);
      }
    }
  }
}
