%%applRuleskb.pl

%%FACTS
prevd( 0 ).
delta( 3 ).         %% 10 is the prefixed value for LazyUpdate
templimit( 30 ).    %% 30 is the prefixed value for temperature
dlimit( 30 ).       %% 20 is the prefixed distance value for LedChange 
led( off ).         %% initial state of the Led


%%RULES
modifyResource(D,ok)  :- modifyResource(D).
modifyResource(D,ko).
 
modifyResource(D) :- delta( DELTA ), prevd( PD ), 
                     %% stdout <- println( values( PD,D,DELTA ) ),
                     V is PD - D,
                     abs( V )  > DELTA, 	                 %%condition for LazyUpdate
                     replaceRule( prevd( _ ), prevd(D) ).        %%replaceRule is defined in sysRules.pl
% modifyResource fails if the condition for LazyUpdate fails.

modifyLed(D,T,ok) :- modifyLed(D,T),!.
modifyLed(D,T,ko).

modifyLed(D,T)    :- templimit(TL),  dlimit(DL),  
                     D < DL, T > TL,     %%condition for LedChange             
                     !, replaceRule( led(_), led( on ) ).
modifyLed(D,T)    :- led(on),
                     replaceRule( led(_), !, led( off ) ). 				 
% modifyLed fails if the codition for LedChange fails and the led is on.

 