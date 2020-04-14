package adx.variants.twodaysgame;

import java.util.HashSet;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.SimpleBidEntry;
import adx.util.Logging;

/**
 * An example of a simple agent playing the TwoDays game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class SimpleTwoDaysOneCampaignAgent extends TwoDaysOneCampaignAgent {

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public SimpleTwoDaysOneCampaignAgent(String host, int port) {
    super(host, port);
  }

  @Override
  protected TwoDaysBidBundle getBidBundle(int day) {
    Logging.log("\t[-] GetBidBundle of day = " + day);
    try {
      // Compute the limit based on the day.
      double limit = 0.0;
      if (day == 1) {
        limit = this.myCampaign.getBudget();
      } else if (day == 2) {
        limit = Math.max(0, this.myCampaign.getBudget() - this.costDay1);
        Logging.log("[-] Got " + this.reachDay1 + " impressions at " + this.costDay1 + " on day 1, setting limit for day 2 at " + limit);
      }
      if (limit > 0.0) {
        // Bidding only on the exact market segment of the campaign.
        Set<SimpleBidEntry> bidEntries = new HashSet<SimpleBidEntry>();
        bidEntries.add(new SimpleBidEntry(this.myCampaign.getMarketSegment(), this.myCampaign.getBudget() / (double) this.myCampaign.getReach(), limit));
        //bidEntries.add(new SimpleBidEntry(this.myCampaign.getMarketSegment(), this.myCampaign.getBudget() / (double) this.myCampaign.getReach(), 1.0));
        Logging.log("[-] bidEntries = " + bidEntries);
        // The bid bundle indicates the campaign id, the limit across all auctions, and the bid entries.
        return new TwoDaysBidBundle(day, this.myCampaign.getId(), limit, bidEntries);
      } else {
        Logging.log("[-] !!!!!!!!!! The budget was already spent on day 1!");
      }
    } catch (AdXException e) {
      Logging.log("[x] Something went wrong getting the bid bundle: " + e.getMessage());
    }
    return null;
  }

  /**
   * Agent's main method.
   * 
   * @param args
   */
  public static void main(String[] args) {
    SimpleTwoDaysOneCampaignAgent agent = new SimpleTwoDaysOneCampaignAgent("localhost", 9898);
    agent.connect("agent1");
  }
}
