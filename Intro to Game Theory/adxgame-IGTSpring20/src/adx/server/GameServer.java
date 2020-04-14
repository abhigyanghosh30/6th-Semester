package adx.server;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map.Entry;

import adx.exceptions.AdXException;
import adx.messages.EndOfDayMessage;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Parameters;
import adx.util.Printer;
import adx.util.Sampling;

import com.esotericsoftware.kryonet.Connection;

/**
 * A concrete implementation of a game server.
 * 
 * @author Enrique Areyan Viqueira
 */
public class GameServer extends GameServerAbstract {

  /**
   * Constructor.
   * 
   * @param port - on which the server will run.
   * @throws IOException in case the server could not be started.
   * @throws AdXException
   */
  public GameServer(int port) throws IOException, AdXException {
    super(port);
  }

  /**
   * Runs the game.
   * 
   * @throws AdXException
   */
  protected void runAdXGame() throws AdXException {
    // First order of business is to accept connections for a fixed amount of time
    Instant deadlineForNewPlayers = Instant.now().plusSeconds(10);
    Logging.log("[-] Accepting connections until " + deadlineForNewPlayers);
    while (Instant.now().isBefore(deadlineForNewPlayers));
    // Do not accept any new agents beyond deadline. Play with present agents.
    this.acceptingNewPlayers = false;
    this.serverState.initStatistics();
    // Check if there is at least one agent to play the game.
    if (this.namesToConnections.size() > 0) {
      while (this.gameNumber < Parameters.TOTAL_SIMULATED_GAMES + 1) {
        Instant endTime = Instant.now().plusSeconds(Parameters.SECONDS_DURATION_DAY);
        this.setUpGame();
        this.sendEndOfDayMessage();
        int day = 0;
        // Play game for the specified number of days.
        while (day < Parameters.TOTAL_SIMULATED_DAYS) {
          if (Instant.now().isAfter(endTime)) {
            // Time is up for the present day, stop accepting bids for this day and run corresponding auctions.
            // this.serverState.printServerState();
            this.serverState.advanceDay();
            // Run auction for the bids received the day before.
            synchronized (this.serverState) {
              synchronized (this) {
                try {
                  this.serverState.runAdAuctions();
                  this.serverState.runCampaignAuctions();
                  this.serverState.updateDailyStatistics();
                } catch (AdXException e) {
                  Logging.log("[x] Error running some auction -> " + e.getMessage());
                }
              }
            }
            endTime = Instant.now().plusSeconds(Parameters.SECONDS_DURATION_DAY);
            day++;
            this.sendEndOfDayMessage();
          }
        }
        // Print results of the game.
        Logging.log("Results for game " + this.gameNumber);
        Logging.log(this.serverState.getStatistics().getNiceProfitScoresTable(day));

        // Prepare to start a new game
        this.gameNumber++;
        this.serverState.saveProfit();
        this.serverState.initServerState(this.gameNumber);
        this.serverState.initStatistics();
        Sampling.campaignId = 0;
      }
      Logging.log("\nGame ended, played " + (this.gameNumber - 1) + " games, final results are: ");
      Logging.log(Printer.getNiceProfitTable(this.serverState.getStatistics().orderProfits(this.serverState.getAverageAcumProfitOverAllGames(this.gameNumber - 1).entrySet()), -1));
      this.gameServer.stop();
    } else {
      Logging.log("[x] There are no players, stopping the server at " + Instant.now());
      this.gameServer.stop();
    }
  }

  /**
   * Sample initial campaigns.
   */
  protected synchronized void setUpGame() {
    Logging.log("[-] Set Up game, sample initial campaigns:");
    for (String agent : this.connectionsToNames.values()) {
      try {
        Campaign c = Sampling.sampleInitialCampaign();
        this.serverState.registerCampaign(c, agent);
      } catch (AdXException e) {
        Logging.log("[x] Error trying to sample an initial campaign.");
        e.printStackTrace();
      }
    }
  }

  /**
   * Send the end of day message to all agents.
   */
  protected synchronized void sendEndOfDayMessage() {
    Logging.log("[-] Sending end of day message. ");
    Instant timeEndOfDay = Instant.now().plusSeconds(Parameters.SECONDS_DURATION_DAY);
    this.serverState.currentDayEnd = timeEndOfDay;
    List<Campaign> listOfCampaigns = null;
    try {
      listOfCampaigns = this.serverState.generateCampaignsOpportunities();
    } catch (AdXException e) {
      Logging.log("[x] Error sampling list of campaigns for auction --> " + e.getMessage());
    }
    for (Entry<String, Connection> agent : this.namesToConnections.entrySet()) {
      String agentName = agent.getKey();
      try {
        agent.getValue()
            .sendTCP(
                new EndOfDayMessage(this.serverState.getCurrentDay() + 1, timeEndOfDay.toString(), this.serverState.getDailySummaryStatistic(agentName),
                    listOfCampaigns, this.serverState.getWonCampaigns(agentName), this.serverState.getQualitScore(agentName), this.serverState
                        .getProfit(agentName)));
      } catch (AdXException e) {
        Logging.log("[x] Error sending the end of day message -> " + e);
      }
    }
  }

  /**
   * Main server method.
   * 
   * @param args
   * @throws AdXException
   */
  public static void main(String[] args) throws AdXException {
    try {
      // Try to initialize the server.
      new GameServer(9898).runAdXGame();
    } catch (IOException e) {
      Logging.log("Error initializing the server --> ");
      e.printStackTrace();
      System.exit(-1);
    }
  }

}
