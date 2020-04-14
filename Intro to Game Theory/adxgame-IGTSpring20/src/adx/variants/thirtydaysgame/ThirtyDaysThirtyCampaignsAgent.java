package adx.variants.thirtydaysgame;

import adx.agent.Agent;
import adx.messages.EndOfDayMessage;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Printer;

/**
 * An abstract class to be implemented by an agent playing the TwoCampaings-TwoDays game.
 * 
 * @author Enrique Areyan Viqueira
 */
abstract public class ThirtyDaysThirtyCampaignsAgent extends Agent {

  /**
   * Campaign of day 1
   */
  protected Campaign[] setCampaigns = new Campaign[30];

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public ThirtyDaysThirtyCampaignsAgent(String host, int port) {
    super(host, port);
  }

  /**
   * Connects the agent and registers its name. For simplicity, the password is fixed here.
   * 
   * @param name
   * @param password
   */
  protected void connect(String login) {
    super.connect(login, "123456");
  }

  @Override
  protected void handleEndOfDayMessage(EndOfDayMessage endOfDayMessage) {
    int currentDay = endOfDayMessage.getDay();
    if (currentDay == 1) {
      Logging.log("\n[-] Playing a new game!");
      // Start of the game, day 1
      this.setCampaigns[currentDay - 1] = endOfDayMessage.getCampaignsWon().get(0);
      Logging.log("[-] My first campaign: " + this.setCampaigns[currentDay - 1]);
      Logging.log("[-] End of Day 1, bid.");
      ThirtyDaysBidBundle bidBundleDay = this.getBidBundle(currentDay);
      if (bidBundleDay != null) {
        this.getClient().sendTCP(bidBundleDay);
      } else {
        Logging.log("[-] WARNING! the bid bundle for day " + currentDay + " was null!, nothing was sent to the server");
      }
    } else if (currentDay <= 30) {
      // Day 2.
      this.setCampaigns[currentDay - 1] = endOfDayMessage.getCampaignsWon().get(0);
      Logging.log("[-] My latest campaign: " + this.setCampaigns[currentDay - 1]);
      Logging.log("[-] Statistics from day " + (currentDay - 1) + ": " + Printer.getNiceStatsTable(endOfDayMessage.getStatistics()));
      Logging.log("[-] Quality from day " + (currentDay - 1) + ": " + endOfDayMessage.getQualityScore());
      Logging.log("[-] Profit from day " + (currentDay - 1) + ": " + endOfDayMessage.getCumulativeProfit());
      Logging.log("[-] End of Day " + (currentDay) + " bid.");
      ThirtyDaysBidBundle bidBundleDay = this.getBidBundle(currentDay);
      if (bidBundleDay != null) {
        this.getClient().sendTCP(bidBundleDay);
      } else {
        Logging.log("[-] WARNING! the bid bundle for day " + currentDay + " was null!, nothing was sent to the server");
      }
    } else {
      // End of Game.
      Logging.log("[-] Statistics from day 30: " + Printer.getNiceStatsTable(endOfDayMessage.getStatistics()));
      Logging.log("[-] Final Profit: " + endOfDayMessage.getCumulativeProfit());
      Logging.log("[-] Final Quality Score: " + endOfDayMessage.getQualityScore());
    }
  }

  /**
   * This is the only function that needs to be implemented by an agent playing the TwoDays Game.
   * 
   * @return the agent's bid bundle.
   */
  abstract protected ThirtyDaysBidBundle getBidBundle(int day);

}
