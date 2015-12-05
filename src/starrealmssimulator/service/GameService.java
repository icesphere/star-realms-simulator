package starrealmssimulator.service;

import starrealmssimulator.bots.*;
import starrealmssimulator.bots.json.JsonBot;
import starrealmssimulator.cards.*;
import starrealmssimulator.model.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameService {

    public static final boolean EXTRA_LOGGING = false;

    private static Map<String, JsonBotCache> botCache = new ConcurrentHashMap<>();

    public SimulationInfo simulateGamesWithJsonBots(List<String> botFiles, int gamesToSimulate) {
        List<Bot> bots = new ArrayList<>();
        for (String botFile : botFiles) {
            bots.add(new JsonBot(botFile));
        }
        return simulateGames(bots, gamesToSimulate);
    }

    public SimulationInfo simulateGames(List<Bot> bots, int gamesToSimulate) {
        List<Game> games = new ArrayList<>(gamesToSimulate);

        int firstPlayerWins = 0;

        int turns = 0;

        int gamesSimulated = 0;

        for (int i = 0; i < gamesToSimulate; i++) {
            games.add(simulateGame(bots));
            gamesSimulated++;
            if (gamesSimulated % 1000 == 0) {
                if (EXTRA_LOGGING) {
                    System.out.println("Simulated " + gamesSimulated + " games");
                }
            }
        }

        SimulationInfo simulationInfo = new SimulationInfo();

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

        boolean playAgainstSelf = bots.get(0).getPlayerName().equals(bots.get(1).getPlayerName());

        String botName = bots.get(0).getPlayerName();
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

        simulationInfo.setPlayerName(botName);

        if (playAgainstSelf) {
            simulationInfo.setOpponentName(botName);
        } else {
            simulationInfo.setOpponentName(opponentName);
        }

        simulationInfo.setSimulationStats(simulationStatsMap);

        String avgTurns = f.format((float) turns / gamesToSimulate);

        if (playAgainstSelf) {
            String winPercent = f.format(((float) firstPlayerWins / gamesToSimulate) * 100) + "%";
            System.out.println(botName + " v " + botName + " - 1st player wins: " + winPercent + " (Avg # turns: " + avgTurns + ")");
        } else {
            System.out.println(botName + " v " + opponentName + " - " + botPercent + " - " + opponentPercent + " (Avg # turns: " + avgTurns + ")");
        }

        return simulationInfo;
    }

    public Game simulateGame(List<Bot> bots) {
        Game game = new Game();

        List<Card> deck = new ArrayList<>();
        deck.addAll(getBaseSetDeck());
        deck.addAll(getYear1PromoCards());

        game.setDeck(deck);

        game.setExplorer(new Explorer());

        Collections.shuffle(game.getDeck());

        game.addCardsToTradeRow(5);

        List<Player> players = new ArrayList<>(bots.size());
        for (Bot bot : bots) {
            try {
                if (bot instanceof JsonBot) {
                    players.add(new JsonBot(((JsonBot) bot).getBotFile()));
                } else {
                    players.add(bot.getClass().newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
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
                return simulateGame(bots);
            } else {
                game.gameLog("-------------------------");
                game.gameLog(game.getCurrentPlayer().getPlayerName() + "'s turn: ");
                game.gameLog("deck #: " + (game.getCurrentPlayer().getCurrentDeckNumber()));
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

    private List<Card> getYear1PromoCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Megahauler());

        cards.add(new TheArk());

        cards.add(new BattleBarge());
        cards.add(new BattleBarge());

        cards.add(new BattleScreecher());
        cards.add(new BattleScreecher());

        cards.add(new Starmarket());
        cards.add(new Starmarket());

        cards.add(new FortressOblivion());
        cards.add(new FortressOblivion());

        cards.add(new BreedingSite());

        cards.add(new StarbaseOmega());

        cards.add(new MercCruiser());
        cards.add(new MercCruiser());
        cards.add(new MercCruiser());

        return cards;
    }

    public static void main(String[] args) {
        GameService service = new GameService();

        List<Bot> bots = new ArrayList<>();

        bots.add(new HareBot());
        bots.add(new AttackBot());
        bots.add(new DefenseBot());
        bots.add(new VelocityBot());
        bots.add(new TortoiseBot());
        bots.add(new ExpensiveBot());
        bots.add(new RandomBot());

        /*List<String> botFiles = new ArrayList<>();

        botFiles.add("HareBot.json");
        botFiles.add("AttackBot.json");
        botFiles.add("DefenseBot.json");
        botFiles.add("VelocityBot.json");
        botFiles.add("TortoiseBot.json");
        botFiles.add("ExpensiveBot.json");*/

        //botFiles.add("SmartBot.json");

        //botFiles.add("DoNothingBot.json");
        //botFiles.add("ExplorerBot.json");
        //botFiles.add("UnrulyBot.json");

        /*botFiles.add("BlueGreenBot.json");
        botFiles.add("BlueYellowBot.json");
        botFiles.add("GreenYellowBot.json");
        botFiles.add("RedBlueBot.json");
        botFiles.add("RedGreenBot.json");
        botFiles.add("YellowRedBot.json");*/

        //service.simulateTwoBots("VelocityBot.json", "SmartBot.json");

        //service.simulateTwoBots(new VelocityBot(), new HareBot());

        //service.simulateAllAgainstAllJsonBots(botFiles);

        //service.simulateOneAgainstAllBotsJsonBots("HareBot.json", botFiles);

        service.simulateOneAgainstAllBots(new HareBot(), bots);
    }

    private void simulateAllAgainstAllJsonBots(List<String> botFiles) {
        for (String botFile : botFiles) {
            simulateOneAgainstAllBotsJsonBots(botFile, botFiles);
            System.out.println("");
        }
    }

    private void simulateAllAgainstAll(List<Bot> bots) {
        for (Bot bot : bots) {
            simulateOneAgainstAllBots(bot, bots);
            System.out.println("");
        }
    }

    private void simulateOneAgainstAllBots(Bot bot, List<Bot> opponentBots) {
        int gamesToSimulate = 10000;

        int totalGames = 0;
        int firstPlayerWins = 0;
        int mainBotWins = 0;

        String botName = bot.getPlayerName();

        System.out.println("--" + botName + "--");

        for (Bot opponentBot : opponentBots) {
            List<Bot> bots = new ArrayList<>(2);
            bots.add(bot);
            bots.add(opponentBot);
            SimulationInfo info = this.simulateGames(bots, gamesToSimulate);
            for (String playerName : info.getSimulationStats().keySet()) {
                SimulationStats stats = info.getSimulationStats().get(playerName);
                firstPlayerWins += stats.getFirstPlayerWins();
                //don't count wins if playing against itself
                if (playerName.equals(botName) && !playerName.equals(info.getOpponentName())) {
                    mainBotWins += stats.getWins();
                }
            }
            totalGames += gamesToSimulate;
        }

        DecimalFormat f = new DecimalFormat("##.00");

        //don't include games against itself in total games
        System.out.println(botName + " Overall wins: " + f.format(((float) mainBotWins / (totalGames - gamesToSimulate)) * 100) + "%");

        //System.out.println("Overall 1st player wins: " + f.format(((float) firstPlayerWins / totalGames) * 100) + "%");
    }

    private void simulateOneAgainstAllBotsJsonBots(String botFile, List<String> opponentBotFiles) {
        Bot bot = new JsonBot(botFile);

        List<Bot> opponentBots = new ArrayList<>();

        for (String opponentBotFile : opponentBotFiles) {
            opponentBots.add(new JsonBot(opponentBotFile));
        }

        simulateOneAgainstAllBots(bot, opponentBots);
    }

    private void simulateTwoBots(String botFile1, String botFile2) {
        int gamesToSimulate = 10000;

        List<String> botFiles = new ArrayList<>();

        botFiles.add(botFile1);
        botFiles.add(botFile2);

        this.simulateGamesWithJsonBots(botFiles, gamesToSimulate);
    }

    private void simulateTwoBots(Bot bot1, Bot bot2) {
        int gamesToSimulate = 10000;

        List<Bot> bots = new ArrayList<>();

        bots.add(bot1);
        bots.add(bot2);

        this.simulateGames(bots, gamesToSimulate);
    }

    public static JsonBotCache getBotCache(String botFile) {
        return botCache.get(botFile);
    }

    public static void addBotToCache(String botFile, JsonBotCache jsonBotCache) {
        botCache.put(botFile, jsonBotCache);
    }
}
