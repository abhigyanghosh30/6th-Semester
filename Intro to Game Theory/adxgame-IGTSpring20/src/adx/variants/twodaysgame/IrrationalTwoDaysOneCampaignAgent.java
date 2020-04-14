package adx.variants.twodaysgame;

import java.util.HashSet;
import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.MarketSegment;
import adx.structures.SimpleBidEntry;

/**
 * An agent that bids 1.0 on every market segment regardless of its campaign. This is used for debugging purposes.
 * 
 * @author Enrique Areyan Viqueira
 */
public class IrrationalTwoDaysOneCampaignAgent extends TwoDaysOneCampaignAgent {

  public IrrationalTwoDaysOneCampaignAgent(String host, int port) {
    super(host, port);
  }

  @Override
  protected TwoDaysBidBundle getBidBundle(int day) {
    try {
      double irrationalBid = 0.5;
      Set<SimpleBidEntry> simpleBidEntries = new HashSet<SimpleBidEntry>();
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.FEMALE_OLD_HIGH_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.FEMALE_OLD_LOW_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.FEMALE_YOUNG_HIGH_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.FEMALE_YOUNG_LOW_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.MALE_OLD_HIGH_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.MALE_OLD_LOW_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.MALE_YOUNG_HIGH_INCOME, irrationalBid, Double.MAX_VALUE));
      simpleBidEntries.add(new SimpleBidEntry(MarketSegment.MALE_YOUNG_LOW_INCOME, irrationalBid, Double.MAX_VALUE));
      return new TwoDaysBidBundle(day, this.myCampaign.getId(), Double.MAX_VALUE, simpleBidEntries);
    } catch (AdXException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Agent's main method.
   * 
   * @param args
   */
  public static void main(String[] args) {
    IrrationalTwoDaysOneCampaignAgent agent = new IrrationalTwoDaysOneCampaignAgent("localhost", 9898);
    agent.connect("irrational");
  }

}
