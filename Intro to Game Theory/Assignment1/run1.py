#!/bin/python3
import gambit
import sys
import numpy as np
if len(sys.argv)<2:
    print("Input format should be ./run input_file output_file")
input_file=sys.argv[1]
output_file=sys.argv[2]
g = gambit.Game.read_game(input_file)
best_strategies = list(gambit.nash.enumpure_solve(g))
print(len(best_strategies))
for strategy in best_strategies:
    print(strategy)


# nos = [len(player.strategies) for player in g.players]

# for number_of_strategies in best_strategies:
#     strategies = np.split(np.array(best_strategies[0]),nos)
#     for strategy in strategies:
#         print(strategy.tolist())

