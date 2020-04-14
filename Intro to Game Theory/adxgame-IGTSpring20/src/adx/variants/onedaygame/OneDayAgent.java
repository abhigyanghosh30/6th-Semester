package adx.variants.onedaygame;

import adx.agent.Agent;
import adx.messages.EndOfDayMessage;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Printer;

/**
 * An abstract class to be implemented by an agent playing the OneDay game.
 * 
 * @author Enrique Areyan Viqueira
 */
abstract public class OneDayAgent extends Agent {

  /**
   * In this game agents have only one campaign
   */
  protected Campaign myCampaign;

  /**
   * Constructor.
   * 
   * @param host
   * @param port
   */
  public OneDayAgent(String host, int port) {
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
      this.myCampaign = endOfDayMessage.getCampaignsWon().get(0);
      Logging.log("\n[-] Playing a new game!");
      Logging.log("[-] My campaign: " + this.myCampaign);
      this.getClient().sendTCP(this.getBidBundle());
    } else {
      Logging.log("[-] Statistics: " + Printer.getNiceStatsTable(endOfDayMessage.getStatistics()));
      Logging.log("[-] Final Profit: " + endOfDayMessage.getCumulativeProfit());
    }
  }

  /**
   * This is the only function that needs to be implemented by an agent playing the OneDay Game.
   * 
   * @return the agent's bid bundle.
   */
  abstract protected OneDayBidBundle getBidBundle();

}
