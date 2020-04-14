package adx.server;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import adx.auctions.AdAuctions;
import adx.auctions.CampaignAuctions;
import adx.exceptions.AdXException;
import adx.statistics.Statistics;
import adx.structures.BidBundle;
import adx.structures.BidEntry;
import adx.structures.Campaign;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Parameters;
import adx.util.Sampling;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * This class maintains the state of the server for a single game.
 * 
 * @author Enrique Areyan Viqueira
 */
public class ServerState {

  /**
   * A unique identifier of the game.
   */
  private int gameId;

  /**
   * Starting time of the game.
   */
  private long gameStartTime;

  /**
   * Current simulated day.
   */
  private int currentDay;

  /**
   * Instant object when the current simulated day ends.
   */
  protected Instant currentDayEnd;

  /**
   * A set of agents names
   */
  private final Set<String> agentsNames;

  /**
   * A map from days to a tuple (Agent, BidBundle)
   */
  private Table<Integer, String, BidBundle> bidBundles;

  /**
   * An object that keeps track of the AdAuctions statistics.
   */
  private Statistics statistics;

  /**
   * An object that stores the campaigns for auction each day.
   */
  private Map<Integer, List<Campaign>> campaignsForAuction;

  /**
   * Profit over all games played.
   */
  private final Map<String, Double> acumProfitOverAllGames;

  /**
   * Constructor.
   * 
   * @param gameId
   * @throws AdXException
   */
  public ServerState(int gameId) throws AdXException {
    initServerState(gameId);
    this.agentsNames = new HashSet<String>();
    this.acumProfitOverAllGames = new HashMap<String, Double>();
  }

  /**
   * Initialize the server for a new game.
   * 
   * @param gameId
   * @throws AdXException
   */
  public void initServerState(int gameId) throws AdXException {
    this.gameId = gameId;
    this.gameStartTime = System.nanoTime();
    this.currentDay = 0;
    this.bidBundles = HashBasedTable.create();
    this.campaignsForAuction = new HashMap<Integer, List<Campaign>>();
  }

  /**
   * Initialize the statistics object.
   * 
   * @throws AdXException
   *           in case the statistics object is initialize more than once.
   */
  public void initStatistics() throws AdXException {
    if (this.agentsNames != null || this.agentsNames.size() == 0) {
      this.statistics = new Statistics(this.agentsNames);
    } else {
      throw new AdXException("Trying to create the statistics object with a null or empty set of agentsName");
    }
  }

  /**
   * Saves the profit of each game into one map.
   * 
   * @throws AdXException
   */
  public void saveProfit() throws AdXException {
    for (String agentName : this.agentsNames) {
      double profit = 0.0;
      if (this.acumProfitOverAllGames.get(agentName) != null) {
        profit = this.acumProfitOverAllGames.get(agentName);
      }
      this.acumProfitOverAllGames.put(agentName, profit + this.statistics.getProfit(this.currentDay, agentName));
    }
  }

  /**
   * Getter.
   * 
   * @return the statistics object.
   */
  public Statistics getStatistics() {
    return this.statistics;
  }

  /**
   * Getter.
   * 
   * @return the time, in nanoseconds, the game started.
   */
  public long getGameStartTime() {
    return this.gameStartTime;
  }

  /**
   * Getter.
   * 
   * @return the current simulated day.
   */
  public int getCurrentDay() {
    return this.currentDay;
  }

  /**
   * Gets the agent's current quality score.
   * 
   * @param agent
   * @return the agent's current quality score.
   * @throws AdXException
   */
  public Double getQualitScore(String agent) throws AdXException {
    return this.statistics.getQualityScore(this.currentDay, agent);
  }

  /**
   * Gets the agent's current profit.
   * 
   * @param agent
   * @return
   * @throws AdXException
   */
  public Double getProfit(String agent) throws AdXException {
    return this.statistics.getProfit(this.currentDay, agent);
  }

  /**
   * Given an agent, returns a list of all campaigns won by the agent in the current day.
   * 
   * @param agent
   * @return the list of campaigns won by an agent.
   */
  public List<Campaign> getWonCampaigns(String agent) {
    return this.statistics.getStatisticsCampaign().getWonCampaigns(this.currentDay, agent);
  }

