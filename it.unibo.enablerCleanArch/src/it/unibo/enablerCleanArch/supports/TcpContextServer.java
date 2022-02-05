package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArchapplHandlers.ContextMsgHandler;

/*
 * Decorator
 * TcpContextServer è un singleton che si crea un proprio gestore di messaggi di tipo ContextMsgHandler
 * E' un decorator di TcpServer che offre i metodi addComponent/removeComponent che delega al ContextMsgHandler
 * Il ContextMsgHandler gestisce messaggi della forma 'estesa':
 *   msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) e 
 *  ridirige al RECEIVER il CONTENT con la connessione
 *  il RECEIVER elabora il messaggio e invia un msg di risposta sulla connessione 
 *  per i messaggi che costituiscono richieste
 *  
 *  Il RECEIVER potrebbe ricevere il messaggio in forma estesa, 
 *  ma non sarebbe più quello usato nella versione precedente.
 *  
 *  TcpContextServer svolge un ruolo simile a CoapServer, 
 *  senza un concetto di naming delle risorse basato su path.
 */
public class TcpContextServer extends TcpServer{
	private ContextMsgHandler ctxMsgHandler;
 
	public TcpContextServer(String name, int port ) {  
		super(name, port,  new ContextMsgHandler("ctxH"));
		this.ctxMsgHandler = (ContextMsgHandler) userDefHandler;  //Inherited
 	}
	
	public void addComponent( String devname, IApplMsgHandler h) {
		ctxMsgHandler.addComponent(devname, h);
	}
	public void removeComponent( String devname ) {
		ctxMsgHandler.removeComponent( devname );
	}
 
}
