package adx.variants.twodaysgame;

import adx.agent.Agent;
import adx.messages.EndOfDayMessage;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Printer;

/**
 * An abstract class to be implemented by an agent playing the TwoDays game.
 * 
 * @author Enrique Areyan Viqueira
 */
abstract public class TwoDaysOneCampaignAgent extends Agent {

  /**
   * In this game agents have only one campaign.
   */
  protected Campaign myCampaign;

  /**
   * Easy access to the statistics from day 1, reach and cost.
   */
  protected int reachDay1;
  protected double costDay1;

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public TwoDaysOneCampaignAgent(String host, int port) {
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
      this.myCampaign = endOfDayMessage.getCampaignsWon().get(0);
      Logging.log("[-] My campaign: " + this.myCampaign);
      Logging.log("[-] End of Day 1, bid.");
      TwoDaysBidBundle bidBundleDay1 = this.getBidBundle(1);
      if (bidBundleDay1 != null) {
        this.getClient().sendTCP(bidBundleDay1);
      } else {
        Logging.log("[-] WARNING! the bid bundle for day 1 was null!, nothing was sent to the server");
      }
    } else if (currentDay == 2) {
      // Day 2.
      Pair<Integer, Double> statsDay1 = endOfDayMessage.getStatistics().get(this.myCampaign.getId());
      this.reachDay1 = statsDay1.getElement1();
      this.costDay1 = statsDay1.getElement2();
      Logging.log("[-] Statistics from day 1: " + Printer.getNiceStatsTable(1, endOfDayMessage.getStatistics()));
      Logging.log("[-] Profit from day 1: " + endOfDayMessage.getCumulativeProfit());
      Logging.log("[-] End of Day 2, bid.");
      TwoDaysBidBundle bidBundleDay2 = this.getBidBundle(2);
      if (bidBundleDay2 != null) {
        this.getClient().sendTCP(bidBundleDay2);
      } else {
        Logging.log("[-] WARNING! the bid bundle for day 2 was null!, nothing was sent to the server");
      }
    } else {
      // End of Game.
      Logging.log("[-] Statistics from day 2: " + Printer.getNiceStatsTable(2, endOfDayMessage.getStatistics()));
      Logging.log("[-] Final Profit: " + endOfDayMessage.getCumulativeProfit());
      Logging.log("[-] Final Quality Score: " + endOfDayMessage.getQualityScore());
    }
  }

  /**
   * This is the only function that needs to be implemented by an agent playing the TwoDays Game.
   * 
   * @return the agent's bid bundle.
   */
  abstract protected TwoDaysBidBundle getBidBundle(int day);

}
