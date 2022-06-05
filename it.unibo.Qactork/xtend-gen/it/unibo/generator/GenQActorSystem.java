package it.unibo.generator;

import it.unibo.generator.GenQActor;
import it.unibo.generator.GenQActorCtxSystem;
import it.unibo.generator.GenSystemInfo;
import it.unibo.qactork.Context;
import it.unibo.qactork.QActorDeclaration;
import it.unibo.qactork.QActorSystem;
import it.unibo.qactork.QActorSystemSpec;
import it.unibo.qactork.generator.common.GenUtils;
import it.unibo.qactork.generator.common.SysKb;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class GenQActorSystem {
  private final GenQActorCtxSystem genQActorCtxSystem = new GenQActorCtxSystem();
  
  private final GenQActor genQActor = new GenQActor();
  
  private final GenSystemInfo genSystemInfo = new GenSystemInfo();
  
  private boolean tracing = false;
  
  private boolean msglogging = false;
  
  public void doGenerate(final QActorSystem system, final SysKb kb) {
    this.tracing = system.isTrace();
    this.msglogging = system.isLogmsg();
    this.doGenerate(system.getSpec(), kb);
  }
  
  public void doGenerate(final QActorSystemSpec system, final SysKb kb) {
    this.genSystemInfo.doGenerate(system, this.tracing, this.msglogging, kb);
    GenUtils.genFileDir("..", "", "settings", "gradle", this.genSettingsGradle(system.getName()));
    EList<Context> _context = system.getContext();
    for (final Context context : _context) {
      {
        GenUtils.setPackageName(context.getName());
        this.genQActorCtxSystem.doGenerate(system, context, kb);
      }
    }
    EList<QActorDeclaration> _actor = system.getActor();
    for (final QActorDeclaration actor : _actor) {
      {
        GenUtils.setPackageName(actor.getName());
        this.genQActor.doGenerate(actor, kb);
      }
    }
  }
  
  public CharSequence genSettingsGradle(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("rootProject.name = \"unibo.");
    _builder.append(name);
    _builder.append("\"");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
}
