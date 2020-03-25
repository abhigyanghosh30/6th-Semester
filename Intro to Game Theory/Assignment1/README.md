Intorduction to Game Theory
===========================

Assignment 1
-------------

##### Snehashis Pal - 2018201072
##### Abhigyan Ghosh - 20171089

### Finding Pure Strategy Nash Equilibrium

#### Requirements

- The code is written in [`python3`](https://docs.python.org/3.8/) and is [`python2`](https://docs.python.org/2/) compatible.
- [Gambit Library version 15](http://www.gambit-project.org/gambit15/index.html) with the [Python API](http://www.gambit-project.org/gambit15/pyapi.html) installed.  

#### Solution Approach

> By definition the strategy of a player, in nash equilibrum with other players, provides the maximum payoff. This means that if we find a strategy which gives a better payoff provided that the remaining players play the same strategies, the corresponding strategy profile(set of all player strategy) is not a nash equilibrium. This is the idea behind the algorithm provided. 

> - Initially assume all profiles are NE.
> - Scan through all strategy profiles. 
> - For each strategy profile, and a player strategy within it, if the payoff for a player is highier than some other strategy in a strategy profile, then that strategy profile is not a NE.

> Hence in NE the best response for a player given the strategies of the other players is the one with the highest payoff. 

> The goal is to, for every player store the best response strategy given all possible combinations of the other players. This is done by storing the best responses of a player given strategies of the other players in a profile, in a dictionary structure. If we find find a better strategy(as we are scanning through the profiles), we remove the previous best response and the corresponding previous strategy profile from the NE set. We do this for each player in each strategy profile. The remaining strategy profiles after all the strategies have been analyzed constitute a PSNE.

#### Complexity


> The number of possible strategy profiles are A1 x A2 x A3 ... An in PSNE with A1, A2 as the set of actions for player 1, player 2 and so one, and for each strategy profile we are updating the best response dictionary for all the players. Hence the complexity is on the order of O(N x A1 x A2 x ... x An).
