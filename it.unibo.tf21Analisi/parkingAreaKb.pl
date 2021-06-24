slotFree( 1 ).
slotFree( 2 ).
slotFree( 3 ).
slotFree( 4 ).
slotFree( 5 ).
slotFree( 6 ).

indoorfree.

occupaSlot( N ):-
	retract( slotFree( N ) ).
	
isfreeslot(N) :- slotFree(N).