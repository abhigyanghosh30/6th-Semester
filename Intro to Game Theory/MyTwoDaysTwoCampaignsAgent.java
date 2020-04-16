package lab08;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.google.common.collect.ImmutableMap;

import adx.agent.AgentLogic;
import adx.exceptions.AdXException;
import adx.server.OfflineGameServer;
import adx.structures.SimpleBidEntry;
import adx.util.AgentStartupUtil;
import adx.structures.Campaign;
import adx.structures.MarketSegment;
import adx.variants.twodaysgame.SimpleTwoDaysTwoCampaingsAgent;
import adx.variants.twodaysgame.TwoDaysBidBundle;
import adx.variants.twodaysgame.TwoDaysTwoCampaignsAgent;
import adx.variants.twodaysgame.TwoDaysTwoCampaignsGameServerOffline;

public class MyTwoDaysTwoCampaignsAgent extends TwoDaysTwoCampaignsAgent {
	private static final String NAME = ""; // TODO: enter a name. please remember to submit the Google form.

	public MyTwoDaysTwoCampaignsAgent() {
		// TODO: fill this in (if necessary)
	}

	@Override
	protected TwoDaysBidBundle getBidBundle(int day) throws AdXException {
		// TODO fill this in
		if (day == 1) {
			// ...
		} else { // day = 2
			// ...
		}
		return null;
	}

	private static double qualityScore(int impressions, int reach) {
		// Here's the quality score function if you want to use it.
		return (2.0 / 4.08577) * (Math.atan(4.08577 * ((impressions + 0.0) / reach) - 3.08577) - Math.atan(-3.08577));
	}

	public static void main(String[] args) throws IOException, AdXException {
		// Here's an opportunity to test offline against some mystery agents. Just run
		// this file in Eclipse to do so.
		// Feel free to change the type of agents. SimpleTwoDaysTwoCampaingsAgent is a
		// simple pre-defined bot.
		// Note: this runs offline, so:
		// a) It's much faster than the online test; don't worry if there's no delays.
		// b) You should still run the test script mentioned in the handout to make sure
		// your agent works online.
		if (args.length == 0) {
			Map<String, AgentLogic> test_agents = new ImmutableMap.Builder<String, AgentLogic>()
					.put("me", new MyTwoDaysTwoCampaignsAgent())
					.put("opponent_1", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_2", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_3", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_4", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_5", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_6", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_7", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_8", new SimpleTwoDaysTwoCampaingsAgent())
					.put("opponent_9", new SimpleTwoDaysTwoCampaingsAgent()).build();

			// Don't change this.
			OfflineGameServer.initParams(new String[] { "offline_config.ini", "TWO-DAYS-TWO-CAMPAIGNS" });
			AgentStartupUtil.testOffline(test_agents, new TwoDaysTwoCampaignsGameServerOffline());
		} else {
			// Don't change this.
			AgentStartupUtil.startOnline(new MyTwoDaysTwoCampaignsAgent(), args, NAME);
		}
	}

}
