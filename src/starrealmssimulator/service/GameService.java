package starrealmssimulator.service;

import starrealmssimulator.bots.JsonBot;
import starrealmssimulator.cards.*;
import starrealmssimulator.model.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameService {

    public static final boolean EXTRA_LOGGING = false;

    private static Map<String, JsonBotCache> botCache = new ConcurrentHashMap<>();

    public Map<String, SimulationStats> simulateGames(List<String> botFiles, int gamesToSimulate) {
        List<Game> games = new ArrayList<>(gamesToSimulate);

        int firstPlayerWins = 0;

        int turns = 0;

        int gamesSimulated = 0;

        for (int i=0; i < gamesToSimulate; i++) {
            games.add(simulateGame(botFiles));
            gamesSimulated++;
            if (gamesSimulated % 1000 == 0) {
                if (EXTRA_LOGGING) {
                    System.out.println("Simulated " + gamesSimulated + " games");
                }
            }
        }

        Map<String, SimulationStats> simulationStatsMap = new HashMap<>();

        for (Game game : games) {
            Player winner = game.getWinner();
            SimulationStats simulationStats = simulationStatsMap.get(winner.getPlayerName());
            if (simulationStats == null) {
                simulationStats = new SimulationStats();
            }
            simulationStats.addWin();
            if (winner.isFirstPlayer()) {
                firstPlayerWins++;
                simulationStats.addFirstPlayerWin();
            }

            simulationStatsMap.put(winner.getPlayerName(), simulationStats);

            turns += game.getTurn();
        }

        DecimalFormat f = new DecimalFormat("##.00");

        boolean playAgainstSelf = botFiles.get(0).equals(botFiles.get(1));

        String botName = botFiles.get(0).substring(0, botFiles.get(0).length() - 5);
        String botPercent = "";
        String opponentPercent = "";
        String opponentName = "";

        for (String playerName : simulationStatsMap.keySet()) {
            SimulationStats simulationStats = simulationStatsMap.get(playerName);
            String winPercent = f.format(((float) simulationStats.getWins() / gamesToSimulate) * 100) + "%";
            if (playerName.equals(botName)) {
                botPercent = winPercent;
            } else {
                opponentName = playerName;
                opponentPercent = winPercent;
            }
        }


        String avgTurns = f.format((float) turns / gamesToSimulate);

        if (playAgainstSelf) {
            String winPercent = f.format(((float) firstPlayerWins / gamesToSimulate) * 100) + "%";
            System.out.println(botName + " v " + botName + " - 1st player wins: " + winPercent + " (Avg # turns: " + avgTurns + ")");
        } else {
            System.out.println(botName + " v " + opponentName + " - " +  botPercent + " - " + opponentPercent + " (Avg # turns: " + avgTurns + ")");
        }

        return simulationStatsMap;
    }

    public Game simulateGame(List<String> botFiles) {
        Game game = new Game();
        game.setDeck(getBaseSetDeck());
        game.setExplorer(new Explorer());

        Collections.shuffle(game.getDeck());

        game.addCardsToTradeRow(5);

        List<Player> players = new ArrayList<>(botFiles.size());
        for (String botFile : botFiles) {
            players.add(new JsonBot(botFile));
        }

        Player player1 = players.get(0);
        Player player2 = players.get(1);

        Collections.shuffle(players);

        players.get(0).setFirstPlayer(true);

        setupPlayer(player1, game, player2);
        setupPlayer(player2, game, player1);

        game.setPlayers(players);

        game.startGame();

        while (!game.isGameOver()) {
            if (game.getTurn() > 200) {
                if (EXTRA_LOGGING) {
                    System.out.println("-----Game stuck, trying again-----");
                }
                return simulateGame(botFiles);
            } else {
                game.gameLog("-------------------------");
                game.gameLog(game.getCurrentPlayer().getPlayerName() + "'s turn: ");
                game.gameLog("deck #: " + (game.getCurrentPlayer().getShuffles() + 1));
                game.gameLog("");
                game.gameLog("Trade Row: " + game.getCardsAsString(game.getTradeRow()));
                game.gameLog("");
                game.gameLog("Hand: " + game.getCardsAsString(game.getCurrentPlayer().getHand()));
                game.gameLog("Discard: " + game.getCardsAsString(game.getCurrentPlayer().getDiscard()));
                game.gameLog("Deck: " + game.getCardsAsString(game.getCurrentPlayer().getDeck()));
                game.gameLog("Bases in play: " + game.getCardsAsString(game.getCurrentPlayer().getBases()));
                game.gameLog("");
                game.getCurrentPlayer().takeTurn();
            }
        }

        return game;
    }

    private void setupPlayer(Player player, Game game, Player opponent) {
        player.setGame(game);
        player.setOpponent(opponent);
        player.setDeck(getStartingCards());
        Collections.shuffle(player.getDeck());
    }

    private List<Card> getStartingCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Scout());
        cards.add(new Scout());
        cards.add(new Scout());
        cards.add(new Scout());
        cards.add(new Scout());
        cards.add(new Scout());
        cards.add(new Scout());
        cards.add(new Scout());

        cards.add(new Viper());
        cards.add(new Viper());

        return cards;
    }

    private List<Card> getBaseSetDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new TradeBot());
        cards.add(new TradeBot());
        cards.add(new TradeBot());

        cards.add(new MissileBot());
        cards.add(new MissileBot());
        cards.add(new MissileBot());

        cards.add(new SupplyBot());
        cards.add(new SupplyBot());
        cards.add(new SupplyBot());

        cards.add(new PatrolMech());
        cards.add(new PatrolMech());

        cards.add(new StealthNeedle());

        cards.add(new BattleMech());

        cards.add(new MissileMech());

        cards.add(new BattleStation());
        cards.add(new BattleStation());

        cards.add(new MechWorld());

        cards.add(new BrainWorld());

        cards.add(new MachineBase());

        cards.add(new Junkyard());

        cards.add(new ImperialFighter());
        cards.add(new ImperialFighter());
        cards.add(new ImperialFighter());

        cards.add(new ImperialFrigate());
        cards.add(new ImperialFrigate());
        cards.add(new ImperialFrigate());

        cards.add(new SurveyShip());
        cards.add(new SurveyShip());
        cards.add(new SurveyShip());

        cards.add(new Corvette());
        cards.add(new Corvette());

        cards.add(new Battlecruiser());

        cards.add(new Dreadnaught());

        cards.add(new SpaceStation());
        cards.add(new SpaceStation());

        cards.add(new RecyclingStation());
        cards.add(new RecyclingStation());

        cards.add(new WarWorld());

        cards.add(new RoyalRedoubt());

        cards.add(new FleetHQ());

        cards.add(new FederationShuttle());
        cards.add(new FederationShuttle());
        cards.add(new FederationShuttle());
        
        cards.add(new Cutter());
        cards.add(new Cutter());
        cards.add(new Cutter());

        cards.add(new EmbassyYacht());
        cards.add(new EmbassyYacht());

        cards.add(new Freighter());
        cards.add(new Freighter());

        cards.add(new CommandShip());

        cards.add(new TradeEscort());

        cards.add(new Flagship());

        cards.add(new TradingPost());
        cards.add(new TradingPost());

        cards.add(new BarterWorld());
        cards.add(new BarterWorld());

        cards.add(new DefenseCenter());

        cards.add(new CentralOffice());

        cards.add(new PortOfCall());

        cards.add(new BlobFighter());
        cards.add(new BlobFighter());
        cards.add(new BlobFighter());

        cards.add(new TradePod());
        cards.add(new TradePod());
        cards.add(new TradePod());

        cards.add(new BattlePod());
        cards.add(new BattlePod());

        cards.add(new Ram());
        cards.add(new Ram());

        cards.add(new BlobDestroyer());
        cards.add(new BlobDestroyer());

        cards.add(new BattleBlob());

        cards.add(new BlobCarrier());

        cards.add(new Mothership());

        cards.add(new BlobWheel());
        cards.add(new BlobWheel());
        cards.add(new BlobWheel());

        cards.add(new TheHive());

        cards.add(new BlobWorld());

        return cards;
    }

    public static void main(String[] args) {
        GameService service = new GameService();

        //service.simulateTwoBots(service);

        service.simulateOneAgainstAllBots(service, "HareBot.json");
    }

    private void simulateOneAgainstAllBots(GameService service, String botFile) {

        int gamesToSimulate = 10000;

        List<String> opponents = new ArrayList<>();

        List<String> bots = new ArrayList<>();
        bots.add("HareBot");
        bots.add("AttackBot");
        bots.add("DefenseBot");
        bots.add("VelocityBot");
        bots.add("TortoiseBot");
        bots.add("ExpensiveBot");

        //bots.add("SmartBot");

        //bots.add("DoNothingBot");
        //bots.add("ExplorerBot");

        //bots.add("UnrulyBot");

        //bots.add("BlueGreenBot");
        /*bots.add("BlueYellowBot");
        bots.add("GreenYellowBot");
        bots.add("RedBlueBot");
        bots.add("RedGreenBot");
        bots.add("YellowRedBot");*/

        for (String opponentBot : bots) {
            String opponentBotFile = opponentBot + ".json";
            opponents.add(opponentBotFile);
        }

        int totalGames = 0;
        int firstPlayerWins = 0;
        int mainBotWins = 0;

        String botName = botFile.substring(0, botFile.length() - 5);

        System.out.println("--" + botName + "--");

        for (String opponentBotFile : opponents) {
            List<String> botFiles = new ArrayList<>(2);
            botFiles.add(botFile);
            botFiles.add(opponentBotFile);
            Map<String, SimulationStats> statsMap = service.simulateGames(botFiles, gamesToSimulate);
            for (String playerName : statsMap.keySet()) {
                SimulationStats stats = statsMap.get(playerName);
                firstPlayerWins += stats.getFirstPlayerWins();
                if (playerName.equals(botName)) {
                    mainBotWins += stats.getWins();
                }
            }
            totalGames += gamesToSimulate;
        }

        System.out.println("");
        System.out.println("********");
        System.out.println("");

        DecimalFormat f = new DecimalFormat("##.00");

        System.out.println(botName + " Overall wins: " + f.format(((float) mainBotWins / totalGames) * 100) + "%");
        System.out.println("Overall 1st player wins: " + f.format(((float) firstPlayerWins / totalGames) * 100) + "%");
    }

    private void simulateTwoBots(GameService service) {
        int gamesToSimulate = 10000;

        List<String> botFiles = new ArrayList<>();

        botFiles.add("HareBot.json");
        botFiles.add("TortoiseBot.json");

        service.simulateGames(botFiles, gamesToSimulate);
    }

    public static JsonBotCache getBotCache(String botFile) {
        return botCache.get(botFile);
    }

    public static void addBotToCache(String botFile, JsonBotCache jsonBotCache) {
        botCache.put(botFile, jsonBotCache);
    }
}
