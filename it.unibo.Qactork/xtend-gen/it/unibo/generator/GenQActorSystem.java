package it.unibo.generator;

import it.unibo.qactork.QActorSystem;
import it.unibo.qactork.QActorSystemSpec;
import it.unibo.qactork.generator.common.SysKb;

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
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field context is undefined for the type QActorSystemSpec"
      + "\nThe method or field actor is undefined for the type QActorSystemSpec"
      + "\nname cannot be resolved"
      + "\nname cannot be resolved");
  }
}
