package adx.variants.onedaygame;

import java.util.Set;

import adx.exceptions.AdXException;
import adx.structures.BidBundle;
import adx.structures.SimpleBidEntry;
import adx.util.BidBundleHelper;

/**
 * A specialized object for the OneDay game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class OneDayBidBundle extends BidBundle {

  /**
   * A bid bundle is composed of bid entries.
   */
  protected Set<SimpleBidEntry> bidEntries;

  /**
   * Constructor.
   */
  public OneDayBidBundle() {
    super();
  }

  /**
   * Constructor for a bidbundle to play only one day games.
   * 
   * @param bidEntries
   * @param campaignsLimits
   * @throws AdXException
   */
  public OneDayBidBundle(int campaignId, double limit, Set<SimpleBidEntry> simpleBidEntries) throws AdXException {
    super(1, BidBundleHelper.createBidEntries(campaignId, simpleBidEntries), BidBundleHelper.createLimits(campaignId, limit), null);
    this.bidEntries = simpleBidEntries;
  }

  @Override
  public String toString() {
    String ret = "\n\t OneDayBidBundle:";
    if (this.bidEntries != null && this.bidEntries.size() > 0) {
      for (SimpleBidEntry simpleBidEntry : this.bidEntries) {
        if (simpleBidEntry != null) {
          ret += "\n\t\t" + simpleBidEntry;
        }
      }
    } else {
      ret += " [EMPTY] ";
    }
    return ret;
  }
}
