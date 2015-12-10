package starrealmssimulator.service;

import starrealmssimulator.bots.*;
import starrealmssimulator.bots.json.JsonBot;
import starrealmssimulator.cards.*;
import starrealmssimulator.gambits.*;
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

        return getSimulationInfo(games, bots.get(0).getPlayerName(), bots.get(1).getPlayerName());
    }

    private SimulationInfo getSimulationInfo(List<Game> games, String player1Name, String player2Name) {
        int firstPlayerWins = 0;

        int turns = 0;

        int gamesSimulated = games.size();

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

        boolean playAgainstSelf = player1Name.equals(player2Name);

        String botName = player1Name;
        String botPercent = "";
        String opponentPercent = "";
        String opponentName = "";

        for (String playerName : simulationStatsMap.keySet()) {
            SimulationStats simulationStats = simulationStatsMap.get(playerName);
            String winPercent = f.format(((float) simulationStats.getWins() / gamesSimulated) * 100) + "%";
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

        String avgTurns = f.format((float) turns / gamesSimulated);

        if (playAgainstSelf) {
            String winPercent = f.format(((float) firstPlayerWins / gamesSimulated) * 100) + "%";
            System.out.println(botName + " v " + botName + ": 1st player wins: " + winPercent + " (Avg # turns: " + avgTurns + ")");
        } else {
            System.out.println(botName + " v " + opponentName + ": " + botPercent + " - " + opponentPercent + " (Avg # turns: " + avgTurns + ")");
        }

        return simulationInfo;
    }

    public Game simulateGame(List<Bot> bots) {
        Game game = new Game();

        List<Card> deck = new ArrayList<>();
        deck.addAll(getBaseSetDeck());
        deck.addAll(getYear1PromoCards());
        deck.addAll(getBasesAndBattleships());

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

        addGambits(game);

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

    public List<Card> getBasesAndBattleships() {
        List<Card> cards = new ArrayList<>();

        cards.add(new MegaMech());

        cards.add(new DefenseBot());
        cards.add(new DefenseBot());

        cards.add(new ImperialTrader());

        cards.add(new FighterBase());
        cards.add(new FighterBase());

        cards.add(new Obliterator());

        cards.add(new TradeWheel());
        cards.add(new TradeWheel());

        cards.add(new ConstructionHauler());

        cards.add(new TradeRaft());
        cards.add(new TradeRaft());

        return cards;
    }

    public List<Gambit> getGambits() {
        List<Gambit> gambits = new ArrayList<>();

        gambits.add(new BoldRaid());

        gambits.add(new EnergyShield());

        gambits.add(new FrontierFleet());

        gambits.add(new PoliticalManeuver());

        gambits.add(new RiseToPower());

        gambits.add(new SalvageOperation());

        gambits.add(new SmugglingRun());

        gambits.add(new SurpriseAssault());

        gambits.add(new UnlikelyAlliance());

        return gambits;
    }

    private void addGambits(Game game) {
        List<Gambit> gambits = getGambits();
        Collections.shuffle(gambits);

        int i = 0;

        for (Player player : game.getPlayers()) {
            player.getGambits().add(gambits.get(i));
            game.gameLog(player.getPlayerName() + " starts with gambit " + gambits.get(i).getName());
            i++;
            player.getGambits().add(gambits.get(i));
            game.gameLog(player.getPlayerName() + " starts with gambit " + gambits.get(i).getName());
            i++;
        }
    }

    public static void main(String[] args) {
        GameService service = new GameService();

        List<Bot> bots = new ArrayList<>();

        bots.add(new HareBot());
        bots.add(new AttackBot());
        bots.add(new DefenseAndBaseBot());
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

        //service.simulateAllAgainstAll(bots);

        //service.simulateAllAgainstAllJsonBots(botFiles);

        //service.simulateOneAgainstAllBotsJsonBots("HareBot.json", botFiles);

        //service.simulateOneAgainstAllBots(new HareBot(), bots);

        service.simulateGameToEnd(service.getGameState(), 1000);
    }

    private GameState getGameState() {
        GameState gameState = new GameState();

        gameState.includeBasesAndBattleships = true;
        gameState.includeYearOnePromos = true;
        gameState.turn = 20;

        gameState.tradeRow = "junkyard, supplybot, federationshuttle, portofcall, blobworld";

        gameState.bot = "";
        gameState.hand = "scout, scout, merccruiser, viper, viper";
        gameState.deck = "starmarket, missilemech, supplybot, scout, scout, scout";
        gameState.discard = "scout, scout, centraloffice, tradepod, blobfighter, tradepod, scout, battlepod, recyclingstation";
        gameState.basesInPlay = "fleethq";

        gameState.opponentBot = "defense";
        gameState.opponentHandAndDeck = "battleblob, ram, tradewheel, embassyyacht, federationshuttle, tradeescort, tradingpost, missilebot, missilebot, stealthneedle, spacestation, scout, scout, scout, scout, scout, scout, scout, viper, viper";

        return gameState;
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

    public void simulateGameToEnd(GameState gameState, int timesToSimulate) {
        List<Game> games = new ArrayList<>(timesToSimulate);

        for (int i = 0; i < timesToSimulate; i++) {
            games.add(simulateGameToEnd(gameState));
        }

        int wins = 0;

        Bot player = getBotFromBotName(gameState.bot);
        player.setPlayerName(player.getPlayerName() + "(Player)");

        Bot opponent = getBotFromBotName(gameState.opponentBot);
        opponent.setPlayerName(opponent.getPlayerName() + "(Opponent)");

        SimulationInfo info = getSimulationInfo(games, player.getPlayerName(), opponent.getPlayerName());
        for (String playerName : info.getSimulationStats().keySet()) {
            SimulationStats stats = info.getSimulationStats().get(playerName);
            if (playerName.equals(player.getPlayerName())) {
                wins += stats.getWins();
            }
        }

        DecimalFormat f = new DecimalFormat("##.00");

        System.out.println("Player wins: " + f.format(((float) wins / timesToSimulate) * 100) + "%");
    }

    public Game simulateGameToEnd(GameState gameState) {
        Bot player = getBotFromBotName(gameState.bot);
        player.setPlayerName(player.getPlayerName() + "(Player)");

        Bot opponent = getBotFromBotName(gameState.opponentBot);
        opponent.setPlayerName(opponent.getPlayerName() + "(Opponent)");

        Game game = new Game();

        List<Card> deck = new ArrayList<>();
        deck.addAll(getBaseSetDeck());
        if (gameState.includeYearOnePromos) {
            deck.addAll(getYear1PromoCards());
        }
        if (gameState.includeBasesAndBattleships) {
            deck.addAll(getBasesAndBattleships());
        }

        game.setDeck(deck);

        game.setExplorer(new Explorer());

        player.setGame(game);
        player.setOpponent(opponent);
        player.setShuffles(4);
        player.getHand().addAll(getCardsFromCardNames(gameState.hand));
        player.getDeck().addAll(getCardsFromCardNames(gameState.deck));
        player.getDiscard().addAll(getCardsFromCardNames(gameState.discard));
        player.getBases().addAll(getBasesFromCardNames(gameState.basesInPlay));

        opponent.setGame(game);
        opponent.setOpponent(player);
        opponent.setShuffles(4);
        opponent.getDeck().addAll(getCardsFromCardNames(gameState.opponentHandAndDeck));
        opponent.getDiscard().addAll(getCardsFromCardNames(gameState.opponentDiscard));
        opponent.getBases().addAll(getBasesFromCardNames(gameState.opponentBasesInPlay));
        opponent.drawCards(5);

        if (gameState.includeGambits) {
            player.getGambits().addAll(getGambitsFromGambitNames(gameState.gambits));
            opponent.getGambits().addAll(getGambitsFromGambitNames(gameState.opponentGambits));
        }

        game.getDeck().removeAll(player.getAllCards());
        game.getDeck().removeAll(opponent.getAllCards());

        List<Card> tradeRowCards = getCardsFromCardNames(gameState.tradeRow);
        game.getTradeRow().addAll(tradeRowCards);
        game.getDeck().removeAll(tradeRowCards);

        Collections.shuffle(game.getDeck());

        List<Player> players = new ArrayList<>(2);
        players.add(player);
        players.add(opponent);

        game.setPlayers(players);

        game.setTurn(gameState.turn);

        if (!gameState.currentPlayer) {
            game.setCurrentPlayerIndex(1);
        }

        while (!game.isGameOver()) {
            if (game.getTurn() > 200) {
                if (EXTRA_LOGGING) {
                    System.out.println("-----Game stuck, trying again-----");
                }
                return simulateGameToEnd(gameState);
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

    public Bot getBotFromBotName(String botName) {
        if (botName == null || botName.isEmpty()) {
            return new EndGameBot();
        }

        botName = botName.replaceAll("\\s", "").toLowerCase();

        switch (botName) {
            case "attack":
            case "attackbot":
                return new AttackBot();
            case "defense":
            case "defensebot":
            case "defenseandbasebot":
                return new DefenseAndBaseBot();
            case "expensive":
            case "expensivebot":
                return new ExpensiveBot();
            case "hare":
            case "harebot":
                return new HareBot();
            case "random":
            case "randombot":
                return new RandomBot();
            case "tortoise":
            case "tortoisebot":
                return new TortoiseBot();
            case "velocity":
            case "velocitybot":
                return new VelocityBot();
            default:
                return new EndGameBot();
        }
    }

    private List<Gambit> getGambitsFromGambitNames(String gambitNames) {
        List<Gambit> gambits = new ArrayList<>();

        String[] gambitNameArray = gambitNames.split(",");

        for (String gambitName : gambitNameArray) {
            Gambit gambit = getGambitFromName(gambitName);
            if (gambit == null) {
                System.out.println("Gambit not found for: " + gambitName);
            } else {
                gambits.add(gambit);
            }
        }

        return gambits;
    }

    private Gambit getGambitFromName(String gambitName) {
        gambitName = gambitName.replaceAll("\\s", "").toLowerCase();

        switch (gambitName) {
            case "boldraid":
                return new BoldRaid();
            case "energyshield":
                return new EnergyShield();
            case "frontierfleet":
                return new FrontierFleet();
            case "politicalmaneuver":
                return new PoliticalManeuver();
            case "risetopower":
                return new RiseToPower();
            case "salvageoperation":
                return new SalvageOperation();
            case "smugglingrun":
                return new SmugglingRun();
            case "surpriseassault":
                return new SurpriseAssault();
            case "unlikelyalliance":
                return new UnlikelyAlliance();
            default:
                return null;
        }
    }

    private List<Card> getCardsFromCardNames(String cardNames) {
        List<Card> cards = new ArrayList<>();

        if (cardNames == null || cardNames.isEmpty()) {
            return cards;
        }

        String[] cardNameArray = cardNames.split(",");

        for (String cardName : cardNameArray) {
            Card card = getCardFromName(cardName);
            if (card == null) {
                System.out.println("Card not found for: " + cardName);
            } else {
                cards.add(card);
            }
        }

        return cards;
    }

    private List<Base> getBasesFromCardNames(String cardNames) {
        List<Base> bases = new ArrayList<>();

        if (cardNames == null || cardNames.isEmpty()) {
            return bases;
        }

        String[] cardNameArray = cardNames.split(",");

        for (String cardName : cardNameArray) {
            Card card = getCardFromName(cardName);
            if (card == null) {
                System.out.println("Base not found for: " + cardName);
            } else {
                bases.add((Base) card);
            }
        }

        return bases;
    }

    private Card getCardFromName(String cardName) {
        cardName = cardName.replaceAll("\\s", "").toLowerCase();

        switch (cardName) {
            case "barterworld":
                return new BarterWorld();
            case "battlebarge":
                return new BattleBarge();
            case "battleblob":
                return new BattleBlob();
            case "battlecruiser":
                return new Battlecruiser();
            case "battlemech":
                return new BattleMech();
            case "battlepod":
                return new BattlePod();
            case "battlescreecher":
                return new BattleScreecher();
            case "battlestation":
                return new BattleStation();
            case "blobcarrier":
                return new BlobCarrier();
            case "blobdestroyer":
                return new BlobDestroyer();
            case "blobfighter":
                return new BlobFighter();
            case "blobwheel":
                return new BlobWheel();
            case "blobworld":
                return new BlobWorld();
            case "brainworld":
                return new BrainWorld();
            case "breedingsite":
                return new BreedingSite();
            case "centraloffice":
                return new CentralOffice();
            case "commandship":
                return new CommandShip();
            case "constructionhauler":
                return new ConstructionHauler();
            case "corvette":
                return new Corvette();
            case "cutter":
                return new Cutter();
            case "defensebot":
                return new DefenseBot();
            case "defensecenter":
                return new DefenseCenter();
            case "dreadnaught":
                return new Dreadnaught();
            case "embassyyacht":
                return new EmbassyYacht();
            case "explorer":
                return new Explorer();
            case "federationshuttle":
                return new FederationShuttle();
            case "fighterbase":
                return new FighterBase();
            case "flagship":
                return new Flagship();
            case "fleethq":
                return new FleetHQ();
            case "fortressoblivion":
                return new FortressOblivion();
            case "freighter":
                return new Freighter();
            case "imperialfighter":
                return new ImperialFighter();
            case "imperialfrigate":
                return new ImperialFrigate();
            case "imperialtrader":
                return new ImperialTrader();
            case "junkyard":
                return new Junkyard();
            case "machinebase":
                return new MachineBase();
            case "mechworld":
                return new MechWorld();
            case "megahauler":
                return new Megahauler();
            case "megamech":
                return new MegaMech();
            case "merccruiser":
                return new MercCruiser();
            case "missilebot":
                return new MissileBot();
            case "missilemech":
                return new MissileMech();
            case "mothership":
                return new Mothership();
            case "obliterator":
                return new Obliterator();
            case "patrolmech":
                return new PatrolMech();
            case "portofcall":
                return new PortOfCall();
            case "ram":
                return new Ram();
            case "recyclingstation":
                return new RecyclingStation();
            case "royalredoubt":
                return new RoyalRedoubt();
            case "scout":
                return new Scout();
            case "spacestation":
                return new SpaceStation();
            case "starbaseomega":
                return new StarbaseOmega();
            case "starmarket":
                return new Starmarket();
            case "stealthneedle":
                return new StealthNeedle();
            case "supplybot":
                return new SupplyBot();
            case "surveyship":
                return new SurveyShip();
            case "theark":
            case "ark":
                return new TheArk();
            case "thehive":
            case "hive":
                return new TheHive();
            case "tradebot":
                return new TradeBot();
            case "tradeescort":
                return new TradeEscort();
            case "tradepod":
                return new TradePod();
            case "traderaft":
                return new TradeRaft();
            case "tradewheel":
                return new TradeWheel();
            case "tradingpost":
                return new TradingPost();
            case "viper":
                return new Viper();
            case "warworld":
                return new WarWorld();
            default:
                return null;
        }
    }
}