  /**
   * Returns the daily summary statistics.
   * 
   * @param agent
   * @return the daily summary statistics for a given agent.
   */
  public Map<Integer, Pair<Integer, Double>> getDailySummaryStatistic(String agent) {
    return this.statistics.getStatisticsAds().getDailySummaryStatistic(this.currentDay, agent);
  }

  /**
   * Getter.
   * 
   * @return the cumulative profit over numberOfGames many games played
   */
  public Map<String, Double> getAverageAcumProfitOverAllGames(int numberOfGames) {
    Map<String, Double> average = new HashMap<String, Double>();
    // Agent is the key, profit is the value.
    for (Entry<String, Double> agentProfit : this.acumProfitOverAllGames.entrySet()) {
      average.put(agentProfit.getKey(), agentProfit.getValue() / (double) numberOfGames);
    }
    return average;
  }

  /**
   * Advances the simulated day.
   */
  public void advanceDay() {
    this.currentDay++;
    //Logging.log("[*] Day " + this.currentDay + " ended at " + Instant.now());
  }

  /**
   * Registers an agent.
   * 
   * @param agentName
   */
  public void registerAgent(String agentName) {
    this.agentsNames.add(agentName);
  }

  /**
   * Registers a campaign.
   * 
   * @param campaign
   * @param agent
   * @throws AdXException
   */
  public void registerCampaign(Campaign campaign, String agentName) throws AdXException {
    this.statistics.getStatisticsCampaign().registerCampaign(this.currentDay, campaign, agentName);
  }

  /**
   * Updates quality scores.
   * 
   * @param day
   * @throws AdXException
   */
  public void updateDailyStatistics() throws AdXException {
    this.statistics.updateDailyStatistics(this.currentDay);
  }

  /**
   * Add a bid bundle.
   * 
   * @param day
   * @param agent
   * @param bidBundle
   */
  public Pair<Boolean, String> addBidBundle(int day, String agent, BidBundle bidBundle) {
    try {
      this.validateBidBundle(day, bidBundle, agent);
      //Logging.log("[-] Bid bundle on day " + day + " for agent " + agent + ", accepted.");
      this.bidBundles.put(day, agent, bidBundle);
    } catch (AdXException e) {
      return new Pair<Boolean, String>(false, e.getMessage());
    }
    return new Pair<Boolean, String>(true, "OK");
  }

  /**
   * Validates a bid bundle.
   * 
   * @param bidBundle
   * @param agent
   * @param campaignsOwnership
   * @throws AdXException
   */
  public void validateBidBundle(int day, BidBundle bidBundle, String agent) throws AdXException {
    if (bidBundle == null) {
      throw new AdXException("Received a null bidBundle");
    }
    if (day != this.currentDay + 1) {
      throw new AdXException("Received Bid bundle for day " + day + " for agent " + agent + ", but currently accepting for day " + (this.currentDay + 1)
          + ". Bid Bundle not accepted.");
    }
    for (BidEntry bidEntry : bidBundle.getBidEntries()) {
      int campaignId = bidEntry.getCampaignId();
      if (!this.statistics.getStatisticsCampaign().campaignExists(campaignId)) {
        throw new AdXException("The entry " + bidEntry + " refers to a non-existing campaign.");
      } else if (!this.statistics.getStatisticsCampaign().isOwner(campaignId, agent)) {
        throw new AdXException("The entry " + bidEntry + " refers to a campaign not owned by the agent.");
      } else if (bidEntry.getQuery() == null) {
        throw new AdXException("The entry " + bidEntry + " refers to a null query.");
      } else if (bidEntry.getLimit() < bidEntry.getBid()) {
        throw new AdXException("The entry " + bidEntry + " limit is less than the bid.");
      } else {
        Campaign c = this.statistics.getStatisticsCampaign().getCampaign(campaignId);
        if (c.getStartDay() > day) {
          throw new AdXException("The entry" + bidEntry + " refers to campaign that hasn't started yet: "
              + this.statistics.getStatisticsCampaign().getCampaign(campaignId));
        } else if (c.getEndDay() < day) {
          throw new AdXException("The entry" + bidEntry + " refers to campaign that already ended: "
              + this.statistics.getStatisticsCampaign().getCampaign(campaignId));
        }
      }
    }
  }

