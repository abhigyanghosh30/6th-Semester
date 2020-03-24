import gambit
import sys
import copy

best_responses = {}

def split_profile(profile):
	strategy_profile = []	
	for i in range(len(profile)):
		opponents_strategy_profile = profile[:i] + profile[i+1:]
		strategy_profile.append([profile[i], tuple(opponents_strategy_profile)])
	return strategy_profile

def update_best_response(player, strategy, payoff, opponents_strategy_profile):
	current_payoff, current_strategy_set = best_responses.setdefault(player, {}).setdefault(opponents_strategy_profile, (-10000000000, set()))
	if current_payoff < payoff:
		best_responses[player][opponents_strategy_profile] = (payoff, {strategy})
	elif current_payoff == payoff:
		current_strategy_set.add(strategy)

def generate_best_responses(game):
	
	for profile in game.contingencies:
		player_strategy_profile = split_profile(profile)
		for player, strategy_profile in enumerate(player_strategy_profile):
			player_strategy, opponents_strategy = strategy_profile
			update_best_response(player, player_strategy, int(game[profile][player]), opponents_strategy)

def find_nash_eqb(game):
	
	nash_eqb = []
	for profile in game.contingencies:
		player_strategy_profile = split_profile(profile)
		is_nash_eq = True
		for player, strategy_profile in enumerate(player_strategy_profile):
			player_strategy, opponents_strategy = strategy_profile
			if player_strategy not in best_responses[player][opponents_strategy][1]:
				is_nash_eq = False
				break

		if is_nash_eq:
			nash_eqb.append(profile)
	return nash_eqb


if __name__ == "__main__":
	game = gambit.Game.read_game(sys.argv[1])
	generate_best_responses(game)
	nash_eqb = find_nash_eqb(game)
	print(len(nash_eqb))
	print(nash_eqb)


