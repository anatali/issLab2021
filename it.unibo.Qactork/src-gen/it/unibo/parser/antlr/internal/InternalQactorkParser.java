package it.unibo.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import it.unibo.services.QactorkGrammarAccess;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalQactorkParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_STRING", "RULE_INT", "RULE_VARID", "RULE_KCODE", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'System'", "'-trace'", "'-msglog'", "'.'", "'mqttBroker'", "':'", "'eventTopic'", "'Event'", "'Signal'", "'Token'", "'Dispatch'", "'Request'", "'Reply'", "'Invitation'", "'Context'", "'ip'", "'+mqtt'", "'['", "'host='", "'port='", "']'", "'ExternalQActor'", "'context'", "'CodedQActor'", "'className'", "'QActor'", "'{'", "'}'", "'State'", "'initial'", "'ifSolved'", "'else'", "'if'", "'printCurrentMessage'", "'println'", "'('", "')'", "'solve'", "','", "'discardMsg'", "'On'", "'Off'", "'memoCurrentTime'", "'setDuration'", "'from'", "'forward'", "'-m'", "'emit'", "'request'", "'replyTo'", "'with'", "'askFor'", "'delay'", "'delayVar'", "'delayVarRef'", "'delaySol'", "'onMsg'", "'terminate'", "'updateResource'", "'qrun'", "'myself'", "'run'", "'machineExec'", "'Goto'", "'Transition'", "'whenTime'", "'->'", "'whenTimeVar'", "'whenTimeVarRef'", "'whenTimeSol'", "'whenEvent'", "'and'", "'whenMsg'", "'whenRequest'", "'whenReply'", "'$'", "'#'", "'@'"
    };
    public static final int T__50=50;
    public static final int T__90=90;
    public static final int T__19=19;
    public static final int T__15=15;
    public static final int T__59=59;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__13=13;
    public static final int T__57=57;
    public static final int T__14=14;
    public static final int T__58=58;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int RULE_ID=4;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int RULE_INT=6;
    public static final int RULE_KCODE=8;
    public static final int T__29=29;
    public static final int T__22=22;
    public static final int T__66=66;
    public static final int RULE_ML_COMMENT=9;
    public static final int T__23=23;
    public static final int T__67=67;
    public static final int T__24=24;
    public static final int T__68=68;
    public static final int T__25=25;
    public static final int T__69=69;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__20=20;
    public static final int T__64=64;
    public static final int T__21=21;
    public static final int T__65=65;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int RULE_VARID=7;
    public static final int T__72=72;
    public static final int RULE_STRING=5;
    public static final int RULE_SL_COMMENT=10;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__33=33;
    public static final int T__77=77;
    public static final int T__34=34;
    public static final int T__78=78;
    public static final int T__35=35;
    public static final int T__79=79;
    public static final int T__36=36;
    public static final int T__73=73;
    public static final int EOF=-1;
    public static final int T__30=30;
    public static final int T__74=74;
    public static final int T__31=31;
    public static final int T__75=75;
    public static final int T__32=32;
    public static final int T__76=76;
    public static final int T__80=80;
    public static final int T__81=81;
    public static final int T__82=82;
    public static final int T__83=83;
    public static final int RULE_WS=11;
    public static final int RULE_ANY_OTHER=12;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__44=44;
    public static final int T__88=88;
    public static final int T__45=45;
    public static final int T__89=89;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__40=40;
    public static final int T__84=84;
    public static final int T__41=41;
    public static final int T__85=85;
    public static final int T__42=42;
    public static final int T__86=86;
    public static final int T__43=43;
    public static final int T__87=87;

    // delegates
    // delegators


        public InternalQactorkParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalQactorkParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalQactorkParser.tokenNames; }
    public String getGrammarFileName() { return "InternalQactork.g"; }



     	private QactorkGrammarAccess grammarAccess;

        public InternalQactorkParser(TokenStream input, QactorkGrammarAccess grammarAccess) {
            this(input);
            this.grammarAccess = grammarAccess;
            registerRules(grammarAccess.getGrammar());
        }

        @Override
        protected String getFirstRuleName() {
        	return "QActorSystem";
       	}

       	@Override
       	protected QactorkGrammarAccess getGrammarAccess() {
       		return grammarAccess;
       	}




    // $ANTLR start "entryRuleQActorSystem"
    // InternalQactork.g:64:1: entryRuleQActorSystem returns [EObject current=null] : iv_ruleQActorSystem= ruleQActorSystem EOF ;
    public final EObject entryRuleQActorSystem() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQActorSystem = null;


        try {
            // InternalQactork.g:64:53: (iv_ruleQActorSystem= ruleQActorSystem EOF )
            // InternalQactork.g:65:2: iv_ruleQActorSystem= ruleQActorSystem EOF
            {
             newCompositeNode(grammarAccess.getQActorSystemRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQActorSystem=ruleQActorSystem();

            state._fsp--;

             current =iv_ruleQActorSystem; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQActorSystem"


    // $ANTLR start "ruleQActorSystem"
    // InternalQactork.g:71:1: ruleQActorSystem returns [EObject current=null] : (otherlv_0= 'System' ( (lv_trace_1_0= '-trace' ) )? ( (lv_logmsg_2_0= '-msglog' ) )? ( (lv_spec_3_0= ruleQActorSystemSpec ) ) ) ;
    public final EObject ruleQActorSystem() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_trace_1_0=null;
        Token lv_logmsg_2_0=null;
        EObject lv_spec_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:77:2: ( (otherlv_0= 'System' ( (lv_trace_1_0= '-trace' ) )? ( (lv_logmsg_2_0= '-msglog' ) )? ( (lv_spec_3_0= ruleQActorSystemSpec ) ) ) )
            // InternalQactork.g:78:2: (otherlv_0= 'System' ( (lv_trace_1_0= '-trace' ) )? ( (lv_logmsg_2_0= '-msglog' ) )? ( (lv_spec_3_0= ruleQActorSystemSpec ) ) )
            {
            // InternalQactork.g:78:2: (otherlv_0= 'System' ( (lv_trace_1_0= '-trace' ) )? ( (lv_logmsg_2_0= '-msglog' ) )? ( (lv_spec_3_0= ruleQActorSystemSpec ) ) )
            // InternalQactork.g:79:3: otherlv_0= 'System' ( (lv_trace_1_0= '-trace' ) )? ( (lv_logmsg_2_0= '-msglog' ) )? ( (lv_spec_3_0= ruleQActorSystemSpec ) )
            {
            otherlv_0=(Token)match(input,13,FOLLOW_3); 

            			newLeafNode(otherlv_0, grammarAccess.getQActorSystemAccess().getSystemKeyword_0());
            		
            // InternalQactork.g:83:3: ( (lv_trace_1_0= '-trace' ) )?
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==14) ) {
                alt1=1;
            }
            switch (alt1) {
                case 1 :
                    // InternalQactork.g:84:4: (lv_trace_1_0= '-trace' )
                    {
                    // InternalQactork.g:84:4: (lv_trace_1_0= '-trace' )
                    // InternalQactork.g:85:5: lv_trace_1_0= '-trace'
                    {
                    lv_trace_1_0=(Token)match(input,14,FOLLOW_3); 

                    					newLeafNode(lv_trace_1_0, grammarAccess.getQActorSystemAccess().getTraceTraceKeyword_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getQActorSystemRule());
                    					}
                    					setWithLastConsumed(current, "trace", lv_trace_1_0 != null, "-trace");
                    				

                    }


                    }
                    break;

            }

            // InternalQactork.g:97:3: ( (lv_logmsg_2_0= '-msglog' ) )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==15) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // InternalQactork.g:98:4: (lv_logmsg_2_0= '-msglog' )
                    {
                    // InternalQactork.g:98:4: (lv_logmsg_2_0= '-msglog' )
                    // InternalQactork.g:99:5: lv_logmsg_2_0= '-msglog'
                    {
                    lv_logmsg_2_0=(Token)match(input,15,FOLLOW_3); 

                    					newLeafNode(lv_logmsg_2_0, grammarAccess.getQActorSystemAccess().getLogmsgMsglogKeyword_2_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getQActorSystemRule());
                    					}
                    					setWithLastConsumed(current, "logmsg", lv_logmsg_2_0 != null, "-msglog");
                    				

                    }


                    }
                    break;

            }

            // InternalQactork.g:111:3: ( (lv_spec_3_0= ruleQActorSystemSpec ) )
            // InternalQactork.g:112:4: (lv_spec_3_0= ruleQActorSystemSpec )
            {
            // InternalQactork.g:112:4: (lv_spec_3_0= ruleQActorSystemSpec )
            // InternalQactork.g:113:5: lv_spec_3_0= ruleQActorSystemSpec
            {

            					newCompositeNode(grammarAccess.getQActorSystemAccess().getSpecQActorSystemSpecParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_spec_3_0=ruleQActorSystemSpec();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getQActorSystemRule());
            					}
            					set(
            						current,
            						"spec",
            						lv_spec_3_0,
            						"it.unibo.Qactork.QActorSystemSpec");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQActorSystem"


    // $ANTLR start "entryRuleQualifiedName"
    // InternalQactork.g:134:1: entryRuleQualifiedName returns [String current=null] : iv_ruleQualifiedName= ruleQualifiedName EOF ;
    public final String entryRuleQualifiedName() throws RecognitionException {
        String current = null;

        AntlrDatatypeRuleToken iv_ruleQualifiedName = null;


        try {
            // InternalQactork.g:134:53: (iv_ruleQualifiedName= ruleQualifiedName EOF )
            // InternalQactork.g:135:2: iv_ruleQualifiedName= ruleQualifiedName EOF
            {
             newCompositeNode(grammarAccess.getQualifiedNameRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQualifiedName=ruleQualifiedName();

            state._fsp--;

             current =iv_ruleQualifiedName.getText(); 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQualifiedName"


    // $ANTLR start "ruleQualifiedName"
    // InternalQactork.g:141:1: ruleQualifiedName returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] : (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) ;
    public final AntlrDatatypeRuleToken ruleQualifiedName() throws RecognitionException {
        AntlrDatatypeRuleToken current = new AntlrDatatypeRuleToken();

        Token this_ID_0=null;
        Token kw=null;
        Token this_ID_2=null;


        	enterRule();

        try {
            // InternalQactork.g:147:2: ( (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* ) )
            // InternalQactork.g:148:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            {
            // InternalQactork.g:148:2: (this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )* )
            // InternalQactork.g:149:3: this_ID_0= RULE_ID (kw= '.' this_ID_2= RULE_ID )*
            {
            this_ID_0=(Token)match(input,RULE_ID,FOLLOW_4); 

            			current.merge(this_ID_0);
            		

            			newLeafNode(this_ID_0, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_0());
            		
            // InternalQactork.g:156:3: (kw= '.' this_ID_2= RULE_ID )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==16) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalQactork.g:157:4: kw= '.' this_ID_2= RULE_ID
            	    {
            	    kw=(Token)match(input,16,FOLLOW_5); 

            	    				current.merge(kw);
            	    				newLeafNode(kw, grammarAccess.getQualifiedNameAccess().getFullStopKeyword_1_0());
            	    			
            	    this_ID_2=(Token)match(input,RULE_ID,FOLLOW_4); 

            	    				current.merge(this_ID_2);
            	    			

            	    				newLeafNode(this_ID_2, grammarAccess.getQualifiedNameAccess().getIDTerminalRuleCall_1_1());
            	    			

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQualifiedName"


    // $ANTLR start "entryRuleQActorSystemSpec"
    // InternalQactork.g:174:1: entryRuleQActorSystemSpec returns [EObject current=null] : iv_ruleQActorSystemSpec= ruleQActorSystemSpec EOF ;
    public final EObject entryRuleQActorSystemSpec() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQActorSystemSpec = null;


        try {
            // InternalQactork.g:174:57: (iv_ruleQActorSystemSpec= ruleQActorSystemSpec EOF )
            // InternalQactork.g:175:2: iv_ruleQActorSystemSpec= ruleQActorSystemSpec EOF
            {
             newCompositeNode(grammarAccess.getQActorSystemSpecRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQActorSystemSpec=ruleQActorSystemSpec();

            state._fsp--;

             current =iv_ruleQActorSystemSpec; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQActorSystemSpec"


    // $ANTLR start "ruleQActorSystemSpec"
    // InternalQactork.g:181:1: ruleQActorSystemSpec returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_mqttBroker_1_0= ruleBrokerSpec ) )? ( (lv_message_2_0= ruleMessage ) )* ( (lv_context_3_0= ruleContext ) )* ( (lv_actor_4_0= ruleQActorDeclaration ) )* ) ;
    public final EObject ruleQActorSystemSpec() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        EObject lv_mqttBroker_1_0 = null;

        EObject lv_message_2_0 = null;

        EObject lv_context_3_0 = null;

        EObject lv_actor_4_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:187:2: ( ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_mqttBroker_1_0= ruleBrokerSpec ) )? ( (lv_message_2_0= ruleMessage ) )* ( (lv_context_3_0= ruleContext ) )* ( (lv_actor_4_0= ruleQActorDeclaration ) )* ) )
            // InternalQactork.g:188:2: ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_mqttBroker_1_0= ruleBrokerSpec ) )? ( (lv_message_2_0= ruleMessage ) )* ( (lv_context_3_0= ruleContext ) )* ( (lv_actor_4_0= ruleQActorDeclaration ) )* )
            {
            // InternalQactork.g:188:2: ( ( (lv_name_0_0= RULE_ID ) ) ( (lv_mqttBroker_1_0= ruleBrokerSpec ) )? ( (lv_message_2_0= ruleMessage ) )* ( (lv_context_3_0= ruleContext ) )* ( (lv_actor_4_0= ruleQActorDeclaration ) )* )
            // InternalQactork.g:189:3: ( (lv_name_0_0= RULE_ID ) ) ( (lv_mqttBroker_1_0= ruleBrokerSpec ) )? ( (lv_message_2_0= ruleMessage ) )* ( (lv_context_3_0= ruleContext ) )* ( (lv_actor_4_0= ruleQActorDeclaration ) )*
            {
            // InternalQactork.g:189:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalQactork.g:190:4: (lv_name_0_0= RULE_ID )
            {
            // InternalQactork.g:190:4: (lv_name_0_0= RULE_ID )
            // InternalQactork.g:191:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_6); 

            					newLeafNode(lv_name_0_0, grammarAccess.getQActorSystemSpecAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorSystemSpecRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalQactork.g:207:3: ( (lv_mqttBroker_1_0= ruleBrokerSpec ) )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==17) ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // InternalQactork.g:208:4: (lv_mqttBroker_1_0= ruleBrokerSpec )
                    {
                    // InternalQactork.g:208:4: (lv_mqttBroker_1_0= ruleBrokerSpec )
                    // InternalQactork.g:209:5: lv_mqttBroker_1_0= ruleBrokerSpec
                    {

                    					newCompositeNode(grammarAccess.getQActorSystemSpecAccess().getMqttBrokerBrokerSpecParserRuleCall_1_0());
                    				
                    pushFollow(FOLLOW_7);
                    lv_mqttBroker_1_0=ruleBrokerSpec();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getQActorSystemSpecRule());
                    					}
                    					set(
                    						current,
                    						"mqttBroker",
                    						lv_mqttBroker_1_0,
                    						"it.unibo.Qactork.BrokerSpec");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalQactork.g:226:3: ( (lv_message_2_0= ruleMessage ) )*
            loop5:
            do {
                int alt5=2;
                int LA5_0 = input.LA(1);

                if ( ((LA5_0>=20 && LA5_0<=26)) ) {
                    alt5=1;
                }


                switch (alt5) {
            	case 1 :
            	    // InternalQactork.g:227:4: (lv_message_2_0= ruleMessage )
            	    {
            	    // InternalQactork.g:227:4: (lv_message_2_0= ruleMessage )
            	    // InternalQactork.g:228:5: lv_message_2_0= ruleMessage
            	    {

            	    					newCompositeNode(grammarAccess.getQActorSystemSpecAccess().getMessageMessageParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_7);
            	    lv_message_2_0=ruleMessage();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getQActorSystemSpecRule());
            	    					}
            	    					add(
            	    						current,
            	    						"message",
            	    						lv_message_2_0,
            	    						"it.unibo.Qactork.Message");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop5;
                }
            } while (true);

            // InternalQactork.g:245:3: ( (lv_context_3_0= ruleContext ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( (LA6_0==27) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // InternalQactork.g:246:4: (lv_context_3_0= ruleContext )
            	    {
            	    // InternalQactork.g:246:4: (lv_context_3_0= ruleContext )
            	    // InternalQactork.g:247:5: lv_context_3_0= ruleContext
            	    {

            	    					newCompositeNode(grammarAccess.getQActorSystemSpecAccess().getContextContextParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_8);
            	    lv_context_3_0=ruleContext();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getQActorSystemSpecRule());
            	    					}
            	    					add(
            	    						current,
            	    						"context",
            	    						lv_context_3_0,
            	    						"it.unibo.Qactork.Context");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);

            // InternalQactork.g:264:3: ( (lv_actor_4_0= ruleQActorDeclaration ) )*
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0==34||LA7_0==36||LA7_0==38) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // InternalQactork.g:265:4: (lv_actor_4_0= ruleQActorDeclaration )
            	    {
            	    // InternalQactork.g:265:4: (lv_actor_4_0= ruleQActorDeclaration )
            	    // InternalQactork.g:266:5: lv_actor_4_0= ruleQActorDeclaration
            	    {

            	    					newCompositeNode(grammarAccess.getQActorSystemSpecAccess().getActorQActorDeclarationParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_9);
            	    lv_actor_4_0=ruleQActorDeclaration();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getQActorSystemSpecRule());
            	    					}
            	    					add(
            	    						current,
            	    						"actor",
            	    						lv_actor_4_0,
            	    						"it.unibo.Qactork.QActorDeclaration");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop7;
                }
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQActorSystemSpec"


    // $ANTLR start "entryRuleBrokerSpec"
    // InternalQactork.g:287:1: entryRuleBrokerSpec returns [EObject current=null] : iv_ruleBrokerSpec= ruleBrokerSpec EOF ;
    public final EObject entryRuleBrokerSpec() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleBrokerSpec = null;


        try {
            // InternalQactork.g:287:51: (iv_ruleBrokerSpec= ruleBrokerSpec EOF )
            // InternalQactork.g:288:2: iv_ruleBrokerSpec= ruleBrokerSpec EOF
            {
             newCompositeNode(grammarAccess.getBrokerSpecRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleBrokerSpec=ruleBrokerSpec();

            state._fsp--;

             current =iv_ruleBrokerSpec; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleBrokerSpec"


    // $ANTLR start "ruleBrokerSpec"
    // InternalQactork.g:294:1: ruleBrokerSpec returns [EObject current=null] : (otherlv_0= 'mqttBroker' ( (lv_brokerHost_1_0= RULE_STRING ) ) otherlv_2= ':' ( (lv_brokerPort_3_0= RULE_INT ) ) otherlv_4= 'eventTopic' ( (lv_eventtopic_5_0= RULE_STRING ) ) ) ;
    public final EObject ruleBrokerSpec() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_brokerHost_1_0=null;
        Token otherlv_2=null;
        Token lv_brokerPort_3_0=null;
        Token otherlv_4=null;
        Token lv_eventtopic_5_0=null;


        	enterRule();

        try {
            // InternalQactork.g:300:2: ( (otherlv_0= 'mqttBroker' ( (lv_brokerHost_1_0= RULE_STRING ) ) otherlv_2= ':' ( (lv_brokerPort_3_0= RULE_INT ) ) otherlv_4= 'eventTopic' ( (lv_eventtopic_5_0= RULE_STRING ) ) ) )
            // InternalQactork.g:301:2: (otherlv_0= 'mqttBroker' ( (lv_brokerHost_1_0= RULE_STRING ) ) otherlv_2= ':' ( (lv_brokerPort_3_0= RULE_INT ) ) otherlv_4= 'eventTopic' ( (lv_eventtopic_5_0= RULE_STRING ) ) )
            {
            // InternalQactork.g:301:2: (otherlv_0= 'mqttBroker' ( (lv_brokerHost_1_0= RULE_STRING ) ) otherlv_2= ':' ( (lv_brokerPort_3_0= RULE_INT ) ) otherlv_4= 'eventTopic' ( (lv_eventtopic_5_0= RULE_STRING ) ) )
            // InternalQactork.g:302:3: otherlv_0= 'mqttBroker' ( (lv_brokerHost_1_0= RULE_STRING ) ) otherlv_2= ':' ( (lv_brokerPort_3_0= RULE_INT ) ) otherlv_4= 'eventTopic' ( (lv_eventtopic_5_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,17,FOLLOW_10); 

            			newLeafNode(otherlv_0, grammarAccess.getBrokerSpecAccess().getMqttBrokerKeyword_0());
            		
            // InternalQactork.g:306:3: ( (lv_brokerHost_1_0= RULE_STRING ) )
            // InternalQactork.g:307:4: (lv_brokerHost_1_0= RULE_STRING )
            {
            // InternalQactork.g:307:4: (lv_brokerHost_1_0= RULE_STRING )
            // InternalQactork.g:308:5: lv_brokerHost_1_0= RULE_STRING
            {
            lv_brokerHost_1_0=(Token)match(input,RULE_STRING,FOLLOW_11); 

            					newLeafNode(lv_brokerHost_1_0, grammarAccess.getBrokerSpecAccess().getBrokerHostSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBrokerSpecRule());
            					}
            					setWithLastConsumed(
            						current,
            						"brokerHost",
            						lv_brokerHost_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_12); 

            			newLeafNode(otherlv_2, grammarAccess.getBrokerSpecAccess().getColonKeyword_2());
            		
            // InternalQactork.g:328:3: ( (lv_brokerPort_3_0= RULE_INT ) )
            // InternalQactork.g:329:4: (lv_brokerPort_3_0= RULE_INT )
            {
            // InternalQactork.g:329:4: (lv_brokerPort_3_0= RULE_INT )
            // InternalQactork.g:330:5: lv_brokerPort_3_0= RULE_INT
            {
            lv_brokerPort_3_0=(Token)match(input,RULE_INT,FOLLOW_13); 

            					newLeafNode(lv_brokerPort_3_0, grammarAccess.getBrokerSpecAccess().getBrokerPortINTTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBrokerSpecRule());
            					}
            					setWithLastConsumed(
            						current,
            						"brokerPort",
            						lv_brokerPort_3_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            otherlv_4=(Token)match(input,19,FOLLOW_10); 

            			newLeafNode(otherlv_4, grammarAccess.getBrokerSpecAccess().getEventTopicKeyword_4());
            		
            // InternalQactork.g:350:3: ( (lv_eventtopic_5_0= RULE_STRING ) )
            // InternalQactork.g:351:4: (lv_eventtopic_5_0= RULE_STRING )
            {
            // InternalQactork.g:351:4: (lv_eventtopic_5_0= RULE_STRING )
            // InternalQactork.g:352:5: lv_eventtopic_5_0= RULE_STRING
            {
            lv_eventtopic_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_eventtopic_5_0, grammarAccess.getBrokerSpecAccess().getEventtopicSTRINGTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getBrokerSpecRule());
            					}
            					setWithLastConsumed(
            						current,
            						"eventtopic",
            						lv_eventtopic_5_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleBrokerSpec"


    // $ANTLR start "entryRuleMessage"
    // InternalQactork.g:372:1: entryRuleMessage returns [EObject current=null] : iv_ruleMessage= ruleMessage EOF ;
    public final EObject entryRuleMessage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMessage = null;


        try {
            // InternalQactork.g:372:48: (iv_ruleMessage= ruleMessage EOF )
            // InternalQactork.g:373:2: iv_ruleMessage= ruleMessage EOF
            {
             newCompositeNode(grammarAccess.getMessageRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMessage=ruleMessage();

            state._fsp--;

             current =iv_ruleMessage; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMessage"


    // $ANTLR start "ruleMessage"
    // InternalQactork.g:379:1: ruleMessage returns [EObject current=null] : (this_OutOnlyMessage_0= ruleOutOnlyMessage | this_OutInMessage_1= ruleOutInMessage ) ;
    public final EObject ruleMessage() throws RecognitionException {
        EObject current = null;

        EObject this_OutOnlyMessage_0 = null;

        EObject this_OutInMessage_1 = null;



        	enterRule();

        try {
            // InternalQactork.g:385:2: ( (this_OutOnlyMessage_0= ruleOutOnlyMessage | this_OutInMessage_1= ruleOutInMessage ) )
            // InternalQactork.g:386:2: (this_OutOnlyMessage_0= ruleOutOnlyMessage | this_OutInMessage_1= ruleOutInMessage )
            {
            // InternalQactork.g:386:2: (this_OutOnlyMessage_0= ruleOutOnlyMessage | this_OutInMessage_1= ruleOutInMessage )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( ((LA8_0>=20 && LA8_0<=23)) ) {
                alt8=1;
            }
            else if ( ((LA8_0>=24 && LA8_0<=26)) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // InternalQactork.g:387:3: this_OutOnlyMessage_0= ruleOutOnlyMessage
                    {

                    			newCompositeNode(grammarAccess.getMessageAccess().getOutOnlyMessageParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_OutOnlyMessage_0=ruleOutOnlyMessage();

                    state._fsp--;


                    			current = this_OutOnlyMessage_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:396:3: this_OutInMessage_1= ruleOutInMessage
                    {

                    			newCompositeNode(grammarAccess.getMessageAccess().getOutInMessageParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_OutInMessage_1=ruleOutInMessage();

                    state._fsp--;


                    			current = this_OutInMessage_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMessage"


    // $ANTLR start "entryRuleOutOnlyMessage"
    // InternalQactork.g:408:1: entryRuleOutOnlyMessage returns [EObject current=null] : iv_ruleOutOnlyMessage= ruleOutOnlyMessage EOF ;
    public final EObject entryRuleOutOnlyMessage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOutOnlyMessage = null;


        try {
            // InternalQactork.g:408:55: (iv_ruleOutOnlyMessage= ruleOutOnlyMessage EOF )
            // InternalQactork.g:409:2: iv_ruleOutOnlyMessage= ruleOutOnlyMessage EOF
            {
             newCompositeNode(grammarAccess.getOutOnlyMessageRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOutOnlyMessage=ruleOutOnlyMessage();

            state._fsp--;

             current =iv_ruleOutOnlyMessage; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOutOnlyMessage"


    // $ANTLR start "ruleOutOnlyMessage"
    // InternalQactork.g:415:1: ruleOutOnlyMessage returns [EObject current=null] : (this_Dispatch_0= ruleDispatch | this_Event_1= ruleEvent | this_Signal_2= ruleSignal | this_Token_3= ruleToken ) ;
    public final EObject ruleOutOnlyMessage() throws RecognitionException {
        EObject current = null;

        EObject this_Dispatch_0 = null;

        EObject this_Event_1 = null;

        EObject this_Signal_2 = null;

        EObject this_Token_3 = null;



        	enterRule();

        try {
            // InternalQactork.g:421:2: ( (this_Dispatch_0= ruleDispatch | this_Event_1= ruleEvent | this_Signal_2= ruleSignal | this_Token_3= ruleToken ) )
            // InternalQactork.g:422:2: (this_Dispatch_0= ruleDispatch | this_Event_1= ruleEvent | this_Signal_2= ruleSignal | this_Token_3= ruleToken )
            {
            // InternalQactork.g:422:2: (this_Dispatch_0= ruleDispatch | this_Event_1= ruleEvent | this_Signal_2= ruleSignal | this_Token_3= ruleToken )
            int alt9=4;
            switch ( input.LA(1) ) {
            case 23:
                {
                alt9=1;
                }
                break;
            case 20:
                {
                alt9=2;
                }
                break;
            case 21:
                {
                alt9=3;
                }
                break;
            case 22:
                {
                alt9=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // InternalQactork.g:423:3: this_Dispatch_0= ruleDispatch
                    {

                    			newCompositeNode(grammarAccess.getOutOnlyMessageAccess().getDispatchParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Dispatch_0=ruleDispatch();

                    state._fsp--;


                    			current = this_Dispatch_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:432:3: this_Event_1= ruleEvent
                    {

                    			newCompositeNode(grammarAccess.getOutOnlyMessageAccess().getEventParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Event_1=ruleEvent();

                    state._fsp--;


                    			current = this_Event_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:441:3: this_Signal_2= ruleSignal
                    {

                    			newCompositeNode(grammarAccess.getOutOnlyMessageAccess().getSignalParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Signal_2=ruleSignal();

                    state._fsp--;


                    			current = this_Signal_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalQactork.g:450:3: this_Token_3= ruleToken
                    {

                    			newCompositeNode(grammarAccess.getOutOnlyMessageAccess().getTokenParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_Token_3=ruleToken();

                    state._fsp--;


                    			current = this_Token_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOutOnlyMessage"


    // $ANTLR start "entryRuleOutInMessage"
    // InternalQactork.g:462:1: entryRuleOutInMessage returns [EObject current=null] : iv_ruleOutInMessage= ruleOutInMessage EOF ;
    public final EObject entryRuleOutInMessage() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleOutInMessage = null;


        try {
            // InternalQactork.g:462:53: (iv_ruleOutInMessage= ruleOutInMessage EOF )
            // InternalQactork.g:463:2: iv_ruleOutInMessage= ruleOutInMessage EOF
            {
             newCompositeNode(grammarAccess.getOutInMessageRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleOutInMessage=ruleOutInMessage();

            state._fsp--;

             current =iv_ruleOutInMessage; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleOutInMessage"


    // $ANTLR start "ruleOutInMessage"
    // InternalQactork.g:469:1: ruleOutInMessage returns [EObject current=null] : (this_Request_0= ruleRequest | this_Reply_1= ruleReply | this_Invitation_2= ruleInvitation ) ;
    public final EObject ruleOutInMessage() throws RecognitionException {
        EObject current = null;

        EObject this_Request_0 = null;

        EObject this_Reply_1 = null;

        EObject this_Invitation_2 = null;



        	enterRule();

        try {
            // InternalQactork.g:475:2: ( (this_Request_0= ruleRequest | this_Reply_1= ruleReply | this_Invitation_2= ruleInvitation ) )
            // InternalQactork.g:476:2: (this_Request_0= ruleRequest | this_Reply_1= ruleReply | this_Invitation_2= ruleInvitation )
            {
            // InternalQactork.g:476:2: (this_Request_0= ruleRequest | this_Reply_1= ruleReply | this_Invitation_2= ruleInvitation )
            int alt10=3;
            switch ( input.LA(1) ) {
            case 24:
                {
                alt10=1;
                }
                break;
            case 25:
                {
                alt10=2;
                }
                break;
            case 26:
                {
                alt10=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;
            }

            switch (alt10) {
                case 1 :
                    // InternalQactork.g:477:3: this_Request_0= ruleRequest
                    {

                    			newCompositeNode(grammarAccess.getOutInMessageAccess().getRequestParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_Request_0=ruleRequest();

                    state._fsp--;


                    			current = this_Request_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:486:3: this_Reply_1= ruleReply
                    {

                    			newCompositeNode(grammarAccess.getOutInMessageAccess().getReplyParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Reply_1=ruleReply();

                    state._fsp--;


                    			current = this_Reply_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:495:3: this_Invitation_2= ruleInvitation
                    {

                    			newCompositeNode(grammarAccess.getOutInMessageAccess().getInvitationParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Invitation_2=ruleInvitation();

                    state._fsp--;


                    			current = this_Invitation_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleOutInMessage"


    // $ANTLR start "entryRuleEvent"
    // InternalQactork.g:507:1: entryRuleEvent returns [EObject current=null] : iv_ruleEvent= ruleEvent EOF ;
    public final EObject entryRuleEvent() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEvent = null;


        try {
            // InternalQactork.g:507:46: (iv_ruleEvent= ruleEvent EOF )
            // InternalQactork.g:508:2: iv_ruleEvent= ruleEvent EOF
            {
             newCompositeNode(grammarAccess.getEventRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEvent=ruleEvent();

            state._fsp--;

             current =iv_ruleEvent; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEvent"


    // $ANTLR start "ruleEvent"
    // InternalQactork.g:514:1: ruleEvent returns [EObject current=null] : (otherlv_0= 'Event' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleEvent() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:520:2: ( (otherlv_0= 'Event' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:521:2: (otherlv_0= 'Event' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:521:2: (otherlv_0= 'Event' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:522:3: otherlv_0= 'Event' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,20,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getEventAccess().getEventKeyword_0());
            		
            // InternalQactork.g:526:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:527:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:527:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:528:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getEventAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEventRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getEventAccess().getColonKeyword_2());
            		
            // InternalQactork.g:548:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:549:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:549:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:550:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getEventAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getEventRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEvent"


    // $ANTLR start "entryRuleSignal"
    // InternalQactork.g:571:1: entryRuleSignal returns [EObject current=null] : iv_ruleSignal= ruleSignal EOF ;
    public final EObject entryRuleSignal() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSignal = null;


        try {
            // InternalQactork.g:571:47: (iv_ruleSignal= ruleSignal EOF )
            // InternalQactork.g:572:2: iv_ruleSignal= ruleSignal EOF
            {
             newCompositeNode(grammarAccess.getSignalRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSignal=ruleSignal();

            state._fsp--;

             current =iv_ruleSignal; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSignal"


    // $ANTLR start "ruleSignal"
    // InternalQactork.g:578:1: ruleSignal returns [EObject current=null] : (otherlv_0= 'Signal' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleSignal() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:584:2: ( (otherlv_0= 'Signal' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:585:2: (otherlv_0= 'Signal' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:585:2: (otherlv_0= 'Signal' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:586:3: otherlv_0= 'Signal' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,21,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getSignalAccess().getSignalKeyword_0());
            		
            // InternalQactork.g:590:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:591:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:591:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:592:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getSignalAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getSignalRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getSignalAccess().getColonKeyword_2());
            		
            // InternalQactork.g:612:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:613:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:613:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:614:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getSignalAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSignalRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSignal"


    // $ANTLR start "entryRuleToken"
    // InternalQactork.g:635:1: entryRuleToken returns [EObject current=null] : iv_ruleToken= ruleToken EOF ;
    public final EObject entryRuleToken() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleToken = null;


        try {
            // InternalQactork.g:635:46: (iv_ruleToken= ruleToken EOF )
            // InternalQactork.g:636:2: iv_ruleToken= ruleToken EOF
            {
             newCompositeNode(grammarAccess.getTokenRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleToken=ruleToken();

            state._fsp--;

             current =iv_ruleToken; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleToken"


    // $ANTLR start "ruleToken"
    // InternalQactork.g:642:1: ruleToken returns [EObject current=null] : (otherlv_0= 'Token' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleToken() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:648:2: ( (otherlv_0= 'Token' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:649:2: (otherlv_0= 'Token' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:649:2: (otherlv_0= 'Token' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:650:3: otherlv_0= 'Token' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,22,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getTokenAccess().getTokenKeyword_0());
            		
            // InternalQactork.g:654:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:655:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:655:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:656:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getTokenAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTokenRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getTokenAccess().getColonKeyword_2());
            		
            // InternalQactork.g:676:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:677:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:677:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:678:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getTokenAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTokenRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleToken"


    // $ANTLR start "entryRuleDispatch"
    // InternalQactork.g:699:1: entryRuleDispatch returns [EObject current=null] : iv_ruleDispatch= ruleDispatch EOF ;
    public final EObject entryRuleDispatch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDispatch = null;


        try {
            // InternalQactork.g:699:49: (iv_ruleDispatch= ruleDispatch EOF )
            // InternalQactork.g:700:2: iv_ruleDispatch= ruleDispatch EOF
            {
             newCompositeNode(grammarAccess.getDispatchRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDispatch=ruleDispatch();

            state._fsp--;

             current =iv_ruleDispatch; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDispatch"


    // $ANTLR start "ruleDispatch"
    // InternalQactork.g:706:1: ruleDispatch returns [EObject current=null] : (otherlv_0= 'Dispatch' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleDispatch() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:712:2: ( (otherlv_0= 'Dispatch' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:713:2: (otherlv_0= 'Dispatch' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:713:2: (otherlv_0= 'Dispatch' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:714:3: otherlv_0= 'Dispatch' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,23,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getDispatchAccess().getDispatchKeyword_0());
            		
            // InternalQactork.g:718:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:719:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:719:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:720:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getDispatchAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDispatchRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getDispatchAccess().getColonKeyword_2());
            		
            // InternalQactork.g:740:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:741:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:741:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:742:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getDispatchAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDispatchRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDispatch"


    // $ANTLR start "entryRuleRequest"
    // InternalQactork.g:763:1: entryRuleRequest returns [EObject current=null] : iv_ruleRequest= ruleRequest EOF ;
    public final EObject entryRuleRequest() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRequest = null;


        try {
            // InternalQactork.g:763:48: (iv_ruleRequest= ruleRequest EOF )
            // InternalQactork.g:764:2: iv_ruleRequest= ruleRequest EOF
            {
             newCompositeNode(grammarAccess.getRequestRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRequest=ruleRequest();

            state._fsp--;

             current =iv_ruleRequest; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRequest"


    // $ANTLR start "ruleRequest"
    // InternalQactork.g:770:1: ruleRequest returns [EObject current=null] : (otherlv_0= 'Request' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleRequest() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:776:2: ( (otherlv_0= 'Request' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:777:2: (otherlv_0= 'Request' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:777:2: (otherlv_0= 'Request' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:778:3: otherlv_0= 'Request' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,24,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getRequestAccess().getRequestKeyword_0());
            		
            // InternalQactork.g:782:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:783:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:783:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:784:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getRequestAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRequestRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getRequestAccess().getColonKeyword_2());
            		
            // InternalQactork.g:804:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:805:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:805:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:806:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getRequestAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getRequestRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRequest"


    // $ANTLR start "entryRuleReply"
    // InternalQactork.g:827:1: entryRuleReply returns [EObject current=null] : iv_ruleReply= ruleReply EOF ;
    public final EObject entryRuleReply() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReply = null;


        try {
            // InternalQactork.g:827:46: (iv_ruleReply= ruleReply EOF )
            // InternalQactork.g:828:2: iv_ruleReply= ruleReply EOF
            {
             newCompositeNode(grammarAccess.getReplyRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleReply=ruleReply();

            state._fsp--;

             current =iv_ruleReply; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleReply"


    // $ANTLR start "ruleReply"
    // InternalQactork.g:834:1: ruleReply returns [EObject current=null] : (otherlv_0= 'Reply' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleReply() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:840:2: ( (otherlv_0= 'Reply' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:841:2: (otherlv_0= 'Reply' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:841:2: (otherlv_0= 'Reply' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:842:3: otherlv_0= 'Reply' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,25,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getReplyAccess().getReplyKeyword_0());
            		
            // InternalQactork.g:846:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:847:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:847:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:848:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getReplyAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReplyRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getReplyAccess().getColonKeyword_2());
            		
            // InternalQactork.g:868:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:869:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:869:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:870:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getReplyAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getReplyRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleReply"


    // $ANTLR start "entryRuleInvitation"
    // InternalQactork.g:891:1: entryRuleInvitation returns [EObject current=null] : iv_ruleInvitation= ruleInvitation EOF ;
    public final EObject entryRuleInvitation() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInvitation = null;


        try {
            // InternalQactork.g:891:51: (iv_ruleInvitation= ruleInvitation EOF )
            // InternalQactork.g:892:2: iv_ruleInvitation= ruleInvitation EOF
            {
             newCompositeNode(grammarAccess.getInvitationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInvitation=ruleInvitation();

            state._fsp--;

             current =iv_ruleInvitation; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleInvitation"


    // $ANTLR start "ruleInvitation"
    // InternalQactork.g:898:1: ruleInvitation returns [EObject current=null] : (otherlv_0= 'Invitation' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) ;
    public final EObject ruleInvitation() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        EObject lv_msg_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:904:2: ( (otherlv_0= 'Invitation' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) ) )
            // InternalQactork.g:905:2: (otherlv_0= 'Invitation' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:905:2: (otherlv_0= 'Invitation' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) ) )
            // InternalQactork.g:906:3: otherlv_0= 'Invitation' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= ':' ( (lv_msg_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,26,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getInvitationAccess().getInvitationKeyword_0());
            		
            // InternalQactork.g:910:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:911:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:911:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:912:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(lv_name_1_0, grammarAccess.getInvitationAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getInvitationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getInvitationAccess().getColonKeyword_2());
            		
            // InternalQactork.g:932:3: ( (lv_msg_3_0= rulePHead ) )
            // InternalQactork.g:933:4: (lv_msg_3_0= rulePHead )
            {
            // InternalQactork.g:933:4: (lv_msg_3_0= rulePHead )
            // InternalQactork.g:934:5: lv_msg_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getInvitationAccess().getMsgPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_msg_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getInvitationRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleInvitation"


    // $ANTLR start "entryRuleContext"
    // InternalQactork.g:955:1: entryRuleContext returns [EObject current=null] : iv_ruleContext= ruleContext EOF ;
    public final EObject entryRuleContext() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleContext = null;


        try {
            // InternalQactork.g:955:48: (iv_ruleContext= ruleContext EOF )
            // InternalQactork.g:956:2: iv_ruleContext= ruleContext EOF
            {
             newCompositeNode(grammarAccess.getContextRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleContext=ruleContext();

            state._fsp--;

             current =iv_ruleContext; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleContext"


    // $ANTLR start "ruleContext"
    // InternalQactork.g:962:1: ruleContext returns [EObject current=null] : (otherlv_0= 'Context' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'ip' ( (lv_ip_3_0= ruleComponentIP ) ) ( (lv_mqtt_4_0= '+mqtt' ) )? ) ;
    public final EObject ruleContext() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token lv_mqtt_4_0=null;
        EObject lv_ip_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:968:2: ( (otherlv_0= 'Context' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'ip' ( (lv_ip_3_0= ruleComponentIP ) ) ( (lv_mqtt_4_0= '+mqtt' ) )? ) )
            // InternalQactork.g:969:2: (otherlv_0= 'Context' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'ip' ( (lv_ip_3_0= ruleComponentIP ) ) ( (lv_mqtt_4_0= '+mqtt' ) )? )
            {
            // InternalQactork.g:969:2: (otherlv_0= 'Context' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'ip' ( (lv_ip_3_0= ruleComponentIP ) ) ( (lv_mqtt_4_0= '+mqtt' ) )? )
            // InternalQactork.g:970:3: otherlv_0= 'Context' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'ip' ( (lv_ip_3_0= ruleComponentIP ) ) ( (lv_mqtt_4_0= '+mqtt' ) )?
            {
            otherlv_0=(Token)match(input,27,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getContextAccess().getContextKeyword_0());
            		
            // InternalQactork.g:974:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:975:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:975:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:976:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_15); 

            					newLeafNode(lv_name_1_0, grammarAccess.getContextAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getContextRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,28,FOLLOW_16); 

            			newLeafNode(otherlv_2, grammarAccess.getContextAccess().getIpKeyword_2());
            		
            // InternalQactork.g:996:3: ( (lv_ip_3_0= ruleComponentIP ) )
            // InternalQactork.g:997:4: (lv_ip_3_0= ruleComponentIP )
            {
            // InternalQactork.g:997:4: (lv_ip_3_0= ruleComponentIP )
            // InternalQactork.g:998:5: lv_ip_3_0= ruleComponentIP
            {

            					newCompositeNode(grammarAccess.getContextAccess().getIpComponentIPParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_17);
            lv_ip_3_0=ruleComponentIP();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getContextRule());
            					}
            					set(
            						current,
            						"ip",
            						lv_ip_3_0,
            						"it.unibo.Qactork.ComponentIP");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalQactork.g:1015:3: ( (lv_mqtt_4_0= '+mqtt' ) )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==29) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // InternalQactork.g:1016:4: (lv_mqtt_4_0= '+mqtt' )
                    {
                    // InternalQactork.g:1016:4: (lv_mqtt_4_0= '+mqtt' )
                    // InternalQactork.g:1017:5: lv_mqtt_4_0= '+mqtt'
                    {
                    lv_mqtt_4_0=(Token)match(input,29,FOLLOW_2); 

                    					newLeafNode(lv_mqtt_4_0, grammarAccess.getContextAccess().getMqttMqttKeyword_4_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getContextRule());
                    					}
                    					setWithLastConsumed(current, "mqtt", lv_mqtt_4_0 != null, "+mqtt");
                    				

                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleContext"


    // $ANTLR start "entryRuleComponentIP"
    // InternalQactork.g:1033:1: entryRuleComponentIP returns [EObject current=null] : iv_ruleComponentIP= ruleComponentIP EOF ;
    public final EObject entryRuleComponentIP() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleComponentIP = null;


        try {
            // InternalQactork.g:1033:52: (iv_ruleComponentIP= ruleComponentIP EOF )
            // InternalQactork.g:1034:2: iv_ruleComponentIP= ruleComponentIP EOF
            {
             newCompositeNode(grammarAccess.getComponentIPRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleComponentIP=ruleComponentIP();

            state._fsp--;

             current =iv_ruleComponentIP; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleComponentIP"


    // $ANTLR start "ruleComponentIP"
    // InternalQactork.g:1040:1: ruleComponentIP returns [EObject current=null] : ( () otherlv_1= '[' otherlv_2= 'host=' ( (lv_host_3_0= RULE_STRING ) ) otherlv_4= 'port=' ( (lv_port_5_0= RULE_INT ) ) otherlv_6= ']' ) ;
    public final EObject ruleComponentIP() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token lv_host_3_0=null;
        Token otherlv_4=null;
        Token lv_port_5_0=null;
        Token otherlv_6=null;


        	enterRule();

        try {
            // InternalQactork.g:1046:2: ( ( () otherlv_1= '[' otherlv_2= 'host=' ( (lv_host_3_0= RULE_STRING ) ) otherlv_4= 'port=' ( (lv_port_5_0= RULE_INT ) ) otherlv_6= ']' ) )
            // InternalQactork.g:1047:2: ( () otherlv_1= '[' otherlv_2= 'host=' ( (lv_host_3_0= RULE_STRING ) ) otherlv_4= 'port=' ( (lv_port_5_0= RULE_INT ) ) otherlv_6= ']' )
            {
            // InternalQactork.g:1047:2: ( () otherlv_1= '[' otherlv_2= 'host=' ( (lv_host_3_0= RULE_STRING ) ) otherlv_4= 'port=' ( (lv_port_5_0= RULE_INT ) ) otherlv_6= ']' )
            // InternalQactork.g:1048:3: () otherlv_1= '[' otherlv_2= 'host=' ( (lv_host_3_0= RULE_STRING ) ) otherlv_4= 'port=' ( (lv_port_5_0= RULE_INT ) ) otherlv_6= ']'
            {
            // InternalQactork.g:1048:3: ()
            // InternalQactork.g:1049:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getComponentIPAccess().getComponentIPAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,30,FOLLOW_18); 

            			newLeafNode(otherlv_1, grammarAccess.getComponentIPAccess().getLeftSquareBracketKeyword_1());
            		
            otherlv_2=(Token)match(input,31,FOLLOW_10); 

            			newLeafNode(otherlv_2, grammarAccess.getComponentIPAccess().getHostKeyword_2());
            		
            // InternalQactork.g:1063:3: ( (lv_host_3_0= RULE_STRING ) )
            // InternalQactork.g:1064:4: (lv_host_3_0= RULE_STRING )
            {
            // InternalQactork.g:1064:4: (lv_host_3_0= RULE_STRING )
            // InternalQactork.g:1065:5: lv_host_3_0= RULE_STRING
            {
            lv_host_3_0=(Token)match(input,RULE_STRING,FOLLOW_19); 

            					newLeafNode(lv_host_3_0, grammarAccess.getComponentIPAccess().getHostSTRINGTerminalRuleCall_3_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getComponentIPRule());
            					}
            					setWithLastConsumed(
            						current,
            						"host",
            						lv_host_3_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }

            otherlv_4=(Token)match(input,32,FOLLOW_12); 

            			newLeafNode(otherlv_4, grammarAccess.getComponentIPAccess().getPortKeyword_4());
            		
            // InternalQactork.g:1085:3: ( (lv_port_5_0= RULE_INT ) )
            // InternalQactork.g:1086:4: (lv_port_5_0= RULE_INT )
            {
            // InternalQactork.g:1086:4: (lv_port_5_0= RULE_INT )
            // InternalQactork.g:1087:5: lv_port_5_0= RULE_INT
            {
            lv_port_5_0=(Token)match(input,RULE_INT,FOLLOW_20); 

            					newLeafNode(lv_port_5_0, grammarAccess.getComponentIPAccess().getPortINTTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getComponentIPRule());
            					}
            					setWithLastConsumed(
            						current,
            						"port",
            						lv_port_5_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            otherlv_6=(Token)match(input,33,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getComponentIPAccess().getRightSquareBracketKeyword_6());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleComponentIP"


    // $ANTLR start "entryRuleQActorDeclaration"
    // InternalQactork.g:1111:1: entryRuleQActorDeclaration returns [EObject current=null] : iv_ruleQActorDeclaration= ruleQActorDeclaration EOF ;
    public final EObject entryRuleQActorDeclaration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQActorDeclaration = null;


        try {
            // InternalQactork.g:1111:58: (iv_ruleQActorDeclaration= ruleQActorDeclaration EOF )
            // InternalQactork.g:1112:2: iv_ruleQActorDeclaration= ruleQActorDeclaration EOF
            {
             newCompositeNode(grammarAccess.getQActorDeclarationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQActorDeclaration=ruleQActorDeclaration();

            state._fsp--;

             current =iv_ruleQActorDeclaration; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQActorDeclaration"


    // $ANTLR start "ruleQActorDeclaration"
    // InternalQactork.g:1118:1: ruleQActorDeclaration returns [EObject current=null] : (this_QActor_0= ruleQActor | this_QActorCoded_1= ruleQActorCoded | this_QActorExternal_2= ruleQActorExternal ) ;
    public final EObject ruleQActorDeclaration() throws RecognitionException {
        EObject current = null;

        EObject this_QActor_0 = null;

        EObject this_QActorCoded_1 = null;

        EObject this_QActorExternal_2 = null;



        	enterRule();

        try {
            // InternalQactork.g:1124:2: ( (this_QActor_0= ruleQActor | this_QActorCoded_1= ruleQActorCoded | this_QActorExternal_2= ruleQActorExternal ) )
            // InternalQactork.g:1125:2: (this_QActor_0= ruleQActor | this_QActorCoded_1= ruleQActorCoded | this_QActorExternal_2= ruleQActorExternal )
            {
            // InternalQactork.g:1125:2: (this_QActor_0= ruleQActor | this_QActorCoded_1= ruleQActorCoded | this_QActorExternal_2= ruleQActorExternal )
            int alt12=3;
            switch ( input.LA(1) ) {
            case 38:
                {
                alt12=1;
                }
                break;
            case 36:
                {
                alt12=2;
                }
                break;
            case 34:
                {
                alt12=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;
            }

            switch (alt12) {
                case 1 :
                    // InternalQactork.g:1126:3: this_QActor_0= ruleQActor
                    {

                    			newCompositeNode(grammarAccess.getQActorDeclarationAccess().getQActorParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_QActor_0=ruleQActor();

                    state._fsp--;


                    			current = this_QActor_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:1135:3: this_QActorCoded_1= ruleQActorCoded
                    {

                    			newCompositeNode(grammarAccess.getQActorDeclarationAccess().getQActorCodedParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_QActorCoded_1=ruleQActorCoded();

                    state._fsp--;


                    			current = this_QActorCoded_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:1144:3: this_QActorExternal_2= ruleQActorExternal
                    {

                    			newCompositeNode(grammarAccess.getQActorDeclarationAccess().getQActorExternalParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_QActorExternal_2=ruleQActorExternal();

                    state._fsp--;


                    			current = this_QActorExternal_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQActorDeclaration"


    // $ANTLR start "entryRuleQActorExternal"
    // InternalQactork.g:1156:1: entryRuleQActorExternal returns [EObject current=null] : iv_ruleQActorExternal= ruleQActorExternal EOF ;
    public final EObject entryRuleQActorExternal() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQActorExternal = null;


        try {
            // InternalQactork.g:1156:55: (iv_ruleQActorExternal= ruleQActorExternal EOF )
            // InternalQactork.g:1157:2: iv_ruleQActorExternal= ruleQActorExternal EOF
            {
             newCompositeNode(grammarAccess.getQActorExternalRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQActorExternal=ruleQActorExternal();

            state._fsp--;

             current =iv_ruleQActorExternal; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQActorExternal"


    // $ANTLR start "ruleQActorExternal"
    // InternalQactork.g:1163:1: ruleQActorExternal returns [EObject current=null] : (otherlv_0= 'ExternalQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) ) ;
    public final EObject ruleQActorExternal() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;


        	enterRule();

        try {
            // InternalQactork.g:1169:2: ( (otherlv_0= 'ExternalQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) ) )
            // InternalQactork.g:1170:2: (otherlv_0= 'ExternalQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) )
            {
            // InternalQactork.g:1170:2: (otherlv_0= 'ExternalQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) )
            // InternalQactork.g:1171:3: otherlv_0= 'ExternalQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,34,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getQActorExternalAccess().getExternalQActorKeyword_0());
            		
            // InternalQactork.g:1175:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:1176:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:1176:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:1177:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_21); 

            					newLeafNode(lv_name_1_0, grammarAccess.getQActorExternalAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorExternalRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,35,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getQActorExternalAccess().getContextKeyword_2());
            		
            // InternalQactork.g:1197:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:1198:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:1198:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:1199:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorExternalRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_3, grammarAccess.getQActorExternalAccess().getContextContextCrossReference_3_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQActorExternal"


    // $ANTLR start "entryRuleQActorCoded"
    // InternalQactork.g:1214:1: entryRuleQActorCoded returns [EObject current=null] : iv_ruleQActorCoded= ruleQActorCoded EOF ;
    public final EObject entryRuleQActorCoded() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQActorCoded = null;


        try {
            // InternalQactork.g:1214:52: (iv_ruleQActorCoded= ruleQActorCoded EOF )
            // InternalQactork.g:1215:2: iv_ruleQActorCoded= ruleQActorCoded EOF
            {
             newCompositeNode(grammarAccess.getQActorCodedRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQActorCoded=ruleQActorCoded();

            state._fsp--;

             current =iv_ruleQActorCoded; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQActorCoded"


    // $ANTLR start "ruleQActorCoded"
    // InternalQactork.g:1221:1: ruleQActorCoded returns [EObject current=null] : (otherlv_0= 'CodedQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= 'className' ( (lv_className_5_0= RULE_STRING ) ) ) ;
    public final EObject ruleQActorCoded() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token lv_className_5_0=null;


        	enterRule();

        try {
            // InternalQactork.g:1227:2: ( (otherlv_0= 'CodedQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= 'className' ( (lv_className_5_0= RULE_STRING ) ) ) )
            // InternalQactork.g:1228:2: (otherlv_0= 'CodedQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= 'className' ( (lv_className_5_0= RULE_STRING ) ) )
            {
            // InternalQactork.g:1228:2: (otherlv_0= 'CodedQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= 'className' ( (lv_className_5_0= RULE_STRING ) ) )
            // InternalQactork.g:1229:3: otherlv_0= 'CodedQActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= 'className' ( (lv_className_5_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,36,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getQActorCodedAccess().getCodedQActorKeyword_0());
            		
            // InternalQactork.g:1233:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:1234:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:1234:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:1235:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_21); 

            					newLeafNode(lv_name_1_0, grammarAccess.getQActorCodedAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorCodedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,35,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getQActorCodedAccess().getContextKeyword_2());
            		
            // InternalQactork.g:1255:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:1256:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:1256:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:1257:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorCodedRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_22); 

            					newLeafNode(otherlv_3, grammarAccess.getQActorCodedAccess().getContextContextCrossReference_3_0());
            				

            }


            }

            otherlv_4=(Token)match(input,37,FOLLOW_10); 

            			newLeafNode(otherlv_4, grammarAccess.getQActorCodedAccess().getClassNameKeyword_4());
            		
            // InternalQactork.g:1272:3: ( (lv_className_5_0= RULE_STRING ) )
            // InternalQactork.g:1273:4: (lv_className_5_0= RULE_STRING )
            {
            // InternalQactork.g:1273:4: (lv_className_5_0= RULE_STRING )
            // InternalQactork.g:1274:5: lv_className_5_0= RULE_STRING
            {
            lv_className_5_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_className_5_0, grammarAccess.getQActorCodedAccess().getClassNameSTRINGTerminalRuleCall_5_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorCodedRule());
            					}
            					setWithLastConsumed(
            						current,
            						"className",
            						lv_className_5_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQActorCoded"


    // $ANTLR start "entryRuleQActor"
    // InternalQactork.g:1294:1: entryRuleQActor returns [EObject current=null] : iv_ruleQActor= ruleQActor EOF ;
    public final EObject entryRuleQActor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleQActor = null;


        try {
            // InternalQactork.g:1294:47: (iv_ruleQActor= ruleQActor EOF )
            // InternalQactork.g:1295:2: iv_ruleQActor= ruleQActor EOF
            {
             newCompositeNode(grammarAccess.getQActorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleQActor=ruleQActor();

            state._fsp--;

             current =iv_ruleQActor; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleQActor"


    // $ANTLR start "ruleQActor"
    // InternalQactork.g:1301:1: ruleQActor returns [EObject current=null] : (otherlv_0= 'QActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= '{' ( (lv_start_5_0= ruleAnyAction ) )? ( (lv_states_6_0= ruleState ) )* otherlv_7= '}' ) ;
    public final EObject ruleQActor() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_7=null;
        EObject lv_start_5_0 = null;

        EObject lv_states_6_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:1307:2: ( (otherlv_0= 'QActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= '{' ( (lv_start_5_0= ruleAnyAction ) )? ( (lv_states_6_0= ruleState ) )* otherlv_7= '}' ) )
            // InternalQactork.g:1308:2: (otherlv_0= 'QActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= '{' ( (lv_start_5_0= ruleAnyAction ) )? ( (lv_states_6_0= ruleState ) )* otherlv_7= '}' )
            {
            // InternalQactork.g:1308:2: (otherlv_0= 'QActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= '{' ( (lv_start_5_0= ruleAnyAction ) )? ( (lv_states_6_0= ruleState ) )* otherlv_7= '}' )
            // InternalQactork.g:1309:3: otherlv_0= 'QActor' ( (lv_name_1_0= RULE_ID ) ) otherlv_2= 'context' ( (otherlv_3= RULE_ID ) ) otherlv_4= '{' ( (lv_start_5_0= ruleAnyAction ) )? ( (lv_states_6_0= ruleState ) )* otherlv_7= '}'
            {
            otherlv_0=(Token)match(input,38,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getQActorAccess().getQActorKeyword_0());
            		
            // InternalQactork.g:1313:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:1314:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:1314:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:1315:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_21); 

            					newLeafNode(lv_name_1_0, grammarAccess.getQActorAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_2=(Token)match(input,35,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getQActorAccess().getContextKeyword_2());
            		
            // InternalQactork.g:1335:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:1336:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:1336:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:1337:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getQActorRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_23); 

            					newLeafNode(otherlv_3, grammarAccess.getQActorAccess().getContextContextCrossReference_3_0());
            				

            }


            }

            otherlv_4=(Token)match(input,39,FOLLOW_24); 

            			newLeafNode(otherlv_4, grammarAccess.getQActorAccess().getLeftCurlyBracketKeyword_4());
            		
            // InternalQactork.g:1352:3: ( (lv_start_5_0= ruleAnyAction ) )?
            int alt13=2;
            int LA13_0 = input.LA(1);

            if ( (LA13_0==30) ) {
                alt13=1;
            }
            switch (alt13) {
                case 1 :
                    // InternalQactork.g:1353:4: (lv_start_5_0= ruleAnyAction )
                    {
                    // InternalQactork.g:1353:4: (lv_start_5_0= ruleAnyAction )
                    // InternalQactork.g:1354:5: lv_start_5_0= ruleAnyAction
                    {

                    					newCompositeNode(grammarAccess.getQActorAccess().getStartAnyActionParserRuleCall_5_0());
                    				
                    pushFollow(FOLLOW_25);
                    lv_start_5_0=ruleAnyAction();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getQActorRule());
                    					}
                    					set(
                    						current,
                    						"start",
                    						lv_start_5_0,
                    						"it.unibo.Qactork.AnyAction");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalQactork.g:1371:3: ( (lv_states_6_0= ruleState ) )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==41) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // InternalQactork.g:1372:4: (lv_states_6_0= ruleState )
            	    {
            	    // InternalQactork.g:1372:4: (lv_states_6_0= ruleState )
            	    // InternalQactork.g:1373:5: lv_states_6_0= ruleState
            	    {

            	    					newCompositeNode(grammarAccess.getQActorAccess().getStatesStateParserRuleCall_6_0());
            	    				
            	    pushFollow(FOLLOW_25);
            	    lv_states_6_0=ruleState();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getQActorRule());
            	    					}
            	    					add(
            	    						current,
            	    						"states",
            	    						lv_states_6_0,
            	    						"it.unibo.Qactork.State");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            otherlv_7=(Token)match(input,40,FOLLOW_2); 

            			newLeafNode(otherlv_7, grammarAccess.getQActorAccess().getRightCurlyBracketKeyword_7());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleQActor"


    // $ANTLR start "entryRuleState"
    // InternalQactork.g:1398:1: entryRuleState returns [EObject current=null] : iv_ruleState= ruleState EOF ;
    public final EObject entryRuleState() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleState = null;


        try {
            // InternalQactork.g:1398:46: (iv_ruleState= ruleState EOF )
            // InternalQactork.g:1399:2: iv_ruleState= ruleState EOF
            {
             newCompositeNode(grammarAccess.getStateRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleState=ruleState();

            state._fsp--;

             current =iv_ruleState; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleState"


    // $ANTLR start "ruleState"
    // InternalQactork.g:1405:1: ruleState returns [EObject current=null] : (otherlv_0= 'State' ( (lv_name_1_0= RULE_ID ) ) ( (lv_normal_2_0= 'initial' ) )? otherlv_3= '{' ( (lv_actions_4_0= ruleStateAction ) )* otherlv_5= '}' ( (lv_transition_6_0= ruleTransition ) )? ) ;
    public final EObject ruleState() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token lv_normal_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_actions_4_0 = null;

        EObject lv_transition_6_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:1411:2: ( (otherlv_0= 'State' ( (lv_name_1_0= RULE_ID ) ) ( (lv_normal_2_0= 'initial' ) )? otherlv_3= '{' ( (lv_actions_4_0= ruleStateAction ) )* otherlv_5= '}' ( (lv_transition_6_0= ruleTransition ) )? ) )
            // InternalQactork.g:1412:2: (otherlv_0= 'State' ( (lv_name_1_0= RULE_ID ) ) ( (lv_normal_2_0= 'initial' ) )? otherlv_3= '{' ( (lv_actions_4_0= ruleStateAction ) )* otherlv_5= '}' ( (lv_transition_6_0= ruleTransition ) )? )
            {
            // InternalQactork.g:1412:2: (otherlv_0= 'State' ( (lv_name_1_0= RULE_ID ) ) ( (lv_normal_2_0= 'initial' ) )? otherlv_3= '{' ( (lv_actions_4_0= ruleStateAction ) )* otherlv_5= '}' ( (lv_transition_6_0= ruleTransition ) )? )
            // InternalQactork.g:1413:3: otherlv_0= 'State' ( (lv_name_1_0= RULE_ID ) ) ( (lv_normal_2_0= 'initial' ) )? otherlv_3= '{' ( (lv_actions_4_0= ruleStateAction ) )* otherlv_5= '}' ( (lv_transition_6_0= ruleTransition ) )?
            {
            otherlv_0=(Token)match(input,41,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getStateAccess().getStateKeyword_0());
            		
            // InternalQactork.g:1417:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:1418:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:1418:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:1419:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_26); 

            					newLeafNode(lv_name_1_0, grammarAccess.getStateAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getStateRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalQactork.g:1435:3: ( (lv_normal_2_0= 'initial' ) )?
            int alt15=2;
            int LA15_0 = input.LA(1);

            if ( (LA15_0==42) ) {
                alt15=1;
            }
            switch (alt15) {
                case 1 :
                    // InternalQactork.g:1436:4: (lv_normal_2_0= 'initial' )
                    {
                    // InternalQactork.g:1436:4: (lv_normal_2_0= 'initial' )
                    // InternalQactork.g:1437:5: lv_normal_2_0= 'initial'
                    {
                    lv_normal_2_0=(Token)match(input,42,FOLLOW_23); 

                    					newLeafNode(lv_normal_2_0, grammarAccess.getStateAccess().getNormalInitialKeyword_2_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getStateRule());
                    					}
                    					setWithLastConsumed(current, "normal", lv_normal_2_0 != null, "initial");
                    				

                    }


                    }
                    break;

            }

            otherlv_3=(Token)match(input,39,FOLLOW_27); 

            			newLeafNode(otherlv_3, grammarAccess.getStateAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalQactork.g:1453:3: ( (lv_actions_4_0= ruleStateAction ) )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==30||LA16_0==43||(LA16_0>=45 && LA16_0<=47)||LA16_0==50||LA16_0==52||(LA16_0>=55 && LA16_0<=56)||LA16_0==58||(LA16_0>=60 && LA16_0<=62)||(LA16_0>=64 && LA16_0<=72)||(LA16_0>=74 && LA16_0<=75)) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // InternalQactork.g:1454:4: (lv_actions_4_0= ruleStateAction )
            	    {
            	    // InternalQactork.g:1454:4: (lv_actions_4_0= ruleStateAction )
            	    // InternalQactork.g:1455:5: lv_actions_4_0= ruleStateAction
            	    {

            	    					newCompositeNode(grammarAccess.getStateAccess().getActionsStateActionParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_actions_4_0=ruleStateAction();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getStateRule());
            	    					}
            	    					add(
            	    						current,
            	    						"actions",
            	    						lv_actions_4_0,
            	    						"it.unibo.Qactork.StateAction");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);

            otherlv_5=(Token)match(input,40,FOLLOW_28); 

            			newLeafNode(otherlv_5, grammarAccess.getStateAccess().getRightCurlyBracketKeyword_5());
            		
            // InternalQactork.g:1476:3: ( (lv_transition_6_0= ruleTransition ) )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( ((LA17_0>=76 && LA17_0<=77)) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // InternalQactork.g:1477:4: (lv_transition_6_0= ruleTransition )
                    {
                    // InternalQactork.g:1477:4: (lv_transition_6_0= ruleTransition )
                    // InternalQactork.g:1478:5: lv_transition_6_0= ruleTransition
                    {

                    					newCompositeNode(grammarAccess.getStateAccess().getTransitionTransitionParserRuleCall_6_0());
                    				
                    pushFollow(FOLLOW_2);
                    lv_transition_6_0=ruleTransition();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getStateRule());
                    					}
                    					set(
                    						current,
                    						"transition",
                    						lv_transition_6_0,
                    						"it.unibo.Qactork.Transition");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleState"


    // $ANTLR start "entryRuleStateAction"
    // InternalQactork.g:1499:1: entryRuleStateAction returns [EObject current=null] : iv_ruleStateAction= ruleStateAction EOF ;
    public final EObject entryRuleStateAction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleStateAction = null;


        try {
            // InternalQactork.g:1499:52: (iv_ruleStateAction= ruleStateAction EOF )
            // InternalQactork.g:1500:2: iv_ruleStateAction= ruleStateAction EOF
            {
             newCompositeNode(grammarAccess.getStateActionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleStateAction=ruleStateAction();

            state._fsp--;

             current =iv_ruleStateAction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleStateAction"


    // $ANTLR start "ruleStateAction"
    // InternalQactork.g:1506:1: ruleStateAction returns [EObject current=null] : (this_GuardedStateAction_0= ruleGuardedStateAction | this_IfSolvedAction_1= ruleIfSolvedAction | this_Print_2= rulePrint | this_PrintCurMsg_3= rulePrintCurMsg | this_SolveGoal_4= ruleSolveGoal | this_DiscardMsg_5= ruleDiscardMsg | this_MemoTime_6= ruleMemoTime | this_Duration_7= ruleDuration | this_Forward_8= ruleForward | this_Emit_9= ruleEmit | this_Demand_10= ruleDemand | this_Answer_11= ruleAnswer | this_ReplyReq_12= ruleReplyReq | this_Delay_13= ruleDelay | this_MsgCond_14= ruleMsgCond | this_EndActor_15= ruleEndActor | this_UpdateResource_16= ruleUpdateResource | this_CodeRun_17= ruleCodeRun | this_AnyAction_18= ruleAnyAction | this_Exec_19= ruleExec ) ;
    public final EObject ruleStateAction() throws RecognitionException {
        EObject current = null;

        EObject this_GuardedStateAction_0 = null;

        EObject this_IfSolvedAction_1 = null;

        EObject this_Print_2 = null;

        EObject this_PrintCurMsg_3 = null;

        EObject this_SolveGoal_4 = null;

        EObject this_DiscardMsg_5 = null;

        EObject this_MemoTime_6 = null;

        EObject this_Duration_7 = null;

        EObject this_Forward_8 = null;

        EObject this_Emit_9 = null;

        EObject this_Demand_10 = null;

        EObject this_Answer_11 = null;

        EObject this_ReplyReq_12 = null;

        EObject this_Delay_13 = null;

        EObject this_MsgCond_14 = null;

        EObject this_EndActor_15 = null;

        EObject this_UpdateResource_16 = null;

        EObject this_CodeRun_17 = null;

        EObject this_AnyAction_18 = null;

        EObject this_Exec_19 = null;



        	enterRule();

        try {
            // InternalQactork.g:1512:2: ( (this_GuardedStateAction_0= ruleGuardedStateAction | this_IfSolvedAction_1= ruleIfSolvedAction | this_Print_2= rulePrint | this_PrintCurMsg_3= rulePrintCurMsg | this_SolveGoal_4= ruleSolveGoal | this_DiscardMsg_5= ruleDiscardMsg | this_MemoTime_6= ruleMemoTime | this_Duration_7= ruleDuration | this_Forward_8= ruleForward | this_Emit_9= ruleEmit | this_Demand_10= ruleDemand | this_Answer_11= ruleAnswer | this_ReplyReq_12= ruleReplyReq | this_Delay_13= ruleDelay | this_MsgCond_14= ruleMsgCond | this_EndActor_15= ruleEndActor | this_UpdateResource_16= ruleUpdateResource | this_CodeRun_17= ruleCodeRun | this_AnyAction_18= ruleAnyAction | this_Exec_19= ruleExec ) )
            // InternalQactork.g:1513:2: (this_GuardedStateAction_0= ruleGuardedStateAction | this_IfSolvedAction_1= ruleIfSolvedAction | this_Print_2= rulePrint | this_PrintCurMsg_3= rulePrintCurMsg | this_SolveGoal_4= ruleSolveGoal | this_DiscardMsg_5= ruleDiscardMsg | this_MemoTime_6= ruleMemoTime | this_Duration_7= ruleDuration | this_Forward_8= ruleForward | this_Emit_9= ruleEmit | this_Demand_10= ruleDemand | this_Answer_11= ruleAnswer | this_ReplyReq_12= ruleReplyReq | this_Delay_13= ruleDelay | this_MsgCond_14= ruleMsgCond | this_EndActor_15= ruleEndActor | this_UpdateResource_16= ruleUpdateResource | this_CodeRun_17= ruleCodeRun | this_AnyAction_18= ruleAnyAction | this_Exec_19= ruleExec )
            {
            // InternalQactork.g:1513:2: (this_GuardedStateAction_0= ruleGuardedStateAction | this_IfSolvedAction_1= ruleIfSolvedAction | this_Print_2= rulePrint | this_PrintCurMsg_3= rulePrintCurMsg | this_SolveGoal_4= ruleSolveGoal | this_DiscardMsg_5= ruleDiscardMsg | this_MemoTime_6= ruleMemoTime | this_Duration_7= ruleDuration | this_Forward_8= ruleForward | this_Emit_9= ruleEmit | this_Demand_10= ruleDemand | this_Answer_11= ruleAnswer | this_ReplyReq_12= ruleReplyReq | this_Delay_13= ruleDelay | this_MsgCond_14= ruleMsgCond | this_EndActor_15= ruleEndActor | this_UpdateResource_16= ruleUpdateResource | this_CodeRun_17= ruleCodeRun | this_AnyAction_18= ruleAnyAction | this_Exec_19= ruleExec )
            int alt18=20;
            switch ( input.LA(1) ) {
            case 45:
                {
                alt18=1;
                }
                break;
            case 43:
                {
                alt18=2;
                }
                break;
            case 47:
                {
                alt18=3;
                }
                break;
            case 46:
                {
                alt18=4;
                }
                break;
            case 50:
                {
                alt18=5;
                }
                break;
            case 52:
                {
                alt18=6;
                }
                break;
            case 55:
                {
                alt18=7;
                }
                break;
            case 56:
                {
                alt18=8;
                }
                break;
            case 58:
                {
                alt18=9;
                }
                break;
            case 60:
                {
                alt18=10;
                }
                break;
            case 61:
                {
                alt18=11;
                }
                break;
            case 62:
                {
                alt18=12;
                }
                break;
            case 64:
                {
                alt18=13;
                }
                break;
            case 65:
            case 66:
            case 67:
            case 68:
                {
                alt18=14;
                }
                break;
            case 69:
                {
                alt18=15;
                }
                break;
            case 70:
                {
                alt18=16;
                }
                break;
            case 71:
                {
                alt18=17;
                }
                break;
            case 72:
            case 74:
                {
                alt18=18;
                }
                break;
            case 30:
                {
                alt18=19;
                }
                break;
            case 75:
                {
                alt18=20;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // InternalQactork.g:1514:3: this_GuardedStateAction_0= ruleGuardedStateAction
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getGuardedStateActionParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_GuardedStateAction_0=ruleGuardedStateAction();

                    state._fsp--;


                    			current = this_GuardedStateAction_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:1523:3: this_IfSolvedAction_1= ruleIfSolvedAction
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getIfSolvedActionParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_IfSolvedAction_1=ruleIfSolvedAction();

                    state._fsp--;


                    			current = this_IfSolvedAction_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:1532:3: this_Print_2= rulePrint
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getPrintParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_Print_2=rulePrint();

                    state._fsp--;


                    			current = this_Print_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalQactork.g:1541:3: this_PrintCurMsg_3= rulePrintCurMsg
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getPrintCurMsgParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_PrintCurMsg_3=rulePrintCurMsg();

                    state._fsp--;


                    			current = this_PrintCurMsg_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalQactork.g:1550:3: this_SolveGoal_4= ruleSolveGoal
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getSolveGoalParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_SolveGoal_4=ruleSolveGoal();

                    state._fsp--;


                    			current = this_SolveGoal_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 6 :
                    // InternalQactork.g:1559:3: this_DiscardMsg_5= ruleDiscardMsg
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getDiscardMsgParserRuleCall_5());
                    		
                    pushFollow(FOLLOW_2);
                    this_DiscardMsg_5=ruleDiscardMsg();

                    state._fsp--;


                    			current = this_DiscardMsg_5;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 7 :
                    // InternalQactork.g:1568:3: this_MemoTime_6= ruleMemoTime
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getMemoTimeParserRuleCall_6());
                    		
                    pushFollow(FOLLOW_2);
                    this_MemoTime_6=ruleMemoTime();

                    state._fsp--;


                    			current = this_MemoTime_6;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 8 :
                    // InternalQactork.g:1577:3: this_Duration_7= ruleDuration
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getDurationParserRuleCall_7());
                    		
                    pushFollow(FOLLOW_2);
                    this_Duration_7=ruleDuration();

                    state._fsp--;


                    			current = this_Duration_7;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 9 :
                    // InternalQactork.g:1586:3: this_Forward_8= ruleForward
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getForwardParserRuleCall_8());
                    		
                    pushFollow(FOLLOW_2);
                    this_Forward_8=ruleForward();

                    state._fsp--;


                    			current = this_Forward_8;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 10 :
                    // InternalQactork.g:1595:3: this_Emit_9= ruleEmit
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getEmitParserRuleCall_9());
                    		
                    pushFollow(FOLLOW_2);
                    this_Emit_9=ruleEmit();

                    state._fsp--;


                    			current = this_Emit_9;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 11 :
                    // InternalQactork.g:1604:3: this_Demand_10= ruleDemand
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getDemandParserRuleCall_10());
                    		
                    pushFollow(FOLLOW_2);
                    this_Demand_10=ruleDemand();

                    state._fsp--;


                    			current = this_Demand_10;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 12 :
                    // InternalQactork.g:1613:3: this_Answer_11= ruleAnswer
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getAnswerParserRuleCall_11());
                    		
                    pushFollow(FOLLOW_2);
                    this_Answer_11=ruleAnswer();

                    state._fsp--;


                    			current = this_Answer_11;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 13 :
                    // InternalQactork.g:1622:3: this_ReplyReq_12= ruleReplyReq
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getReplyReqParserRuleCall_12());
                    		
                    pushFollow(FOLLOW_2);
                    this_ReplyReq_12=ruleReplyReq();

                    state._fsp--;


                    			current = this_ReplyReq_12;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 14 :
                    // InternalQactork.g:1631:3: this_Delay_13= ruleDelay
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getDelayParserRuleCall_13());
                    		
                    pushFollow(FOLLOW_2);
                    this_Delay_13=ruleDelay();

                    state._fsp--;


                    			current = this_Delay_13;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 15 :
                    // InternalQactork.g:1640:3: this_MsgCond_14= ruleMsgCond
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getMsgCondParserRuleCall_14());
                    		
                    pushFollow(FOLLOW_2);
                    this_MsgCond_14=ruleMsgCond();

                    state._fsp--;


                    			current = this_MsgCond_14;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 16 :
                    // InternalQactork.g:1649:3: this_EndActor_15= ruleEndActor
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getEndActorParserRuleCall_15());
                    		
                    pushFollow(FOLLOW_2);
                    this_EndActor_15=ruleEndActor();

                    state._fsp--;


                    			current = this_EndActor_15;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 17 :
                    // InternalQactork.g:1658:3: this_UpdateResource_16= ruleUpdateResource
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getUpdateResourceParserRuleCall_16());
                    		
                    pushFollow(FOLLOW_2);
                    this_UpdateResource_16=ruleUpdateResource();

                    state._fsp--;


                    			current = this_UpdateResource_16;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 18 :
                    // InternalQactork.g:1667:3: this_CodeRun_17= ruleCodeRun
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getCodeRunParserRuleCall_17());
                    		
                    pushFollow(FOLLOW_2);
                    this_CodeRun_17=ruleCodeRun();

                    state._fsp--;


                    			current = this_CodeRun_17;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 19 :
                    // InternalQactork.g:1676:3: this_AnyAction_18= ruleAnyAction
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getAnyActionParserRuleCall_18());
                    		
                    pushFollow(FOLLOW_2);
                    this_AnyAction_18=ruleAnyAction();

                    state._fsp--;


                    			current = this_AnyAction_18;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 20 :
                    // InternalQactork.g:1685:3: this_Exec_19= ruleExec
                    {

                    			newCompositeNode(grammarAccess.getStateActionAccess().getExecParserRuleCall_19());
                    		
                    pushFollow(FOLLOW_2);
                    this_Exec_19=ruleExec();

                    state._fsp--;


                    			current = this_Exec_19;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleStateAction"


    // $ANTLR start "entryRuleIfSolvedAction"
    // InternalQactork.g:1697:1: entryRuleIfSolvedAction returns [EObject current=null] : iv_ruleIfSolvedAction= ruleIfSolvedAction EOF ;
    public final EObject entryRuleIfSolvedAction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleIfSolvedAction = null;


        try {
            // InternalQactork.g:1697:55: (iv_ruleIfSolvedAction= ruleIfSolvedAction EOF )
            // InternalQactork.g:1698:2: iv_ruleIfSolvedAction= ruleIfSolvedAction EOF
            {
             newCompositeNode(grammarAccess.getIfSolvedActionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleIfSolvedAction=ruleIfSolvedAction();

            state._fsp--;

             current =iv_ruleIfSolvedAction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleIfSolvedAction"


    // $ANTLR start "ruleIfSolvedAction"
    // InternalQactork.g:1704:1: ruleIfSolvedAction returns [EObject current=null] : ( () otherlv_1= 'ifSolved' otherlv_2= '{' ( (lv_solvedactions_3_0= ruleStateAction ) )* otherlv_4= '}' (otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}' )? ) ;
    public final EObject ruleIfSolvedAction() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        EObject lv_solvedactions_3_0 = null;

        EObject lv_notsolvedactions_7_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:1710:2: ( ( () otherlv_1= 'ifSolved' otherlv_2= '{' ( (lv_solvedactions_3_0= ruleStateAction ) )* otherlv_4= '}' (otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}' )? ) )
            // InternalQactork.g:1711:2: ( () otherlv_1= 'ifSolved' otherlv_2= '{' ( (lv_solvedactions_3_0= ruleStateAction ) )* otherlv_4= '}' (otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}' )? )
            {
            // InternalQactork.g:1711:2: ( () otherlv_1= 'ifSolved' otherlv_2= '{' ( (lv_solvedactions_3_0= ruleStateAction ) )* otherlv_4= '}' (otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}' )? )
            // InternalQactork.g:1712:3: () otherlv_1= 'ifSolved' otherlv_2= '{' ( (lv_solvedactions_3_0= ruleStateAction ) )* otherlv_4= '}' (otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}' )?
            {
            // InternalQactork.g:1712:3: ()
            // InternalQactork.g:1713:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getIfSolvedActionAccess().getIfSolvedActionAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,43,FOLLOW_23); 

            			newLeafNode(otherlv_1, grammarAccess.getIfSolvedActionAccess().getIfSolvedKeyword_1());
            		
            otherlv_2=(Token)match(input,39,FOLLOW_27); 

            			newLeafNode(otherlv_2, grammarAccess.getIfSolvedActionAccess().getLeftCurlyBracketKeyword_2());
            		
            // InternalQactork.g:1727:3: ( (lv_solvedactions_3_0= ruleStateAction ) )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==30||LA19_0==43||(LA19_0>=45 && LA19_0<=47)||LA19_0==50||LA19_0==52||(LA19_0>=55 && LA19_0<=56)||LA19_0==58||(LA19_0>=60 && LA19_0<=62)||(LA19_0>=64 && LA19_0<=72)||(LA19_0>=74 && LA19_0<=75)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // InternalQactork.g:1728:4: (lv_solvedactions_3_0= ruleStateAction )
            	    {
            	    // InternalQactork.g:1728:4: (lv_solvedactions_3_0= ruleStateAction )
            	    // InternalQactork.g:1729:5: lv_solvedactions_3_0= ruleStateAction
            	    {

            	    					newCompositeNode(grammarAccess.getIfSolvedActionAccess().getSolvedactionsStateActionParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_solvedactions_3_0=ruleStateAction();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getIfSolvedActionRule());
            	    					}
            	    					add(
            	    						current,
            	    						"solvedactions",
            	    						lv_solvedactions_3_0,
            	    						"it.unibo.Qactork.StateAction");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            otherlv_4=(Token)match(input,40,FOLLOW_29); 

            			newLeafNode(otherlv_4, grammarAccess.getIfSolvedActionAccess().getRightCurlyBracketKeyword_4());
            		
            // InternalQactork.g:1750:3: (otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}' )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==44) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // InternalQactork.g:1751:4: otherlv_5= 'else' otherlv_6= '{' ( (lv_notsolvedactions_7_0= ruleStateAction ) )* otherlv_8= '}'
                    {
                    otherlv_5=(Token)match(input,44,FOLLOW_23); 

                    				newLeafNode(otherlv_5, grammarAccess.getIfSolvedActionAccess().getElseKeyword_5_0());
                    			
                    otherlv_6=(Token)match(input,39,FOLLOW_27); 

                    				newLeafNode(otherlv_6, grammarAccess.getIfSolvedActionAccess().getLeftCurlyBracketKeyword_5_1());
                    			
                    // InternalQactork.g:1759:4: ( (lv_notsolvedactions_7_0= ruleStateAction ) )*
                    loop20:
                    do {
                        int alt20=2;
                        int LA20_0 = input.LA(1);

                        if ( (LA20_0==30||LA20_0==43||(LA20_0>=45 && LA20_0<=47)||LA20_0==50||LA20_0==52||(LA20_0>=55 && LA20_0<=56)||LA20_0==58||(LA20_0>=60 && LA20_0<=62)||(LA20_0>=64 && LA20_0<=72)||(LA20_0>=74 && LA20_0<=75)) ) {
                            alt20=1;
                        }


                        switch (alt20) {
                    	case 1 :
                    	    // InternalQactork.g:1760:5: (lv_notsolvedactions_7_0= ruleStateAction )
                    	    {
                    	    // InternalQactork.g:1760:5: (lv_notsolvedactions_7_0= ruleStateAction )
                    	    // InternalQactork.g:1761:6: lv_notsolvedactions_7_0= ruleStateAction
                    	    {

                    	    						newCompositeNode(grammarAccess.getIfSolvedActionAccess().getNotsolvedactionsStateActionParserRuleCall_5_2_0());
                    	    					
                    	    pushFollow(FOLLOW_27);
                    	    lv_notsolvedactions_7_0=ruleStateAction();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getIfSolvedActionRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"notsolvedactions",
                    	    							lv_notsolvedactions_7_0,
                    	    							"it.unibo.Qactork.StateAction");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop20;
                        }
                    } while (true);

                    otherlv_8=(Token)match(input,40,FOLLOW_2); 

                    				newLeafNode(otherlv_8, grammarAccess.getIfSolvedActionAccess().getRightCurlyBracketKeyword_5_3());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleIfSolvedAction"


    // $ANTLR start "entryRuleGuardedStateAction"
    // InternalQactork.g:1787:1: entryRuleGuardedStateAction returns [EObject current=null] : iv_ruleGuardedStateAction= ruleGuardedStateAction EOF ;
    public final EObject entryRuleGuardedStateAction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGuardedStateAction = null;


        try {
            // InternalQactork.g:1787:59: (iv_ruleGuardedStateAction= ruleGuardedStateAction EOF )
            // InternalQactork.g:1788:2: iv_ruleGuardedStateAction= ruleGuardedStateAction EOF
            {
             newCompositeNode(grammarAccess.getGuardedStateActionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGuardedStateAction=ruleGuardedStateAction();

            state._fsp--;

             current =iv_ruleGuardedStateAction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGuardedStateAction"


    // $ANTLR start "ruleGuardedStateAction"
    // InternalQactork.g:1794:1: ruleGuardedStateAction returns [EObject current=null] : ( () otherlv_1= 'if' ( (lv_guard_2_0= ruleAnyAction ) ) otherlv_3= '{' ( (lv_okactions_4_0= ruleStateAction ) )* otherlv_5= '}' (otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}' )? ) ;
    public final EObject ruleGuardedStateAction() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_7=null;
        Token otherlv_9=null;
        EObject lv_guard_2_0 = null;

        EObject lv_okactions_4_0 = null;

        EObject lv_koactions_8_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:1800:2: ( ( () otherlv_1= 'if' ( (lv_guard_2_0= ruleAnyAction ) ) otherlv_3= '{' ( (lv_okactions_4_0= ruleStateAction ) )* otherlv_5= '}' (otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}' )? ) )
            // InternalQactork.g:1801:2: ( () otherlv_1= 'if' ( (lv_guard_2_0= ruleAnyAction ) ) otherlv_3= '{' ( (lv_okactions_4_0= ruleStateAction ) )* otherlv_5= '}' (otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}' )? )
            {
            // InternalQactork.g:1801:2: ( () otherlv_1= 'if' ( (lv_guard_2_0= ruleAnyAction ) ) otherlv_3= '{' ( (lv_okactions_4_0= ruleStateAction ) )* otherlv_5= '}' (otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}' )? )
            // InternalQactork.g:1802:3: () otherlv_1= 'if' ( (lv_guard_2_0= ruleAnyAction ) ) otherlv_3= '{' ( (lv_okactions_4_0= ruleStateAction ) )* otherlv_5= '}' (otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}' )?
            {
            // InternalQactork.g:1802:3: ()
            // InternalQactork.g:1803:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getGuardedStateActionAccess().getGuardedStateActionAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,45,FOLLOW_16); 

            			newLeafNode(otherlv_1, grammarAccess.getGuardedStateActionAccess().getIfKeyword_1());
            		
            // InternalQactork.g:1813:3: ( (lv_guard_2_0= ruleAnyAction ) )
            // InternalQactork.g:1814:4: (lv_guard_2_0= ruleAnyAction )
            {
            // InternalQactork.g:1814:4: (lv_guard_2_0= ruleAnyAction )
            // InternalQactork.g:1815:5: lv_guard_2_0= ruleAnyAction
            {

            					newCompositeNode(grammarAccess.getGuardedStateActionAccess().getGuardAnyActionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_23);
            lv_guard_2_0=ruleAnyAction();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getGuardedStateActionRule());
            					}
            					set(
            						current,
            						"guard",
            						lv_guard_2_0,
            						"it.unibo.Qactork.AnyAction");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_3=(Token)match(input,39,FOLLOW_27); 

            			newLeafNode(otherlv_3, grammarAccess.getGuardedStateActionAccess().getLeftCurlyBracketKeyword_3());
            		
            // InternalQactork.g:1836:3: ( (lv_okactions_4_0= ruleStateAction ) )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==30||LA22_0==43||(LA22_0>=45 && LA22_0<=47)||LA22_0==50||LA22_0==52||(LA22_0>=55 && LA22_0<=56)||LA22_0==58||(LA22_0>=60 && LA22_0<=62)||(LA22_0>=64 && LA22_0<=72)||(LA22_0>=74 && LA22_0<=75)) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // InternalQactork.g:1837:4: (lv_okactions_4_0= ruleStateAction )
            	    {
            	    // InternalQactork.g:1837:4: (lv_okactions_4_0= ruleStateAction )
            	    // InternalQactork.g:1838:5: lv_okactions_4_0= ruleStateAction
            	    {

            	    					newCompositeNode(grammarAccess.getGuardedStateActionAccess().getOkactionsStateActionParserRuleCall_4_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_okactions_4_0=ruleStateAction();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getGuardedStateActionRule());
            	    					}
            	    					add(
            	    						current,
            	    						"okactions",
            	    						lv_okactions_4_0,
            	    						"it.unibo.Qactork.StateAction");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            otherlv_5=(Token)match(input,40,FOLLOW_29); 

            			newLeafNode(otherlv_5, grammarAccess.getGuardedStateActionAccess().getRightCurlyBracketKeyword_5());
            		
            // InternalQactork.g:1859:3: (otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}' )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==44) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // InternalQactork.g:1860:4: otherlv_6= 'else' otherlv_7= '{' ( (lv_koactions_8_0= ruleStateAction ) )* otherlv_9= '}'
                    {
                    otherlv_6=(Token)match(input,44,FOLLOW_23); 

                    				newLeafNode(otherlv_6, grammarAccess.getGuardedStateActionAccess().getElseKeyword_6_0());
                    			
                    otherlv_7=(Token)match(input,39,FOLLOW_27); 

                    				newLeafNode(otherlv_7, grammarAccess.getGuardedStateActionAccess().getLeftCurlyBracketKeyword_6_1());
                    			
                    // InternalQactork.g:1868:4: ( (lv_koactions_8_0= ruleStateAction ) )*
                    loop23:
                    do {
                        int alt23=2;
                        int LA23_0 = input.LA(1);

                        if ( (LA23_0==30||LA23_0==43||(LA23_0>=45 && LA23_0<=47)||LA23_0==50||LA23_0==52||(LA23_0>=55 && LA23_0<=56)||LA23_0==58||(LA23_0>=60 && LA23_0<=62)||(LA23_0>=64 && LA23_0<=72)||(LA23_0>=74 && LA23_0<=75)) ) {
                            alt23=1;
                        }


                        switch (alt23) {
                    	case 1 :
                    	    // InternalQactork.g:1869:5: (lv_koactions_8_0= ruleStateAction )
                    	    {
                    	    // InternalQactork.g:1869:5: (lv_koactions_8_0= ruleStateAction )
                    	    // InternalQactork.g:1870:6: lv_koactions_8_0= ruleStateAction
                    	    {

                    	    						newCompositeNode(grammarAccess.getGuardedStateActionAccess().getKoactionsStateActionParserRuleCall_6_2_0());
                    	    					
                    	    pushFollow(FOLLOW_27);
                    	    lv_koactions_8_0=ruleStateAction();

                    	    state._fsp--;


                    	    						if (current==null) {
                    	    							current = createModelElementForParent(grammarAccess.getGuardedStateActionRule());
                    	    						}
                    	    						add(
                    	    							current,
                    	    							"koactions",
                    	    							lv_koactions_8_0,
                    	    							"it.unibo.Qactork.StateAction");
                    	    						afterParserOrEnumRuleCall();
                    	    					

                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop23;
                        }
                    } while (true);

                    otherlv_9=(Token)match(input,40,FOLLOW_2); 

                    				newLeafNode(otherlv_9, grammarAccess.getGuardedStateActionAccess().getRightCurlyBracketKeyword_6_3());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGuardedStateAction"


    // $ANTLR start "entryRulePrintCurMsg"
    // InternalQactork.g:1896:1: entryRulePrintCurMsg returns [EObject current=null] : iv_rulePrintCurMsg= rulePrintCurMsg EOF ;
    public final EObject entryRulePrintCurMsg() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrintCurMsg = null;


        try {
            // InternalQactork.g:1896:52: (iv_rulePrintCurMsg= rulePrintCurMsg EOF )
            // InternalQactork.g:1897:2: iv_rulePrintCurMsg= rulePrintCurMsg EOF
            {
             newCompositeNode(grammarAccess.getPrintCurMsgRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePrintCurMsg=rulePrintCurMsg();

            state._fsp--;

             current =iv_rulePrintCurMsg; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePrintCurMsg"


    // $ANTLR start "rulePrintCurMsg"
    // InternalQactork.g:1903:1: rulePrintCurMsg returns [EObject current=null] : ( () otherlv_1= 'printCurrentMessage' ) ;
    public final EObject rulePrintCurMsg() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;


        	enterRule();

        try {
            // InternalQactork.g:1909:2: ( ( () otherlv_1= 'printCurrentMessage' ) )
            // InternalQactork.g:1910:2: ( () otherlv_1= 'printCurrentMessage' )
            {
            // InternalQactork.g:1910:2: ( () otherlv_1= 'printCurrentMessage' )
            // InternalQactork.g:1911:3: () otherlv_1= 'printCurrentMessage'
            {
            // InternalQactork.g:1911:3: ()
            // InternalQactork.g:1912:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getPrintCurMsgAccess().getPrintCurMsgAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,46,FOLLOW_2); 

            			newLeafNode(otherlv_1, grammarAccess.getPrintCurMsgAccess().getPrintCurrentMessageKeyword_1());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePrintCurMsg"


    // $ANTLR start "entryRulePrint"
    // InternalQactork.g:1926:1: entryRulePrint returns [EObject current=null] : iv_rulePrint= rulePrint EOF ;
    public final EObject entryRulePrint() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePrint = null;


        try {
            // InternalQactork.g:1926:46: (iv_rulePrint= rulePrint EOF )
            // InternalQactork.g:1927:2: iv_rulePrint= rulePrint EOF
            {
             newCompositeNode(grammarAccess.getPrintRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePrint=rulePrint();

            state._fsp--;

             current =iv_rulePrint; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePrint"


    // $ANTLR start "rulePrint"
    // InternalQactork.g:1933:1: rulePrint returns [EObject current=null] : ( () otherlv_1= 'println' otherlv_2= '(' ( (lv_args_3_0= rulePHead ) ) otherlv_4= ')' ) ;
    public final EObject rulePrint() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        EObject lv_args_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:1939:2: ( ( () otherlv_1= 'println' otherlv_2= '(' ( (lv_args_3_0= rulePHead ) ) otherlv_4= ')' ) )
            // InternalQactork.g:1940:2: ( () otherlv_1= 'println' otherlv_2= '(' ( (lv_args_3_0= rulePHead ) ) otherlv_4= ')' )
            {
            // InternalQactork.g:1940:2: ( () otherlv_1= 'println' otherlv_2= '(' ( (lv_args_3_0= rulePHead ) ) otherlv_4= ')' )
            // InternalQactork.g:1941:3: () otherlv_1= 'println' otherlv_2= '(' ( (lv_args_3_0= rulePHead ) ) otherlv_4= ')'
            {
            // InternalQactork.g:1941:3: ()
            // InternalQactork.g:1942:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getPrintAccess().getPrintAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,47,FOLLOW_30); 

            			newLeafNode(otherlv_1, grammarAccess.getPrintAccess().getPrintlnKeyword_1());
            		
            otherlv_2=(Token)match(input,48,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getPrintAccess().getLeftParenthesisKeyword_2());
            		
            // InternalQactork.g:1956:3: ( (lv_args_3_0= rulePHead ) )
            // InternalQactork.g:1957:4: (lv_args_3_0= rulePHead )
            {
            // InternalQactork.g:1957:4: (lv_args_3_0= rulePHead )
            // InternalQactork.g:1958:5: lv_args_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getPrintAccess().getArgsPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_31);
            lv_args_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getPrintRule());
            					}
            					set(
            						current,
            						"args",
            						lv_args_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_4=(Token)match(input,49,FOLLOW_2); 

            			newLeafNode(otherlv_4, grammarAccess.getPrintAccess().getRightParenthesisKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePrint"


    // $ANTLR start "entryRuleSolveGoal"
    // InternalQactork.g:1983:1: entryRuleSolveGoal returns [EObject current=null] : iv_ruleSolveGoal= ruleSolveGoal EOF ;
    public final EObject entryRuleSolveGoal() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleSolveGoal = null;


        try {
            // InternalQactork.g:1983:50: (iv_ruleSolveGoal= ruleSolveGoal EOF )
            // InternalQactork.g:1984:2: iv_ruleSolveGoal= ruleSolveGoal EOF
            {
             newCompositeNode(grammarAccess.getSolveGoalRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleSolveGoal=ruleSolveGoal();

            state._fsp--;

             current =iv_ruleSolveGoal; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleSolveGoal"


    // $ANTLR start "ruleSolveGoal"
    // InternalQactork.g:1990:1: ruleSolveGoal returns [EObject current=null] : ( () otherlv_1= 'solve' otherlv_2= '(' ( (lv_goal_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) ) )? otherlv_6= ')' ) ;
    public final EObject ruleSolveGoal() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        EObject lv_goal_3_0 = null;

        EObject lv_resVar_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:1996:2: ( ( () otherlv_1= 'solve' otherlv_2= '(' ( (lv_goal_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) ) )? otherlv_6= ')' ) )
            // InternalQactork.g:1997:2: ( () otherlv_1= 'solve' otherlv_2= '(' ( (lv_goal_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) ) )? otherlv_6= ')' )
            {
            // InternalQactork.g:1997:2: ( () otherlv_1= 'solve' otherlv_2= '(' ( (lv_goal_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) ) )? otherlv_6= ')' )
            // InternalQactork.g:1998:3: () otherlv_1= 'solve' otherlv_2= '(' ( (lv_goal_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) ) )? otherlv_6= ')'
            {
            // InternalQactork.g:1998:3: ()
            // InternalQactork.g:1999:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getSolveGoalAccess().getSolveGoalAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,50,FOLLOW_30); 

            			newLeafNode(otherlv_1, grammarAccess.getSolveGoalAccess().getSolveKeyword_1());
            		
            otherlv_2=(Token)match(input,48,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getSolveGoalAccess().getLeftParenthesisKeyword_2());
            		
            // InternalQactork.g:2013:3: ( (lv_goal_3_0= rulePHead ) )
            // InternalQactork.g:2014:4: (lv_goal_3_0= rulePHead )
            {
            // InternalQactork.g:2014:4: (lv_goal_3_0= rulePHead )
            // InternalQactork.g:2015:5: lv_goal_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getSolveGoalAccess().getGoalPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_32);
            lv_goal_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getSolveGoalRule());
            					}
            					set(
            						current,
            						"goal",
            						lv_goal_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalQactork.g:2032:3: (otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) ) )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==51) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // InternalQactork.g:2033:4: otherlv_4= ',' ( (lv_resVar_5_0= ruleVariable ) )
                    {
                    otherlv_4=(Token)match(input,51,FOLLOW_33); 

                    				newLeafNode(otherlv_4, grammarAccess.getSolveGoalAccess().getCommaKeyword_4_0());
                    			
                    // InternalQactork.g:2037:4: ( (lv_resVar_5_0= ruleVariable ) )
                    // InternalQactork.g:2038:5: (lv_resVar_5_0= ruleVariable )
                    {
                    // InternalQactork.g:2038:5: (lv_resVar_5_0= ruleVariable )
                    // InternalQactork.g:2039:6: lv_resVar_5_0= ruleVariable
                    {

                    						newCompositeNode(grammarAccess.getSolveGoalAccess().getResVarVariableParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_31);
                    lv_resVar_5_0=ruleVariable();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getSolveGoalRule());
                    						}
                    						set(
                    							current,
                    							"resVar",
                    							lv_resVar_5_0,
                    							"it.unibo.Qactork.Variable");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_6=(Token)match(input,49,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getSolveGoalAccess().getRightParenthesisKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleSolveGoal"


    // $ANTLR start "entryRuleDiscardMsg"
    // InternalQactork.g:2065:1: entryRuleDiscardMsg returns [EObject current=null] : iv_ruleDiscardMsg= ruleDiscardMsg EOF ;
    public final EObject entryRuleDiscardMsg() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDiscardMsg = null;


        try {
            // InternalQactork.g:2065:51: (iv_ruleDiscardMsg= ruleDiscardMsg EOF )
            // InternalQactork.g:2066:2: iv_ruleDiscardMsg= ruleDiscardMsg EOF
            {
             newCompositeNode(grammarAccess.getDiscardMsgRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDiscardMsg=ruleDiscardMsg();

            state._fsp--;

             current =iv_ruleDiscardMsg; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDiscardMsg"


    // $ANTLR start "ruleDiscardMsg"
    // InternalQactork.g:2072:1: ruleDiscardMsg returns [EObject current=null] : ( () otherlv_1= 'discardMsg' ( ( (lv_discard_2_0= 'On' ) ) | otherlv_3= 'Off' ) ) ;
    public final EObject ruleDiscardMsg() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_discard_2_0=null;
        Token otherlv_3=null;


        	enterRule();

        try {
            // InternalQactork.g:2078:2: ( ( () otherlv_1= 'discardMsg' ( ( (lv_discard_2_0= 'On' ) ) | otherlv_3= 'Off' ) ) )
            // InternalQactork.g:2079:2: ( () otherlv_1= 'discardMsg' ( ( (lv_discard_2_0= 'On' ) ) | otherlv_3= 'Off' ) )
            {
            // InternalQactork.g:2079:2: ( () otherlv_1= 'discardMsg' ( ( (lv_discard_2_0= 'On' ) ) | otherlv_3= 'Off' ) )
            // InternalQactork.g:2080:3: () otherlv_1= 'discardMsg' ( ( (lv_discard_2_0= 'On' ) ) | otherlv_3= 'Off' )
            {
            // InternalQactork.g:2080:3: ()
            // InternalQactork.g:2081:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDiscardMsgAccess().getDiscardMsgAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,52,FOLLOW_34); 

            			newLeafNode(otherlv_1, grammarAccess.getDiscardMsgAccess().getDiscardMsgKeyword_1());
            		
            // InternalQactork.g:2091:3: ( ( (lv_discard_2_0= 'On' ) ) | otherlv_3= 'Off' )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==53) ) {
                alt26=1;
            }
            else if ( (LA26_0==54) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;
            }
            switch (alt26) {
                case 1 :
                    // InternalQactork.g:2092:4: ( (lv_discard_2_0= 'On' ) )
                    {
                    // InternalQactork.g:2092:4: ( (lv_discard_2_0= 'On' ) )
                    // InternalQactork.g:2093:5: (lv_discard_2_0= 'On' )
                    {
                    // InternalQactork.g:2093:5: (lv_discard_2_0= 'On' )
                    // InternalQactork.g:2094:6: lv_discard_2_0= 'On'
                    {
                    lv_discard_2_0=(Token)match(input,53,FOLLOW_2); 

                    						newLeafNode(lv_discard_2_0, grammarAccess.getDiscardMsgAccess().getDiscardOnKeyword_2_0_0());
                    					

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getDiscardMsgRule());
                    						}
                    						setWithLastConsumed(current, "discard", lv_discard_2_0 != null, "On");
                    					

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalQactork.g:2107:4: otherlv_3= 'Off'
                    {
                    otherlv_3=(Token)match(input,54,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getDiscardMsgAccess().getOffKeyword_2_1());
                    			

                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDiscardMsg"


    // $ANTLR start "entryRuleMemoTime"
    // InternalQactork.g:2116:1: entryRuleMemoTime returns [EObject current=null] : iv_ruleMemoTime= ruleMemoTime EOF ;
    public final EObject entryRuleMemoTime() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMemoTime = null;


        try {
            // InternalQactork.g:2116:49: (iv_ruleMemoTime= ruleMemoTime EOF )
            // InternalQactork.g:2117:2: iv_ruleMemoTime= ruleMemoTime EOF
            {
             newCompositeNode(grammarAccess.getMemoTimeRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMemoTime=ruleMemoTime();

            state._fsp--;

             current =iv_ruleMemoTime; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMemoTime"


    // $ANTLR start "ruleMemoTime"
    // InternalQactork.g:2123:1: ruleMemoTime returns [EObject current=null] : ( () otherlv_1= 'memoCurrentTime' ( (lv_store_2_0= RULE_VARID ) ) ) ;
    public final EObject ruleMemoTime() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_store_2_0=null;


        	enterRule();

        try {
            // InternalQactork.g:2129:2: ( ( () otherlv_1= 'memoCurrentTime' ( (lv_store_2_0= RULE_VARID ) ) ) )
            // InternalQactork.g:2130:2: ( () otherlv_1= 'memoCurrentTime' ( (lv_store_2_0= RULE_VARID ) ) )
            {
            // InternalQactork.g:2130:2: ( () otherlv_1= 'memoCurrentTime' ( (lv_store_2_0= RULE_VARID ) ) )
            // InternalQactork.g:2131:3: () otherlv_1= 'memoCurrentTime' ( (lv_store_2_0= RULE_VARID ) )
            {
            // InternalQactork.g:2131:3: ()
            // InternalQactork.g:2132:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getMemoTimeAccess().getMemoTimeAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,55,FOLLOW_33); 

            			newLeafNode(otherlv_1, grammarAccess.getMemoTimeAccess().getMemoCurrentTimeKeyword_1());
            		
            // InternalQactork.g:2142:3: ( (lv_store_2_0= RULE_VARID ) )
            // InternalQactork.g:2143:4: (lv_store_2_0= RULE_VARID )
            {
            // InternalQactork.g:2143:4: (lv_store_2_0= RULE_VARID )
            // InternalQactork.g:2144:5: lv_store_2_0= RULE_VARID
            {
            lv_store_2_0=(Token)match(input,RULE_VARID,FOLLOW_2); 

            					newLeafNode(lv_store_2_0, grammarAccess.getMemoTimeAccess().getStoreVARIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMemoTimeRule());
            					}
            					setWithLastConsumed(
            						current,
            						"store",
            						lv_store_2_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMemoTime"


    // $ANTLR start "entryRuleDuration"
    // InternalQactork.g:2164:1: entryRuleDuration returns [EObject current=null] : iv_ruleDuration= ruleDuration EOF ;
    public final EObject entryRuleDuration() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDuration = null;


        try {
            // InternalQactork.g:2164:49: (iv_ruleDuration= ruleDuration EOF )
            // InternalQactork.g:2165:2: iv_ruleDuration= ruleDuration EOF
            {
             newCompositeNode(grammarAccess.getDurationRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDuration=ruleDuration();

            state._fsp--;

             current =iv_ruleDuration; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDuration"


    // $ANTLR start "ruleDuration"
    // InternalQactork.g:2171:1: ruleDuration returns [EObject current=null] : ( () otherlv_1= 'setDuration' ( (lv_store_2_0= RULE_VARID ) ) otherlv_3= 'from' ( (lv_start_4_0= RULE_VARID ) ) ) ;
    public final EObject ruleDuration() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_store_2_0=null;
        Token otherlv_3=null;
        Token lv_start_4_0=null;


        	enterRule();

        try {
            // InternalQactork.g:2177:2: ( ( () otherlv_1= 'setDuration' ( (lv_store_2_0= RULE_VARID ) ) otherlv_3= 'from' ( (lv_start_4_0= RULE_VARID ) ) ) )
            // InternalQactork.g:2178:2: ( () otherlv_1= 'setDuration' ( (lv_store_2_0= RULE_VARID ) ) otherlv_3= 'from' ( (lv_start_4_0= RULE_VARID ) ) )
            {
            // InternalQactork.g:2178:2: ( () otherlv_1= 'setDuration' ( (lv_store_2_0= RULE_VARID ) ) otherlv_3= 'from' ( (lv_start_4_0= RULE_VARID ) ) )
            // InternalQactork.g:2179:3: () otherlv_1= 'setDuration' ( (lv_store_2_0= RULE_VARID ) ) otherlv_3= 'from' ( (lv_start_4_0= RULE_VARID ) )
            {
            // InternalQactork.g:2179:3: ()
            // InternalQactork.g:2180:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getDurationAccess().getDurationAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,56,FOLLOW_33); 

            			newLeafNode(otherlv_1, grammarAccess.getDurationAccess().getSetDurationKeyword_1());
            		
            // InternalQactork.g:2190:3: ( (lv_store_2_0= RULE_VARID ) )
            // InternalQactork.g:2191:4: (lv_store_2_0= RULE_VARID )
            {
            // InternalQactork.g:2191:4: (lv_store_2_0= RULE_VARID )
            // InternalQactork.g:2192:5: lv_store_2_0= RULE_VARID
            {
            lv_store_2_0=(Token)match(input,RULE_VARID,FOLLOW_35); 

            					newLeafNode(lv_store_2_0, grammarAccess.getDurationAccess().getStoreVARIDTerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDurationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"store",
            						lv_store_2_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }

            otherlv_3=(Token)match(input,57,FOLLOW_33); 

            			newLeafNode(otherlv_3, grammarAccess.getDurationAccess().getFromKeyword_3());
            		
            // InternalQactork.g:2212:3: ( (lv_start_4_0= RULE_VARID ) )
            // InternalQactork.g:2213:4: (lv_start_4_0= RULE_VARID )
            {
            // InternalQactork.g:2213:4: (lv_start_4_0= RULE_VARID )
            // InternalQactork.g:2214:5: lv_start_4_0= RULE_VARID
            {
            lv_start_4_0=(Token)match(input,RULE_VARID,FOLLOW_2); 

            					newLeafNode(lv_start_4_0, grammarAccess.getDurationAccess().getStartVARIDTerminalRuleCall_4_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDurationRule());
            					}
            					setWithLastConsumed(
            						current,
            						"start",
            						lv_start_4_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDuration"


    // $ANTLR start "entryRuleForward"
    // InternalQactork.g:2234:1: entryRuleForward returns [EObject current=null] : iv_ruleForward= ruleForward EOF ;
    public final EObject entryRuleForward() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleForward = null;


        try {
            // InternalQactork.g:2234:48: (iv_ruleForward= ruleForward EOF )
            // InternalQactork.g:2235:2: iv_ruleForward= ruleForward EOF
            {
             newCompositeNode(grammarAccess.getForwardRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleForward=ruleForward();

            state._fsp--;

             current =iv_ruleForward; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleForward"


    // $ANTLR start "ruleForward"
    // InternalQactork.g:2241:1: ruleForward returns [EObject current=null] : (otherlv_0= 'forward' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) ;
    public final EObject ruleForward() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_val_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2247:2: ( (otherlv_0= 'forward' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) )
            // InternalQactork.g:2248:2: (otherlv_0= 'forward' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            {
            // InternalQactork.g:2248:2: (otherlv_0= 'forward' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            // InternalQactork.g:2249:3: otherlv_0= 'forward' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,58,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getForwardAccess().getForwardKeyword_0());
            		
            // InternalQactork.g:2253:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:2254:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:2254:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:2255:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getForwardRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_36); 

            					newLeafNode(otherlv_1, grammarAccess.getForwardAccess().getDestQActorDeclarationCrossReference_1_0());
            				

            }


            }

            otherlv_2=(Token)match(input,59,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getForwardAccess().getMKeyword_2());
            		
            // InternalQactork.g:2270:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:2271:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:2271:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:2272:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getForwardRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(otherlv_3, grammarAccess.getForwardAccess().getMsgrefDispatchCrossReference_3_0());
            				

            }


            }

            otherlv_4=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_4, grammarAccess.getForwardAccess().getColonKeyword_4());
            		
            // InternalQactork.g:2287:3: ( (lv_val_5_0= rulePHead ) )
            // InternalQactork.g:2288:4: (lv_val_5_0= rulePHead )
            {
            // InternalQactork.g:2288:4: (lv_val_5_0= rulePHead )
            // InternalQactork.g:2289:5: lv_val_5_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getForwardAccess().getValPHeadParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_2);
            lv_val_5_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getForwardRule());
            					}
            					set(
            						current,
            						"val",
            						lv_val_5_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleForward"


    // $ANTLR start "entryRuleEmit"
    // InternalQactork.g:2310:1: entryRuleEmit returns [EObject current=null] : iv_ruleEmit= ruleEmit EOF ;
    public final EObject entryRuleEmit() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEmit = null;


        try {
            // InternalQactork.g:2310:45: (iv_ruleEmit= ruleEmit EOF )
            // InternalQactork.g:2311:2: iv_ruleEmit= ruleEmit EOF
            {
             newCompositeNode(grammarAccess.getEmitRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEmit=ruleEmit();

            state._fsp--;

             current =iv_ruleEmit; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEmit"


    // $ANTLR start "ruleEmit"
    // InternalQactork.g:2317:1: ruleEmit returns [EObject current=null] : (otherlv_0= 'emit' ( (otherlv_1= RULE_ID ) ) otherlv_2= ':' ( (lv_val_3_0= rulePHead ) ) ) ;
    public final EObject ruleEmit() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        EObject lv_val_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2323:2: ( (otherlv_0= 'emit' ( (otherlv_1= RULE_ID ) ) otherlv_2= ':' ( (lv_val_3_0= rulePHead ) ) ) )
            // InternalQactork.g:2324:2: (otherlv_0= 'emit' ( (otherlv_1= RULE_ID ) ) otherlv_2= ':' ( (lv_val_3_0= rulePHead ) ) )
            {
            // InternalQactork.g:2324:2: (otherlv_0= 'emit' ( (otherlv_1= RULE_ID ) ) otherlv_2= ':' ( (lv_val_3_0= rulePHead ) ) )
            // InternalQactork.g:2325:3: otherlv_0= 'emit' ( (otherlv_1= RULE_ID ) ) otherlv_2= ':' ( (lv_val_3_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,60,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getEmitAccess().getEmitKeyword_0());
            		
            // InternalQactork.g:2329:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:2330:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:2330:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:2331:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEmitRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(otherlv_1, grammarAccess.getEmitAccess().getMsgrefEventCrossReference_1_0());
            				

            }


            }

            otherlv_2=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_2, grammarAccess.getEmitAccess().getColonKeyword_2());
            		
            // InternalQactork.g:2346:3: ( (lv_val_3_0= rulePHead ) )
            // InternalQactork.g:2347:4: (lv_val_3_0= rulePHead )
            {
            // InternalQactork.g:2347:4: (lv_val_3_0= rulePHead )
            // InternalQactork.g:2348:5: lv_val_3_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getEmitAccess().getValPHeadParserRuleCall_3_0());
            				
            pushFollow(FOLLOW_2);
            lv_val_3_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getEmitRule());
            					}
            					set(
            						current,
            						"val",
            						lv_val_3_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEmit"


    // $ANTLR start "entryRuleDemand"
    // InternalQactork.g:2369:1: entryRuleDemand returns [EObject current=null] : iv_ruleDemand= ruleDemand EOF ;
    public final EObject entryRuleDemand() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDemand = null;


        try {
            // InternalQactork.g:2369:47: (iv_ruleDemand= ruleDemand EOF )
            // InternalQactork.g:2370:2: iv_ruleDemand= ruleDemand EOF
            {
             newCompositeNode(grammarAccess.getDemandRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDemand=ruleDemand();

            state._fsp--;

             current =iv_ruleDemand; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDemand"


    // $ANTLR start "ruleDemand"
    // InternalQactork.g:2376:1: ruleDemand returns [EObject current=null] : (otherlv_0= 'request' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) ;
    public final EObject ruleDemand() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_val_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2382:2: ( (otherlv_0= 'request' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) )
            // InternalQactork.g:2383:2: (otherlv_0= 'request' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            {
            // InternalQactork.g:2383:2: (otherlv_0= 'request' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            // InternalQactork.g:2384:3: otherlv_0= 'request' ( (otherlv_1= RULE_ID ) ) otherlv_2= '-m' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,61,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getDemandAccess().getRequestKeyword_0());
            		
            // InternalQactork.g:2388:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:2389:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:2389:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:2390:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDemandRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_36); 

            					newLeafNode(otherlv_1, grammarAccess.getDemandAccess().getDestQActorDeclarationCrossReference_1_0());
            				

            }


            }

            otherlv_2=(Token)match(input,59,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getDemandAccess().getMKeyword_2());
            		
            // InternalQactork.g:2405:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:2406:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:2406:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:2407:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDemandRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(otherlv_3, grammarAccess.getDemandAccess().getMsgrefRequestCrossReference_3_0());
            				

            }


            }

            otherlv_4=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_4, grammarAccess.getDemandAccess().getColonKeyword_4());
            		
            // InternalQactork.g:2422:3: ( (lv_val_5_0= rulePHead ) )
            // InternalQactork.g:2423:4: (lv_val_5_0= rulePHead )
            {
            // InternalQactork.g:2423:4: (lv_val_5_0= rulePHead )
            // InternalQactork.g:2424:5: lv_val_5_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getDemandAccess().getValPHeadParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_2);
            lv_val_5_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDemandRule());
            					}
            					set(
            						current,
            						"val",
            						lv_val_5_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDemand"


    // $ANTLR start "entryRuleAnswer"
    // InternalQactork.g:2445:1: entryRuleAnswer returns [EObject current=null] : iv_ruleAnswer= ruleAnswer EOF ;
    public final EObject entryRuleAnswer() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnswer = null;


        try {
            // InternalQactork.g:2445:47: (iv_ruleAnswer= ruleAnswer EOF )
            // InternalQactork.g:2446:2: iv_ruleAnswer= ruleAnswer EOF
            {
             newCompositeNode(grammarAccess.getAnswerRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAnswer=ruleAnswer();

            state._fsp--;

             current =iv_ruleAnswer; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnswer"


    // $ANTLR start "ruleAnswer"
    // InternalQactork.g:2452:1: ruleAnswer returns [EObject current=null] : (otherlv_0= 'replyTo' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'with' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) ;
    public final EObject ruleAnswer() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_val_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2458:2: ( (otherlv_0= 'replyTo' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'with' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) )
            // InternalQactork.g:2459:2: (otherlv_0= 'replyTo' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'with' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            {
            // InternalQactork.g:2459:2: (otherlv_0= 'replyTo' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'with' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            // InternalQactork.g:2460:3: otherlv_0= 'replyTo' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'with' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,62,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getAnswerAccess().getReplyToKeyword_0());
            		
            // InternalQactork.g:2464:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:2465:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:2465:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:2466:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAnswerRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_37); 

            					newLeafNode(otherlv_1, grammarAccess.getAnswerAccess().getReqrefRequestCrossReference_1_0());
            				

            }


            }

            otherlv_2=(Token)match(input,63,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getAnswerAccess().getWithKeyword_2());
            		
            // InternalQactork.g:2481:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:2482:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:2482:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:2483:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAnswerRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(otherlv_3, grammarAccess.getAnswerAccess().getMsgrefReplyCrossReference_3_0());
            				

            }


            }

            otherlv_4=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_4, grammarAccess.getAnswerAccess().getColonKeyword_4());
            		
            // InternalQactork.g:2498:3: ( (lv_val_5_0= rulePHead ) )
            // InternalQactork.g:2499:4: (lv_val_5_0= rulePHead )
            {
            // InternalQactork.g:2499:4: (lv_val_5_0= rulePHead )
            // InternalQactork.g:2500:5: lv_val_5_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getAnswerAccess().getValPHeadParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_2);
            lv_val_5_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getAnswerRule());
            					}
            					set(
            						current,
            						"val",
            						lv_val_5_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnswer"


    // $ANTLR start "entryRuleReplyReq"
    // InternalQactork.g:2521:1: entryRuleReplyReq returns [EObject current=null] : iv_ruleReplyReq= ruleReplyReq EOF ;
    public final EObject entryRuleReplyReq() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReplyReq = null;


        try {
            // InternalQactork.g:2521:49: (iv_ruleReplyReq= ruleReplyReq EOF )
            // InternalQactork.g:2522:2: iv_ruleReplyReq= ruleReplyReq EOF
            {
             newCompositeNode(grammarAccess.getReplyReqRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleReplyReq=ruleReplyReq();

            state._fsp--;

             current =iv_ruleReplyReq; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleReplyReq"


    // $ANTLR start "ruleReplyReq"
    // InternalQactork.g:2528:1: ruleReplyReq returns [EObject current=null] : (otherlv_0= 'askFor' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'request' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) ;
    public final EObject ruleReplyReq() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        EObject lv_val_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2534:2: ( (otherlv_0= 'askFor' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'request' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) ) )
            // InternalQactork.g:2535:2: (otherlv_0= 'askFor' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'request' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            {
            // InternalQactork.g:2535:2: (otherlv_0= 'askFor' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'request' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) ) )
            // InternalQactork.g:2536:3: otherlv_0= 'askFor' ( (otherlv_1= RULE_ID ) ) otherlv_2= 'request' ( (otherlv_3= RULE_ID ) ) otherlv_4= ':' ( (lv_val_5_0= rulePHead ) )
            {
            otherlv_0=(Token)match(input,64,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getReplyReqAccess().getAskForKeyword_0());
            		
            // InternalQactork.g:2540:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:2541:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:2541:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:2542:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReplyReqRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_38); 

            					newLeafNode(otherlv_1, grammarAccess.getReplyReqAccess().getReqrefRequestCrossReference_1_0());
            				

            }


            }

            otherlv_2=(Token)match(input,61,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getReplyReqAccess().getRequestKeyword_2());
            		
            // InternalQactork.g:2557:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:2558:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:2558:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:2559:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReplyReqRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(otherlv_3, grammarAccess.getReplyReqAccess().getMsgrefRequestCrossReference_3_0());
            				

            }


            }

            otherlv_4=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_4, grammarAccess.getReplyReqAccess().getColonKeyword_4());
            		
            // InternalQactork.g:2574:3: ( (lv_val_5_0= rulePHead ) )
            // InternalQactork.g:2575:4: (lv_val_5_0= rulePHead )
            {
            // InternalQactork.g:2575:4: (lv_val_5_0= rulePHead )
            // InternalQactork.g:2576:5: lv_val_5_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getReplyReqAccess().getValPHeadParserRuleCall_5_0());
            				
            pushFollow(FOLLOW_2);
            lv_val_5_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getReplyReqRule());
            					}
            					set(
            						current,
            						"val",
            						lv_val_5_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleReplyReq"


    // $ANTLR start "entryRuleDelay"
    // InternalQactork.g:2597:1: entryRuleDelay returns [EObject current=null] : iv_ruleDelay= ruleDelay EOF ;
    public final EObject entryRuleDelay() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDelay = null;


        try {
            // InternalQactork.g:2597:46: (iv_ruleDelay= ruleDelay EOF )
            // InternalQactork.g:2598:2: iv_ruleDelay= ruleDelay EOF
            {
             newCompositeNode(grammarAccess.getDelayRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDelay=ruleDelay();

            state._fsp--;

             current =iv_ruleDelay; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDelay"


    // $ANTLR start "ruleDelay"
    // InternalQactork.g:2604:1: ruleDelay returns [EObject current=null] : (this_DelayInt_0= ruleDelayInt | this_DelayVar_1= ruleDelayVar | this_DelayVref_2= ruleDelayVref | this_DelaySol_3= ruleDelaySol ) ;
    public final EObject ruleDelay() throws RecognitionException {
        EObject current = null;

        EObject this_DelayInt_0 = null;

        EObject this_DelayVar_1 = null;

        EObject this_DelayVref_2 = null;

        EObject this_DelaySol_3 = null;



        	enterRule();

        try {
            // InternalQactork.g:2610:2: ( (this_DelayInt_0= ruleDelayInt | this_DelayVar_1= ruleDelayVar | this_DelayVref_2= ruleDelayVref | this_DelaySol_3= ruleDelaySol ) )
            // InternalQactork.g:2611:2: (this_DelayInt_0= ruleDelayInt | this_DelayVar_1= ruleDelayVar | this_DelayVref_2= ruleDelayVref | this_DelaySol_3= ruleDelaySol )
            {
            // InternalQactork.g:2611:2: (this_DelayInt_0= ruleDelayInt | this_DelayVar_1= ruleDelayVar | this_DelayVref_2= ruleDelayVref | this_DelaySol_3= ruleDelaySol )
            int alt27=4;
            switch ( input.LA(1) ) {
            case 65:
                {
                alt27=1;
                }
                break;
            case 66:
                {
                alt27=2;
                }
                break;
            case 67:
                {
                alt27=3;
                }
                break;
            case 68:
                {
                alt27=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // InternalQactork.g:2612:3: this_DelayInt_0= ruleDelayInt
                    {

                    			newCompositeNode(grammarAccess.getDelayAccess().getDelayIntParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_DelayInt_0=ruleDelayInt();

                    state._fsp--;


                    			current = this_DelayInt_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:2621:3: this_DelayVar_1= ruleDelayVar
                    {

                    			newCompositeNode(grammarAccess.getDelayAccess().getDelayVarParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_DelayVar_1=ruleDelayVar();

                    state._fsp--;


                    			current = this_DelayVar_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:2630:3: this_DelayVref_2= ruleDelayVref
                    {

                    			newCompositeNode(grammarAccess.getDelayAccess().getDelayVrefParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_DelayVref_2=ruleDelayVref();

                    state._fsp--;


                    			current = this_DelayVref_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalQactork.g:2639:3: this_DelaySol_3= ruleDelaySol
                    {

                    			newCompositeNode(grammarAccess.getDelayAccess().getDelaySolParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_DelaySol_3=ruleDelaySol();

                    state._fsp--;


                    			current = this_DelaySol_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDelay"


    // $ANTLR start "entryRuleDelayInt"
    // InternalQactork.g:2651:1: entryRuleDelayInt returns [EObject current=null] : iv_ruleDelayInt= ruleDelayInt EOF ;
    public final EObject entryRuleDelayInt() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDelayInt = null;


        try {
            // InternalQactork.g:2651:49: (iv_ruleDelayInt= ruleDelayInt EOF )
            // InternalQactork.g:2652:2: iv_ruleDelayInt= ruleDelayInt EOF
            {
             newCompositeNode(grammarAccess.getDelayIntRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDelayInt=ruleDelayInt();

            state._fsp--;

             current =iv_ruleDelayInt; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDelayInt"


    // $ANTLR start "ruleDelayInt"
    // InternalQactork.g:2658:1: ruleDelayInt returns [EObject current=null] : (otherlv_0= 'delay' ( (lv_time_1_0= RULE_INT ) ) ) ;
    public final EObject ruleDelayInt() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_time_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:2664:2: ( (otherlv_0= 'delay' ( (lv_time_1_0= RULE_INT ) ) ) )
            // InternalQactork.g:2665:2: (otherlv_0= 'delay' ( (lv_time_1_0= RULE_INT ) ) )
            {
            // InternalQactork.g:2665:2: (otherlv_0= 'delay' ( (lv_time_1_0= RULE_INT ) ) )
            // InternalQactork.g:2666:3: otherlv_0= 'delay' ( (lv_time_1_0= RULE_INT ) )
            {
            otherlv_0=(Token)match(input,65,FOLLOW_12); 

            			newLeafNode(otherlv_0, grammarAccess.getDelayIntAccess().getDelayKeyword_0());
            		
            // InternalQactork.g:2670:3: ( (lv_time_1_0= RULE_INT ) )
            // InternalQactork.g:2671:4: (lv_time_1_0= RULE_INT )
            {
            // InternalQactork.g:2671:4: (lv_time_1_0= RULE_INT )
            // InternalQactork.g:2672:5: lv_time_1_0= RULE_INT
            {
            lv_time_1_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_time_1_0, grammarAccess.getDelayIntAccess().getTimeINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getDelayIntRule());
            					}
            					setWithLastConsumed(
            						current,
            						"time",
            						lv_time_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDelayInt"


    // $ANTLR start "entryRuleDelayVar"
    // InternalQactork.g:2692:1: entryRuleDelayVar returns [EObject current=null] : iv_ruleDelayVar= ruleDelayVar EOF ;
    public final EObject entryRuleDelayVar() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDelayVar = null;


        try {
            // InternalQactork.g:2692:49: (iv_ruleDelayVar= ruleDelayVar EOF )
            // InternalQactork.g:2693:2: iv_ruleDelayVar= ruleDelayVar EOF
            {
             newCompositeNode(grammarAccess.getDelayVarRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDelayVar=ruleDelayVar();

            state._fsp--;

             current =iv_ruleDelayVar; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDelayVar"


    // $ANTLR start "ruleDelayVar"
    // InternalQactork.g:2699:1: ruleDelayVar returns [EObject current=null] : (otherlv_0= 'delayVar' ( (lv_refvar_1_0= ruleVariable ) ) ) ;
    public final EObject ruleDelayVar() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_refvar_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2705:2: ( (otherlv_0= 'delayVar' ( (lv_refvar_1_0= ruleVariable ) ) ) )
            // InternalQactork.g:2706:2: (otherlv_0= 'delayVar' ( (lv_refvar_1_0= ruleVariable ) ) )
            {
            // InternalQactork.g:2706:2: (otherlv_0= 'delayVar' ( (lv_refvar_1_0= ruleVariable ) ) )
            // InternalQactork.g:2707:3: otherlv_0= 'delayVar' ( (lv_refvar_1_0= ruleVariable ) )
            {
            otherlv_0=(Token)match(input,66,FOLLOW_33); 

            			newLeafNode(otherlv_0, grammarAccess.getDelayVarAccess().getDelayVarKeyword_0());
            		
            // InternalQactork.g:2711:3: ( (lv_refvar_1_0= ruleVariable ) )
            // InternalQactork.g:2712:4: (lv_refvar_1_0= ruleVariable )
            {
            // InternalQactork.g:2712:4: (lv_refvar_1_0= ruleVariable )
            // InternalQactork.g:2713:5: lv_refvar_1_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getDelayVarAccess().getRefvarVariableParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_refvar_1_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDelayVarRule());
            					}
            					set(
            						current,
            						"refvar",
            						lv_refvar_1_0,
            						"it.unibo.Qactork.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDelayVar"


    // $ANTLR start "entryRuleDelayVref"
    // InternalQactork.g:2734:1: entryRuleDelayVref returns [EObject current=null] : iv_ruleDelayVref= ruleDelayVref EOF ;
    public final EObject entryRuleDelayVref() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDelayVref = null;


        try {
            // InternalQactork.g:2734:50: (iv_ruleDelayVref= ruleDelayVref EOF )
            // InternalQactork.g:2735:2: iv_ruleDelayVref= ruleDelayVref EOF
            {
             newCompositeNode(grammarAccess.getDelayVrefRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDelayVref=ruleDelayVref();

            state._fsp--;

             current =iv_ruleDelayVref; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDelayVref"


    // $ANTLR start "ruleDelayVref"
    // InternalQactork.g:2741:1: ruleDelayVref returns [EObject current=null] : (otherlv_0= 'delayVarRef' ( (lv_reftime_1_0= ruleVarRef ) ) ) ;
    public final EObject ruleDelayVref() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_reftime_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2747:2: ( (otherlv_0= 'delayVarRef' ( (lv_reftime_1_0= ruleVarRef ) ) ) )
            // InternalQactork.g:2748:2: (otherlv_0= 'delayVarRef' ( (lv_reftime_1_0= ruleVarRef ) ) )
            {
            // InternalQactork.g:2748:2: (otherlv_0= 'delayVarRef' ( (lv_reftime_1_0= ruleVarRef ) ) )
            // InternalQactork.g:2749:3: otherlv_0= 'delayVarRef' ( (lv_reftime_1_0= ruleVarRef ) )
            {
            otherlv_0=(Token)match(input,67,FOLLOW_39); 

            			newLeafNode(otherlv_0, grammarAccess.getDelayVrefAccess().getDelayVarRefKeyword_0());
            		
            // InternalQactork.g:2753:3: ( (lv_reftime_1_0= ruleVarRef ) )
            // InternalQactork.g:2754:4: (lv_reftime_1_0= ruleVarRef )
            {
            // InternalQactork.g:2754:4: (lv_reftime_1_0= ruleVarRef )
            // InternalQactork.g:2755:5: lv_reftime_1_0= ruleVarRef
            {

            					newCompositeNode(grammarAccess.getDelayVrefAccess().getReftimeVarRefParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_reftime_1_0=ruleVarRef();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDelayVrefRule());
            					}
            					set(
            						current,
            						"reftime",
            						lv_reftime_1_0,
            						"it.unibo.Qactork.VarRef");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDelayVref"


    // $ANTLR start "entryRuleDelaySol"
    // InternalQactork.g:2776:1: entryRuleDelaySol returns [EObject current=null] : iv_ruleDelaySol= ruleDelaySol EOF ;
    public final EObject entryRuleDelaySol() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleDelaySol = null;


        try {
            // InternalQactork.g:2776:49: (iv_ruleDelaySol= ruleDelaySol EOF )
            // InternalQactork.g:2777:2: iv_ruleDelaySol= ruleDelaySol EOF
            {
             newCompositeNode(grammarAccess.getDelaySolRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleDelaySol=ruleDelaySol();

            state._fsp--;

             current =iv_ruleDelaySol; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleDelaySol"


    // $ANTLR start "ruleDelaySol"
    // InternalQactork.g:2783:1: ruleDelaySol returns [EObject current=null] : (otherlv_0= 'delaySol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) ) ;
    public final EObject ruleDelaySol() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_refsoltime_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2789:2: ( (otherlv_0= 'delaySol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) ) )
            // InternalQactork.g:2790:2: (otherlv_0= 'delaySol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) )
            {
            // InternalQactork.g:2790:2: (otherlv_0= 'delaySol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) )
            // InternalQactork.g:2791:3: otherlv_0= 'delaySol' ( (lv_refsoltime_1_0= ruleVarSolRef ) )
            {
            otherlv_0=(Token)match(input,68,FOLLOW_40); 

            			newLeafNode(otherlv_0, grammarAccess.getDelaySolAccess().getDelaySolKeyword_0());
            		
            // InternalQactork.g:2795:3: ( (lv_refsoltime_1_0= ruleVarSolRef ) )
            // InternalQactork.g:2796:4: (lv_refsoltime_1_0= ruleVarSolRef )
            {
            // InternalQactork.g:2796:4: (lv_refsoltime_1_0= ruleVarSolRef )
            // InternalQactork.g:2797:5: lv_refsoltime_1_0= ruleVarSolRef
            {

            					newCompositeNode(grammarAccess.getDelaySolAccess().getRefsoltimeVarSolRefParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_refsoltime_1_0=ruleVarSolRef();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getDelaySolRule());
            					}
            					set(
            						current,
            						"refsoltime",
            						lv_refsoltime_1_0,
            						"it.unibo.Qactork.VarSolRef");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleDelaySol"


    // $ANTLR start "entryRuleMsgCond"
    // InternalQactork.g:2818:1: entryRuleMsgCond returns [EObject current=null] : iv_ruleMsgCond= ruleMsgCond EOF ;
    public final EObject entryRuleMsgCond() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMsgCond = null;


        try {
            // InternalQactork.g:2818:48: (iv_ruleMsgCond= ruleMsgCond EOF )
            // InternalQactork.g:2819:2: iv_ruleMsgCond= ruleMsgCond EOF
            {
             newCompositeNode(grammarAccess.getMsgCondRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMsgCond=ruleMsgCond();

            state._fsp--;

             current =iv_ruleMsgCond; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMsgCond"


    // $ANTLR start "ruleMsgCond"
    // InternalQactork.g:2825:1: ruleMsgCond returns [EObject current=null] : (otherlv_0= 'onMsg' otherlv_1= '(' ( (otherlv_2= RULE_ID ) ) otherlv_3= ':' ( (lv_msg_4_0= rulePHead ) ) otherlv_5= ')' otherlv_6= '{' ( (lv_condactions_7_0= ruleStateAction ) )* otherlv_8= '}' (otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) ) )? ) ;
    public final EObject ruleMsgCond() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        Token otherlv_9=null;
        EObject lv_msg_4_0 = null;

        EObject lv_condactions_7_0 = null;

        EObject lv_ifnot_10_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2831:2: ( (otherlv_0= 'onMsg' otherlv_1= '(' ( (otherlv_2= RULE_ID ) ) otherlv_3= ':' ( (lv_msg_4_0= rulePHead ) ) otherlv_5= ')' otherlv_6= '{' ( (lv_condactions_7_0= ruleStateAction ) )* otherlv_8= '}' (otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) ) )? ) )
            // InternalQactork.g:2832:2: (otherlv_0= 'onMsg' otherlv_1= '(' ( (otherlv_2= RULE_ID ) ) otherlv_3= ':' ( (lv_msg_4_0= rulePHead ) ) otherlv_5= ')' otherlv_6= '{' ( (lv_condactions_7_0= ruleStateAction ) )* otherlv_8= '}' (otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) ) )? )
            {
            // InternalQactork.g:2832:2: (otherlv_0= 'onMsg' otherlv_1= '(' ( (otherlv_2= RULE_ID ) ) otherlv_3= ':' ( (lv_msg_4_0= rulePHead ) ) otherlv_5= ')' otherlv_6= '{' ( (lv_condactions_7_0= ruleStateAction ) )* otherlv_8= '}' (otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) ) )? )
            // InternalQactork.g:2833:3: otherlv_0= 'onMsg' otherlv_1= '(' ( (otherlv_2= RULE_ID ) ) otherlv_3= ':' ( (lv_msg_4_0= rulePHead ) ) otherlv_5= ')' otherlv_6= '{' ( (lv_condactions_7_0= ruleStateAction ) )* otherlv_8= '}' (otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) ) )?
            {
            otherlv_0=(Token)match(input,69,FOLLOW_30); 

            			newLeafNode(otherlv_0, grammarAccess.getMsgCondAccess().getOnMsgKeyword_0());
            		
            otherlv_1=(Token)match(input,48,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getMsgCondAccess().getLeftParenthesisKeyword_1());
            		
            // InternalQactork.g:2841:3: ( (otherlv_2= RULE_ID ) )
            // InternalQactork.g:2842:4: (otherlv_2= RULE_ID )
            {
            // InternalQactork.g:2842:4: (otherlv_2= RULE_ID )
            // InternalQactork.g:2843:5: otherlv_2= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMsgCondRule());
            					}
            				
            otherlv_2=(Token)match(input,RULE_ID,FOLLOW_11); 

            					newLeafNode(otherlv_2, grammarAccess.getMsgCondAccess().getMessageMessageCrossReference_2_0());
            				

            }


            }

            otherlv_3=(Token)match(input,18,FOLLOW_14); 

            			newLeafNode(otherlv_3, grammarAccess.getMsgCondAccess().getColonKeyword_3());
            		
            // InternalQactork.g:2858:3: ( (lv_msg_4_0= rulePHead ) )
            // InternalQactork.g:2859:4: (lv_msg_4_0= rulePHead )
            {
            // InternalQactork.g:2859:4: (lv_msg_4_0= rulePHead )
            // InternalQactork.g:2860:5: lv_msg_4_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getMsgCondAccess().getMsgPHeadParserRuleCall_4_0());
            				
            pushFollow(FOLLOW_31);
            lv_msg_4_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getMsgCondRule());
            					}
            					set(
            						current,
            						"msg",
            						lv_msg_4_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_5=(Token)match(input,49,FOLLOW_23); 

            			newLeafNode(otherlv_5, grammarAccess.getMsgCondAccess().getRightParenthesisKeyword_5());
            		
            otherlv_6=(Token)match(input,39,FOLLOW_27); 

            			newLeafNode(otherlv_6, grammarAccess.getMsgCondAccess().getLeftCurlyBracketKeyword_6());
            		
            // InternalQactork.g:2885:3: ( (lv_condactions_7_0= ruleStateAction ) )*
            loop28:
            do {
                int alt28=2;
                int LA28_0 = input.LA(1);

                if ( (LA28_0==30||LA28_0==43||(LA28_0>=45 && LA28_0<=47)||LA28_0==50||LA28_0==52||(LA28_0>=55 && LA28_0<=56)||LA28_0==58||(LA28_0>=60 && LA28_0<=62)||(LA28_0>=64 && LA28_0<=72)||(LA28_0>=74 && LA28_0<=75)) ) {
                    alt28=1;
                }


                switch (alt28) {
            	case 1 :
            	    // InternalQactork.g:2886:4: (lv_condactions_7_0= ruleStateAction )
            	    {
            	    // InternalQactork.g:2886:4: (lv_condactions_7_0= ruleStateAction )
            	    // InternalQactork.g:2887:5: lv_condactions_7_0= ruleStateAction
            	    {

            	    					newCompositeNode(grammarAccess.getMsgCondAccess().getCondactionsStateActionParserRuleCall_7_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_condactions_7_0=ruleStateAction();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getMsgCondRule());
            	    					}
            	    					add(
            	    						current,
            	    						"condactions",
            	    						lv_condactions_7_0,
            	    						"it.unibo.Qactork.StateAction");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop28;
                }
            } while (true);

            otherlv_8=(Token)match(input,40,FOLLOW_29); 

            			newLeafNode(otherlv_8, grammarAccess.getMsgCondAccess().getRightCurlyBracketKeyword_8());
            		
            // InternalQactork.g:2908:3: (otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) ) )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==44) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // InternalQactork.g:2909:4: otherlv_9= 'else' ( (lv_ifnot_10_0= ruleNoMsgCond ) )
                    {
                    otherlv_9=(Token)match(input,44,FOLLOW_23); 

                    				newLeafNode(otherlv_9, grammarAccess.getMsgCondAccess().getElseKeyword_9_0());
                    			
                    // InternalQactork.g:2913:4: ( (lv_ifnot_10_0= ruleNoMsgCond ) )
                    // InternalQactork.g:2914:5: (lv_ifnot_10_0= ruleNoMsgCond )
                    {
                    // InternalQactork.g:2914:5: (lv_ifnot_10_0= ruleNoMsgCond )
                    // InternalQactork.g:2915:6: lv_ifnot_10_0= ruleNoMsgCond
                    {

                    						newCompositeNode(grammarAccess.getMsgCondAccess().getIfnotNoMsgCondParserRuleCall_9_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_ifnot_10_0=ruleNoMsgCond();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getMsgCondRule());
                    						}
                    						set(
                    							current,
                    							"ifnot",
                    							lv_ifnot_10_0,
                    							"it.unibo.Qactork.NoMsgCond");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMsgCond"


    // $ANTLR start "entryRuleEndActor"
    // InternalQactork.g:2937:1: entryRuleEndActor returns [EObject current=null] : iv_ruleEndActor= ruleEndActor EOF ;
    public final EObject entryRuleEndActor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEndActor = null;


        try {
            // InternalQactork.g:2937:49: (iv_ruleEndActor= ruleEndActor EOF )
            // InternalQactork.g:2938:2: iv_ruleEndActor= ruleEndActor EOF
            {
             newCompositeNode(grammarAccess.getEndActorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEndActor=ruleEndActor();

            state._fsp--;

             current =iv_ruleEndActor; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEndActor"


    // $ANTLR start "ruleEndActor"
    // InternalQactork.g:2944:1: ruleEndActor returns [EObject current=null] : (otherlv_0= 'terminate' ( (lv_arg_1_0= RULE_INT ) ) ) ;
    public final EObject ruleEndActor() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_arg_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:2950:2: ( (otherlv_0= 'terminate' ( (lv_arg_1_0= RULE_INT ) ) ) )
            // InternalQactork.g:2951:2: (otherlv_0= 'terminate' ( (lv_arg_1_0= RULE_INT ) ) )
            {
            // InternalQactork.g:2951:2: (otherlv_0= 'terminate' ( (lv_arg_1_0= RULE_INT ) ) )
            // InternalQactork.g:2952:3: otherlv_0= 'terminate' ( (lv_arg_1_0= RULE_INT ) )
            {
            otherlv_0=(Token)match(input,70,FOLLOW_12); 

            			newLeafNode(otherlv_0, grammarAccess.getEndActorAccess().getTerminateKeyword_0());
            		
            // InternalQactork.g:2956:3: ( (lv_arg_1_0= RULE_INT ) )
            // InternalQactork.g:2957:4: (lv_arg_1_0= RULE_INT )
            {
            // InternalQactork.g:2957:4: (lv_arg_1_0= RULE_INT )
            // InternalQactork.g:2958:5: lv_arg_1_0= RULE_INT
            {
            lv_arg_1_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            					newLeafNode(lv_arg_1_0, grammarAccess.getEndActorAccess().getArgINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEndActorRule());
            					}
            					setWithLastConsumed(
            						current,
            						"arg",
            						lv_arg_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEndActor"


    // $ANTLR start "entryRuleUpdateResource"
    // InternalQactork.g:2978:1: entryRuleUpdateResource returns [EObject current=null] : iv_ruleUpdateResource= ruleUpdateResource EOF ;
    public final EObject entryRuleUpdateResource() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleUpdateResource = null;


        try {
            // InternalQactork.g:2978:55: (iv_ruleUpdateResource= ruleUpdateResource EOF )
            // InternalQactork.g:2979:2: iv_ruleUpdateResource= ruleUpdateResource EOF
            {
             newCompositeNode(grammarAccess.getUpdateResourceRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleUpdateResource=ruleUpdateResource();

            state._fsp--;

             current =iv_ruleUpdateResource; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleUpdateResource"


    // $ANTLR start "ruleUpdateResource"
    // InternalQactork.g:2985:1: ruleUpdateResource returns [EObject current=null] : ( () otherlv_1= 'updateResource' ( (lv_val_2_0= ruleAnyAction ) ) ) ;
    public final EObject ruleUpdateResource() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        EObject lv_val_2_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:2991:2: ( ( () otherlv_1= 'updateResource' ( (lv_val_2_0= ruleAnyAction ) ) ) )
            // InternalQactork.g:2992:2: ( () otherlv_1= 'updateResource' ( (lv_val_2_0= ruleAnyAction ) ) )
            {
            // InternalQactork.g:2992:2: ( () otherlv_1= 'updateResource' ( (lv_val_2_0= ruleAnyAction ) ) )
            // InternalQactork.g:2993:3: () otherlv_1= 'updateResource' ( (lv_val_2_0= ruleAnyAction ) )
            {
            // InternalQactork.g:2993:3: ()
            // InternalQactork.g:2994:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getUpdateResourceAccess().getUpdateResourceAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,71,FOLLOW_16); 

            			newLeafNode(otherlv_1, grammarAccess.getUpdateResourceAccess().getUpdateResourceKeyword_1());
            		
            // InternalQactork.g:3004:3: ( (lv_val_2_0= ruleAnyAction ) )
            // InternalQactork.g:3005:4: (lv_val_2_0= ruleAnyAction )
            {
            // InternalQactork.g:3005:4: (lv_val_2_0= ruleAnyAction )
            // InternalQactork.g:3006:5: lv_val_2_0= ruleAnyAction
            {

            					newCompositeNode(grammarAccess.getUpdateResourceAccess().getValAnyActionParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_2);
            lv_val_2_0=ruleAnyAction();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getUpdateResourceRule());
            					}
            					set(
            						current,
            						"val",
            						lv_val_2_0,
            						"it.unibo.Qactork.AnyAction");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleUpdateResource"


    // $ANTLR start "entryRuleNoMsgCond"
    // InternalQactork.g:3027:1: entryRuleNoMsgCond returns [EObject current=null] : iv_ruleNoMsgCond= ruleNoMsgCond EOF ;
    public final EObject entryRuleNoMsgCond() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNoMsgCond = null;


        try {
            // InternalQactork.g:3027:50: (iv_ruleNoMsgCond= ruleNoMsgCond EOF )
            // InternalQactork.g:3028:2: iv_ruleNoMsgCond= ruleNoMsgCond EOF
            {
             newCompositeNode(grammarAccess.getNoMsgCondRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNoMsgCond=ruleNoMsgCond();

            state._fsp--;

             current =iv_ruleNoMsgCond; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNoMsgCond"


    // $ANTLR start "ruleNoMsgCond"
    // InternalQactork.g:3034:1: ruleNoMsgCond returns [EObject current=null] : ( () otherlv_1= '{' ( (lv_notcondactions_2_0= ruleStateAction ) )* otherlv_3= '}' ) ;
    public final EObject ruleNoMsgCond() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token otherlv_3=null;
        EObject lv_notcondactions_2_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3040:2: ( ( () otherlv_1= '{' ( (lv_notcondactions_2_0= ruleStateAction ) )* otherlv_3= '}' ) )
            // InternalQactork.g:3041:2: ( () otherlv_1= '{' ( (lv_notcondactions_2_0= ruleStateAction ) )* otherlv_3= '}' )
            {
            // InternalQactork.g:3041:2: ( () otherlv_1= '{' ( (lv_notcondactions_2_0= ruleStateAction ) )* otherlv_3= '}' )
            // InternalQactork.g:3042:3: () otherlv_1= '{' ( (lv_notcondactions_2_0= ruleStateAction ) )* otherlv_3= '}'
            {
            // InternalQactork.g:3042:3: ()
            // InternalQactork.g:3043:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getNoMsgCondAccess().getNoMsgCondAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,39,FOLLOW_27); 

            			newLeafNode(otherlv_1, grammarAccess.getNoMsgCondAccess().getLeftCurlyBracketKeyword_1());
            		
            // InternalQactork.g:3053:3: ( (lv_notcondactions_2_0= ruleStateAction ) )*
            loop30:
            do {
                int alt30=2;
                int LA30_0 = input.LA(1);

                if ( (LA30_0==30||LA30_0==43||(LA30_0>=45 && LA30_0<=47)||LA30_0==50||LA30_0==52||(LA30_0>=55 && LA30_0<=56)||LA30_0==58||(LA30_0>=60 && LA30_0<=62)||(LA30_0>=64 && LA30_0<=72)||(LA30_0>=74 && LA30_0<=75)) ) {
                    alt30=1;
                }


                switch (alt30) {
            	case 1 :
            	    // InternalQactork.g:3054:4: (lv_notcondactions_2_0= ruleStateAction )
            	    {
            	    // InternalQactork.g:3054:4: (lv_notcondactions_2_0= ruleStateAction )
            	    // InternalQactork.g:3055:5: lv_notcondactions_2_0= ruleStateAction
            	    {

            	    					newCompositeNode(grammarAccess.getNoMsgCondAccess().getNotcondactionsStateActionParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_27);
            	    lv_notcondactions_2_0=ruleStateAction();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getNoMsgCondRule());
            	    					}
            	    					add(
            	    						current,
            	    						"notcondactions",
            	    						lv_notcondactions_2_0,
            	    						"it.unibo.Qactork.StateAction");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop30;
                }
            } while (true);

            otherlv_3=(Token)match(input,40,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getNoMsgCondAccess().getRightCurlyBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNoMsgCond"


    // $ANTLR start "entryRuleAnyAction"
    // InternalQactork.g:3080:1: entryRuleAnyAction returns [EObject current=null] : iv_ruleAnyAction= ruleAnyAction EOF ;
    public final EObject entryRuleAnyAction() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleAnyAction = null;


        try {
            // InternalQactork.g:3080:50: (iv_ruleAnyAction= ruleAnyAction EOF )
            // InternalQactork.g:3081:2: iv_ruleAnyAction= ruleAnyAction EOF
            {
             newCompositeNode(grammarAccess.getAnyActionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleAnyAction=ruleAnyAction();

            state._fsp--;

             current =iv_ruleAnyAction; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleAnyAction"


    // $ANTLR start "ruleAnyAction"
    // InternalQactork.g:3087:1: ruleAnyAction returns [EObject current=null] : ( () otherlv_1= '[' ( (lv_body_2_0= RULE_KCODE ) ) otherlv_3= ']' ) ;
    public final EObject ruleAnyAction() throws RecognitionException {
        EObject current = null;

        Token otherlv_1=null;
        Token lv_body_2_0=null;
        Token otherlv_3=null;


        	enterRule();

        try {
            // InternalQactork.g:3093:2: ( ( () otherlv_1= '[' ( (lv_body_2_0= RULE_KCODE ) ) otherlv_3= ']' ) )
            // InternalQactork.g:3094:2: ( () otherlv_1= '[' ( (lv_body_2_0= RULE_KCODE ) ) otherlv_3= ']' )
            {
            // InternalQactork.g:3094:2: ( () otherlv_1= '[' ( (lv_body_2_0= RULE_KCODE ) ) otherlv_3= ']' )
            // InternalQactork.g:3095:3: () otherlv_1= '[' ( (lv_body_2_0= RULE_KCODE ) ) otherlv_3= ']'
            {
            // InternalQactork.g:3095:3: ()
            // InternalQactork.g:3096:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getAnyActionAccess().getAnyActionAction_0(),
            					current);
            			

            }

            otherlv_1=(Token)match(input,30,FOLLOW_41); 

            			newLeafNode(otherlv_1, grammarAccess.getAnyActionAccess().getLeftSquareBracketKeyword_1());
            		
            // InternalQactork.g:3106:3: ( (lv_body_2_0= RULE_KCODE ) )
            // InternalQactork.g:3107:4: (lv_body_2_0= RULE_KCODE )
            {
            // InternalQactork.g:3107:4: (lv_body_2_0= RULE_KCODE )
            // InternalQactork.g:3108:5: lv_body_2_0= RULE_KCODE
            {
            lv_body_2_0=(Token)match(input,RULE_KCODE,FOLLOW_20); 

            					newLeafNode(lv_body_2_0, grammarAccess.getAnyActionAccess().getBodyKCODETerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getAnyActionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"body",
            						lv_body_2_0,
            						"it.unibo.Qactork.KCODE");
            				

            }


            }

            otherlv_3=(Token)match(input,33,FOLLOW_2); 

            			newLeafNode(otherlv_3, grammarAccess.getAnyActionAccess().getRightSquareBracketKeyword_3());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleAnyAction"


    // $ANTLR start "entryRuleCodeRun"
    // InternalQactork.g:3132:1: entryRuleCodeRun returns [EObject current=null] : iv_ruleCodeRun= ruleCodeRun EOF ;
    public final EObject entryRuleCodeRun() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCodeRun = null;


        try {
            // InternalQactork.g:3132:48: (iv_ruleCodeRun= ruleCodeRun EOF )
            // InternalQactork.g:3133:2: iv_ruleCodeRun= ruleCodeRun EOF
            {
             newCompositeNode(grammarAccess.getCodeRunRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCodeRun=ruleCodeRun();

            state._fsp--;

             current =iv_ruleCodeRun; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCodeRun"


    // $ANTLR start "ruleCodeRun"
    // InternalQactork.g:3139:1: ruleCodeRun returns [EObject current=null] : (this_CodeRunActor_0= ruleCodeRunActor | this_CodeRunSimple_1= ruleCodeRunSimple ) ;
    public final EObject ruleCodeRun() throws RecognitionException {
        EObject current = null;

        EObject this_CodeRunActor_0 = null;

        EObject this_CodeRunSimple_1 = null;



        	enterRule();

        try {
            // InternalQactork.g:3145:2: ( (this_CodeRunActor_0= ruleCodeRunActor | this_CodeRunSimple_1= ruleCodeRunSimple ) )
            // InternalQactork.g:3146:2: (this_CodeRunActor_0= ruleCodeRunActor | this_CodeRunSimple_1= ruleCodeRunSimple )
            {
            // InternalQactork.g:3146:2: (this_CodeRunActor_0= ruleCodeRunActor | this_CodeRunSimple_1= ruleCodeRunSimple )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==72) ) {
                alt31=1;
            }
            else if ( (LA31_0==74) ) {
                alt31=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }
            switch (alt31) {
                case 1 :
                    // InternalQactork.g:3147:3: this_CodeRunActor_0= ruleCodeRunActor
                    {

                    			newCompositeNode(grammarAccess.getCodeRunAccess().getCodeRunActorParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_CodeRunActor_0=ruleCodeRunActor();

                    state._fsp--;


                    			current = this_CodeRunActor_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:3156:3: this_CodeRunSimple_1= ruleCodeRunSimple
                    {

                    			newCompositeNode(grammarAccess.getCodeRunAccess().getCodeRunSimpleParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_CodeRunSimple_1=ruleCodeRunSimple();

                    state._fsp--;


                    			current = this_CodeRunSimple_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCodeRun"


    // $ANTLR start "entryRuleCodeRunActor"
    // InternalQactork.g:3168:1: entryRuleCodeRunActor returns [EObject current=null] : iv_ruleCodeRunActor= ruleCodeRunActor EOF ;
    public final EObject entryRuleCodeRunActor() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCodeRunActor = null;


        try {
            // InternalQactork.g:3168:53: (iv_ruleCodeRunActor= ruleCodeRunActor EOF )
            // InternalQactork.g:3169:2: iv_ruleCodeRunActor= ruleCodeRunActor EOF
            {
             newCompositeNode(grammarAccess.getCodeRunActorRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCodeRunActor=ruleCodeRunActor();

            state._fsp--;

             current =iv_ruleCodeRunActor; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCodeRunActor"


    // $ANTLR start "ruleCodeRunActor"
    // InternalQactork.g:3175:1: ruleCodeRunActor returns [EObject current=null] : (otherlv_0= 'qrun' ( (lv_aitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' otherlv_3= 'myself' (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )* )? otherlv_8= ')' ) ;
    public final EObject ruleCodeRunActor() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        Token otherlv_8=null;
        AntlrDatatypeRuleToken lv_aitem_1_0 = null;

        EObject lv_args_5_0 = null;

        EObject lv_args_7_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3181:2: ( (otherlv_0= 'qrun' ( (lv_aitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' otherlv_3= 'myself' (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )* )? otherlv_8= ')' ) )
            // InternalQactork.g:3182:2: (otherlv_0= 'qrun' ( (lv_aitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' otherlv_3= 'myself' (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )* )? otherlv_8= ')' )
            {
            // InternalQactork.g:3182:2: (otherlv_0= 'qrun' ( (lv_aitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' otherlv_3= 'myself' (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )* )? otherlv_8= ')' )
            // InternalQactork.g:3183:3: otherlv_0= 'qrun' ( (lv_aitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' otherlv_3= 'myself' (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )* )? otherlv_8= ')'
            {
            otherlv_0=(Token)match(input,72,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getCodeRunActorAccess().getQrunKeyword_0());
            		
            // InternalQactork.g:3187:3: ( (lv_aitem_1_0= ruleQualifiedName ) )
            // InternalQactork.g:3188:4: (lv_aitem_1_0= ruleQualifiedName )
            {
            // InternalQactork.g:3188:4: (lv_aitem_1_0= ruleQualifiedName )
            // InternalQactork.g:3189:5: lv_aitem_1_0= ruleQualifiedName
            {

            					newCompositeNode(grammarAccess.getCodeRunActorAccess().getAitemQualifiedNameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_30);
            lv_aitem_1_0=ruleQualifiedName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCodeRunActorRule());
            					}
            					set(
            						current,
            						"aitem",
            						lv_aitem_1_0,
            						"it.unibo.Qactork.QualifiedName");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,48,FOLLOW_42); 

            			newLeafNode(otherlv_2, grammarAccess.getCodeRunActorAccess().getLeftParenthesisKeyword_2());
            		
            otherlv_3=(Token)match(input,73,FOLLOW_32); 

            			newLeafNode(otherlv_3, grammarAccess.getCodeRunActorAccess().getMyselfKeyword_3());
            		
            // InternalQactork.g:3214:3: (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )* )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==51) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // InternalQactork.g:3215:4: otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )*
                    {
                    otherlv_4=(Token)match(input,51,FOLLOW_14); 

                    				newLeafNode(otherlv_4, grammarAccess.getCodeRunActorAccess().getCommaKeyword_4_0());
                    			
                    // InternalQactork.g:3219:4: ( (lv_args_5_0= rulePHead ) )
                    // InternalQactork.g:3220:5: (lv_args_5_0= rulePHead )
                    {
                    // InternalQactork.g:3220:5: (lv_args_5_0= rulePHead )
                    // InternalQactork.g:3221:6: lv_args_5_0= rulePHead
                    {

                    						newCompositeNode(grammarAccess.getCodeRunActorAccess().getArgsPHeadParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_32);
                    lv_args_5_0=rulePHead();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCodeRunActorRule());
                    						}
                    						add(
                    							current,
                    							"args",
                    							lv_args_5_0,
                    							"it.unibo.Qactork.PHead");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalQactork.g:3238:4: (otherlv_6= ',' ( (lv_args_7_0= rulePHead ) ) )*
                    loop32:
                    do {
                        int alt32=2;
                        int LA32_0 = input.LA(1);

                        if ( (LA32_0==51) ) {
                            alt32=1;
                        }


                        switch (alt32) {
                    	case 1 :
                    	    // InternalQactork.g:3239:5: otherlv_6= ',' ( (lv_args_7_0= rulePHead ) )
                    	    {
                    	    otherlv_6=(Token)match(input,51,FOLLOW_14); 

                    	    					newLeafNode(otherlv_6, grammarAccess.getCodeRunActorAccess().getCommaKeyword_4_2_0());
                    	    				
                    	    // InternalQactork.g:3243:5: ( (lv_args_7_0= rulePHead ) )
                    	    // InternalQactork.g:3244:6: (lv_args_7_0= rulePHead )
                    	    {
                    	    // InternalQactork.g:3244:6: (lv_args_7_0= rulePHead )
                    	    // InternalQactork.g:3245:7: lv_args_7_0= rulePHead
                    	    {

                    	    							newCompositeNode(grammarAccess.getCodeRunActorAccess().getArgsPHeadParserRuleCall_4_2_1_0());
                    	    						
                    	    pushFollow(FOLLOW_32);
                    	    lv_args_7_0=rulePHead();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getCodeRunActorRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"args",
                    	    								lv_args_7_0,
                    	    								"it.unibo.Qactork.PHead");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop32;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_8=(Token)match(input,49,FOLLOW_2); 

            			newLeafNode(otherlv_8, grammarAccess.getCodeRunActorAccess().getRightParenthesisKeyword_5());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCodeRunActor"


    // $ANTLR start "entryRuleCodeRunSimple"
    // InternalQactork.g:3272:1: entryRuleCodeRunSimple returns [EObject current=null] : iv_ruleCodeRunSimple= ruleCodeRunSimple EOF ;
    public final EObject entryRuleCodeRunSimple() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleCodeRunSimple = null;


        try {
            // InternalQactork.g:3272:54: (iv_ruleCodeRunSimple= ruleCodeRunSimple EOF )
            // InternalQactork.g:3273:2: iv_ruleCodeRunSimple= ruleCodeRunSimple EOF
            {
             newCompositeNode(grammarAccess.getCodeRunSimpleRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleCodeRunSimple=ruleCodeRunSimple();

            state._fsp--;

             current =iv_ruleCodeRunSimple; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleCodeRunSimple"


    // $ANTLR start "ruleCodeRunSimple"
    // InternalQactork.g:3279:1: ruleCodeRunSimple returns [EObject current=null] : (otherlv_0= 'run' ( (lv_bitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' ( ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )* )? otherlv_6= ')' ) ;
    public final EObject ruleCodeRunSimple() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_6=null;
        AntlrDatatypeRuleToken lv_bitem_1_0 = null;

        EObject lv_args_3_0 = null;

        EObject lv_args_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3285:2: ( (otherlv_0= 'run' ( (lv_bitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' ( ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )* )? otherlv_6= ')' ) )
            // InternalQactork.g:3286:2: (otherlv_0= 'run' ( (lv_bitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' ( ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )* )? otherlv_6= ')' )
            {
            // InternalQactork.g:3286:2: (otherlv_0= 'run' ( (lv_bitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' ( ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )* )? otherlv_6= ')' )
            // InternalQactork.g:3287:3: otherlv_0= 'run' ( (lv_bitem_1_0= ruleQualifiedName ) ) otherlv_2= '(' ( ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )* )? otherlv_6= ')'
            {
            otherlv_0=(Token)match(input,74,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getCodeRunSimpleAccess().getRunKeyword_0());
            		
            // InternalQactork.g:3291:3: ( (lv_bitem_1_0= ruleQualifiedName ) )
            // InternalQactork.g:3292:4: (lv_bitem_1_0= ruleQualifiedName )
            {
            // InternalQactork.g:3292:4: (lv_bitem_1_0= ruleQualifiedName )
            // InternalQactork.g:3293:5: lv_bitem_1_0= ruleQualifiedName
            {

            					newCompositeNode(grammarAccess.getCodeRunSimpleAccess().getBitemQualifiedNameParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_30);
            lv_bitem_1_0=ruleQualifiedName();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getCodeRunSimpleRule());
            					}
            					set(
            						current,
            						"bitem",
            						lv_bitem_1_0,
            						"it.unibo.Qactork.QualifiedName");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,48,FOLLOW_43); 

            			newLeafNode(otherlv_2, grammarAccess.getCodeRunSimpleAccess().getLeftParenthesisKeyword_2());
            		
            // InternalQactork.g:3314:3: ( ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )* )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( ((LA35_0>=RULE_ID && LA35_0<=RULE_VARID)||(LA35_0>=88 && LA35_0<=90)) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // InternalQactork.g:3315:4: ( (lv_args_3_0= rulePHead ) ) (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )*
                    {
                    // InternalQactork.g:3315:4: ( (lv_args_3_0= rulePHead ) )
                    // InternalQactork.g:3316:5: (lv_args_3_0= rulePHead )
                    {
                    // InternalQactork.g:3316:5: (lv_args_3_0= rulePHead )
                    // InternalQactork.g:3317:6: lv_args_3_0= rulePHead
                    {

                    						newCompositeNode(grammarAccess.getCodeRunSimpleAccess().getArgsPHeadParserRuleCall_3_0_0());
                    					
                    pushFollow(FOLLOW_32);
                    lv_args_3_0=rulePHead();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getCodeRunSimpleRule());
                    						}
                    						add(
                    							current,
                    							"args",
                    							lv_args_3_0,
                    							"it.unibo.Qactork.PHead");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    // InternalQactork.g:3334:4: (otherlv_4= ',' ( (lv_args_5_0= rulePHead ) ) )*
                    loop34:
                    do {
                        int alt34=2;
                        int LA34_0 = input.LA(1);

                        if ( (LA34_0==51) ) {
                            alt34=1;
                        }


                        switch (alt34) {
                    	case 1 :
                    	    // InternalQactork.g:3335:5: otherlv_4= ',' ( (lv_args_5_0= rulePHead ) )
                    	    {
                    	    otherlv_4=(Token)match(input,51,FOLLOW_14); 

                    	    					newLeafNode(otherlv_4, grammarAccess.getCodeRunSimpleAccess().getCommaKeyword_3_1_0());
                    	    				
                    	    // InternalQactork.g:3339:5: ( (lv_args_5_0= rulePHead ) )
                    	    // InternalQactork.g:3340:6: (lv_args_5_0= rulePHead )
                    	    {
                    	    // InternalQactork.g:3340:6: (lv_args_5_0= rulePHead )
                    	    // InternalQactork.g:3341:7: lv_args_5_0= rulePHead
                    	    {

                    	    							newCompositeNode(grammarAccess.getCodeRunSimpleAccess().getArgsPHeadParserRuleCall_3_1_1_0());
                    	    						
                    	    pushFollow(FOLLOW_32);
                    	    lv_args_5_0=rulePHead();

                    	    state._fsp--;


                    	    							if (current==null) {
                    	    								current = createModelElementForParent(grammarAccess.getCodeRunSimpleRule());
                    	    							}
                    	    							add(
                    	    								current,
                    	    								"args",
                    	    								lv_args_5_0,
                    	    								"it.unibo.Qactork.PHead");
                    	    							afterParserOrEnumRuleCall();
                    	    						

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop34;
                        }
                    } while (true);


                    }
                    break;

            }

            otherlv_6=(Token)match(input,49,FOLLOW_2); 

            			newLeafNode(otherlv_6, grammarAccess.getCodeRunSimpleAccess().getRightParenthesisKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleCodeRunSimple"


    // $ANTLR start "entryRuleExec"
    // InternalQactork.g:3368:1: entryRuleExec returns [EObject current=null] : iv_ruleExec= ruleExec EOF ;
    public final EObject entryRuleExec() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExec = null;


        try {
            // InternalQactork.g:3368:45: (iv_ruleExec= ruleExec EOF )
            // InternalQactork.g:3369:2: iv_ruleExec= ruleExec EOF
            {
             newCompositeNode(grammarAccess.getExecRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExec=ruleExec();

            state._fsp--;

             current =iv_ruleExec; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExec"


    // $ANTLR start "ruleExec"
    // InternalQactork.g:3375:1: ruleExec returns [EObject current=null] : (otherlv_0= 'machineExec' ( (lv_action_1_0= RULE_STRING ) ) ) ;
    public final EObject ruleExec() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_action_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:3381:2: ( (otherlv_0= 'machineExec' ( (lv_action_1_0= RULE_STRING ) ) ) )
            // InternalQactork.g:3382:2: (otherlv_0= 'machineExec' ( (lv_action_1_0= RULE_STRING ) ) )
            {
            // InternalQactork.g:3382:2: (otherlv_0= 'machineExec' ( (lv_action_1_0= RULE_STRING ) ) )
            // InternalQactork.g:3383:3: otherlv_0= 'machineExec' ( (lv_action_1_0= RULE_STRING ) )
            {
            otherlv_0=(Token)match(input,75,FOLLOW_10); 

            			newLeafNode(otherlv_0, grammarAccess.getExecAccess().getMachineExecKeyword_0());
            		
            // InternalQactork.g:3387:3: ( (lv_action_1_0= RULE_STRING ) )
            // InternalQactork.g:3388:4: (lv_action_1_0= RULE_STRING )
            {
            // InternalQactork.g:3388:4: (lv_action_1_0= RULE_STRING )
            // InternalQactork.g:3389:5: lv_action_1_0= RULE_STRING
            {
            lv_action_1_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            					newLeafNode(lv_action_1_0, grammarAccess.getExecAccess().getActionSTRINGTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getExecRule());
            					}
            					setWithLastConsumed(
            						current,
            						"action",
            						lv_action_1_0,
            						"org.eclipse.xtext.common.Terminals.STRING");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExec"


    // $ANTLR start "entryRuleTransition"
    // InternalQactork.g:3409:1: entryRuleTransition returns [EObject current=null] : iv_ruleTransition= ruleTransition EOF ;
    public final EObject entryRuleTransition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTransition = null;


        try {
            // InternalQactork.g:3409:51: (iv_ruleTransition= ruleTransition EOF )
            // InternalQactork.g:3410:2: iv_ruleTransition= ruleTransition EOF
            {
             newCompositeNode(grammarAccess.getTransitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTransition=ruleTransition();

            state._fsp--;

             current =iv_ruleTransition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTransition"


    // $ANTLR start "ruleTransition"
    // InternalQactork.g:3416:1: ruleTransition returns [EObject current=null] : (this_EmptyTransition_0= ruleEmptyTransition | this_NonEmptyTransition_1= ruleNonEmptyTransition ) ;
    public final EObject ruleTransition() throws RecognitionException {
        EObject current = null;

        EObject this_EmptyTransition_0 = null;

        EObject this_NonEmptyTransition_1 = null;



        	enterRule();

        try {
            // InternalQactork.g:3422:2: ( (this_EmptyTransition_0= ruleEmptyTransition | this_NonEmptyTransition_1= ruleNonEmptyTransition ) )
            // InternalQactork.g:3423:2: (this_EmptyTransition_0= ruleEmptyTransition | this_NonEmptyTransition_1= ruleNonEmptyTransition )
            {
            // InternalQactork.g:3423:2: (this_EmptyTransition_0= ruleEmptyTransition | this_NonEmptyTransition_1= ruleNonEmptyTransition )
            int alt36=2;
            int LA36_0 = input.LA(1);

            if ( (LA36_0==76) ) {
                alt36=1;
            }
            else if ( (LA36_0==77) ) {
                alt36=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }
            switch (alt36) {
                case 1 :
                    // InternalQactork.g:3424:3: this_EmptyTransition_0= ruleEmptyTransition
                    {

                    			newCompositeNode(grammarAccess.getTransitionAccess().getEmptyTransitionParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_EmptyTransition_0=ruleEmptyTransition();

                    state._fsp--;


                    			current = this_EmptyTransition_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:3433:3: this_NonEmptyTransition_1= ruleNonEmptyTransition
                    {

                    			newCompositeNode(grammarAccess.getTransitionAccess().getNonEmptyTransitionParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_NonEmptyTransition_1=ruleNonEmptyTransition();

                    state._fsp--;


                    			current = this_NonEmptyTransition_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTransition"


    // $ANTLR start "entryRuleEmptyTransition"
    // InternalQactork.g:3445:1: entryRuleEmptyTransition returns [EObject current=null] : iv_ruleEmptyTransition= ruleEmptyTransition EOF ;
    public final EObject entryRuleEmptyTransition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEmptyTransition = null;


        try {
            // InternalQactork.g:3445:56: (iv_ruleEmptyTransition= ruleEmptyTransition EOF )
            // InternalQactork.g:3446:2: iv_ruleEmptyTransition= ruleEmptyTransition EOF
            {
             newCompositeNode(grammarAccess.getEmptyTransitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEmptyTransition=ruleEmptyTransition();

            state._fsp--;

             current =iv_ruleEmptyTransition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEmptyTransition"


    // $ANTLR start "ruleEmptyTransition"
    // InternalQactork.g:3452:1: ruleEmptyTransition returns [EObject current=null] : (otherlv_0= 'Goto' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) ) )? ) ;
    public final EObject ruleEmptyTransition() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_eguard_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3458:2: ( (otherlv_0= 'Goto' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) ) )? ) )
            // InternalQactork.g:3459:2: (otherlv_0= 'Goto' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) ) )? )
            {
            // InternalQactork.g:3459:2: (otherlv_0= 'Goto' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) ) )? )
            // InternalQactork.g:3460:3: otherlv_0= 'Goto' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) ) )?
            {
            otherlv_0=(Token)match(input,76,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getEmptyTransitionAccess().getGotoKeyword_0());
            		
            // InternalQactork.g:3464:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:3465:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:3465:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:3466:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEmptyTransitionRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_44); 

            					newLeafNode(otherlv_1, grammarAccess.getEmptyTransitionAccess().getTargetStateStateCrossReference_1_0());
            				

            }


            }

            // InternalQactork.g:3477:3: (otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) ) )?
            int alt37=2;
            int LA37_0 = input.LA(1);

            if ( (LA37_0==45) ) {
                alt37=1;
            }
            switch (alt37) {
                case 1 :
                    // InternalQactork.g:3478:4: otherlv_2= 'if' ( (lv_eguard_3_0= ruleAnyAction ) ) otherlv_4= 'else' ( (otherlv_5= RULE_ID ) )
                    {
                    otherlv_2=(Token)match(input,45,FOLLOW_16); 

                    				newLeafNode(otherlv_2, grammarAccess.getEmptyTransitionAccess().getIfKeyword_2_0());
                    			
                    // InternalQactork.g:3482:4: ( (lv_eguard_3_0= ruleAnyAction ) )
                    // InternalQactork.g:3483:5: (lv_eguard_3_0= ruleAnyAction )
                    {
                    // InternalQactork.g:3483:5: (lv_eguard_3_0= ruleAnyAction )
                    // InternalQactork.g:3484:6: lv_eguard_3_0= ruleAnyAction
                    {

                    						newCompositeNode(grammarAccess.getEmptyTransitionAccess().getEguardAnyActionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_45);
                    lv_eguard_3_0=ruleAnyAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getEmptyTransitionRule());
                    						}
                    						set(
                    							current,
                    							"eguard",
                    							lv_eguard_3_0,
                    							"it.unibo.Qactork.AnyAction");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }

                    otherlv_4=(Token)match(input,44,FOLLOW_5); 

                    				newLeafNode(otherlv_4, grammarAccess.getEmptyTransitionAccess().getElseKeyword_2_2());
                    			
                    // InternalQactork.g:3505:4: ( (otherlv_5= RULE_ID ) )
                    // InternalQactork.g:3506:5: (otherlv_5= RULE_ID )
                    {
                    // InternalQactork.g:3506:5: (otherlv_5= RULE_ID )
                    // InternalQactork.g:3507:6: otherlv_5= RULE_ID
                    {

                    						if (current==null) {
                    							current = createModelElement(grammarAccess.getEmptyTransitionRule());
                    						}
                    					
                    otherlv_5=(Token)match(input,RULE_ID,FOLLOW_2); 

                    						newLeafNode(otherlv_5, grammarAccess.getEmptyTransitionAccess().getOthertargetStateStateCrossReference_2_3_0());
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEmptyTransition"


    // $ANTLR start "entryRuleNonEmptyTransition"
    // InternalQactork.g:3523:1: entryRuleNonEmptyTransition returns [EObject current=null] : iv_ruleNonEmptyTransition= ruleNonEmptyTransition EOF ;
    public final EObject entryRuleNonEmptyTransition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleNonEmptyTransition = null;


        try {
            // InternalQactork.g:3523:59: (iv_ruleNonEmptyTransition= ruleNonEmptyTransition EOF )
            // InternalQactork.g:3524:2: iv_ruleNonEmptyTransition= ruleNonEmptyTransition EOF
            {
             newCompositeNode(grammarAccess.getNonEmptyTransitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleNonEmptyTransition=ruleNonEmptyTransition();

            state._fsp--;

             current =iv_ruleNonEmptyTransition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleNonEmptyTransition"


    // $ANTLR start "ruleNonEmptyTransition"
    // InternalQactork.g:3530:1: ruleNonEmptyTransition returns [EObject current=null] : (otherlv_0= 'Transition' ( (lv_name_1_0= RULE_ID ) ) ( (lv_duration_2_0= ruleTimeout ) )? ( (lv_trans_3_0= ruleInputTransition ) )* (otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) ) )? ) ;
    public final EObject ruleNonEmptyTransition() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_name_1_0=null;
        Token otherlv_4=null;
        EObject lv_duration_2_0 = null;

        EObject lv_trans_3_0 = null;

        EObject lv_elseempty_5_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3536:2: ( (otherlv_0= 'Transition' ( (lv_name_1_0= RULE_ID ) ) ( (lv_duration_2_0= ruleTimeout ) )? ( (lv_trans_3_0= ruleInputTransition ) )* (otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) ) )? ) )
            // InternalQactork.g:3537:2: (otherlv_0= 'Transition' ( (lv_name_1_0= RULE_ID ) ) ( (lv_duration_2_0= ruleTimeout ) )? ( (lv_trans_3_0= ruleInputTransition ) )* (otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) ) )? )
            {
            // InternalQactork.g:3537:2: (otherlv_0= 'Transition' ( (lv_name_1_0= RULE_ID ) ) ( (lv_duration_2_0= ruleTimeout ) )? ( (lv_trans_3_0= ruleInputTransition ) )* (otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) ) )? )
            // InternalQactork.g:3538:3: otherlv_0= 'Transition' ( (lv_name_1_0= RULE_ID ) ) ( (lv_duration_2_0= ruleTimeout ) )? ( (lv_trans_3_0= ruleInputTransition ) )* (otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) ) )?
            {
            otherlv_0=(Token)match(input,77,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getNonEmptyTransitionAccess().getTransitionKeyword_0());
            		
            // InternalQactork.g:3542:3: ( (lv_name_1_0= RULE_ID ) )
            // InternalQactork.g:3543:4: (lv_name_1_0= RULE_ID )
            {
            // InternalQactork.g:3543:4: (lv_name_1_0= RULE_ID )
            // InternalQactork.g:3544:5: lv_name_1_0= RULE_ID
            {
            lv_name_1_0=(Token)match(input,RULE_ID,FOLLOW_46); 

            					newLeafNode(lv_name_1_0, grammarAccess.getNonEmptyTransitionAccess().getNameIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getNonEmptyTransitionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_1_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            // InternalQactork.g:3560:3: ( (lv_duration_2_0= ruleTimeout ) )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==78||(LA38_0>=80 && LA38_0<=82)) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // InternalQactork.g:3561:4: (lv_duration_2_0= ruleTimeout )
                    {
                    // InternalQactork.g:3561:4: (lv_duration_2_0= ruleTimeout )
                    // InternalQactork.g:3562:5: lv_duration_2_0= ruleTimeout
                    {

                    					newCompositeNode(grammarAccess.getNonEmptyTransitionAccess().getDurationTimeoutParserRuleCall_2_0());
                    				
                    pushFollow(FOLLOW_47);
                    lv_duration_2_0=ruleTimeout();

                    state._fsp--;


                    					if (current==null) {
                    						current = createModelElementForParent(grammarAccess.getNonEmptyTransitionRule());
                    					}
                    					set(
                    						current,
                    						"duration",
                    						lv_duration_2_0,
                    						"it.unibo.Qactork.Timeout");
                    					afterParserOrEnumRuleCall();
                    				

                    }


                    }
                    break;

            }

            // InternalQactork.g:3579:3: ( (lv_trans_3_0= ruleInputTransition ) )*
            loop39:
            do {
                int alt39=2;
                int LA39_0 = input.LA(1);

                if ( (LA39_0==83||(LA39_0>=85 && LA39_0<=87)) ) {
                    alt39=1;
                }


                switch (alt39) {
            	case 1 :
            	    // InternalQactork.g:3580:4: (lv_trans_3_0= ruleInputTransition )
            	    {
            	    // InternalQactork.g:3580:4: (lv_trans_3_0= ruleInputTransition )
            	    // InternalQactork.g:3581:5: lv_trans_3_0= ruleInputTransition
            	    {

            	    					newCompositeNode(grammarAccess.getNonEmptyTransitionAccess().getTransInputTransitionParserRuleCall_3_0());
            	    				
            	    pushFollow(FOLLOW_47);
            	    lv_trans_3_0=ruleInputTransition();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getNonEmptyTransitionRule());
            	    					}
            	    					add(
            	    						current,
            	    						"trans",
            	    						lv_trans_3_0,
            	    						"it.unibo.Qactork.InputTransition");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    break loop39;
                }
            } while (true);

            // InternalQactork.g:3598:3: (otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) ) )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==44) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // InternalQactork.g:3599:4: otherlv_4= 'else' ( (lv_elseempty_5_0= ruleEmptyTransition ) )
                    {
                    otherlv_4=(Token)match(input,44,FOLLOW_48); 

                    				newLeafNode(otherlv_4, grammarAccess.getNonEmptyTransitionAccess().getElseKeyword_4_0());
                    			
                    // InternalQactork.g:3603:4: ( (lv_elseempty_5_0= ruleEmptyTransition ) )
                    // InternalQactork.g:3604:5: (lv_elseempty_5_0= ruleEmptyTransition )
                    {
                    // InternalQactork.g:3604:5: (lv_elseempty_5_0= ruleEmptyTransition )
                    // InternalQactork.g:3605:6: lv_elseempty_5_0= ruleEmptyTransition
                    {

                    						newCompositeNode(grammarAccess.getNonEmptyTransitionAccess().getElseemptyEmptyTransitionParserRuleCall_4_1_0());
                    					
                    pushFollow(FOLLOW_2);
                    lv_elseempty_5_0=ruleEmptyTransition();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getNonEmptyTransitionRule());
                    						}
                    						set(
                    							current,
                    							"elseempty",
                    							lv_elseempty_5_0,
                    							"it.unibo.Qactork.EmptyTransition");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleNonEmptyTransition"


    // $ANTLR start "entryRuleTimeout"
    // InternalQactork.g:3627:1: entryRuleTimeout returns [EObject current=null] : iv_ruleTimeout= ruleTimeout EOF ;
    public final EObject entryRuleTimeout() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimeout = null;


        try {
            // InternalQactork.g:3627:48: (iv_ruleTimeout= ruleTimeout EOF )
            // InternalQactork.g:3628:2: iv_ruleTimeout= ruleTimeout EOF
            {
             newCompositeNode(grammarAccess.getTimeoutRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimeout=ruleTimeout();

            state._fsp--;

             current =iv_ruleTimeout; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTimeout"


    // $ANTLR start "ruleTimeout"
    // InternalQactork.g:3634:1: ruleTimeout returns [EObject current=null] : (this_TimeoutInt_0= ruleTimeoutInt | this_TimeoutVar_1= ruleTimeoutVar | this_TimeoutSol_2= ruleTimeoutSol | this_TimeoutVarRef_3= ruleTimeoutVarRef ) ;
    public final EObject ruleTimeout() throws RecognitionException {
        EObject current = null;

        EObject this_TimeoutInt_0 = null;

        EObject this_TimeoutVar_1 = null;

        EObject this_TimeoutSol_2 = null;

        EObject this_TimeoutVarRef_3 = null;



        	enterRule();

        try {
            // InternalQactork.g:3640:2: ( (this_TimeoutInt_0= ruleTimeoutInt | this_TimeoutVar_1= ruleTimeoutVar | this_TimeoutSol_2= ruleTimeoutSol | this_TimeoutVarRef_3= ruleTimeoutVarRef ) )
            // InternalQactork.g:3641:2: (this_TimeoutInt_0= ruleTimeoutInt | this_TimeoutVar_1= ruleTimeoutVar | this_TimeoutSol_2= ruleTimeoutSol | this_TimeoutVarRef_3= ruleTimeoutVarRef )
            {
            // InternalQactork.g:3641:2: (this_TimeoutInt_0= ruleTimeoutInt | this_TimeoutVar_1= ruleTimeoutVar | this_TimeoutSol_2= ruleTimeoutSol | this_TimeoutVarRef_3= ruleTimeoutVarRef )
            int alt41=4;
            switch ( input.LA(1) ) {
            case 78:
                {
                alt41=1;
                }
                break;
            case 80:
                {
                alt41=2;
                }
                break;
            case 82:
                {
                alt41=3;
                }
                break;
            case 81:
                {
                alt41=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 41, 0, input);

                throw nvae;
            }

            switch (alt41) {
                case 1 :
                    // InternalQactork.g:3642:3: this_TimeoutInt_0= ruleTimeoutInt
                    {

                    			newCompositeNode(grammarAccess.getTimeoutAccess().getTimeoutIntParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_TimeoutInt_0=ruleTimeoutInt();

                    state._fsp--;


                    			current = this_TimeoutInt_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:3651:3: this_TimeoutVar_1= ruleTimeoutVar
                    {

                    			newCompositeNode(grammarAccess.getTimeoutAccess().getTimeoutVarParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_TimeoutVar_1=ruleTimeoutVar();

                    state._fsp--;


                    			current = this_TimeoutVar_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:3660:3: this_TimeoutSol_2= ruleTimeoutSol
                    {

                    			newCompositeNode(grammarAccess.getTimeoutAccess().getTimeoutSolParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_TimeoutSol_2=ruleTimeoutSol();

                    state._fsp--;


                    			current = this_TimeoutSol_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalQactork.g:3669:3: this_TimeoutVarRef_3= ruleTimeoutVarRef
                    {

                    			newCompositeNode(grammarAccess.getTimeoutAccess().getTimeoutVarRefParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_TimeoutVarRef_3=ruleTimeoutVarRef();

                    state._fsp--;


                    			current = this_TimeoutVarRef_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimeout"


    // $ANTLR start "entryRuleTimeoutInt"
    // InternalQactork.g:3681:1: entryRuleTimeoutInt returns [EObject current=null] : iv_ruleTimeoutInt= ruleTimeoutInt EOF ;
    public final EObject entryRuleTimeoutInt() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimeoutInt = null;


        try {
            // InternalQactork.g:3681:51: (iv_ruleTimeoutInt= ruleTimeoutInt EOF )
            // InternalQactork.g:3682:2: iv_ruleTimeoutInt= ruleTimeoutInt EOF
            {
             newCompositeNode(grammarAccess.getTimeoutIntRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimeoutInt=ruleTimeoutInt();

            state._fsp--;

             current =iv_ruleTimeoutInt; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTimeoutInt"


    // $ANTLR start "ruleTimeoutInt"
    // InternalQactork.g:3688:1: ruleTimeoutInt returns [EObject current=null] : (otherlv_0= 'whenTime' ( (lv_msec_1_0= RULE_INT ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) ;
    public final EObject ruleTimeoutInt() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_msec_1_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;


        	enterRule();

        try {
            // InternalQactork.g:3694:2: ( (otherlv_0= 'whenTime' ( (lv_msec_1_0= RULE_INT ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) )
            // InternalQactork.g:3695:2: (otherlv_0= 'whenTime' ( (lv_msec_1_0= RULE_INT ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            {
            // InternalQactork.g:3695:2: (otherlv_0= 'whenTime' ( (lv_msec_1_0= RULE_INT ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            // InternalQactork.g:3696:3: otherlv_0= 'whenTime' ( (lv_msec_1_0= RULE_INT ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,78,FOLLOW_12); 

            			newLeafNode(otherlv_0, grammarAccess.getTimeoutIntAccess().getWhenTimeKeyword_0());
            		
            // InternalQactork.g:3700:3: ( (lv_msec_1_0= RULE_INT ) )
            // InternalQactork.g:3701:4: (lv_msec_1_0= RULE_INT )
            {
            // InternalQactork.g:3701:4: (lv_msec_1_0= RULE_INT )
            // InternalQactork.g:3702:5: lv_msec_1_0= RULE_INT
            {
            lv_msec_1_0=(Token)match(input,RULE_INT,FOLLOW_49); 

            					newLeafNode(lv_msec_1_0, grammarAccess.getTimeoutIntAccess().getMsecINTTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimeoutIntRule());
            					}
            					setWithLastConsumed(
            						current,
            						"msec",
            						lv_msec_1_0,
            						"org.eclipse.xtext.common.Terminals.INT");
            				

            }


            }

            otherlv_2=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getTimeoutIntAccess().getHyphenMinusGreaterThanSignKeyword_2());
            		
            // InternalQactork.g:3722:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:3723:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:3723:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:3724:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimeoutIntRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_3, grammarAccess.getTimeoutIntAccess().getTargetStateStateCrossReference_3_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimeoutInt"


    // $ANTLR start "entryRuleTimeoutVar"
    // InternalQactork.g:3739:1: entryRuleTimeoutVar returns [EObject current=null] : iv_ruleTimeoutVar= ruleTimeoutVar EOF ;
    public final EObject entryRuleTimeoutVar() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimeoutVar = null;


        try {
            // InternalQactork.g:3739:51: (iv_ruleTimeoutVar= ruleTimeoutVar EOF )
            // InternalQactork.g:3740:2: iv_ruleTimeoutVar= ruleTimeoutVar EOF
            {
             newCompositeNode(grammarAccess.getTimeoutVarRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimeoutVar=ruleTimeoutVar();

            state._fsp--;

             current =iv_ruleTimeoutVar; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTimeoutVar"


    // $ANTLR start "ruleTimeoutVar"
    // InternalQactork.g:3746:1: ruleTimeoutVar returns [EObject current=null] : (otherlv_0= 'whenTimeVar' ( (lv_variable_1_0= ruleVariable ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) ;
    public final EObject ruleTimeoutVar() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        EObject lv_variable_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3752:2: ( (otherlv_0= 'whenTimeVar' ( (lv_variable_1_0= ruleVariable ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) )
            // InternalQactork.g:3753:2: (otherlv_0= 'whenTimeVar' ( (lv_variable_1_0= ruleVariable ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            {
            // InternalQactork.g:3753:2: (otherlv_0= 'whenTimeVar' ( (lv_variable_1_0= ruleVariable ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            // InternalQactork.g:3754:3: otherlv_0= 'whenTimeVar' ( (lv_variable_1_0= ruleVariable ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,80,FOLLOW_33); 

            			newLeafNode(otherlv_0, grammarAccess.getTimeoutVarAccess().getWhenTimeVarKeyword_0());
            		
            // InternalQactork.g:3758:3: ( (lv_variable_1_0= ruleVariable ) )
            // InternalQactork.g:3759:4: (lv_variable_1_0= ruleVariable )
            {
            // InternalQactork.g:3759:4: (lv_variable_1_0= ruleVariable )
            // InternalQactork.g:3760:5: lv_variable_1_0= ruleVariable
            {

            					newCompositeNode(grammarAccess.getTimeoutVarAccess().getVariableVariableParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_49);
            lv_variable_1_0=ruleVariable();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTimeoutVarRule());
            					}
            					set(
            						current,
            						"variable",
            						lv_variable_1_0,
            						"it.unibo.Qactork.Variable");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getTimeoutVarAccess().getHyphenMinusGreaterThanSignKeyword_2());
            		
            // InternalQactork.g:3781:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:3782:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:3782:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:3783:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimeoutVarRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_3, grammarAccess.getTimeoutVarAccess().getTargetStateStateCrossReference_3_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimeoutVar"


    // $ANTLR start "entryRuleTimeoutVarRef"
    // InternalQactork.g:3798:1: entryRuleTimeoutVarRef returns [EObject current=null] : iv_ruleTimeoutVarRef= ruleTimeoutVarRef EOF ;
    public final EObject entryRuleTimeoutVarRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimeoutVarRef = null;


        try {
            // InternalQactork.g:3798:54: (iv_ruleTimeoutVarRef= ruleTimeoutVarRef EOF )
            // InternalQactork.g:3799:2: iv_ruleTimeoutVarRef= ruleTimeoutVarRef EOF
            {
             newCompositeNode(grammarAccess.getTimeoutVarRefRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimeoutVarRef=ruleTimeoutVarRef();

            state._fsp--;

             current =iv_ruleTimeoutVarRef; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTimeoutVarRef"


    // $ANTLR start "ruleTimeoutVarRef"
    // InternalQactork.g:3805:1: ruleTimeoutVarRef returns [EObject current=null] : (otherlv_0= 'whenTimeVarRef' ( (lv_refvar_1_0= ruleVarRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) ;
    public final EObject ruleTimeoutVarRef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        EObject lv_refvar_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3811:2: ( (otherlv_0= 'whenTimeVarRef' ( (lv_refvar_1_0= ruleVarRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) )
            // InternalQactork.g:3812:2: (otherlv_0= 'whenTimeVarRef' ( (lv_refvar_1_0= ruleVarRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            {
            // InternalQactork.g:3812:2: (otherlv_0= 'whenTimeVarRef' ( (lv_refvar_1_0= ruleVarRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            // InternalQactork.g:3813:3: otherlv_0= 'whenTimeVarRef' ( (lv_refvar_1_0= ruleVarRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,81,FOLLOW_39); 

            			newLeafNode(otherlv_0, grammarAccess.getTimeoutVarRefAccess().getWhenTimeVarRefKeyword_0());
            		
            // InternalQactork.g:3817:3: ( (lv_refvar_1_0= ruleVarRef ) )
            // InternalQactork.g:3818:4: (lv_refvar_1_0= ruleVarRef )
            {
            // InternalQactork.g:3818:4: (lv_refvar_1_0= ruleVarRef )
            // InternalQactork.g:3819:5: lv_refvar_1_0= ruleVarRef
            {

            					newCompositeNode(grammarAccess.getTimeoutVarRefAccess().getRefvarVarRefParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_49);
            lv_refvar_1_0=ruleVarRef();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTimeoutVarRefRule());
            					}
            					set(
            						current,
            						"refvar",
            						lv_refvar_1_0,
            						"it.unibo.Qactork.VarRef");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getTimeoutVarRefAccess().getHyphenMinusGreaterThanSignKeyword_2());
            		
            // InternalQactork.g:3840:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:3841:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:3841:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:3842:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimeoutVarRefRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_3, grammarAccess.getTimeoutVarRefAccess().getTargetStateStateCrossReference_3_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimeoutVarRef"


    // $ANTLR start "entryRuleTimeoutSol"
    // InternalQactork.g:3857:1: entryRuleTimeoutSol returns [EObject current=null] : iv_ruleTimeoutSol= ruleTimeoutSol EOF ;
    public final EObject entryRuleTimeoutSol() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleTimeoutSol = null;


        try {
            // InternalQactork.g:3857:51: (iv_ruleTimeoutSol= ruleTimeoutSol EOF )
            // InternalQactork.g:3858:2: iv_ruleTimeoutSol= ruleTimeoutSol EOF
            {
             newCompositeNode(grammarAccess.getTimeoutSolRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleTimeoutSol=ruleTimeoutSol();

            state._fsp--;

             current =iv_ruleTimeoutSol; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleTimeoutSol"


    // $ANTLR start "ruleTimeoutSol"
    // InternalQactork.g:3864:1: ruleTimeoutSol returns [EObject current=null] : (otherlv_0= 'whenTimeSol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) ;
    public final EObject ruleTimeoutSol() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_2=null;
        Token otherlv_3=null;
        EObject lv_refsoltime_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3870:2: ( (otherlv_0= 'whenTimeSol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) ) )
            // InternalQactork.g:3871:2: (otherlv_0= 'whenTimeSol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            {
            // InternalQactork.g:3871:2: (otherlv_0= 'whenTimeSol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) ) )
            // InternalQactork.g:3872:3: otherlv_0= 'whenTimeSol' ( (lv_refsoltime_1_0= ruleVarSolRef ) ) otherlv_2= '->' ( (otherlv_3= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,82,FOLLOW_40); 

            			newLeafNode(otherlv_0, grammarAccess.getTimeoutSolAccess().getWhenTimeSolKeyword_0());
            		
            // InternalQactork.g:3876:3: ( (lv_refsoltime_1_0= ruleVarSolRef ) )
            // InternalQactork.g:3877:4: (lv_refsoltime_1_0= ruleVarSolRef )
            {
            // InternalQactork.g:3877:4: (lv_refsoltime_1_0= ruleVarSolRef )
            // InternalQactork.g:3878:5: lv_refsoltime_1_0= ruleVarSolRef
            {

            					newCompositeNode(grammarAccess.getTimeoutSolAccess().getRefsoltimeVarSolRefParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_49);
            lv_refsoltime_1_0=ruleVarSolRef();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getTimeoutSolRule());
            					}
            					set(
            						current,
            						"refsoltime",
            						lv_refsoltime_1_0,
            						"it.unibo.Qactork.VarSolRef");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            otherlv_2=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_2, grammarAccess.getTimeoutSolAccess().getHyphenMinusGreaterThanSignKeyword_2());
            		
            // InternalQactork.g:3899:3: ( (otherlv_3= RULE_ID ) )
            // InternalQactork.g:3900:4: (otherlv_3= RULE_ID )
            {
            // InternalQactork.g:3900:4: (otherlv_3= RULE_ID )
            // InternalQactork.g:3901:5: otherlv_3= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getTimeoutSolRule());
            					}
            				
            otherlv_3=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_3, grammarAccess.getTimeoutSolAccess().getTargetStateStateCrossReference_3_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleTimeoutSol"


    // $ANTLR start "entryRuleInputTransition"
    // InternalQactork.g:3916:1: entryRuleInputTransition returns [EObject current=null] : iv_ruleInputTransition= ruleInputTransition EOF ;
    public final EObject entryRuleInputTransition() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleInputTransition = null;


        try {
            // InternalQactork.g:3916:56: (iv_ruleInputTransition= ruleInputTransition EOF )
            // InternalQactork.g:3917:2: iv_ruleInputTransition= ruleInputTransition EOF
            {
             newCompositeNode(grammarAccess.getInputTransitionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleInputTransition=ruleInputTransition();

            state._fsp--;

             current =iv_ruleInputTransition; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleInputTransition"


    // $ANTLR start "ruleInputTransition"
    // InternalQactork.g:3923:1: ruleInputTransition returns [EObject current=null] : (this_EventTransSwitch_0= ruleEventTransSwitch | this_MsgTransSwitch_1= ruleMsgTransSwitch | this_RequestTransSwitch_2= ruleRequestTransSwitch | this_ReplyTransSwitch_3= ruleReplyTransSwitch ) ;
    public final EObject ruleInputTransition() throws RecognitionException {
        EObject current = null;

        EObject this_EventTransSwitch_0 = null;

        EObject this_MsgTransSwitch_1 = null;

        EObject this_RequestTransSwitch_2 = null;

        EObject this_ReplyTransSwitch_3 = null;



        	enterRule();

        try {
            // InternalQactork.g:3929:2: ( (this_EventTransSwitch_0= ruleEventTransSwitch | this_MsgTransSwitch_1= ruleMsgTransSwitch | this_RequestTransSwitch_2= ruleRequestTransSwitch | this_ReplyTransSwitch_3= ruleReplyTransSwitch ) )
            // InternalQactork.g:3930:2: (this_EventTransSwitch_0= ruleEventTransSwitch | this_MsgTransSwitch_1= ruleMsgTransSwitch | this_RequestTransSwitch_2= ruleRequestTransSwitch | this_ReplyTransSwitch_3= ruleReplyTransSwitch )
            {
            // InternalQactork.g:3930:2: (this_EventTransSwitch_0= ruleEventTransSwitch | this_MsgTransSwitch_1= ruleMsgTransSwitch | this_RequestTransSwitch_2= ruleRequestTransSwitch | this_ReplyTransSwitch_3= ruleReplyTransSwitch )
            int alt42=4;
            switch ( input.LA(1) ) {
            case 83:
                {
                alt42=1;
                }
                break;
            case 85:
                {
                alt42=2;
                }
                break;
            case 86:
                {
                alt42=3;
                }
                break;
            case 87:
                {
                alt42=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // InternalQactork.g:3931:3: this_EventTransSwitch_0= ruleEventTransSwitch
                    {

                    			newCompositeNode(grammarAccess.getInputTransitionAccess().getEventTransSwitchParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_EventTransSwitch_0=ruleEventTransSwitch();

                    state._fsp--;


                    			current = this_EventTransSwitch_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:3940:3: this_MsgTransSwitch_1= ruleMsgTransSwitch
                    {

                    			newCompositeNode(grammarAccess.getInputTransitionAccess().getMsgTransSwitchParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_MsgTransSwitch_1=ruleMsgTransSwitch();

                    state._fsp--;


                    			current = this_MsgTransSwitch_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:3949:3: this_RequestTransSwitch_2= ruleRequestTransSwitch
                    {

                    			newCompositeNode(grammarAccess.getInputTransitionAccess().getRequestTransSwitchParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_RequestTransSwitch_2=ruleRequestTransSwitch();

                    state._fsp--;


                    			current = this_RequestTransSwitch_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalQactork.g:3958:3: this_ReplyTransSwitch_3= ruleReplyTransSwitch
                    {

                    			newCompositeNode(grammarAccess.getInputTransitionAccess().getReplyTransSwitchParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_ReplyTransSwitch_3=ruleReplyTransSwitch();

                    state._fsp--;


                    			current = this_ReplyTransSwitch_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleInputTransition"


    // $ANTLR start "entryRuleEventTransSwitch"
    // InternalQactork.g:3970:1: entryRuleEventTransSwitch returns [EObject current=null] : iv_ruleEventTransSwitch= ruleEventTransSwitch EOF ;
    public final EObject entryRuleEventTransSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleEventTransSwitch = null;


        try {
            // InternalQactork.g:3970:57: (iv_ruleEventTransSwitch= ruleEventTransSwitch EOF )
            // InternalQactork.g:3971:2: iv_ruleEventTransSwitch= ruleEventTransSwitch EOF
            {
             newCompositeNode(grammarAccess.getEventTransSwitchRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleEventTransSwitch=ruleEventTransSwitch();

            state._fsp--;

             current =iv_ruleEventTransSwitch; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleEventTransSwitch"


    // $ANTLR start "ruleEventTransSwitch"
    // InternalQactork.g:3977:1: ruleEventTransSwitch returns [EObject current=null] : (otherlv_0= 'whenEvent' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) ;
    public final EObject ruleEventTransSwitch() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_guard_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:3983:2: ( (otherlv_0= 'whenEvent' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) )
            // InternalQactork.g:3984:2: (otherlv_0= 'whenEvent' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            {
            // InternalQactork.g:3984:2: (otherlv_0= 'whenEvent' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            // InternalQactork.g:3985:3: otherlv_0= 'whenEvent' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,83,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getEventTransSwitchAccess().getWhenEventKeyword_0());
            		
            // InternalQactork.g:3989:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:3990:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:3990:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:3991:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEventTransSwitchRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_50); 

            					newLeafNode(otherlv_1, grammarAccess.getEventTransSwitchAccess().getMessageEventCrossReference_1_0());
            				

            }


            }

            // InternalQactork.g:4002:3: (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )?
            int alt43=2;
            int LA43_0 = input.LA(1);

            if ( (LA43_0==84) ) {
                alt43=1;
            }
            switch (alt43) {
                case 1 :
                    // InternalQactork.g:4003:4: otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) )
                    {
                    otherlv_2=(Token)match(input,84,FOLLOW_16); 

                    				newLeafNode(otherlv_2, grammarAccess.getEventTransSwitchAccess().getAndKeyword_2_0());
                    			
                    // InternalQactork.g:4007:4: ( (lv_guard_3_0= ruleAnyAction ) )
                    // InternalQactork.g:4008:5: (lv_guard_3_0= ruleAnyAction )
                    {
                    // InternalQactork.g:4008:5: (lv_guard_3_0= ruleAnyAction )
                    // InternalQactork.g:4009:6: lv_guard_3_0= ruleAnyAction
                    {

                    						newCompositeNode(grammarAccess.getEventTransSwitchAccess().getGuardAnyActionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_49);
                    lv_guard_3_0=ruleAnyAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getEventTransSwitchRule());
                    						}
                    						set(
                    							current,
                    							"guard",
                    							lv_guard_3_0,
                    							"it.unibo.Qactork.AnyAction");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_4, grammarAccess.getEventTransSwitchAccess().getHyphenMinusGreaterThanSignKeyword_3());
            		
            // InternalQactork.g:4031:3: ( (otherlv_5= RULE_ID ) )
            // InternalQactork.g:4032:4: (otherlv_5= RULE_ID )
            {
            // InternalQactork.g:4032:4: (otherlv_5= RULE_ID )
            // InternalQactork.g:4033:5: otherlv_5= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getEventTransSwitchRule());
            					}
            				
            otherlv_5=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_5, grammarAccess.getEventTransSwitchAccess().getTargetStateStateCrossReference_4_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleEventTransSwitch"


    // $ANTLR start "entryRuleMsgTransSwitch"
    // InternalQactork.g:4048:1: entryRuleMsgTransSwitch returns [EObject current=null] : iv_ruleMsgTransSwitch= ruleMsgTransSwitch EOF ;
    public final EObject entryRuleMsgTransSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleMsgTransSwitch = null;


        try {
            // InternalQactork.g:4048:55: (iv_ruleMsgTransSwitch= ruleMsgTransSwitch EOF )
            // InternalQactork.g:4049:2: iv_ruleMsgTransSwitch= ruleMsgTransSwitch EOF
            {
             newCompositeNode(grammarAccess.getMsgTransSwitchRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleMsgTransSwitch=ruleMsgTransSwitch();

            state._fsp--;

             current =iv_ruleMsgTransSwitch; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleMsgTransSwitch"


    // $ANTLR start "ruleMsgTransSwitch"
    // InternalQactork.g:4055:1: ruleMsgTransSwitch returns [EObject current=null] : (otherlv_0= 'whenMsg' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) ;
    public final EObject ruleMsgTransSwitch() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_guard_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:4061:2: ( (otherlv_0= 'whenMsg' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) )
            // InternalQactork.g:4062:2: (otherlv_0= 'whenMsg' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            {
            // InternalQactork.g:4062:2: (otherlv_0= 'whenMsg' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            // InternalQactork.g:4063:3: otherlv_0= 'whenMsg' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,85,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getMsgTransSwitchAccess().getWhenMsgKeyword_0());
            		
            // InternalQactork.g:4067:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:4068:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:4068:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:4069:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMsgTransSwitchRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_50); 

            					newLeafNode(otherlv_1, grammarAccess.getMsgTransSwitchAccess().getMessageDispatchCrossReference_1_0());
            				

            }


            }

            // InternalQactork.g:4080:3: (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )?
            int alt44=2;
            int LA44_0 = input.LA(1);

            if ( (LA44_0==84) ) {
                alt44=1;
            }
            switch (alt44) {
                case 1 :
                    // InternalQactork.g:4081:4: otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) )
                    {
                    otherlv_2=(Token)match(input,84,FOLLOW_16); 

                    				newLeafNode(otherlv_2, grammarAccess.getMsgTransSwitchAccess().getAndKeyword_2_0());
                    			
                    // InternalQactork.g:4085:4: ( (lv_guard_3_0= ruleAnyAction ) )
                    // InternalQactork.g:4086:5: (lv_guard_3_0= ruleAnyAction )
                    {
                    // InternalQactork.g:4086:5: (lv_guard_3_0= ruleAnyAction )
                    // InternalQactork.g:4087:6: lv_guard_3_0= ruleAnyAction
                    {

                    						newCompositeNode(grammarAccess.getMsgTransSwitchAccess().getGuardAnyActionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_49);
                    lv_guard_3_0=ruleAnyAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getMsgTransSwitchRule());
                    						}
                    						set(
                    							current,
                    							"guard",
                    							lv_guard_3_0,
                    							"it.unibo.Qactork.AnyAction");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_4, grammarAccess.getMsgTransSwitchAccess().getHyphenMinusGreaterThanSignKeyword_3());
            		
            // InternalQactork.g:4109:3: ( (otherlv_5= RULE_ID ) )
            // InternalQactork.g:4110:4: (otherlv_5= RULE_ID )
            {
            // InternalQactork.g:4110:4: (otherlv_5= RULE_ID )
            // InternalQactork.g:4111:5: otherlv_5= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getMsgTransSwitchRule());
            					}
            				
            otherlv_5=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_5, grammarAccess.getMsgTransSwitchAccess().getTargetStateStateCrossReference_4_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleMsgTransSwitch"


    // $ANTLR start "entryRuleRequestTransSwitch"
    // InternalQactork.g:4126:1: entryRuleRequestTransSwitch returns [EObject current=null] : iv_ruleRequestTransSwitch= ruleRequestTransSwitch EOF ;
    public final EObject entryRuleRequestTransSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRequestTransSwitch = null;


        try {
            // InternalQactork.g:4126:59: (iv_ruleRequestTransSwitch= ruleRequestTransSwitch EOF )
            // InternalQactork.g:4127:2: iv_ruleRequestTransSwitch= ruleRequestTransSwitch EOF
            {
             newCompositeNode(grammarAccess.getRequestTransSwitchRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRequestTransSwitch=ruleRequestTransSwitch();

            state._fsp--;

             current =iv_ruleRequestTransSwitch; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRequestTransSwitch"


    // $ANTLR start "ruleRequestTransSwitch"
    // InternalQactork.g:4133:1: ruleRequestTransSwitch returns [EObject current=null] : (otherlv_0= 'whenRequest' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) ;
    public final EObject ruleRequestTransSwitch() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_guard_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:4139:2: ( (otherlv_0= 'whenRequest' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) )
            // InternalQactork.g:4140:2: (otherlv_0= 'whenRequest' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            {
            // InternalQactork.g:4140:2: (otherlv_0= 'whenRequest' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            // InternalQactork.g:4141:3: otherlv_0= 'whenRequest' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,86,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getRequestTransSwitchAccess().getWhenRequestKeyword_0());
            		
            // InternalQactork.g:4145:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:4146:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:4146:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:4147:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRequestTransSwitchRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_50); 

            					newLeafNode(otherlv_1, grammarAccess.getRequestTransSwitchAccess().getMessageRequestCrossReference_1_0());
            				

            }


            }

            // InternalQactork.g:4158:3: (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )?
            int alt45=2;
            int LA45_0 = input.LA(1);

            if ( (LA45_0==84) ) {
                alt45=1;
            }
            switch (alt45) {
                case 1 :
                    // InternalQactork.g:4159:4: otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) )
                    {
                    otherlv_2=(Token)match(input,84,FOLLOW_16); 

                    				newLeafNode(otherlv_2, grammarAccess.getRequestTransSwitchAccess().getAndKeyword_2_0());
                    			
                    // InternalQactork.g:4163:4: ( (lv_guard_3_0= ruleAnyAction ) )
                    // InternalQactork.g:4164:5: (lv_guard_3_0= ruleAnyAction )
                    {
                    // InternalQactork.g:4164:5: (lv_guard_3_0= ruleAnyAction )
                    // InternalQactork.g:4165:6: lv_guard_3_0= ruleAnyAction
                    {

                    						newCompositeNode(grammarAccess.getRequestTransSwitchAccess().getGuardAnyActionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_49);
                    lv_guard_3_0=ruleAnyAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getRequestTransSwitchRule());
                    						}
                    						set(
                    							current,
                    							"guard",
                    							lv_guard_3_0,
                    							"it.unibo.Qactork.AnyAction");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_4, grammarAccess.getRequestTransSwitchAccess().getHyphenMinusGreaterThanSignKeyword_3());
            		
            // InternalQactork.g:4187:3: ( (otherlv_5= RULE_ID ) )
            // InternalQactork.g:4188:4: (otherlv_5= RULE_ID )
            {
            // InternalQactork.g:4188:4: (otherlv_5= RULE_ID )
            // InternalQactork.g:4189:5: otherlv_5= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRequestTransSwitchRule());
            					}
            				
            otherlv_5=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_5, grammarAccess.getRequestTransSwitchAccess().getTargetStateStateCrossReference_4_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRequestTransSwitch"


    // $ANTLR start "entryRuleReplyTransSwitch"
    // InternalQactork.g:4204:1: entryRuleReplyTransSwitch returns [EObject current=null] : iv_ruleReplyTransSwitch= ruleReplyTransSwitch EOF ;
    public final EObject entryRuleReplyTransSwitch() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleReplyTransSwitch = null;


        try {
            // InternalQactork.g:4204:57: (iv_ruleReplyTransSwitch= ruleReplyTransSwitch EOF )
            // InternalQactork.g:4205:2: iv_ruleReplyTransSwitch= ruleReplyTransSwitch EOF
            {
             newCompositeNode(grammarAccess.getReplyTransSwitchRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleReplyTransSwitch=ruleReplyTransSwitch();

            state._fsp--;

             current =iv_ruleReplyTransSwitch; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleReplyTransSwitch"


    // $ANTLR start "ruleReplyTransSwitch"
    // InternalQactork.g:4211:1: ruleReplyTransSwitch returns [EObject current=null] : (otherlv_0= 'whenReply' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) ;
    public final EObject ruleReplyTransSwitch() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token otherlv_1=null;
        Token otherlv_2=null;
        Token otherlv_4=null;
        Token otherlv_5=null;
        EObject lv_guard_3_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:4217:2: ( (otherlv_0= 'whenReply' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) ) )
            // InternalQactork.g:4218:2: (otherlv_0= 'whenReply' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            {
            // InternalQactork.g:4218:2: (otherlv_0= 'whenReply' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) ) )
            // InternalQactork.g:4219:3: otherlv_0= 'whenReply' ( (otherlv_1= RULE_ID ) ) (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )? otherlv_4= '->' ( (otherlv_5= RULE_ID ) )
            {
            otherlv_0=(Token)match(input,87,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getReplyTransSwitchAccess().getWhenReplyKeyword_0());
            		
            // InternalQactork.g:4223:3: ( (otherlv_1= RULE_ID ) )
            // InternalQactork.g:4224:4: (otherlv_1= RULE_ID )
            {
            // InternalQactork.g:4224:4: (otherlv_1= RULE_ID )
            // InternalQactork.g:4225:5: otherlv_1= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReplyTransSwitchRule());
            					}
            				
            otherlv_1=(Token)match(input,RULE_ID,FOLLOW_50); 

            					newLeafNode(otherlv_1, grammarAccess.getReplyTransSwitchAccess().getMessageReplyCrossReference_1_0());
            				

            }


            }

            // InternalQactork.g:4236:3: (otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) ) )?
            int alt46=2;
            int LA46_0 = input.LA(1);

            if ( (LA46_0==84) ) {
                alt46=1;
            }
            switch (alt46) {
                case 1 :
                    // InternalQactork.g:4237:4: otherlv_2= 'and' ( (lv_guard_3_0= ruleAnyAction ) )
                    {
                    otherlv_2=(Token)match(input,84,FOLLOW_16); 

                    				newLeafNode(otherlv_2, grammarAccess.getReplyTransSwitchAccess().getAndKeyword_2_0());
                    			
                    // InternalQactork.g:4241:4: ( (lv_guard_3_0= ruleAnyAction ) )
                    // InternalQactork.g:4242:5: (lv_guard_3_0= ruleAnyAction )
                    {
                    // InternalQactork.g:4242:5: (lv_guard_3_0= ruleAnyAction )
                    // InternalQactork.g:4243:6: lv_guard_3_0= ruleAnyAction
                    {

                    						newCompositeNode(grammarAccess.getReplyTransSwitchAccess().getGuardAnyActionParserRuleCall_2_1_0());
                    					
                    pushFollow(FOLLOW_49);
                    lv_guard_3_0=ruleAnyAction();

                    state._fsp--;


                    						if (current==null) {
                    							current = createModelElementForParent(grammarAccess.getReplyTransSwitchRule());
                    						}
                    						set(
                    							current,
                    							"guard",
                    							lv_guard_3_0,
                    							"it.unibo.Qactork.AnyAction");
                    						afterParserOrEnumRuleCall();
                    					

                    }


                    }


                    }
                    break;

            }

            otherlv_4=(Token)match(input,79,FOLLOW_5); 

            			newLeafNode(otherlv_4, grammarAccess.getReplyTransSwitchAccess().getHyphenMinusGreaterThanSignKeyword_3());
            		
            // InternalQactork.g:4265:3: ( (otherlv_5= RULE_ID ) )
            // InternalQactork.g:4266:4: (otherlv_5= RULE_ID )
            {
            // InternalQactork.g:4266:4: (otherlv_5= RULE_ID )
            // InternalQactork.g:4267:5: otherlv_5= RULE_ID
            {

            					if (current==null) {
            						current = createModelElement(grammarAccess.getReplyTransSwitchRule());
            					}
            				
            otherlv_5=(Token)match(input,RULE_ID,FOLLOW_2); 

            					newLeafNode(otherlv_5, grammarAccess.getReplyTransSwitchAccess().getTargetStateStateCrossReference_4_0());
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleReplyTransSwitch"


    // $ANTLR start "entryRulePHead"
    // InternalQactork.g:4282:1: entryRulePHead returns [EObject current=null] : iv_rulePHead= rulePHead EOF ;
    public final EObject entryRulePHead() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePHead = null;


        try {
            // InternalQactork.g:4282:46: (iv_rulePHead= rulePHead EOF )
            // InternalQactork.g:4283:2: iv_rulePHead= rulePHead EOF
            {
             newCompositeNode(grammarAccess.getPHeadRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePHead=rulePHead();

            state._fsp--;

             current =iv_rulePHead; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePHead"


    // $ANTLR start "rulePHead"
    // InternalQactork.g:4289:1: rulePHead returns [EObject current=null] : (this_PAtom_0= rulePAtom | this_PStruct_1= rulePStruct | this_PStructRef_2= rulePStructRef ) ;
    public final EObject rulePHead() throws RecognitionException {
        EObject current = null;

        EObject this_PAtom_0 = null;

        EObject this_PStruct_1 = null;

        EObject this_PStructRef_2 = null;



        	enterRule();

        try {
            // InternalQactork.g:4295:2: ( (this_PAtom_0= rulePAtom | this_PStruct_1= rulePStruct | this_PStructRef_2= rulePStructRef ) )
            // InternalQactork.g:4296:2: (this_PAtom_0= rulePAtom | this_PStruct_1= rulePStruct | this_PStructRef_2= rulePStructRef )
            {
            // InternalQactork.g:4296:2: (this_PAtom_0= rulePAtom | this_PStruct_1= rulePStruct | this_PStructRef_2= rulePStructRef )
            int alt47=3;
            switch ( input.LA(1) ) {
            case RULE_STRING:
            case RULE_INT:
            case RULE_VARID:
            case 89:
            case 90:
                {
                alt47=1;
                }
                break;
            case RULE_ID:
                {
                int LA47_2 = input.LA(2);

                if ( (LA47_2==EOF||(LA47_2>=20 && LA47_2<=27)||LA47_2==30||LA47_2==34||LA47_2==36||LA47_2==38||LA47_2==40||LA47_2==43||(LA47_2>=45 && LA47_2<=47)||(LA47_2>=49 && LA47_2<=52)||(LA47_2>=55 && LA47_2<=56)||LA47_2==58||(LA47_2>=60 && LA47_2<=62)||(LA47_2>=64 && LA47_2<=72)||(LA47_2>=74 && LA47_2<=75)) ) {
                    alt47=1;
                }
                else if ( (LA47_2==48) ) {
                    alt47=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 47, 2, input);

                    throw nvae;
                }
                }
                break;
            case 88:
                {
                int LA47_3 = input.LA(2);

                if ( (LA47_3==RULE_ID) ) {
                    alt47=3;
                }
                else if ( (LA47_3==RULE_VARID) ) {
                    alt47=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 47, 3, input);

                    throw nvae;
                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 47, 0, input);

                throw nvae;
            }

            switch (alt47) {
                case 1 :
                    // InternalQactork.g:4297:3: this_PAtom_0= rulePAtom
                    {

                    			newCompositeNode(grammarAccess.getPHeadAccess().getPAtomParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_PAtom_0=rulePAtom();

                    state._fsp--;


                    			current = this_PAtom_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:4306:3: this_PStruct_1= rulePStruct
                    {

                    			newCompositeNode(grammarAccess.getPHeadAccess().getPStructParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_PStruct_1=rulePStruct();

                    state._fsp--;


                    			current = this_PStruct_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:4315:3: this_PStructRef_2= rulePStructRef
                    {

                    			newCompositeNode(grammarAccess.getPHeadAccess().getPStructRefParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_PStructRef_2=rulePStructRef();

                    state._fsp--;


                    			current = this_PStructRef_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePHead"


    // $ANTLR start "entryRulePAtom"
    // InternalQactork.g:4327:1: entryRulePAtom returns [EObject current=null] : iv_rulePAtom= rulePAtom EOF ;
    public final EObject entryRulePAtom() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePAtom = null;


        try {
            // InternalQactork.g:4327:46: (iv_rulePAtom= rulePAtom EOF )
            // InternalQactork.g:4328:2: iv_rulePAtom= rulePAtom EOF
            {
             newCompositeNode(grammarAccess.getPAtomRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePAtom=rulePAtom();

            state._fsp--;

             current =iv_rulePAtom; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePAtom"


    // $ANTLR start "rulePAtom"
    // InternalQactork.g:4334:1: rulePAtom returns [EObject current=null] : (this_PAtomString_0= rulePAtomString | this_Variable_1= ruleVariable | this_PAtomNum_2= rulePAtomNum | this_PAtomic_3= rulePAtomic | this_VarRef_4= ruleVarRef | this_VarSolRef_5= ruleVarSolRef | this_VarRefInStr_6= ruleVarRefInStr ) ;
    public final EObject rulePAtom() throws RecognitionException {
        EObject current = null;

        EObject this_PAtomString_0 = null;

        EObject this_Variable_1 = null;

        EObject this_PAtomNum_2 = null;

        EObject this_PAtomic_3 = null;

        EObject this_VarRef_4 = null;

        EObject this_VarSolRef_5 = null;

        EObject this_VarRefInStr_6 = null;



        	enterRule();

        try {
            // InternalQactork.g:4340:2: ( (this_PAtomString_0= rulePAtomString | this_Variable_1= ruleVariable | this_PAtomNum_2= rulePAtomNum | this_PAtomic_3= rulePAtomic | this_VarRef_4= ruleVarRef | this_VarSolRef_5= ruleVarSolRef | this_VarRefInStr_6= ruleVarRefInStr ) )
            // InternalQactork.g:4341:2: (this_PAtomString_0= rulePAtomString | this_Variable_1= ruleVariable | this_PAtomNum_2= rulePAtomNum | this_PAtomic_3= rulePAtomic | this_VarRef_4= ruleVarRef | this_VarSolRef_5= ruleVarSolRef | this_VarRefInStr_6= ruleVarRefInStr )
            {
            // InternalQactork.g:4341:2: (this_PAtomString_0= rulePAtomString | this_Variable_1= ruleVariable | this_PAtomNum_2= rulePAtomNum | this_PAtomic_3= rulePAtomic | this_VarRef_4= ruleVarRef | this_VarSolRef_5= ruleVarSolRef | this_VarRefInStr_6= ruleVarRefInStr )
            int alt48=7;
            switch ( input.LA(1) ) {
            case RULE_STRING:
                {
                alt48=1;
                }
                break;
            case RULE_VARID:
                {
                alt48=2;
                }
                break;
            case RULE_INT:
                {
                alt48=3;
                }
                break;
            case RULE_ID:
                {
                alt48=4;
                }
                break;
            case 88:
                {
                alt48=5;
                }
                break;
            case 90:
                {
                alt48=6;
                }
                break;
            case 89:
                {
                alt48=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 48, 0, input);

                throw nvae;
            }

            switch (alt48) {
                case 1 :
                    // InternalQactork.g:4342:3: this_PAtomString_0= rulePAtomString
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getPAtomStringParserRuleCall_0());
                    		
                    pushFollow(FOLLOW_2);
                    this_PAtomString_0=rulePAtomString();

                    state._fsp--;


                    			current = this_PAtomString_0;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 2 :
                    // InternalQactork.g:4351:3: this_Variable_1= ruleVariable
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getVariableParserRuleCall_1());
                    		
                    pushFollow(FOLLOW_2);
                    this_Variable_1=ruleVariable();

                    state._fsp--;


                    			current = this_Variable_1;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 3 :
                    // InternalQactork.g:4360:3: this_PAtomNum_2= rulePAtomNum
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getPAtomNumParserRuleCall_2());
                    		
                    pushFollow(FOLLOW_2);
                    this_PAtomNum_2=rulePAtomNum();

                    state._fsp--;


                    			current = this_PAtomNum_2;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 4 :
                    // InternalQactork.g:4369:3: this_PAtomic_3= rulePAtomic
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getPAtomicParserRuleCall_3());
                    		
                    pushFollow(FOLLOW_2);
                    this_PAtomic_3=rulePAtomic();

                    state._fsp--;


                    			current = this_PAtomic_3;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 5 :
                    // InternalQactork.g:4378:3: this_VarRef_4= ruleVarRef
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getVarRefParserRuleCall_4());
                    		
                    pushFollow(FOLLOW_2);
                    this_VarRef_4=ruleVarRef();

                    state._fsp--;


                    			current = this_VarRef_4;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 6 :
                    // InternalQactork.g:4387:3: this_VarSolRef_5= ruleVarSolRef
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getVarSolRefParserRuleCall_5());
                    		
                    pushFollow(FOLLOW_2);
                    this_VarSolRef_5=ruleVarSolRef();

                    state._fsp--;


                    			current = this_VarSolRef_5;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;
                case 7 :
                    // InternalQactork.g:4396:3: this_VarRefInStr_6= ruleVarRefInStr
                    {

                    			newCompositeNode(grammarAccess.getPAtomAccess().getVarRefInStrParserRuleCall_6());
                    		
                    pushFollow(FOLLOW_2);
                    this_VarRefInStr_6=ruleVarRefInStr();

                    state._fsp--;


                    			current = this_VarRefInStr_6;
                    			afterParserOrEnumRuleCall();
                    		

                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePAtom"


    // $ANTLR start "entryRulePAtomString"
    // InternalQactork.g:4408:1: entryRulePAtomString returns [EObject current=null] : iv_rulePAtomString= rulePAtomString EOF ;
    public final EObject entryRulePAtomString() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePAtomString = null;


        try {
            // InternalQactork.g:4408:52: (iv_rulePAtomString= rulePAtomString EOF )
            // InternalQactork.g:4409:2: iv_rulePAtomString= rulePAtomString EOF
            {
             newCompositeNode(grammarAccess.getPAtomStringRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePAtomString=rulePAtomString();

            state._fsp--;

             current =iv_rulePAtomString; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePAtomString"


    // $ANTLR start "rulePAtomString"
    // InternalQactork.g:4415:1: rulePAtomString returns [EObject current=null] : ( (lv_val_0_0= RULE_STRING ) ) ;
    public final EObject rulePAtomString() throws RecognitionException {
        EObject current = null;

        Token lv_val_0_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4421:2: ( ( (lv_val_0_0= RULE_STRING ) ) )
            // InternalQactork.g:4422:2: ( (lv_val_0_0= RULE_STRING ) )
            {
            // InternalQactork.g:4422:2: ( (lv_val_0_0= RULE_STRING ) )
            // InternalQactork.g:4423:3: (lv_val_0_0= RULE_STRING )
            {
            // InternalQactork.g:4423:3: (lv_val_0_0= RULE_STRING )
            // InternalQactork.g:4424:4: lv_val_0_0= RULE_STRING
            {
            lv_val_0_0=(Token)match(input,RULE_STRING,FOLLOW_2); 

            				newLeafNode(lv_val_0_0, grammarAccess.getPAtomStringAccess().getValSTRINGTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getPAtomStringRule());
            				}
            				setWithLastConsumed(
            					current,
            					"val",
            					lv_val_0_0,
            					"org.eclipse.xtext.common.Terminals.STRING");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePAtomString"


    // $ANTLR start "entryRulePAtomic"
    // InternalQactork.g:4443:1: entryRulePAtomic returns [EObject current=null] : iv_rulePAtomic= rulePAtomic EOF ;
    public final EObject entryRulePAtomic() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePAtomic = null;


        try {
            // InternalQactork.g:4443:48: (iv_rulePAtomic= rulePAtomic EOF )
            // InternalQactork.g:4444:2: iv_rulePAtomic= rulePAtomic EOF
            {
             newCompositeNode(grammarAccess.getPAtomicRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePAtomic=rulePAtomic();

            state._fsp--;

             current =iv_rulePAtomic; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePAtomic"


    // $ANTLR start "rulePAtomic"
    // InternalQactork.g:4450:1: rulePAtomic returns [EObject current=null] : ( (lv_val_0_0= RULE_ID ) ) ;
    public final EObject rulePAtomic() throws RecognitionException {
        EObject current = null;

        Token lv_val_0_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4456:2: ( ( (lv_val_0_0= RULE_ID ) ) )
            // InternalQactork.g:4457:2: ( (lv_val_0_0= RULE_ID ) )
            {
            // InternalQactork.g:4457:2: ( (lv_val_0_0= RULE_ID ) )
            // InternalQactork.g:4458:3: (lv_val_0_0= RULE_ID )
            {
            // InternalQactork.g:4458:3: (lv_val_0_0= RULE_ID )
            // InternalQactork.g:4459:4: lv_val_0_0= RULE_ID
            {
            lv_val_0_0=(Token)match(input,RULE_ID,FOLLOW_2); 

            				newLeafNode(lv_val_0_0, grammarAccess.getPAtomicAccess().getValIDTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getPAtomicRule());
            				}
            				setWithLastConsumed(
            					current,
            					"val",
            					lv_val_0_0,
            					"org.eclipse.xtext.common.Terminals.ID");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePAtomic"


    // $ANTLR start "entryRulePAtomNum"
    // InternalQactork.g:4478:1: entryRulePAtomNum returns [EObject current=null] : iv_rulePAtomNum= rulePAtomNum EOF ;
    public final EObject entryRulePAtomNum() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePAtomNum = null;


        try {
            // InternalQactork.g:4478:49: (iv_rulePAtomNum= rulePAtomNum EOF )
            // InternalQactork.g:4479:2: iv_rulePAtomNum= rulePAtomNum EOF
            {
             newCompositeNode(grammarAccess.getPAtomNumRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePAtomNum=rulePAtomNum();

            state._fsp--;

             current =iv_rulePAtomNum; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePAtomNum"


    // $ANTLR start "rulePAtomNum"
    // InternalQactork.g:4485:1: rulePAtomNum returns [EObject current=null] : ( (lv_val_0_0= RULE_INT ) ) ;
    public final EObject rulePAtomNum() throws RecognitionException {
        EObject current = null;

        Token lv_val_0_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4491:2: ( ( (lv_val_0_0= RULE_INT ) ) )
            // InternalQactork.g:4492:2: ( (lv_val_0_0= RULE_INT ) )
            {
            // InternalQactork.g:4492:2: ( (lv_val_0_0= RULE_INT ) )
            // InternalQactork.g:4493:3: (lv_val_0_0= RULE_INT )
            {
            // InternalQactork.g:4493:3: (lv_val_0_0= RULE_INT )
            // InternalQactork.g:4494:4: lv_val_0_0= RULE_INT
            {
            lv_val_0_0=(Token)match(input,RULE_INT,FOLLOW_2); 

            				newLeafNode(lv_val_0_0, grammarAccess.getPAtomNumAccess().getValINTTerminalRuleCall_0());
            			

            				if (current==null) {
            					current = createModelElement(grammarAccess.getPAtomNumRule());
            				}
            				setWithLastConsumed(
            					current,
            					"val",
            					lv_val_0_0,
            					"org.eclipse.xtext.common.Terminals.INT");
            			

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePAtomNum"


    // $ANTLR start "entryRulePStructRef"
    // InternalQactork.g:4513:1: entryRulePStructRef returns [EObject current=null] : iv_rulePStructRef= rulePStructRef EOF ;
    public final EObject entryRulePStructRef() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePStructRef = null;


        try {
            // InternalQactork.g:4513:51: (iv_rulePStructRef= rulePStructRef EOF )
            // InternalQactork.g:4514:2: iv_rulePStructRef= rulePStructRef EOF
            {
             newCompositeNode(grammarAccess.getPStructRefRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePStructRef=rulePStructRef();

            state._fsp--;

             current =iv_rulePStructRef; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePStructRef"


    // $ANTLR start "rulePStructRef"
    // InternalQactork.g:4520:1: rulePStructRef returns [EObject current=null] : (otherlv_0= '$' ( (lv_struct_1_0= rulePStruct ) ) ) ;
    public final EObject rulePStructRef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        EObject lv_struct_1_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:4526:2: ( (otherlv_0= '$' ( (lv_struct_1_0= rulePStruct ) ) ) )
            // InternalQactork.g:4527:2: (otherlv_0= '$' ( (lv_struct_1_0= rulePStruct ) ) )
            {
            // InternalQactork.g:4527:2: (otherlv_0= '$' ( (lv_struct_1_0= rulePStruct ) ) )
            // InternalQactork.g:4528:3: otherlv_0= '$' ( (lv_struct_1_0= rulePStruct ) )
            {
            otherlv_0=(Token)match(input,88,FOLLOW_5); 

            			newLeafNode(otherlv_0, grammarAccess.getPStructRefAccess().getDollarSignKeyword_0());
            		
            // InternalQactork.g:4532:3: ( (lv_struct_1_0= rulePStruct ) )
            // InternalQactork.g:4533:4: (lv_struct_1_0= rulePStruct )
            {
            // InternalQactork.g:4533:4: (lv_struct_1_0= rulePStruct )
            // InternalQactork.g:4534:5: lv_struct_1_0= rulePStruct
            {

            					newCompositeNode(grammarAccess.getPStructRefAccess().getStructPStructParserRuleCall_1_0());
            				
            pushFollow(FOLLOW_2);
            lv_struct_1_0=rulePStruct();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getPStructRefRule());
            					}
            					set(
            						current,
            						"struct",
            						lv_struct_1_0,
            						"it.unibo.Qactork.PStruct");
            					afterParserOrEnumRuleCall();
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePStructRef"


    // $ANTLR start "entryRulePStruct"
    // InternalQactork.g:4555:1: entryRulePStruct returns [EObject current=null] : iv_rulePStruct= rulePStruct EOF ;
    public final EObject entryRulePStruct() throws RecognitionException {
        EObject current = null;

        EObject iv_rulePStruct = null;


        try {
            // InternalQactork.g:4555:48: (iv_rulePStruct= rulePStruct EOF )
            // InternalQactork.g:4556:2: iv_rulePStruct= rulePStruct EOF
            {
             newCompositeNode(grammarAccess.getPStructRule()); 
            pushFollow(FOLLOW_1);
            iv_rulePStruct=rulePStruct();

            state._fsp--;

             current =iv_rulePStruct; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRulePStruct"


    // $ANTLR start "rulePStruct"
    // InternalQactork.g:4562:1: rulePStruct returns [EObject current=null] : ( ( (lv_functor_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_msgArg_2_0= rulePHead ) ) (otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) ) )* otherlv_5= ')' ) ;
    public final EObject rulePStruct() throws RecognitionException {
        EObject current = null;

        Token lv_functor_0_0=null;
        Token otherlv_1=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_msgArg_2_0 = null;

        EObject lv_msgArg_4_0 = null;



        	enterRule();

        try {
            // InternalQactork.g:4568:2: ( ( ( (lv_functor_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_msgArg_2_0= rulePHead ) ) (otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) ) )* otherlv_5= ')' ) )
            // InternalQactork.g:4569:2: ( ( (lv_functor_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_msgArg_2_0= rulePHead ) ) (otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) ) )* otherlv_5= ')' )
            {
            // InternalQactork.g:4569:2: ( ( (lv_functor_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_msgArg_2_0= rulePHead ) ) (otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) ) )* otherlv_5= ')' )
            // InternalQactork.g:4570:3: ( (lv_functor_0_0= RULE_ID ) ) otherlv_1= '(' ( (lv_msgArg_2_0= rulePHead ) ) (otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) ) )* otherlv_5= ')'
            {
            // InternalQactork.g:4570:3: ( (lv_functor_0_0= RULE_ID ) )
            // InternalQactork.g:4571:4: (lv_functor_0_0= RULE_ID )
            {
            // InternalQactork.g:4571:4: (lv_functor_0_0= RULE_ID )
            // InternalQactork.g:4572:5: lv_functor_0_0= RULE_ID
            {
            lv_functor_0_0=(Token)match(input,RULE_ID,FOLLOW_30); 

            					newLeafNode(lv_functor_0_0, grammarAccess.getPStructAccess().getFunctorIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getPStructRule());
            					}
            					setWithLastConsumed(
            						current,
            						"functor",
            						lv_functor_0_0,
            						"org.eclipse.xtext.common.Terminals.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,48,FOLLOW_14); 

            			newLeafNode(otherlv_1, grammarAccess.getPStructAccess().getLeftParenthesisKeyword_1());
            		
            // InternalQactork.g:4592:3: ( (lv_msgArg_2_0= rulePHead ) )
            // InternalQactork.g:4593:4: (lv_msgArg_2_0= rulePHead )
            {
            // InternalQactork.g:4593:4: (lv_msgArg_2_0= rulePHead )
            // InternalQactork.g:4594:5: lv_msgArg_2_0= rulePHead
            {

            					newCompositeNode(grammarAccess.getPStructAccess().getMsgArgPHeadParserRuleCall_2_0());
            				
            pushFollow(FOLLOW_32);
            lv_msgArg_2_0=rulePHead();

            state._fsp--;


            					if (current==null) {
            						current = createModelElementForParent(grammarAccess.getPStructRule());
            					}
            					add(
            						current,
            						"msgArg",
            						lv_msgArg_2_0,
            						"it.unibo.Qactork.PHead");
            					afterParserOrEnumRuleCall();
            				

            }


            }

            // InternalQactork.g:4611:3: (otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) ) )*
            loop49:
            do {
                int alt49=2;
                int LA49_0 = input.LA(1);

                if ( (LA49_0==51) ) {
                    alt49=1;
                }


                switch (alt49) {
            	case 1 :
            	    // InternalQactork.g:4612:4: otherlv_3= ',' ( (lv_msgArg_4_0= rulePHead ) )
            	    {
            	    otherlv_3=(Token)match(input,51,FOLLOW_14); 

            	    				newLeafNode(otherlv_3, grammarAccess.getPStructAccess().getCommaKeyword_3_0());
            	    			
            	    // InternalQactork.g:4616:4: ( (lv_msgArg_4_0= rulePHead ) )
            	    // InternalQactork.g:4617:5: (lv_msgArg_4_0= rulePHead )
            	    {
            	    // InternalQactork.g:4617:5: (lv_msgArg_4_0= rulePHead )
            	    // InternalQactork.g:4618:6: lv_msgArg_4_0= rulePHead
            	    {

            	    						newCompositeNode(grammarAccess.getPStructAccess().getMsgArgPHeadParserRuleCall_3_1_0());
            	    					
            	    pushFollow(FOLLOW_32);
            	    lv_msgArg_4_0=rulePHead();

            	    state._fsp--;


            	    						if (current==null) {
            	    							current = createModelElementForParent(grammarAccess.getPStructRule());
            	    						}
            	    						add(
            	    							current,
            	    							"msgArg",
            	    							lv_msgArg_4_0,
            	    							"it.unibo.Qactork.PHead");
            	    						afterParserOrEnumRuleCall();
            	    					

            	    }


            	    }


            	    }
            	    break;

            	default :
            	    break loop49;
                }
            } while (true);

            otherlv_5=(Token)match(input,49,FOLLOW_2); 

            			newLeafNode(otherlv_5, grammarAccess.getPStructAccess().getRightParenthesisKeyword_4());
            		

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "rulePStruct"


    // $ANTLR start "entryRuleVariable"
    // InternalQactork.g:4644:1: entryRuleVariable returns [EObject current=null] : iv_ruleVariable= ruleVariable EOF ;
    public final EObject entryRuleVariable() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleVariable = null;


        try {
            // InternalQactork.g:4644:49: (iv_ruleVariable= ruleVariable EOF )
            // InternalQactork.g:4645:2: iv_ruleVariable= ruleVariable EOF
            {
             newCompositeNode(grammarAccess.getVariableRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleVariable=ruleVariable();

            state._fsp--;

             current =iv_ruleVariable; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleVariable"


    // $ANTLR start "ruleVariable"
    // InternalQactork.g:4651:1: ruleVariable returns [EObject current=null] : ( () ( (lv_varName_1_0= RULE_VARID ) ) ) ;
    public final EObject ruleVariable() throws RecognitionException {
        EObject current = null;

        Token lv_varName_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4657:2: ( ( () ( (lv_varName_1_0= RULE_VARID ) ) ) )
            // InternalQactork.g:4658:2: ( () ( (lv_varName_1_0= RULE_VARID ) ) )
            {
            // InternalQactork.g:4658:2: ( () ( (lv_varName_1_0= RULE_VARID ) ) )
            // InternalQactork.g:4659:3: () ( (lv_varName_1_0= RULE_VARID ) )
            {
            // InternalQactork.g:4659:3: ()
            // InternalQactork.g:4660:4: 
            {

            				current = forceCreateModelElement(
            					grammarAccess.getVariableAccess().getVariableAction_0(),
            					current);
            			

            }

            // InternalQactork.g:4666:3: ( (lv_varName_1_0= RULE_VARID ) )
            // InternalQactork.g:4667:4: (lv_varName_1_0= RULE_VARID )
            {
            // InternalQactork.g:4667:4: (lv_varName_1_0= RULE_VARID )
            // InternalQactork.g:4668:5: lv_varName_1_0= RULE_VARID
            {
            lv_varName_1_0=(Token)match(input,RULE_VARID,FOLLOW_2); 

            					newLeafNode(lv_varName_1_0, grammarAccess.getVariableAccess().getVarNameVARIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getVariableRule());
            					}
            					setWithLastConsumed(
            						current,
            						"varName",
            						lv_varName_1_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleVariable"


    // $ANTLR start "entryRuleVarRef"
    // InternalQactork.g:4688:1: entryRuleVarRef returns [EObject current=null] : iv_ruleVarRef= ruleVarRef EOF ;
    public final EObject entryRuleVarRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleVarRef = null;


        try {
            // InternalQactork.g:4688:47: (iv_ruleVarRef= ruleVarRef EOF )
            // InternalQactork.g:4689:2: iv_ruleVarRef= ruleVarRef EOF
            {
             newCompositeNode(grammarAccess.getVarRefRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleVarRef=ruleVarRef();

            state._fsp--;

             current =iv_ruleVarRef; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleVarRef"


    // $ANTLR start "ruleVarRef"
    // InternalQactork.g:4695:1: ruleVarRef returns [EObject current=null] : (otherlv_0= '$' ( (lv_varName_1_0= RULE_VARID ) ) ) ;
    public final EObject ruleVarRef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_varName_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4701:2: ( (otherlv_0= '$' ( (lv_varName_1_0= RULE_VARID ) ) ) )
            // InternalQactork.g:4702:2: (otherlv_0= '$' ( (lv_varName_1_0= RULE_VARID ) ) )
            {
            // InternalQactork.g:4702:2: (otherlv_0= '$' ( (lv_varName_1_0= RULE_VARID ) ) )
            // InternalQactork.g:4703:3: otherlv_0= '$' ( (lv_varName_1_0= RULE_VARID ) )
            {
            otherlv_0=(Token)match(input,88,FOLLOW_33); 

            			newLeafNode(otherlv_0, grammarAccess.getVarRefAccess().getDollarSignKeyword_0());
            		
            // InternalQactork.g:4707:3: ( (lv_varName_1_0= RULE_VARID ) )
            // InternalQactork.g:4708:4: (lv_varName_1_0= RULE_VARID )
            {
            // InternalQactork.g:4708:4: (lv_varName_1_0= RULE_VARID )
            // InternalQactork.g:4709:5: lv_varName_1_0= RULE_VARID
            {
            lv_varName_1_0=(Token)match(input,RULE_VARID,FOLLOW_2); 

            					newLeafNode(lv_varName_1_0, grammarAccess.getVarRefAccess().getVarNameVARIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getVarRefRule());
            					}
            					setWithLastConsumed(
            						current,
            						"varName",
            						lv_varName_1_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleVarRef"


    // $ANTLR start "entryRuleVarRefInStr"
    // InternalQactork.g:4729:1: entryRuleVarRefInStr returns [EObject current=null] : iv_ruleVarRefInStr= ruleVarRefInStr EOF ;
    public final EObject entryRuleVarRefInStr() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleVarRefInStr = null;


        try {
            // InternalQactork.g:4729:52: (iv_ruleVarRefInStr= ruleVarRefInStr EOF )
            // InternalQactork.g:4730:2: iv_ruleVarRefInStr= ruleVarRefInStr EOF
            {
             newCompositeNode(grammarAccess.getVarRefInStrRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleVarRefInStr=ruleVarRefInStr();

            state._fsp--;

             current =iv_ruleVarRefInStr; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleVarRefInStr"


    // $ANTLR start "ruleVarRefInStr"
    // InternalQactork.g:4736:1: ruleVarRefInStr returns [EObject current=null] : (otherlv_0= '#' ( (lv_varName_1_0= RULE_VARID ) ) ) ;
    public final EObject ruleVarRefInStr() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_varName_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4742:2: ( (otherlv_0= '#' ( (lv_varName_1_0= RULE_VARID ) ) ) )
            // InternalQactork.g:4743:2: (otherlv_0= '#' ( (lv_varName_1_0= RULE_VARID ) ) )
            {
            // InternalQactork.g:4743:2: (otherlv_0= '#' ( (lv_varName_1_0= RULE_VARID ) ) )
            // InternalQactork.g:4744:3: otherlv_0= '#' ( (lv_varName_1_0= RULE_VARID ) )
            {
            otherlv_0=(Token)match(input,89,FOLLOW_33); 

            			newLeafNode(otherlv_0, grammarAccess.getVarRefInStrAccess().getNumberSignKeyword_0());
            		
            // InternalQactork.g:4748:3: ( (lv_varName_1_0= RULE_VARID ) )
            // InternalQactork.g:4749:4: (lv_varName_1_0= RULE_VARID )
            {
            // InternalQactork.g:4749:4: (lv_varName_1_0= RULE_VARID )
            // InternalQactork.g:4750:5: lv_varName_1_0= RULE_VARID
            {
            lv_varName_1_0=(Token)match(input,RULE_VARID,FOLLOW_2); 

            					newLeafNode(lv_varName_1_0, grammarAccess.getVarRefInStrAccess().getVarNameVARIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getVarRefInStrRule());
            					}
            					setWithLastConsumed(
            						current,
            						"varName",
            						lv_varName_1_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleVarRefInStr"


    // $ANTLR start "entryRuleVarSolRef"
    // InternalQactork.g:4770:1: entryRuleVarSolRef returns [EObject current=null] : iv_ruleVarSolRef= ruleVarSolRef EOF ;
    public final EObject entryRuleVarSolRef() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleVarSolRef = null;


        try {
            // InternalQactork.g:4770:50: (iv_ruleVarSolRef= ruleVarSolRef EOF )
            // InternalQactork.g:4771:2: iv_ruleVarSolRef= ruleVarSolRef EOF
            {
             newCompositeNode(grammarAccess.getVarSolRefRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleVarSolRef=ruleVarSolRef();

            state._fsp--;

             current =iv_ruleVarSolRef; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleVarSolRef"


    // $ANTLR start "ruleVarSolRef"
    // InternalQactork.g:4777:1: ruleVarSolRef returns [EObject current=null] : (otherlv_0= '@' ( (lv_varName_1_0= RULE_VARID ) ) ) ;
    public final EObject ruleVarSolRef() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_varName_1_0=null;


        	enterRule();

        try {
            // InternalQactork.g:4783:2: ( (otherlv_0= '@' ( (lv_varName_1_0= RULE_VARID ) ) ) )
            // InternalQactork.g:4784:2: (otherlv_0= '@' ( (lv_varName_1_0= RULE_VARID ) ) )
            {
            // InternalQactork.g:4784:2: (otherlv_0= '@' ( (lv_varName_1_0= RULE_VARID ) ) )
            // InternalQactork.g:4785:3: otherlv_0= '@' ( (lv_varName_1_0= RULE_VARID ) )
            {
            otherlv_0=(Token)match(input,90,FOLLOW_33); 

            			newLeafNode(otherlv_0, grammarAccess.getVarSolRefAccess().getCommercialAtKeyword_0());
            		
            // InternalQactork.g:4789:3: ( (lv_varName_1_0= RULE_VARID ) )
            // InternalQactork.g:4790:4: (lv_varName_1_0= RULE_VARID )
            {
            // InternalQactork.g:4790:4: (lv_varName_1_0= RULE_VARID )
            // InternalQactork.g:4791:5: lv_varName_1_0= RULE_VARID
            {
            lv_varName_1_0=(Token)match(input,RULE_VARID,FOLLOW_2); 

            					newLeafNode(lv_varName_1_0, grammarAccess.getVarSolRefAccess().getVarNameVARIDTerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getVarSolRefRule());
            					}
            					setWithLastConsumed(
            						current,
            						"varName",
            						lv_varName_1_0,
            						"it.unibo.Qactork.VARID");
            				

            }


            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleVarSolRef"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x000000000000C010L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000010002L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x000000540FF20002L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x000000540FF00002L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000005408000002L});
    public static final BitSet FOLLOW_9 = new BitSet(new long[]{0x0000005400000002L});
    public static final BitSet FOLLOW_10 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_11 = new BitSet(new long[]{0x0000000000040000L});
    public static final BitSet FOLLOW_12 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_13 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_14 = new BitSet(new long[]{0x00000000000000F0L,0x0000000007000000L});
    public static final BitSet FOLLOW_15 = new BitSet(new long[]{0x0000000010000000L});
    public static final BitSet FOLLOW_16 = new BitSet(new long[]{0x0000000040000000L});
    public static final BitSet FOLLOW_17 = new BitSet(new long[]{0x0000000020000002L});
    public static final BitSet FOLLOW_18 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_19 = new BitSet(new long[]{0x0000000100000000L});
    public static final BitSet FOLLOW_20 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_21 = new BitSet(new long[]{0x0000000800000000L});
    public static final BitSet FOLLOW_22 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_23 = new BitSet(new long[]{0x0000008000000000L});
    public static final BitSet FOLLOW_24 = new BitSet(new long[]{0x0000030040000000L});
    public static final BitSet FOLLOW_25 = new BitSet(new long[]{0x0000030000000000L});
    public static final BitSet FOLLOW_26 = new BitSet(new long[]{0x0000048000000000L});
    public static final BitSet FOLLOW_27 = new BitSet(new long[]{0x7594E90040000000L,0x0000000000000DFFL});
    public static final BitSet FOLLOW_28 = new BitSet(new long[]{0x0000000000000002L,0x0000000000003000L});
    public static final BitSet FOLLOW_29 = new BitSet(new long[]{0x0000100000000002L});
    public static final BitSet FOLLOW_30 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_31 = new BitSet(new long[]{0x0002000000000000L});
    public static final BitSet FOLLOW_32 = new BitSet(new long[]{0x000A000000000000L});
    public static final BitSet FOLLOW_33 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_34 = new BitSet(new long[]{0x0060000000000000L});
    public static final BitSet FOLLOW_35 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_36 = new BitSet(new long[]{0x0800000000000000L});
    public static final BitSet FOLLOW_37 = new BitSet(new long[]{0x8000000000000000L});
    public static final BitSet FOLLOW_38 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_39 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_40 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_41 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_42 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000200L});
    public static final BitSet FOLLOW_43 = new BitSet(new long[]{0x00020000000000F0L,0x0000000007000000L});
    public static final BitSet FOLLOW_44 = new BitSet(new long[]{0x0000200000000002L});
    public static final BitSet FOLLOW_45 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_46 = new BitSet(new long[]{0x0000100000000002L,0x0000000000EF4000L});
    public static final BitSet FOLLOW_47 = new BitSet(new long[]{0x0000100000000002L,0x0000000000E80000L});
    public static final BitSet FOLLOW_48 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_49 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_50 = new BitSet(new long[]{0x0000000000000000L,0x0000000000108000L});

}