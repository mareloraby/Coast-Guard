> This repository holds our solutions for the coast guard problem of the CSEN901 Introduction to Artificial Intelligence course - Winter 2022


                                                   v  ~.      v
                                          v           /|
                                                     / |          v
                                              v     /__|__
                                                  \--------/
                                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                

# Coast Guard Problem

A coast guard is in charge of a rescue boat that goes into the sea to rescue other sinking ships. When rescuing a ship, they need to rescue any living people on it and to retrieve its black box after there are no more passengers thereon to rescue. If a ship sinks completely, it becomes a wreck and they still have to retrieve the black box before it is damaged. Each ship loses one passenger every time step. Additionally, each black box incurs an additional damage point every time
step once the ship becomes a wreck. One time step is counted every time an action is performed. Goal is reached when there are no living passengers who are not rescued, there are no undamaged boxes which have not been retrieved, and the rescue boat is not carrying any passengers. They would also like to rescue as many people as possible and retrieve as many black boxes as possible.

**This could be defined as a search problem, where a search agent uses different search strategies to save as much passengers as possible, and retrieve as much black boxes as possible. The search problem is defined as follows:**

- Operators:  Up, Down, Left, Right, Pick-up, Drop, Retrieve

- Initial State: Coast guard, ships and stations are at random locations within the grid boundaries. Each ship has a random number of passengers p onboard, where  $0 < p <= 100$ . There are no wrecks, and no two items are in the same cell.

- State Space: Each state represents grid dimensions, maximum number of passengers the coast guard rescue boat can carry, current coast guard location, current locations of all stations, current locations of all ships and number of remaining passengers on each ship, current locations of all wrecks and the counter number of each black box, current remaining capacity in the coast guard rescue boat, remaining passengers on the grid, remaining ships on the grid, remaining wrecks on the grid, dead passengers so far, retrieved boxes so far and the lost boxes so far.
It is represented in our implementation as follows: griddims; maxCapacity; location of coast guard; locations of stations; locations of ships and \# of passengers on each ship; locations of wrecks and the current damage; remaining cg capacity; \# remaining ships; \# remaining boxes; \# dead passengers ; \# retrieved boxes; \# lost boxes.
  
- Goal Test: No living passengers on grid. No passengers on rescue boat. No unretrieved undamaged boxes.
- Path Cost: < deaths , lost boxes>

## Project 1 

The first project was implemented in Java.  The generic search procedure was implemented: where it takes a problem and a search strategy as inputs.
 The search strategies included:
 - BFS
 - DFS
 - IDS
 - UC
 - Greedy with two different heuristics.
 - A* with two different heuristics.
 
