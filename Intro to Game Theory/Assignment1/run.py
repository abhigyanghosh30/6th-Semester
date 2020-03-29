import gambit
import sys
import time

best_responses = {}
nash_eq = set()

def split_profile(profile):
	strategy_profile = []	
	for i in range(len(profile)):
		opponents_strategy_profile = profile[:i] + profile[i+1:]
		strategy_profile.append([profile[i], tuple(opponents_strategy_profile)])
	return strategy_profile

def remove_profiles(player, current_strategy_set, opponents_strategy_profile):
	for strategy in current_strategy_set:
		profile = opponents_strategy_profile[:player] + (strategy, ) + opponents_strategy_profile[player:]
		nash_eq.discard(profile)

def update_best_response(player, strategy, payoff, opponents_strategy_profile):
	current_payoff, current_strategy_set = best_responses.setdefault(player, {}).setdefault(opponents_strategy_profile, (-10000000000, set()))
	if current_payoff < payoff:
		remove_profiles(player, current_strategy_set, opponents_strategy_profile)
		best_responses[player][opponents_strategy_profile] = (payoff, {strategy})
	elif current_payoff == payoff:
		current_strategy_set.add(strategy)
	else:
		return False
	return True

def generate_best_responses(game):
	
	for profile in game.contingencies:
		nash_eq.add(tuple(profile))
		player_strategy_profile = split_profile(profile)
		for player, strategy_profile in enumerate(player_strategy_profile):
			player_strategy, opponents_strategy = strategy_profile
			if not update_best_response(player, player_strategy, int(game[profile][player]), opponents_strategy):
				nash_eq.discard(tuple(profile))

if __name__ == "__main__":
	game = gambit.Game.read_game(sys.argv[1])
	generate_best_responses(game)
	nash_eqb = list(sorted(nash_eq))
	f = open(sys.argv[2],'w')
	f.write(str(len(nash_eqb)))
	f.write('\n')
	neq = nash_eqb
	for eqb in neq:
		eqb = [str(i) for i in eqb]
		f.write(" ".join(eqb))
		f.write('\n')
	f.close()


