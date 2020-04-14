package adx.variants.thirtydaysgame;

import java.io.IOException;
import java.time.Instant;
import java.util.Hashtable;

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
public class ThirtyDaysThirtyCampaignsGameServer extends GameServer {

  private Hashtable<String, Double> AvgQualityScores;

  /**
   * Constructor.
   * 
   * @param port
   * @throws IOException
   * @throws AdXException
   */
  public ThirtyDaysThirtyCampaignsGameServer(int port) throws IOException, AdXException {
    super(port);
  }

  @Override
  protected void runAdXGame() throws AdXException {
    // First order of business is to accept connections for a fixed amount of time
    Instant deadlineForNewPlayers = Instant.now().plusSeconds(Parameters.WAIT_TIME);
    Logging.log("[-] Accepting connections until " + deadlineForNewPlayers);
    while (Instant.now().isBefore(deadlineForNewPlayers));
    // Do not accept any new agents beyond deadline. Play with present agents.
    this.acceptingNewPlayers = false;
    this.serverState.initStatistics();
    
    //Initialize quality scores
    AvgQualityScores = new Hashtable<String, Double>();
    for (String agent : this.connectionsToNames.values()) {
    	AvgQualityScores.put(agent, 0.0);
    }
    
    // Check if there is at least one agent to play the game.
    if (this.namesToConnections.size() > 0) {
      while (this.gameNumber < Parameters.TOTAL_SIMULATED_GAMES + 1) {
        Instant endTime = Instant.now().plusSeconds(Parameters.SECONDS_DURATION_DAY);
        this.setUpGame();
        this.sendEndOfDayMessage();
        int day = 0;
        
        Hashtable<String, Double> TotalQualityScores = new Hashtable<String, Double>();
        for (String agent : this.connectionsToNames.values()) {
          	TotalQualityScores.put(agent, 0.0);
        }
        
        // Play game for the specified number of days.
        while (day < 30) {
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
                  if (day < 29) {
                    // Once the day is done, distribute campaigns for the next day.
                    this.distributeNextCampaigns(day);
                  }
                  
                  // sum all quality scores
                  for (String agent : this.connectionsToNames.values()) {
                  	TotalQualityScores.put(agent, TotalQualityScores.get(agent) + this.serverState.getQualitScore(agent));
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
        Logging.log(TotalQualityScores.toString());

        // store average q_scores
        for (String agent : this.connectionsToNames.values()) {
        	Double AvgQScore = TotalQualityScores.get(agent) / 30.0;
          	AvgQualityScores.put(agent, AvgQualityScores.get(agent) + AvgQScore);
        }
        
        // Prepare to start a new game
        this.gameNumber++;
        this.serverState.saveProfit();
        this.serverState.initServerState(this.gameNumber);
        this.serverState.initStatistics();
        Sampling.campaignId = 0;
      }
      
      // store average total q_scores
      for (String agent : this.connectionsToNames.values()) {
        	AvgQualityScores.put(agent, AvgQualityScores.get(agent) / Parameters.TOTAL_SIMULATED_GAMES);
      }
      Logging.log("\nGame ended, played " + (this.gameNumber - 1) + " games, final results are: ");
      Logging.log(Printer.getNiceProfitTable(
          this.serverState.getStatistics().orderProfits(this.serverState.getAverageAcumProfitOverAllGames(this.gameNumber - 1).entrySet()), -1));
      Logging.log(AvgQualityScores.toString());
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
  protected void distributeNextCampaigns(int day) throws AdXException {
    Logging.log("[-] Distribute next day campaings.");
    for (String agent : this.connectionsToNames.values()) {
      Logging.log("\t\t Sending a new campaign to " + agent + " with QS = " + this.serverState.getQualitScore(agent));
      Campaign c = Sampling.sampleCampaign(day + 1);
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
      new ThirtyDaysThirtyCampaignsGameServer(9898).runAdXGame();
    } catch (IOException e) {
      Logging.log("Error initializing the server --> ");
      e.printStackTrace();
      System.exit(-1);
    }
  }

}
