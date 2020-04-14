package adx.server;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import adx.exceptions.AdXException;
import adx.messages.ACKMessage;
import adx.messages.ConnectServerMessage;
import adx.structures.BidBundle;
import adx.util.Logging;
import adx.util.Pair;
import adx.util.Startup;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage.KeepAlive;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * A simple server for the AdX game.
 * 
 * @author Enrique Areyan Viqueira
 */
abstract public class GameServerAbstract {

  /**
   * Server Kryo object.
   */
  protected final Server gameServer;

  /**
   * Maps that contains all the connections.
   */
  protected Map<String, Connection> namesToConnections;
  protected Map<Connection, String> connectionsToNames;

  /**
   * A boolean that indicates whether the server accepts new players.
   */
  protected boolean acceptingNewPlayers;

  /**
   * Current game.
   */
  protected int gameNumber = 1;

  /**
   * An object that maintains the state of the server.
   */
  protected ServerState serverState;

  /**
   * Server constructor.
   * 
   * @param port
   * @throws IOException
   * @throws AdXException 
   */
  public GameServerAbstract(int port) throws IOException, AdXException {

    Logging.log("[-] Server Initialized at " + Instant.now());
    this.namesToConnections = new ConcurrentHashMap<String, Connection>();
    this.connectionsToNames = new ConcurrentHashMap<Connection, String>();
    this.acceptingNewPlayers = true;
    this.gameServer = new Server();
    this.gameServer.start();
    this.gameServer.bind(port, port);
    Startup.start(this.gameServer.getKryo());
    this.gameServer.addListener(new Listener() {
      public void received(Connection connection, Object message) {
        // Logging.log("Received a connection -- > " + message);
        try {
          if (message instanceof ConnectServerMessage) {
            handleJoinGameMessage((ConnectServerMessage) message, connection);
          } else if (message instanceof BidBundle) {
            handleBidBundleMessage((BidBundle) message, connection);
          } else if (message instanceof KeepAlive) {
            // Internal kryo message, ignore.
          } else {
            Logging.log("[x] Received an unknown message from " + connection + ", here it is " + message);
          }
        } catch (Exception e) {
          Logging.log("An exception occurred while trying to parse a message in the server");
          e.printStackTrace();
        }
      }
    });
    this.serverState = new ServerState(this.gameNumber);
  }
  
  // A better way is to have all the credentials in memory once at startup
  protected synchronized boolean areAgentCredentialsValid(String agentName, String agentPassword) {
    // For development purposes, this map contains the allowable agents
    // along with their passwords. This should be obtained from a database.
    HashMap<String, String> agentsInfo = new HashMap<String, String>();
    // Sundays
    agentsInfo.put("agent0", "123456");
    agentsInfo.put("agent1", "123456");
    return agentsInfo.containsKey(agentName) && agentsInfo.get(agentName).equals(agentPassword);
  }
  
  /**
   * Handles a Join Game message.
   * 
   * @param joinGameMessage
   * @param agentConnection
   * @throws Exception
   */
  protected void handleJoinGameMessage(ConnectServerMessage joinGameMessage, Connection agentConnection) throws Exception {
    if (!this.acceptingNewPlayers) {
      joinGameMessage.setServerResponse("Not accepting agents");
      joinGameMessage.setStatusCode(3);
      agentConnection.sendTCP(joinGameMessage);
      return;
    }
    String agentName = joinGameMessage.getAgentName();
    String agentPassword = joinGameMessage.getAgentPassword();
    Logging.log("\t[-] Trying to register agent: " + agentName + ", with password: " + agentPassword);
    String serverResponse;
    int statusCode;
    if (this.namesToConnections.containsKey(agentName)) {
      Logging.log("\t\t[x] Agent " + agentName + " is already registered");
      serverResponse = "Already Registered";
      statusCode = 0;
    } else if (areAgentCredentialsValid(agentName, agentPassword)) {
      Logging.log("\t\t[-] Agent credentials are valid, agent registered");
      this.namesToConnections.put(agentName, agentConnection);
      this.connectionsToNames.put(agentConnection, agentName);
      this.serverState.registerAgent(agentName);
      serverResponse = "OK";
      statusCode = 1;
    } else {
      Logging.log("\t\t[-] Could not register agent: credentials are not valid");
      serverResponse = "Invalid Credentials";
      statusCode = 2;
    }
    joinGameMessage.setServerResponse(serverResponse);
    joinGameMessage.setStatusCode(statusCode);
    agentConnection.sendTCP(joinGameMessage);
  }  

  /**
   * Handles a BidBundle message.
   * 
   * @param bidBundle
   * @param connection
   */
  protected void handleBidBundleMessage(BidBundle bidBundle, Connection connection) {
    Logging.log("[-] Received the following bid bundle: \n\t " + bidBundle + ", from " + this.connectionsToNames.get(connection));
    Pair<Boolean, String> bidBundleAccept = this.serverState.addBidBundle(bidBundle.getDay(), this.connectionsToNames.get(connection), bidBundle);
    if (bidBundleAccept.getElement1()) {
      connection.sendTCP(new ACKMessage(true, "Bid bundle for day " + bidBundle.getDay() + " received OK."));
    } else {
      connection.sendTCP(new ACKMessage(false, "Bid bundle for day " + bidBundle.getDay() + " not accepted. Server replied: " + bidBundleAccept.getElement2()));
    }
  }
  
  /**
   * Runs the game. Must be implemented by the extending class.
   * @throws AdXException 
   */
  abstract protected void runAdXGame() throws AdXException;
  
}
