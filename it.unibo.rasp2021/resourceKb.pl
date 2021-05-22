%%resourceKb.pl

 
unify( A, B ) :- A = B.
allHLines(P1,HL):-
	findall( secondPoint(P2), horizontalLine(P1,P2), HL).

 
initStepCounter:- assign(stepcounter,1).
 	
 
 
 

 