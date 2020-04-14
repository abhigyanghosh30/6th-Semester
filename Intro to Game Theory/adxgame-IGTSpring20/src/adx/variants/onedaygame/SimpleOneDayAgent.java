package adx.variants.onedaygame;

import java.util.HashSet;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.SimpleBidEntry;
import adx.util.Logging;

/**
 * An example of a simple agent playing the OneDay game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class SimpleOneDayAgent extends OneDayAgent {

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public SimpleOneDayAgent(String host, int port) {
    super(host, port);
  }

  @Override
  protected OneDayBidBundle getBidBundle() {
    try {
      // Bidding only on the exact market segment of the campaign.
      Set<SimpleBidEntry> bidEntries = new HashSet<SimpleBidEntry>();
      bidEntries.add(new SimpleBidEntry(this.myCampaign.getMarketSegment(), this.myCampaign.getBudget() / (double) this.myCampaign.getReach(), this.myCampaign.getBudget()));
      Logging.log("[-] bidEntries = " + bidEntries);
      // The bid bundle indicates the campaign id, the limit across all auctions, and the bid entries.
      return new OneDayBidBundle(this.myCampaign.getId(), this.myCampaign.getBudget(), bidEntries);
    } catch (AdXException e) {
      Logging.log("[x] Something went wrong getting the bid bundle: " + e.getMessage());
      return null;
    }
  }

  /**
   * Agent's main method.
   * 
   * @param args
   */
  public static void main(String[] args) {
    SimpleOneDayAgent agent = new SimpleOneDayAgent("localhost", 9898);
    agent.connect("agent0");
  }
}