A detailed description of our implementation, and the input and output parameters are mentioned in [Project 1 AI report](https://github.com/mareloraby/Coast-Guard/blob/master/AI%20Project%201%20Report.pdf).
### Example run

In [src/code/Main.java](src/code/Main.java),
generate a random grid input using ```costGuard.genGrid()```. The dimensions of the grid, the locations of
the coast guard, stations, ships, number of passengers for each ship, and the coast
guard capacity are all randomly generated. 
Uncomment (remove ```\\```) these lines, choose desired strategy and run Main.java:
```
System.out.println("Randomly initialized grid: "); 
String generatedGrid = coastGuard.genGrid(); 
coastGuard.solve(generatedGrid,"BF",true);
```

You could also pass your grid as input in the place of ```generatedGrid```, as follows:
```
System.out.println("User generated grid: ");
String gridExample1 = "6,7;82;1,4;2,3;1,1,58,3,0,58,4,2,72;";
String gridExample2 = "5,6;50;0,1;0,4,3,3;1,1,90;";
coastGuard.solve(gridExample1,"BF",true);
```

<details>
	<summary> Output  </summary>

```
Initial State: 6,7;82;4,1;3,2;1,1,58,0,3,58,2,4,72;$;82;188;3;0;0;0;0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  58sh0  |    *    |    *    |  cg82   |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  58sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  72sh2  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;4,2;3,2;1,1,57,0,3,57,2,4,71;$;82;185;3;0;3;0;0
Action Taken: DOWN
Dead so far: 3  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  57sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |  cg82   |    *    |
+--------+---------+---------+---------+---------+---------+
  57sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  71sh2  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;4,3;3,2;1,1,56,0,3,56,2,4,70;$;82;182;3;0;6;0;0
Action Taken: DOWN
Dead so far: 6  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  56sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  56sh1  |    *    |    *    |    *    |  cg82   |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  70sh2  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;4,4;3,2;1,1,55,0,3,55,2,4,69;$;82;179;3;0;9;0;0
Action Taken: DOWN
Dead so far: 9  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  55sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  55sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  69sh2  |    *    |  cg82   |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,4;3,2;1,1,54,0,3,54,2,4,68;$;82;176;3;0;12;0;0
Action Taken: LEFT
Dead so far: 12  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  54sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  54sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  68sh2  |  cg82   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;2,4;3,2;1,1,53,0,3,53,2,4,67;$;82;173;3;0;15;0;0
Action Taken: LEFT
Dead so far: 15  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  53sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  53sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |(c)67sh2 |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;2,4;3,2;1,1,52,0,3,52;2,4,0;15;171;2;1;17;0;0
Action Taken: PICKUP
Dead so far: 17  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  52sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  52sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    | (c)0wr0 |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;2,3;3,2;1,1,51,0,3,51;2,4,1;15;169;2;1;19;0;0
Action Taken: UP
Dead so far: 19  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  51sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  51sh1  |    *    |  cg82   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  1wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;2,2;3,2;1,1,50,0,3,50;2,4,2;15;167;2;1;21;0;0
Action Taken: UP
Dead so far: 21  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  50sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  cg82   |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  50sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  2wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,2;3,2;1,1,49,0,3,49;2,4,3;15;165;2;1;23;0;0
Action Taken: RIGHT
Dead so far: 23  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  49sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    | (c)st0  |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  49sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  3wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,2;3,2;1,1,48,0,3,48;2,4,4;82;96;2;1;25;0;0
Action Taken: DROP
Dead so far: 25  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  48sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    | (c)st0  |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  48sh1  |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  4wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,3;3,2;1,1,47,0,3,47;2,4,5;82;94;2;1;27;0;0
Action Taken: DOWN
Dead so far: 27  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  47sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  47sh1  |    *    |    *    |  cg82   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  5wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;2,3;3,2;1,1,46,0,3,46;2,4,6;82;92;2;1;29;0;0
Action Taken: LEFT
Dead so far: 29  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  46sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  46sh1  |    *    |  cg82   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  6wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;1,3;3,2;1,1,45,0,3,45;2,4,7;82;90;2;1;31;0;0
Action Taken: LEFT
Dead so far: 31  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  45sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  45sh1  |  cg82   |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  7wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;0,3;3,2;1,1,44,0,3,44;2,4,8;82;88;2;1;33;0;0
Action Taken: LEFT
Dead so far: 33  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  44sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
(c)44sh1 |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  8wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;0,3;3,2;1,1,43;2,4,9,0,3,0;38;87;1;2;34;0;0
Action Taken: PICKUP
Dead so far: 34  Retrieved boxes so far: 0 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  43sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
 (c)0wr1 |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  9wr0   |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;0,3;3,2;1,1,42;2,4,10;38;86;1;1;35;1;0
Action Taken: RETRIEVE
Dead so far: 35  Retrieved boxes so far: 1 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  42sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  cg82   |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  10wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;0,2;3,2;1,1,41;2,4,11;38;85;1;1;36;1;0
Action Taken: UP
Dead so far: 36  Retrieved boxes so far: 1 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  41sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  cg82   |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  11wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;0,1;3,2;1,1,40;2,4,12;38;84;1;1;37;1;0
Action Taken: UP
Dead so far: 37  Retrieved boxes so far: 1 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
  cg82   |  40sh0  |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  12wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;1,1;3,2;1,1,39;2,4,13;38;83;1;1;38;1;0
Action Taken: RIGHT
Dead so far: 38  Retrieved boxes so far: 1 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |(c)39sh0 |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  13wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;1,1;3,2;#;2,4,14,1,1,0;0;82;1;2;39;1;0
Action Taken: PICKUP
Dead so far: 39  Retrieved boxes so far: 1 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    | (c)0wr1 |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  14wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;1,1;3,2;#;2,4,15;0;82;1;1;39;2;0
Action Taken: RETRIEVE
Dead so far: 39  Retrieved boxes so far: 2 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  cg82   |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  15wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;1,2;3,2;#;2,4,16;0;82;1;1;39;2;0
Action Taken: DOWN
Dead so far: 39  Retrieved boxes so far: 2 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |  cg82   |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  16wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;2,2;3,2;#;2,4,17;0;82;1;1;39;2;0
Action Taken: RIGHT
Dead so far: 39  Retrieved boxes so far: 2 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  cg82   |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  17wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,2;3,2;#;2,4,18;0;82;1;1;39;2;0
Action Taken: RIGHT
Dead so far: 39  Retrieved boxes so far: 2 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    | (c)st0  |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  18wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,2;3,2;#;2,4,19;82;0;1;1;39;2;0
Action Taken: DROP
Dead so far: 39  Retrieved boxes so far: 2 Lost boxes so far: 0
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    | (c)st0  |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |  19wr0  |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################
currState: 6,7;82;3,1;3,2;#;$;82;0;1;0;39;2;1
Action Taken: UP
Dead so far: 39  Retrieved boxes so far: 2 Lost boxes so far: 1
6x7
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |  cg82   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |   st0   |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
+--------+---------+---------+---------+---------+---------+
    *    |    *    |    *    |    *    |    *    |    *    |
---------+---------+---------+---------+---------+---------+
##########################################################################################################

Result: DOWN,DOWN,DOWN,LEFT,LEFT,PICKUP,UP,UP,RIGHT,DROP,DOWN,LEFT,LEFT,LEFT,PICKUP,RETRIEVE,UP,UP,RIGHT,PICKUP,RETRIEVE,DOWN,RIGHT,RIGHT,DROP,UP;39;2;88968

```
</details>


<hr>

## Project 2  
In [Project 2](/SuccessorState) we implemented a simplified logic-based
version of the Coast Guard agent using prolog. This agent reasons using the situation calculus.

**The following simplifying assumptions were made:**

- There are no black boxes to retrieve.

- There is only one station and a maximum of two ships.

- Each ship has exactly one passenger who does not expire with time.

- The agent’s capacity can be either 1 or 2.

- The grid size is 3x3 or 4x4.

### Example run:

- ```goal(S)``` query is used to get one possible solution (path) to the goal. To get more possible paths, input ```;```.
    <details>
    <summary> Query: <code>goal(S).</code> </summary>
      <code>S = result(drop, result(up, result(left, result(pickup, result(down, result(right, result(drop, result(left, result(pickup, result(down, result(right, s0)))))))))))</code>  
      </details>

- You could also ask if a following goal path is true:
    <details>
    <summary> Query:
    <code>goal(result(drop, result(up, result(left, result(pickup, result(down, result(right, result(drop, result(left, result(pickup, result(down, result(right, s0)))))))))))).</code>  
    </summary>
    true.
    </details>


# Developers:
- Rowan Amgad
- Maryam ElOraby
