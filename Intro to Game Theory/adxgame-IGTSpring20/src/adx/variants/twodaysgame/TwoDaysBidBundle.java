package adx.variants.twodaysgame;

import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.SimpleBidEntry;
import adx.util.BidBundleHelper;

/**
 * A specialized object for the TwoDay game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class TwoDaysBidBundle extends BidBundle {

  /**
   * A bid bundle is composed of bid entries.
   */
  protected Set<SimpleBidEntry> bidEntries;

  /**
   * Constructor.
   */
  public TwoDaysBidBundle() {
    super();
  }

  /**
   * Constructor for a bidbundle to play only one day games.
   * 
   * @param bidEntries
   * @param campaignsLimits
   * @throws AdXException
   */
  public TwoDaysBidBundle(int day, int campaignId, double limit, Set<SimpleBidEntry> simpleBidEntries) throws AdXException {
    super(day, BidBundleHelper.createBidEntries(campaignId, simpleBidEntries), BidBundleHelper.createLimits(campaignId, limit), null);
    if (day != 1 && day != 2) {
      throw new AdXException("The day of a TwoDays BidBundle must be either 1 or 2, received: " + day);
    }
  }
}