  /**
   * Generates and stores a list of campaigns opportunities for the current simulated day.
   * 
   * @throws AdXException
   */
  public List<Campaign> generateCampaignsOpportunities() throws AdXException {
    if (!this.campaignsForAuction.containsKey(this.currentDay + 1)) {
      List<Campaign> listOfCampaigns = Sampling.sampleCampaingList(this.currentDay + 1, Parameters.NUMBER_AUCTION_CAMPAINGS);
      this.campaignsForAuction.put(this.currentDay + 1, listOfCampaigns);
    } else {
      throw new AdXException("[x] Already sample campaign opportunities for day: " + (this.currentDay + 1));
    }
    return this.campaignsForAuction.get(this.currentDay + 1);
  }

  /**
   * Run all campaigns auctions.
   * 
   * @throws AdXException
   */
  public void runCampaignAuctions() throws AdXException {
    //Logging.log("[-] Try to run campaign auction for day " + this.currentDay);
    List<Campaign> campaignsForAuction = this.campaignsForAuction.get(this.currentDay);
    for (Campaign campaign : campaignsForAuction) {
      // Logging.log("\t\t [-] Campaign = " + campaign);
      List<Pair<String, Double>> filteredBids = CampaignAuctions.filterBids(campaign, this.bidBundles.row(this.currentDay),
          this.statistics.getQualityScores(this.currentDay - 1));
      // Logging.log("\t\t [-] Filtered bids = " + filteredBids);
      if (filteredBids.size() > 0) {
        Pair<String, Double> winner = CampaignAuctions.runCampaignAuction(filteredBids);
        // Logging.log("\t\t\t [-] Winner!!! = " + winner.getElement1());
        this.registerCampaign(campaign, winner.getElement1());
        if (winner.getElement2() == Double.MAX_VALUE) {
          campaign.setBudget(campaign.getReach());
        } else {
          campaign.setBudget(winner.getElement2());
        }
      }
    }
    Logging.log("[-] Done running campaign auction for day " + this.currentDay);
  }

  /**
   * Runs all the ad auctions.
   * 
   * @throws AdXException
   */
  public void runAdAuctions(double reserve) throws AdXException {
    //Logging.log("[-] Try to run ad auctions for day: " + this.currentDay);
    AdAuctions.runAllAuctions(this.currentDay, this.bidBundles.row(this.currentDay), this.statistics, reserve);
    //Logging.log("[-] Done running ad auctions for day: " + this.currentDay);
  }
  
  /**
   * Wrapper to run auctions with no reserve.
   * 
   * @throws AdXException
   */
  public void runAdAuctions() throws AdXException {
    this.runAdAuctions(0.0);
  }

  /**
   * Printer.
   * 
   * @return
   */
  public String printNiceCampaignForAuctionList() {
    String ret = "";
    if (this.campaignsForAuction.entrySet().size() > 0) {
      for (Entry<Integer, List<Campaign>> x : this.campaignsForAuction.entrySet()) {
        ret += "\n\t\t Day: " + x.getKey();
        for (Campaign c : x.getValue()) {
          ret += "\n\t\t\t " + c;
        }
      }
    } else {
      ret += "\n\t\t Currently, there are no campaigns for auction";
    }
    return ret;
  }

  /**
   * A light string representation for debugging purposes.
   */
  public void printServerState() {
    Logging.log("[-] Server State: \n\t Game Id = " + this.gameId + "\n\t Day: " + this.currentDay + "\n\t EndOfDay: "
        + ((this.currentDayEnd != null) ? this.currentDayEnd.toString() : "") + "\n\t Agents Names: " + this.agentsNames + "\n\t Campaings Ownership: "
        + this.statistics.getStatisticsCampaign().printNiceCampaignOwnership() + "\n\t Campaings: "
        + this.statistics.getStatisticsCampaign().printNiceCampaignTable() + "\n\t Map of Campaigns: "
        + this.statistics.getStatisticsCampaign().printNiceAgentCampaignTable() +
        // "\n\t BidBundles = " + this.bidBundles +
        "\n\t Campaign For Auction: " + this.printNiceCampaignForAuctionList());
    if (this.statistics != null)
      Logging.log("\t Ad Statistics: " + this.statistics.getStatisticsAds().printNiceAdStatisticsTable());
    Logging.log("\t Quality Scores: " + this.statistics.getNiceQualityScoresTable());
    Logging.log("\t Profit: " + this.statistics.getNiceProfitScoresTable());
    Logging.log("\t Summary: " + this.statistics.getStatisticsAds().printNiceSummaryTable());
  }

}
