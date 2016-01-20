package starrealmssimulator.service;

import starrealmssimulator.bots.*;
import starrealmssimulator.bots.json.JsonBot;
import starrealmssimulator.cards.bases.blob.*;
import starrealmssimulator.cards.bases.outposts.machinecult.*;
import starrealmssimulator.cards.bases.outposts.starempire.*;
import starrealmssimulator.cards.bases.outposts.tradefederation.*;
import starrealmssimulator.cards.bases.starempire.FleetHQ;
import starrealmssimulator.cards.bases.starempire.OrbitalPlatform;
import starrealmssimulator.cards.bases.starempire.StarbaseOmega;
import starrealmssimulator.cards.bases.tradefederation.*;
import starrealmssimulator.cards.events.*;
import starrealmssimulator.cards.heroes.*;
import starrealmssimulator.cards.gambits.*;
import starrealmssimulator.cards.ships.*;
import starrealmssimulator.cards.ships.blob.*;
import starrealmssimulator.cards.ships.machinecult.*;
import starrealmssimulator.cards.ships.starempire.*;
import starrealmssimulator.cards.ships.tradefederation.*;
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
        deck.addAll(getCrisisBasesAndBattleships());
        deck.addAll(getCrisisEvents());

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
                game.getCurrentPlayer().setupTurn();
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

    public List<Card> getCrisisBasesAndBattleships() {
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

    public List<Card> getCrisisEvents() {
        List<Card> cards = new ArrayList<>();

        cards.add(new BlackHole());

        cards.add(new Bombardment());

        cards.add(new Comet());
        cards.add(new Comet());

        cards.add(new GalacticSummit());

        cards.add(new Quasar());
        cards.add(new Quasar());

        cards.add(new Supernova());

        cards.add(new TradeMission());
        cards.add(new TradeMission());

        cards.add(new WarpJump());
        cards.add(new WarpJump());

        return cards;
    }

    public List<Card> getCrisisFleetsAndFortresses() {
        List<Card> cards = new ArrayList<>();

        cards.add(new BorderFort());

        cards.add(new PatrolBot());
        cards.add(new PatrolBot());

        cards.add(new StarFortress());

        cards.add(new CargoLaunch());
        cards.add(new CargoLaunch());

        cards.add(new DeathWorld());

        cards.add(new SpikePod());
        cards.add(new SpikePod());

        cards.add(new CapitolWorld());

        cards.add(new CustomsFrigate());
        cards.add(new CustomsFrigate());

        return cards;
    }

    public List<Hero> getCrisisHeroes() {
        List<Hero> cards = new ArrayList<>();

        cards.add(new RamPilot());
        cards.add(new RamPilot());

        cards.add(new BlobOverlord());

        cards.add(new SpecialOpsDirector());
        cards.add(new SpecialOpsDirector());

        cards.add(new CeoTorres());

        cards.add(new WarElder());
        cards.add(new WarElder());

        cards.add(new HighPriestLyle());

        cards.add(new CunningCaptain());
        cards.add(new CunningCaptain());

        cards.add(new AdmiralRasmussen());

        return cards;
    }

    public List<Card> getColonyWarsDeck() {
        List<Card> cards = new ArrayList<>();

        cards.add(new SolarSkiff());
        cards.add(new SolarSkiff());
        cards.add(new SolarSkiff());

        cards.add(new TradeHauler());
        cards.add(new TradeHauler());
        cards.add(new TradeHauler());

        cards.add(new PatrolCutter());
        cards.add(new PatrolCutter());
        cards.add(new PatrolCutter());

        cards.add(new FrontierFerry());
        cards.add(new FrontierFerry());

        cards.add(new ColonySeedShip());

        cards.add(new Peacekeeper());

        cards.add(new StorageSilo());
        cards.add(new StorageSilo());

        cards.add(new CentralStation());
        cards.add(new CentralStation());

        cards.add(new FederationShipyard());

        cards.add(new LoyalColony());

        cards.add(new FactoryWorld());

        cards.add(new StarBarge());
        cards.add(new StarBarge());
        cards.add(new StarBarge());

        cards.add(new Lancer());
        cards.add(new Lancer());
        cards.add(new Lancer());

        cards.add(new Falcon());
        cards.add(new Falcon());

        cards.add(new Gunship());
        cards.add(new Gunship());

        cards.add(new HeavyCruiser());

        cards.add(new AgingBattleship());

        cards.add(new EmperorsDreadnaught());

        cards.add(new OrbitalPlatform());
        cards.add(new OrbitalPlatform());
        cards.add(new OrbitalPlatform());

        cards.add(new CommandCenter());
        cards.add(new CommandCenter());

        cards.add(new SupplyDepot());

        cards.add(new ImperialPalace());

        cards.add(new Swarmer());
        cards.add(new Swarmer());
        cards.add(new Swarmer());

        cards.add(new Predator());
        cards.add(new Predator());
        cards.add(new Predator());

        cards.add(new Swarmer());
        cards.add(new Swarmer());
        cards.add(new Swarmer());

        cards.add(new Ravager());
        cards.add(new Ravager());

        cards.add(new Parasite());

        cards.add(new Moonwurm());

        cards.add(new Leviathan());

        cards.add(new StellarReef());
        cards.add(new StellarReef());
        cards.add(new StellarReef());

        cards.add(new Bioformer());
        cards.add(new Bioformer());

        cards.add(new PlasmaVent());

        cards.add(new BattleBot());
        cards.add(new BattleBot());
        cards.add(new BattleBot());

        cards.add(new RepairBot());
        cards.add(new RepairBot());
        cards.add(new RepairBot());

        cards.add(new ConvoyBot());
        cards.add(new ConvoyBot());
        cards.add(new ConvoyBot());

        cards.add(new MiningMech());
        cards.add(new MiningMech());

        cards.add(new MechCruiser());

        cards.add(new TheWrecker());

        cards.add(new WarningBeacon());
        cards.add(new WarningBeacon());
        cards.add(new WarningBeacon());

        cards.add(new TheOracle());

        cards.add(new StealthTower());

        cards.add(new FrontierStation());

        cards.add(new TheIncinerator());

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

        gameState.includeCrisisBasesAndBattleships = "Y";
        gameState.includeYearOnePromos = "Y";
        gameState.turn = 20;

        gameState.tradeRow = "junkyard, supplyb, flags, poc, bwheel";

        gameState.bot = "";
        gameState.authority = 41;
        gameState.hand = "missilem, s*2, mc, tpod";
        gameState.deck = "bpod, bf, tpod, co, fshuttle, tpost, supplyb, rs, s*5, v*2";
        gameState.discard = "";
        gameState.basesInPlay = "fleethq, smarket";

        gameState.opponentBot = "defense";
        gameState.opponentAuthority = 20;
        gameState.opponentDiscard = "tescort, missileb, v, s, tpost";
        gameState.opponentHandAndDeck = "bb, ram, twheel, ey, fshuttle, missileb, sn, spaces, s*5, v";

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

    public Map<Card, CardToBuySimulationResults> simulateBestCardToBuy(GameState gameState, int timesToSimulate) {
        Map<Card, CardToBuySimulationResults> resultsByCard = new LinkedHashMap<>();

        List<Card> tradeRowCards = getCardsFromCardNames(gameState.tradeRow);

        boolean playerGoesFirst = gameState.determineCurrentPlayer();

        for (Card card : tradeRowCards) {
            CardToBuySimulationResults cardToBuySimulationResults = new CardToBuySimulationResults();

            SimulationResults results = simulateGameToEnd(gameState, timesToSimulate, card);

            float ableToBuyFirstTurnPercentage = ((float) results.getTotalGamesCounted() / timesToSimulate) * 100;

            cardToBuySimulationResults.setAbleToBuyFirstTurnPercentage(ableToBuyFirstTurnPercentage);

            if (playerGoesFirst) {
                cardToBuySimulationResults.setWinPercentage(results.getWinPercentage());
            } else {
                cardToBuySimulationResults.setWinPercentage(100 - results.getWinPercentage());
            }

            resultsByCard.put(card, cardToBuySimulationResults);
        }

        return resultsByCard;
    }

    public SimulationResults simulateGameToEnd(GameState gameState, int timesToSimulate) {
        return simulateGameToEnd(gameState, timesToSimulate, null);
    }

    public SimulationResults simulateGameToEnd(GameState gameState, int timesToSimulate, Card cardToBuyOnFirstTurn) {
        SimulationResults results = new SimulationResults();

        boolean createdWinGameLog = false;
        boolean createdLossGameLog = false;

        int totalGamesCounted = 0;

        System.out.println("Game State: \n\n" + gameState.toString() + "\n\n");

        List<Game> games = new ArrayList<>(timesToSimulate);

        Map<String, Map<Integer, Integer>> averageAuthorityByPlayerByTurn = new HashMap<>();

        float turnTotal = 0;

        int wins = 0;

        Bot player = getBotFromBotName(gameState.bot);
        player.setPlayerName(player.getPlayerName() + "(Player)");
        averageAuthorityByPlayerByTurn.put(player.getPlayerName(), new HashMap<>());

        Bot opponent = getBotFromBotName(gameState.opponentBot);
        opponent.setPlayerName(opponent.getPlayerName() + "(Opponent)");
        averageAuthorityByPlayerByTurn.put(opponent.getPlayerName(), new HashMap<>());

        for (int i = 0; i < timesToSimulate; i++) {
            boolean createGameLog = !createdWinGameLog || !createdLossGameLog;
            Game game = simulateGameToEnd(gameState, createGameLog, cardToBuyOnFirstTurn);
            if (cardToBuyOnFirstTurn != null && !game.getWinner().isBoughtSpecifiedCardOnFirstTurn() && !game.getLoser().isBoughtSpecifiedCardOnFirstTurn()) {
                continue;
            }
            if (game.getWinner().getPlayerName().equals(player.getPlayerName())) {
                wins++;
                if (createGameLog) {
                    if (!createdWinGameLog) {
                        results.setWinGameLog(game.getGameLog().toString());
                        createdWinGameLog = true;
                    }
                    game.setGameLog(null);
                }
            } else {
                if (!createdLossGameLog) {
                    results.setLossGameLog(game.getGameLog().toString());
                    createdLossGameLog = true;
                }
                game.setGameLog(null);
            }
            totalGamesCounted++;
            games.add(game);
            turnTotal += game.getTurn();
        }

        for (Game game : games) {
            Map<String, TreeMap<Integer, Integer>> authorityByPlayerByTurn = game.getAuthorityByPlayerByTurn();
            for (String playerName : authorityByPlayerByTurn.keySet()) {
                Map<Integer, Integer> authorityByTurn = authorityByPlayerByTurn.get(playerName);
                Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);

                for (Integer turn : authorityByTurn.keySet()) {
                    Integer authority = averageAuthorityByTurn.get(turn);
                    if (authority == null) {
                        authority = 0;
                    }

                    authority += authorityByTurn.get(turn);

                    averageAuthorityByTurn.put(turn, authority);
                }
            }
        }

        for (String playerName : averageAuthorityByPlayerByTurn.keySet()) {
            Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);
            for (Integer turn : averageAuthorityByTurn.keySet()) {
                Integer authority = averageAuthorityByTurn.get(turn);
                authority = authority / games.size();
                averageAuthorityByTurn.put(turn, authority);
            }
        }

        DecimalFormat f = new DecimalFormat("##.00");

        float winPercentage = ((float) wins / totalGamesCounted) * 100;

        System.out.println("Player wins: " + f.format(winPercentage) + "%");

        results.setTotalGamesCounted(totalGamesCounted);
        results.setWinPercentage(winPercentage);
        results.setAverageNumTurns(turnTotal / totalGamesCounted);

        for (String playerName : averageAuthorityByPlayerByTurn.keySet()) {
            Map<Integer, Integer> averageAuthorityByTurn = averageAuthorityByPlayerByTurn.get(playerName);
            if (playerName.equals(player.getPlayerName())) {
                results.setPlayerAverageAuthorityByTurn(averageAuthorityByTurn);
            } else {
                results.setOpponentAverageAuthorityByTurn(averageAuthorityByTurn);
            }
        }

        return results;
    }

    public Game simulateGameToEnd(GameState gameState, boolean createGameLog, Card cardToBuyOnFirstTurn) {

        boolean playerGoesFirst = gameState.determineCurrentPlayer();

        Bot player = getBotFromBotName(gameState.bot);
        player.setPlayerName(player.getPlayerName() + "(Player)");
        if (playerGoesFirst) {
            player.setCardToBuyOnFirstTurn(cardToBuyOnFirstTurn);
        }

        Bot opponent = getBotFromBotName(gameState.opponentBot);
        opponent.setPlayerName(opponent.getPlayerName() + "(Opponent)");
        if (!playerGoesFirst) {
            opponent.setCardToBuyOnFirstTurn(cardToBuyOnFirstTurn);
        }

        Game game = new Game();
        game.setCreateGameLog(createGameLog);

        List<Card> deck = new ArrayList<>();
        if (gameState.determineIncludeBaseSet()) {
            deck.addAll(getBaseSetDeck());
        }
        if (gameState.determineIncludeColonyWars()) {
            deck.addAll(getColonyWarsDeck());
        }
        if (gameState.determineIncludeYearOnePromos()) {
            deck.addAll(getYear1PromoCards());
        }
        if (gameState.determineIncludeCrisisBasesAndBattleships()) {
            deck.addAll(getCrisisBasesAndBattleships());
        }
        if (gameState.determineIncludeCrisisEvents()) {
            deck.addAll(getCrisisEvents());
        }
        if (gameState.determineIncludeCrisisFleetsAndFortresses()) {
            deck.addAll(getCrisisFleetsAndFortresses());
        }
        if (gameState.determineIncludeCrisisHeroes()) {
            deck.addAll(getCrisisHeroes());
        }

        game.setDeck(deck);

        game.setExplorer(new Explorer());

        game.gameLog("Setting up cards for Player");
        player.setGame(game);
        player.setOpponent(opponent);
        player.setShuffles(gameState.shuffles);
        player.setAuthority(gameState.authority);
        if (gameState.hand.isEmpty() && gameState.deck.isEmpty() && gameState.discard.isEmpty()) {
            player.getDeck().addAll(getStartingCards());
            Collections.shuffle(player.getDeck());
            if (playerGoesFirst && gameState.turn == 0) {
                player.drawCards(3);
            } else {
                player.drawCards(5);
            }
        } else {
            player.getDeck().addAll(getCardsFromCardNames(gameState.deck));
            Collections.shuffle(player.getDeck());
            if (gameState.hand.isEmpty()) {
                if (playerGoesFirst && gameState.turn == 0) {
                    player.drawCards(3);
                } else {
                    player.drawCards(5);
                }
            } else {
                player.getHand().addAll(getCardsFromCardNames(gameState.hand));
            }
            player.getDiscard().addAll(getCardsFromCardNames(gameState.discard));
        }
        player.getBases().addAll(getBasesFromCardNames(gameState.basesInPlay));

        game.gameLog("-------------------------");
        game.gameLog("Setting up cards for Opponent");
        opponent.setGame(game);
        opponent.setOpponent(player);
        opponent.setShuffles(gameState.opponentShuffles);
        opponent.setAuthority(gameState.opponentAuthority);
        if (gameState.opponentHandAndDeck.isEmpty() && gameState.opponentDiscard.isEmpty()) {
            opponent.getDeck().addAll(getStartingCards());
        } else {
            opponent.getDeck().addAll(getCardsFromCardNames(gameState.opponentHandAndDeck));
            opponent.getDiscard().addAll(getCardsFromCardNames(gameState.opponentDiscard));
        }
        Collections.shuffle(opponent.getDeck());
        if (!playerGoesFirst && gameState.turn == 0) {
            opponent.drawCards(3);
        } else {
            opponent.drawCards(5);
        }
        opponent.getBases().addAll(getBasesFromCardNames(gameState.opponentBasesInPlay));

        if (gameState.determineIncludeGambits()) {
            player.getGambits().addAll(getGambitsFromGambitNames(gameState.gambits));
            opponent.getGambits().addAll(getGambitsFromGambitNames(gameState.opponentGambits));
        }

        if (gameState.determineIncludeCrisisHeroes()) {
            player.getHeroes().addAll(getHeroesFromHeroNames(gameState.heroesInPlay));
            opponent.getHeroes().addAll(getHeroesFromHeroNames(gameState.opponentHeroesInPlay));
        }

        game.getDeck().removeAll(player.getAllCards());
        game.getDeck().removeAll(opponent.getAllCards());

        Collections.shuffle(game.getDeck());

        List<Player> players = new ArrayList<>(2);
        players.add(player);
        players.add(opponent);

        game.setPlayers(players);

        game.setTurn(gameState.turn);

        game.setupPlayerAuthorityMap();

        if (!playerGoesFirst) {
            game.setCurrentPlayerIndex(1);
        }

        if (gameState.tradeRow == null || gameState.tradeRow.isEmpty()) {
            game.gameLog("-------------------------");
            game.addCardsToTradeRow(5);
        } else {
            List<Card> tradeRowCards = getCardsFromCardNames(gameState.tradeRow);
            game.getDeck().removeAll(tradeRowCards);
            for (Card card : tradeRowCards) {
                game.addCardToTradeRow(card);
            }
        }

        while (!game.isGameOver()) {
            if (game.getTurn() > 200) {
                if (EXTRA_LOGGING) {
                    System.out.println("-----Game stuck, trying again-----");
                }
                return simulateGameToEnd(gameState, createGameLog, cardToBuyOnFirstTurn);
            } else {
                game.getCurrentPlayer().setupTurn();
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

    public Gambit getGambitFromName(String gambitName) {
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
            int multiplier = 1;
            String cardNameWithoutMultiplier = cardName;
            if (cardName.contains("*")) {
                cardNameWithoutMultiplier = cardName.substring(0, cardName.indexOf("*"));
                multiplier = Integer.parseInt(cardName.substring(cardName.indexOf("*") + 1).trim());
            }
            Card card = getCardFromName(cardNameWithoutMultiplier);
            if (card == null) {
                System.out.println("Card not found for: " + cardName);
            } else {
                if (multiplier == 1) {
                    cards.add(card);
                } else {
                    for (int i = 1; i <= multiplier; i++) {
                        try {
                            cards.add(card.getClass().newInstance());
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
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

    public Card getCardFromName(String cardName) {
        cardName = cardName.replaceAll("\\s", "").toLowerCase();
        cardName = cardName.replaceAll("'", "");

        switch (cardName) {
            case "ab":
            case "agingb":
            case "agingbattleship":
                return new AgingBattleship();

            case "barterw":
            case "barterworld":
                return new BarterWorld();

            case "bbarge":
            case "battlebarge":
                return new BattleBarge();

            case "bb":
            case "bblob":
            case "battleb":
            case "battleblob":
                return new BattleBlob();

            case "bbo":
            case "bbot":
            case "battlebot":
                return new BattleBot();

            case "bcr":
            case "bcruiser":
            case "battlecruiser":
                return new Battlecruiser();

            case "biof":
            case "bioformer":
                return new Bioformer();

            case "bm":
            case "bmech":
            case "battlem":
            case "battlemech":
                return new BattleMech();

            case "bp":
            case "bpod":
            case "battlepod":
                return new BattlePod();

            case "bsc":
            case "bscreecher":
            case "battlescreecher":
                return new BattleScreecher();

            case "bs":
            case "bstation":
            case "battles":
            case "battlestation":
                return new BattleStation();

            case "bh":
            case "blackhole":
                return new BlackHole();

            case "bca":
            case "bcarrier":
            case "blobcarrier":
                return new BlobCarrier();

            case "bd":
            case "bdestroyer":
            case "blobdestroyer":
                return new BlobDestroyer();

            case "bf":
            case "bfighter":
            case "blobf":
            case "blobfighter":
                return new BlobFighter();

            case "bwh":
            case "bwheel":
            case "blobwheel":
                return new BlobWheel();

            case "bw":
            case "bworld":
            case "blobw":
            case "blobworld":
                return new BlobWorld();

            case "bom":
            case "bombardment":
                return new Bombardment();

            case "bfort":
            case "borderfort":
                return new BorderFort();

            case "brw":
            case "brainw":
            case "brainworld":
                return new BrainWorld();

            case "brs":
            case "breedings":
            case "breedingsite":
                return new BreedingSite();

            case "cw":
            case "capitolw":
            case "capitolworld":
                return new CapitolWorld();

            case "cl":
            case "cargolaunch":
                return new CargoLaunch();

            case "cp":
            case "cargop":
            case "cargopod":
                return new CargoPod();

            case "co":
            case "centraloffice":
                return new CentralOffice();

            case "ces":
            case "centrals":
            case "cstation":
            case "centralstation":
                return new CentralStation();

            case "css":
            case "colonyseeds":
            case "colonyseedship":
                return new ColonySeedShip();

            case "comet":
                return new Comet();

            case "cc":
            case "ccenter":
            case "commandc":
            case "commandcenter":
                return new CommandCenter();

            case "cs":
            case "commands":
            case "commandship":
                return new CommandShip();

            case "ch":
            case "constructionhauler":
                return new ConstructionHauler();

            case "cb":
            case "convoyb":
            case "convoybot":
                return new ConvoyBot();

            case "cor":
            case "corvette":
                return new Corvette();

            case "cf":
            case "customsfrigate":
                return new CustomsFrigate();

            case "cu":
            case "cutter":
                return new Cutter();

            case "dw":
            case "deathw":
            case "deathworld":
                return new DeathWorld();

            case "db":
            case "dbot":
            case "defenseb":
            case "defensebot":
                return new DefenseBot();

            case "dc":
            case "dcenter":
            case "defensecenter":
                return new DefenseCenter();

            case "dn":
            case "dreadnaught":
                return new Dreadnaught();

            case "ey":
            case "embassyyacht":
                return new EmbassyYacht();

            case "ed":
            case "emperorsdreadnaught":
                return new EmperorsDreadnaught();

            case "e":
            case "explorer":
                return new Explorer();

            case "fw":
            case "factoryw":
            case "factoryworld":
                return new FactoryWorld();

            case "fa":
            case "falcon":
                return new Falcon();

            case "fsy":
            case "fshipyard":
            case "federationshipyard":
                return new FederationShipyard();

            case "fs":
            case "fshuttle":
            case "federationshuttle":
                return new FederationShuttle();

            case "fb":
            case "fbase":
            case "fighterbase":
                return new FighterBase();

            case "fls":
            case "flags":
            case "flagship":
                return new Flagship();

            case "fhq":
            case "fleethq":
                return new FleetHQ();

            case "fo":
            case "fortressoblivion":
                return new FortressOblivion();

            case "fr":
            case "freighter":
                return new Freighter();

            case "ff":
            case "fferry":
            case "frontierferry":
                return new FrontierFerry();

            case "frs":
            case "frontiers":
            case "fstation":
            case "frontierstation":
                return new FrontierStation();

            case "gs":
            case "galacticsummit":
                return new GalacticSummit();

            case "gus":
            case "gship":
            case "gunship":
                return new Gunship();

            case "hc":
            case "heavyc":
            case "heavycruiser":
                return new HeavyCruiser();

            case "if":
            case "ifighter":
            case "imperialf":
            case "imperialfighter":
                return new ImperialFighter();

            case "ifr":
            case "ifrigate":
            case "imperialfrigate":
                return new ImperialFrigate();

            case "ip":
            case "ipalace":
            case "imperialpalace":
                return new ImperialPalace();

            case "it":
            case "itrader":
            case "imperialtrader":
            case "imperialt":
                return new ImperialTrader();

            case "jy":
            case "junkyard":
                return new Junkyard();

            case "la":
            case "lancer":
                return new Lancer();

            case "le":
            case "leviathan":
                return new Leviathan();

            case "lc":
            case "loyalc":
            case "loyalcolony":
                return new LoyalColony();

            case "mab":
            case "machineb":
            case "machinebase":
                return new MachineBase();

            case "mechc":
            case "mcruiser":
            case "mechcruiser":
                return new MechCruiser();

            case "mw":
            case "mworld":
            case "mechw":
            case "mechworld":
                return new MechWorld();

            case "mh":
            case "megahauler":
                return new Megahauler();

            case "mem":
            case "megam":
            case "megamech":
                return new MegaMech();

            case "mc":
            case "merccruiser":
                return new MercCruiser();

            case "mim":
            case "miningm":
            case "miningmech":
                return new MiningMech();

            case "mb":
            case "missileb":
            case "missilebot":
                return new MissileBot();

            case "mm":
            case "missilem":
            case "missilemech":
                return new MissileMech();

            case "mow":
            case "moonw":
            case "moonworm":
            case "moonwurm":
                return new Moonwurm();

            case "ms":
            case "mothers":
            case "mothership":
                return new Mothership();

            case "ob":
            case "obliterator":
                return new Obliterator();

            case "op":
            case "orbitalplatform":
                return new OrbitalPlatform();

            case "pa":
            case "parasite":
                return new Parasite();

            case "pb":
            case "patrolb":
            case "patrolbot":
                return new PatrolBot();

            case "pc":
            case "pcutter":
            case "patrolcutter":
                return new PatrolCutter();

            case "pm":
            case "patrolm":
            case "patrolmech":
                return new PatrolMech();

            case "pk":
            case "peacekeeper":
                return new Peacekeeper();

            case "pv":
            case "plasmavent":
                return new PlasmaVent();

            case "poc":
            case "portofcall":
                return new PortOfCall();

            case "pr":
            case "predator":
                return new Predator();

            case "qu":
            case "quasaar":
                return new Quasar();

            case "ram":
                return new Ram();

            case "rav":
            case "ravager":
                return new Ravager();

            case "rs":
            case "recyclings":
            case "recyclingstation":
                return new RecyclingStation();

            case "rb":
            case "repairb":
            case "repairbot":
                return new RepairBot();

            case "rr":
            case "royalredoubt":
                return new RoyalRedoubt();

            case "s":
            case "scout":
                return new Scout();

            case "sos":
            case "solars":
            case "sskiff":
            case "solarskiff":
                return new SolarSkiff();

            case "ss":
            case "spaces":
            case "spacestation":
                return new SpaceStation();

            case "sp":
            case "spikep":
            case "spikepod":
                return new SpikePod();

            case "sba":
            case "sbarge":
            case "starbarge":
                return new StarBarge();

            case "so":
            case "somega":
            case "starbaseomega":
                return new StarbaseOmega();

            case "sf":
            case "sfortress":
            case "starfortress":
                return new StarFortress();

            case "sm":
            case "smarket":
            case "starmarket":
                return new Starmarket();

            case "sn":
            case "stealthneedle":
                return new StealthNeedle();

            case "st":
            case "stealtht":
            case "stower":
            case "stealthtower":
                return new StealthTower();

            case "sr":
            case "stellarreef":
                return new StellarReef();

            case "ssi":
            case "storagesilo":
                return new StorageSilo();

            case "sun":
            case "supernova":
                return new Supernova();

            case "sb":
            case "supplyb":
            case "supplybot":
                return new SupplyBot();

            case "sd":
            case "sdepot":
            case "supplydepot":
                return new SupplyDepot();

            case "sus":
            case "surveys":
            case "surveyship":
                return new SurveyShip();

            case "sw":
            case "swarmer":
                return new Swarmer();

            case "ark":
            case "theark":
                return new TheArk();

            case "hive":
            case "thehive":
                return new TheHive();

            case "in":
            case "incinerator":
            case "theincinerator":
                return new TheIncinerator();

            case "or":
            case "oracle":
            case "theoracle":
                return new TheOracle();

            case "wr":
            case "thew":
            case "wrecker":
            case "thewrecker":
                return new TheWrecker();

            case "tb":
            case "tbot":
            case "tradebot":
                return new TradeBot();

            case "te":
            case "tescort":
            case "tradeescort":
                return new TradeEscort();

            case "th":
            case "thauler":
            case "tradehauler":
                return new TradeHauler();

            case "tm":
            case "tmission":
            case "trademission":
                return new TradeMission();

            case "tp":
            case "tpod":
            case "tradepod":
                return new TradePod();

            case "tr":
            case "traft":
            case "traderaft":
                return new TradeRaft();

            case "tw":
            case "twheel":
            case "tradewheel":
                return new TradeWheel();

            case "trp":
            case "tpost":
            case "tradingpost":
                return new TradingPost();

            case "v":
            case "viper":
                return new Viper();

            case "wb":
            case "warningbeacon":
                return new WarningBeacon();

            case "wj":
            case "warpjump":
                return new WarpJump();

            case "ww":
            case "warw":
            case "warworld":
                return new WarWorld();

            default:
                return null;
        }
    }

    private List<Hero> getHeroesFromHeroNames(String heroNames) {
        List<Hero> heroes = new ArrayList<>();

        String[] heroNameArray = heroNames.split(",");

        for (String heroName : heroNameArray) {
            Hero hero = getHeroFromName(heroName);
            if (hero == null) {
                System.out.println("Hero not found for: " + heroName);
            } else {
                heroes.add(hero);
            }
        }

        return heroes;
    }

    public Hero getHeroFromName(String heroName) {
        heroName = heroName.replaceAll("\\s", "").toLowerCase();

        switch (heroName) {
            case "ar":
            case "admiralrasmussen":
                return new AdmiralRasmussen();
            case "bo":
            case "boverlord":
            case "bloboverlord":
                return new BlobOverlord();
            case "ct":
            case "ceotorres":
                return new CeoTorres();
            case "cc":
            case "cunningcaptain":
                return new CunningCaptain();
            case "hpl":
            case "highpriestlyle":
                return new HighPriestLyle();
            case "rp":
            case "rampilot":
                return new RamPilot();
            case "sod":
            case "specialopsdirector":
                return new SpecialOpsDirector();
            case "we":
            case "warelder":
                return new WarElder();
            default:
                return null;
        }
    }
}
