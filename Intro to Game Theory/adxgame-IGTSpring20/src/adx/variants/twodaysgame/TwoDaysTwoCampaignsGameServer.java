package adx.variants.twodaysgame;

import java.io.IOException;
import java.time.Instant;

import adx.exceptions.AdXException;
import adx.server.GameServer;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Parameters;
import adx.util.Printer;
import adx.util.Sampling;

/**
 * Implementation of the TwoCampaign-TwoDay game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class TwoDaysTwoCampaignsGameServer extends GameServer {

  /**
   * Constructor.
   * 
   * @param port
   * @throws IOException
   * @throws AdXException
   */
  public TwoDaysTwoCampaignsGameServer(int port) throws IOException, AdXException {
    super(port);
  }

  @Override
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
        while (day < 2) {
          if (Instant.now().isAfter(endTime)) {
            // Time is up for the present day, stop accepting bids for this day and run corresponding auctions.
            // this.serverState.printServerState();
            this.serverState.advanceDay();
            // Run auction for the bids received the day before.
            synchronized (this.serverState) {
              synchronized (this) {
                try {
                  this.serverState.runAdAuctions();
                  this.serverState.updateDailyStatistics();
                  if (day == 0) {
                    // Once the first day is done, distribute campaigns for the second day.
                    this.distributeSecondCampaigns();
                  }
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
      Logging.log(Printer.getNiceProfitTable(
          this.serverState.getStatistics().orderProfits(this.serverState.getAverageAcumProfitOverAllGames(this.gameNumber - 1).entrySet()), -1));
      this.gameServer.stop();
    } else {
      Logging.log("[x] There are no players, stopping the server at " + Instant.now());
      this.gameServer.stop();
    }
  }

  /**
   * This is the main difference of this game. On the second day, a new campaign is given to each agent based on their performance on the first day.
   * 
   * @throws AdXException
   */
  protected void distributeSecondCampaigns() throws AdXException {
    Logging.log("[-] Distribute second day campaings.");
    for (String agent : this.connectionsToNames.values()) {
      Logging.log("\t\t Sending a new campaign to " + agent + " with QS = " + this.serverState.getQualitScore(agent));
      Campaign c = Sampling.sampleCampaign(1);
      // To avoid potential problem with the campaign object, the budget is at least 0.1.
      c.setBudget(Math.max(0.1, c.getReach() * this.serverState.getQualitScore(agent)));
      Logging.log("\t\t\t" + c);
      this.serverState.registerCampaign(c, agent);
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
      new TwoDaysTwoCampaignsGameServer(9898).runAdXGame();
    } catch (IOException e) {
      Logging.log("Error initializing the server --> ");
      e.printStackTrace();
      System.exit(-1);
    }
  }

}
