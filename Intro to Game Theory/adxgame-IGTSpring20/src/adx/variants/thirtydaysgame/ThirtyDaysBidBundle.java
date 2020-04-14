package adx.variants.thirtydaysgame;

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
public class ThirtyDaysBidBundle extends BidBundle {

  /**
   * A bid bundle is composed of bid entries.
   */
  protected Set<SimpleBidEntry> bidEntries;

  /**
   * Constructor.
   */
  public ThirtyDaysBidBundle() {
    super();
  }

  /**
   * Constructor for a bidbundle to play thirty day games.
   * 
   * @param bidEntries
   * @param campaignsLimits
   * @throws AdXException
   */
  public ThirtyDaysBidBundle(int day, int campaignId, double limit, Set<SimpleBidEntry> simpleBidEntries) throws AdXException {
    super(day, BidBundleHelper.createBidEntries(campaignId, simpleBidEntries), BidBundleHelper.createLimits(campaignId, limit), null);
    if (!(day >= 1 && day <= 30)) {
      throw new AdXException("The day of a ThirtyDays BidBundle must be [1,30], received: " + day);
    }
  }
}
