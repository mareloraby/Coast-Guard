:-include('KB.pl').

/* Movement Actions*/
up(A,PrevX,CgY,CgX,CgY,PrevShips,PrevCarriedCount,PrevCarriedCount,PrevShips):-
    CgX >= 0,
    PrevX is CgX+1,
    A = up.
down(A,PrevX,CgY,CgX,CgY,PrevShips,PrevCarriedCount,PrevCarriedCount,PrevShips):-
    grid(D,D),CgX < D,
    PrevX is CgX-1,
    A = down.
left(A,CgX,PrevY,CgX,CgY,PrevShips,PrevCarriedCount,PrevCarriedCount,PrevShips):-
    CgY >= 0,
    PrevY is CgY+1,
    A = left.
right(A,CgX,PrevY,CgX,CgY,PrevShips,PrevCarriedCount,PrevCarriedCount,PrevShips):-
    grid(D,D),CgY < D,
    PrevY is CgY-1,
    A = right.

/* This was the last and only passenger to pickup in the grid. The ship location will be added to the previous ships list and removed from current ships list
    The grid contains only 1 ship*/
pickup(A,CgX,CgY,CgX,CgY, CurrCarriedCount,[],[[CgX,CgY]],PrevCarriedCount):-
    capacity(C),
    (ships_loc([[CgX,CgY]]);
    ships_loc([[CgX,CgY],_]);
    ships_loc([_,[CgX,CgY]])),
    CurrCarriedCount =< C,
    PrevCarriedCount is CurrCarriedCount - 1,
    A = pickup.

/* 1 passenger will be remaning to pickup. The ship's location where its passenger was picked up will be added to the previous ships list and removed from current ships list
   The grid contains 2 ships*/
pickup(A,CgX,CgY,CgX,CgY, 1,[Sh2],[[CgX,CgY],Sh2],0):-
    (ships_loc([[CgX,CgY],_]);
    ships_loc([_,[CgX,CgY]])),
    A = pickup.

pickup(A,CgX,CgY,CgX,CgY, 1,[Sh2],[Sh2,[CgX,CgY]],0):-
    (ships_loc([[CgX,CgY],_]);
    ships_loc([_,[CgX,CgY]])),
    A = pickup.

/* Carrying Two passengers and the grid had 2 ships.*/
drop(A,CgX,CgY,CgX,CgY, 0,[],[],2):-
    station(CgX,CgY),
    A = drop.

/*Carrying one passenger and the grid had 1 ship.*/
drop(A,CgX,CgY,CgX,CgY, 0,[],[],1):-
    station(CgX,CgY),
    A = drop.

/*Carrying one passenger and the grid had 2 ships.*/
drop(A,CgX,CgY,CgX,CgY, 0,[Sh1],[Sh1],1):-
    station(CgX,CgY),
    (ships_loc([Sh1,_]);
    ships_loc([_,Sh1])),
    A = drop.

/* Base case of cgAction: if coastGuard will need just 1 move to reach the initial position*/
cgAction(CgX,CgY,PrevShips,0,result(A,s0)):-
    agent_loc(PrevX,PrevY),   /*agent standing at initial position mentioned in the KB*/
    ships_loc(PrevShips),
    (down(A,PrevX,PrevY,CgX,CgY,_,_,_,_);
    left(A,PrevX,PrevY,CgX,CgY,_,_,_,_);
    right(A,PrevX,PrevY,CgX,CgY,_,_,_,_);
    up(A,PrevX,PrevY,CgX,CgY,_,_,_,_)).

/* Recursive case of coast guard actions till reaching the above base case */
cgAction(CgX,CgY,CurrShips,CurrCarriedCount,result(A,S)):-
    (
	down(A,PrevX,PrevY,CgX,CgY,PrevShips,PrevCarriedCount,CurrCarriedCount,CurrShips);
    up(A,PrevX,PrevY,CgX,CgY,PrevShips,PrevCarriedCount,CurrCarriedCount,CurrShips);
    left(A,PrevX,PrevY,CgX,CgY,PrevShips,PrevCarriedCount,CurrCarriedCount,CurrShips);
    right(A,PrevX,PrevY,CgX,CgY,PrevShips,PrevCarriedCount,CurrCarriedCount,CurrShips);
    pickup(A,PrevX,PrevY,CgX,CgY,CurrCarriedCount,CurrShips,PrevShips,PrevCarriedCount);
    drop(A,PrevX,PrevY,CgX,CgY,CurrCarriedCount,CurrShips,PrevShips,PrevCarriedCount)
	),
    cgAction(PrevX,PrevY,PrevShips,PrevCarriedCount,S).


goal(S):-
	ids(S,1).

ids(X,L):-
	(call_with_depth_limit(goal2(X),L,R), number(R));
	(call_with_depth_limit(goal2(X),L,R), R=depth_limit_exceeded,
	L1 is L+1, ids(X,L1)).

goal2(S):-
	station(CgX,CgY),   /*currently standing at a station as the last actions shall always be drop*/ 
    cgAction(CgX,CgY,[],0,S).

