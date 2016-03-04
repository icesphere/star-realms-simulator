package starrealmssimulator.service;

import starrealmssimulator.bots.*;
import starrealmssimulator.bots.json.JsonBot;
import starrealmssimulator.bots.strategies.VelocityStrategy;
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

        int firstPlayerAuthDiff = 0;
        int secondPlayerAuthDiff = 0;
        
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
                firstPlayerAuthDiff += game.getPlayers().get(0).getAuthority() - game.getPlayers().get(1).getAuthority();
            }
            else
            {
                secondPlayerAuthDiff += game.getPlayers().get(1).getAuthority() - game.getPlayers().get(0).getAuthority();
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
        String botAuthDif = Integer.toString(firstPlayerAuthDiff / gamesSimulated);
        String opponentAuthDif = Integer.toString(secondPlayerAuthDiff / gamesSimulated);

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
            System.out.println(botName + " v " + opponentName + ": " + botPercent + " - " + opponentPercent + " (Avg # turns: " + avgTurns + ") Avg Auth Diffs: " + botAuthDif + " / " + opponentAuthDif);
        }

        return simulationInfo;
    }

    public Game simulateGame(List<Bot> bots) {
        Game game = new Game();

        List<Card> deck = new ArrayList<>();
        deck.addAll(getBaseSetDeck());
        deck.addAll(getYear1PromoCards());
        deck.addAll(getCrisisBasesAndBattleships());
        //deck.addAll(getCrisisEvents());

        game.setDeck(deck);

        game.setExplorer(new Explorer());

        Collections.shuffle(game.getDeck());

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

        game.addCardsToTradeRow(5);

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

    public List<Card> getStartingCards() {
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

    public List<Card> getBaseSetDeck() {
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

    public List<Card> getYear1PromoCards() {
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

        List<String> botFiles = new ArrayList<>();

        botFiles.add("HareBot.json");
        botFiles.add("AttackBot.json");
        botFiles.add("DefenseBot.json");
        botFiles.add("VelocityBot.json");
        botFiles.add("TortoiseBot.json");
        botFiles.add("ExpensiveBot.json");

        botFiles.add("SmartBot.json");

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

        service.simulateTwoBots(new VelocityBot(), new HareBot());

        //service.simulateAllAgainstAll(bots);

        //service.simulateAllAgainstAllJsonBots(botFiles);

        //service.simulateOneAgainstAllBotsJsonBots("HareBot.json", botFiles);

        //service.simulateOneAgainstAllBots(new HareBot(), bots);

        //service.simulateGameToEnd(new GameStateGame(service.getGameState(), service), 1000);
    }

    private GameState getGameState() {
        GameState gameState = new GameState();

        gameState.includeCrisisBasesAndBattleships = "Y";
        gameState.includeYearOnePromos = "Y";
        gameState.turn = 20;

        gameState.tradeRow = "junkyard, supbot, flags, poc, bwheel";

        gameState.bot = "";
        gameState.authority = 41;
        gameState.hand = "mismec, s*2, mercru, tpod";
        gameState.deck = "bpod, blofig, tpod, comshi, fedshu, tpost, supbot, recsta, s*5, v*2";
        gameState.discard = "";
        gameState.basesInPlay = "fleethq, smarket";

        gameState.opponentBot = "defense";
        gameState.opponentAuthority = 20;
        gameState.opponentDiscard = "tescort, missileb, v, s, tpost";
        gameState.opponentHandAndDeck = "batbar, ram, twheel, embyac, fshuttle, missileb, stenee, spaces, s*5, v";

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

    public GameState getGameStateFromGame(Game game) {
        Player player = game.getCurrentPlayer();
        Player opponent = player.getOpponent();

        GameState gameState = new GameState();

        gameState.turn = game.getTurn();
        gameState.currentPlayer = "Y";
        gameState.includeBaseSet = game.getCardSets().contains(CardSet.CORE) ? "Y" : "N";
        gameState.includeColonyWars = game.getCardSets().contains(CardSet.COLONY_WARS) ? "Y" : "N";
        gameState.includeYearOnePromos = game.getCardSets().contains(CardSet.PROMO_YEAR_1) ? "Y" : "N";
        gameState.includeCrisisBasesAndBattleships = game.getCardSets().contains(CardSet.CRISIS_BASES_AND_BATTLESHIPS) ? "Y" : "N";
        gameState.includeCrisisEvents = game.getCardSets().contains(CardSet.CRISIS_BASES_AND_BATTLESHIPS) ? "Y" : "N";
        gameState.includeCrisisFleetsAndFortresses = game.getCardSets().contains(CardSet.CRISIS_FLEETS_AND_FORTRESSES) ? "Y" : "N";
        gameState.includeCrisisHeroes = game.getCardSets().contains(CardSet.CRISIS_HEROES) ? "Y" : "N";
        gameState.includeGambits = game.getCardSets().contains(CardSet.GAMBITS) ? "Y" : "N";

        gameState.tradeRow = game.getCardsAsString(game.getTradeRow());

        gameState.tradeRowCardsScrapped = game.getCardsAsString(game.getTradeRowCardsScrapped());

        gameState.bot = player.getClass().getSimpleName();
        gameState.authority = player.getAuthority();
        gameState.shuffles = player.getShuffles();
        gameState.hand = game.getCardsAsString(player.getHand());
        gameState.deck = game.getCardsAsString(player.getDeck());
        gameState.discard = game.getCardsAsString(player.getDiscard());
        gameState.basesInPlay = game.getCardsAsString(player.getBases());
        gameState.heroesInPlay = game.getCardsAsString(player.getHeroes());
        gameState.gambits = game.getCardsAsString(player.getGambits());

        //partial turn
        gameState.played = game.getCardsAsString(player.getPlayed());
        gameState.inPlay = game.getCardsAsString(player.getInPlay());
        gameState.combat = player.getCombat();
        gameState.trade = player.getTrade();
        gameState.nextShipToTopOfDeck = player.isNextShipToTopOfDeck();
        gameState.nextShipOrBaseToTopOfDeck = player.isNextShipOrBaseToTopOfDeck();
        gameState.nextShipOrBaseToHand = player.isNextShipOrBaseToHand();
        gameState.nextBaseToHand = player.isNextBaseToHand();
        gameState.allShipsAddOneCombat = player.isAllShipsAddOneCombat();
        gameState.allFactionsAllied = player.isAllFactionsAllied();
        gameState.preventFirstDamage = player.isPreventFirstDamage();

        gameState.opponentBot = opponent.getClass().getSimpleName();
        gameState.opponentAuthority = opponent.getAuthority();
        gameState.opponentShuffles = opponent.getShuffles();
        gameState.opponentHandAndDeck = game.getCardsAsString(opponent.getHandAndDeck());
        gameState.opponentDiscard = game.getCardsAsString(player.getDiscard());
        gameState.opponentBasesInPlay = game.getCardsAsString(player.getBases());
        gameState.opponentHeroesInPlay = game.getCardsAsString(player.getHeroes());
        gameState.opponentGambits = game.getCardsAsString(player.getGambits());

        return gameState;
    }

    public Map<String, Float> simulateBestBot(GameState gameState, int timesToSimulate) {
        Map<String, Float> botResults = new LinkedHashMap<>();

        List<String> bots = new ArrayList<>();

        bots.add("Attack Bot");
        bots.add("Defense Bot");
        bots.add("Economy Bot");
        bots.add("End Game Bot");
        bots.add("Expensive Bot");
        bots.add("Hare Bot");
        bots.add("Random Bot");
        bots.add("Tortoise Bot");
        bots.add("Velocity Bot");

        for (String bot : bots) {
            gameState.bot = bot;

            GameStateHolder gameStateHolder = new GameStateGame(gameState, this);

            SimulationResults results = simulateGameToEnd(gameStateHolder, timesToSimulate, null);

            botResults.put(bot, results.getWinPercentage());
        }

        return botResults;
    }

    public Map<Card, CardToBuySimulationResults> simulateBestCardToBuy(GameState gameState, int timesToSimulate) {
        return simulateBestCardToBuy(new GameStateGame(gameState, this), timesToSimulate);
    }

    public Map<Card, CardToBuySimulationResults> simulateBestCardToBuy(GameStateHolder gameStateHolder, int timesToSimulate) {
        Map<Card, CardToBuySimulationResults> resultsByCard = new LinkedHashMap<>();

        List<Card> cards = gameStateHolder.getTradeRow();
        cards.add(new Explorer());
        cards.add(new DoNotBuyCard());

        boolean playerGoesFirst = gameStateHolder.getGameState().determineCurrentPlayer();

        for (Card card : cards) {
            CardToBuySimulationResults cardToBuySimulationResults = new CardToBuySimulationResults();

            SimulationResults results = simulateGameToEnd(gameStateHolder, timesToSimulate, card);

            float ableToBuyFirstTurnPercentage = ((float) results.getTotalGamesCounted() / timesToSimulate) * 100;

            cardToBuySimulationResults.setAbleToBuyFirstTurnPercentage(ableToBuyFirstTurnPercentage);

            if (playerGoesFirst || results.getWinPercentage() < .01) {
                cardToBuySimulationResults.setWinPercentage(results.getWinPercentage());
            } else {
                cardToBuySimulationResults.setWinPercentage(100 - results.getWinPercentage());
            }

            resultsByCard.put(card, cardToBuySimulationResults);
        }

        return resultsByCard;
    }

    public SimulationResults simulateGameToEnd(GameStateHolder gameStateHolder, int timesToSimulate) {
        return simulateGameToEnd(gameStateHolder, timesToSimulate, null);
    }

    public SimulationResults simulateGameToEnd(GameStateHolder gameStateHolder, int timesToSimulate, Card cardToBuyThisTurn) {
        SimulationResults results = new SimulationResults();

        boolean createdWinGameLog = false;
        boolean createdLossGameLog = false;

        int totalGamesCounted = 0;

        List<Game> games = new ArrayList<>(timesToSimulate);

        Map<String, Map<Integer, Integer>> averageAuthorityByPlayerByTurn = new HashMap<>();

        float turnTotal = 0;

        int wins = 0;

        Player player = gameStateHolder.getPlayerInstance();
        player.setPlayerName(player.getClass().getSimpleName() + "(Player)");
        averageAuthorityByPlayerByTurn.put(player.getPlayerName(), new HashMap<>());

        Player opponent = gameStateHolder.getOpponentInstance();
        opponent.setPlayerName(opponent.getClass().getSimpleName() + "(Opponent)");
        averageAuthorityByPlayerByTurn.put(opponent.getPlayerName(), new HashMap<>());

        for (int i = 0; i < timesToSimulate; i++) {
            boolean createGameLog = !createdWinGameLog || !createdLossGameLog;
            Game game = simulateGameToEnd(gameStateHolder, createGameLog, cardToBuyThisTurn);
            if (cardToBuyThisTurn != null && !game.getWinner().isBoughtSpecifiedCardOnFirstTurn() && !game.getLoser().isBoughtSpecifiedCardOnFirstTurn()) {
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

        float winPercentage;
        if (totalGamesCounted > 0) {
            winPercentage = ((float) wins / totalGamesCounted) * 100;
        } else {
            winPercentage = 0;
        }

        if (EXTRA_LOGGING) {
            System.out.println("Player wins: " + f.format(winPercentage) + "%");
        }

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

    public Game simulateGameToEnd(GameStateHolder gameStateHolder, boolean createGameLog, Card cardToBuyThisTurn) {

        Game game = gameStateHolder.getGameInstance();

        boolean playerIsCurrentPlayer = gameStateHolder.playerIsCurrentPlayerForThisGameInstance();

        game.setCreateGameLog(createGameLog);

        Player player;

        if (playerIsCurrentPlayer) {
            player = game.getCurrentPlayer();
            if (player instanceof SimulatorBot && cardToBuyThisTurn != null) {
                ((SimulatorBot) player).setStrategy(new VelocityStrategy());
            }
        } else {
            player = game.getCurrentPlayer().getOpponent();
        }

        Player opponent = player.getOpponent();

        player.setPlayerName(player.getClass().getSimpleName() + "(Player)");
        opponent.setPlayerName(opponent.getClass().getSimpleName() + "(Opponent)");

        if (playerIsCurrentPlayer) {
            player.setCardToBuyThisTurn(cardToBuyThisTurn);
        } else {
            opponent.setCardToBuyThisTurn(cardToBuyThisTurn);
        }

        if (cardToBuyThisTurn != null) {
            game.setTrackAuthority(false);
        } else {
            game.setupPlayerAuthorityMap();
        }

        while (!game.isGameOver()) {
            if (game.getTurn() > 200) {
                if (EXTRA_LOGGING) {
                    System.out.println("-----Game stuck, trying again-----");
                }
                return simulateGameToEnd(gameStateHolder, createGameLog, cardToBuyThisTurn);
            } else {
                if (game.isGameOver()) {
                    return game;
                }
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
            case "economy":
            case "economybot":
                return new EconomyBot();
            case "expensive":
            case "expensivebot":
                return new ExpensiveBot();
            case "hare":
            case "harebot":
                return new HareBot();
            case "random":
            case "randombot":
                return new RandomBot();
            case "simulator":
            case "simulatorbot":
                return new SimulatorBot(this);
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

    public List<Gambit> getGambitsFromGambitNames(String gambitNames) {
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
            case "bolrai":
            case "boldraid":
                return new BoldRaid();
            case "eneshi":
            case "energyshield":
                return new EnergyShield();
            case "frofle":
            case "frontierfleet":
                return new FrontierFleet();
            case "polman":
            case "politicalmaneuver":
                return new PoliticalManeuver();
            case "ristopow":
            case "risetopower":
                return new RiseToPower();
            case "salope":
            case "salvageoperation":
                return new SalvageOperation();
            case "smurun":
            case "smugglingrun":
                return new SmugglingRun();
            case "surass":
            case "surpriseassault":
                return new SurpriseAssault();
            case "unlall":
            case "unlikelyalliance":
                return new UnlikelyAlliance();
            default:
                return null;
        }
    }

    public List<Card> getCardsFromCardNames(String cardNames) {
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

    public List<Base> getBasesFromCardNames(String cardNames) {
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
            case "agibat":
            case "agingb":
            case "agingbattleship":
                return new AgingBattleship();

            case "barwor":
            case "barterw":
            case "barterworld":
                return new BarterWorld();

            case "batbar":
            case "bbarge":
            case "battlebarge":
                return new BattleBarge();

            case "batblo":
            case "bblob":
            case "battleb":
            case "battleblob":
                return new BattleBlob();

            case "batbot":
            case "bbot":
            case "battlebot":
                return new BattleBot();

            case "batcru":
            case "bcr":
            case "bcruiser":
            case "battlecruiser":
                return new Battlecruiser();

            case "biofor":
            case "biof":
            case "bioformer":
                return new Bioformer();

            case "batmec":
            case "bm":
            case "bmech":
            case "battlem":
            case "battlemech":
                return new BattleMech();

            case "batpod":
            case "bp":
            case "bpod":
            case "battlepod":
                return new BattlePod();

            case "batscr":
            case "bscreecher":
            case "battlescreecher":
                return new BattleScreecher();

            case "batsta":
            case "bs":
            case "bstation":
            case "battles":
            case "battlestation":
                return new BattleStation();

            case "blahol":
            case "blackhole":
                return new BlackHole();

            case "blocar":
            case "bcarrier":
            case "blobcarrier":
                return new BlobCarrier();

            case "blodes":
            case "bdestroyer":
            case "blobdestroyer":
                return new BlobDestroyer();

            case "blofig":
            case "bf":
            case "bfighter":
            case "blobf":
            case "blobfighter":
                return new BlobFighter();

            case "blowhe":
            case "bwheel":
            case "blobwheel":
                return new BlobWheel();

            case "blowor":
            case "bw":
            case "bworld":
            case "blobw":
            case "blobworld":
                return new BlobWorld();

            case "bom":
            case "bombardment":
                return new Bombardment();

            case "borfor":
            case "bfort":
            case "borderfort":
                return new BorderFort();

            case "brawor":
            case "brainw":
            case "brainworld":
                return new BrainWorld();

            case "bresit":
            case "breedings":
            case "breedingsite":
                return new BreedingSite();

            case "capwor":
            case "capitolw":
            case "capitolworld":
                return new CapitolWorld();

            case "carlau":
            case "cargolaunch":
                return new CargoLaunch();

            case "carpod":
            case "cargop":
            case "cargopod":
                return new CargoPod();

            case "cenoff":
            case "centraloffice":
                return new CentralOffice();

            case "censta":
            case "centrals":
            case "cstation":
            case "centralstation":
                return new CentralStation();

            case "colseeshi":
            case "colonyseeds":
            case "colonyseedship":
                return new ColonySeedShip();

            case "com":
            case "comet":
                return new Comet();

            case "comcen":
            case "ccenter":
            case "commandc":
            case "commandcenter":
                return new CommandCenter();

            case "comshi":
            case "cs":
            case "commands":
            case "commandship":
                return new CommandShip();

            case "conhau":
            case "constructionhauler":
                return new ConstructionHauler();

            case "conbot":
            case "convoyb":
            case "convoybot":
                return new ConvoyBot();

            case "cor":
            case "corvette":
                return new Corvette();

            case "cusfri":
            case "customsfrigate":
                return new CustomsFrigate();

            case "cut":
            case "cutter":
                return new Cutter();

            case "deawor":
            case "deathw":
            case "deathworld":
                return new DeathWorld();

            case "defbot":
            case "db":
            case "dbot":
            case "defenseb":
            case "defensebot":
                return new DefenseBot();

            case "defcen":
            case "dc":
            case "dcenter":
            case "defensecenter":
                return new DefenseCenter();

            case "dre":
            case "dreadnaught":
                return new Dreadnaught();

            case "embyac":
            case "embassyyacht":
                return new EmbassyYacht();

            case "empdre":
            case "emperorsdreadnaught":
                return new EmperorsDreadnaught();

            case "exp":
            case "e":
            case "explorer":
                return new Explorer();

            case "facwor":
            case "factoryw":
            case "factoryworld":
                return new FactoryWorld();

            case "fal":
            case "falcon":
                return new Falcon();

            case "fedshi":
            case "fshipyard":
            case "federationshipyard":
                return new FederationShipyard();

            case "fedshu":
            case "fs":
            case "fshuttle":
            case "federationshuttle":
                return new FederationShuttle();

            case "figbas":
            case "fighterb":
            case "fighterbase":
                return new FighterBase();

            case "fla":
            case "flags":
            case "flagship":
                return new Flagship();

            case "flehq":
            case "fhq":
            case "fleethq":
                return new FleetHQ();

            case "forobl":
            case "fortressoblivion":
                return new FortressOblivion();

            case "fre":
            case "freighter":
                return new Freighter();

            case "frofer":
            case "fferry":
            case "frontierferry":
                return new FrontierFerry();

            case "frosta":
            case "frontiers":
            case "fstation":
            case "frontierstation":
                return new FrontierStation();

            case "galsum":
            case "galacticsummit":
                return new GalacticSummit();

            case "gun":
            case "guns":
            case "gunship":
                return new Gunship();

            case "heacru":
            case "heavyc":
            case "heavycruiser":
                return new HeavyCruiser();

            case "impfig":
            case "if":
            case "ifighter":
            case "imperialf":
            case "imperialfighter":
                return new ImperialFighter();

            case "impfri":
            case "ifrigate":
            case "imperialfrigate":
                return new ImperialFrigate();

            case "imppal":
            case "ipalace":
            case "imperialpalace":
                return new ImperialPalace();

            case "imptra":
            case "itrader":
            case "imperialtrader":
            case "imperialt":
                return new ImperialTrader();

            case "jun":
            case "junkyard":
                return new Junkyard();

            case "lan":
            case "lancer":
                return new Lancer();

            case "lev":
            case "leviathan":
                return new Leviathan();

            case "loycol":
            case "loyalc":
            case "loyalcolony":
                return new LoyalColony();

            case "macbas":
            case "machineb":
            case "machinebase":
                return new MachineBase();

            case "meccru":
            case "mechc":
            case "mcruiser":
            case "mechcruiser":
                return new MechCruiser();

            case "mecwor":
            case "mw":
            case "mworld":
            case "mechw":
            case "mechworld":
                return new MechWorld();

            case "meg":
            case "megahauler":
                return new Megahauler();

            case "megmec":
            case "megam":
            case "megamech":
                return new MegaMech();

            case "mercru":
            case "mercc":
            case "merccruiser":
            case "mc":
                return new MercCruiser();

            case "minmec":
            case "miningm":
            case "miningmech":
                return new MiningMech();

            case "misbot":
            case "missileb":
            case "missilebot":
                return new MissileBot();

            case "mismec":
            case "missilem":
            case "missilemech":
                return new MissileMech();

            case "moo":
            case "moonw":
            case "moonworm":
            case "moonwurm":
                return new Moonwurm();

            case "mot":
            case "mothers":
            case "mothership":
                return new Mothership();

            case "obl":
            case "obliterator":
                return new Obliterator();

            case "orbpla":
            case "orbitalplatform":
                return new OrbitalPlatform();

            case "par":
            case "parasite":
                return new Parasite();

            case "patbot":
            case "pb":
            case "patrolb":
            case "patrolbot":
                return new PatrolBot();

            case "patcut":
            case "pcutter":
            case "patrolcutter":
                return new PatrolCutter();

            case "patmec":
            case "pm":
            case "patrolm":
            case "patrolmech":
                return new PatrolMech();

            case "pea":
            case "peacekeeper":
                return new Peacekeeper();

            case "plaven":
            case "plasmavent":
                return new PlasmaVent();

            case "porofcal":
            case "poc":
            case "portofcall":
                return new PortOfCall();

            case "pre":
            case "predator":
                return new Predator();

            case "qua":
            case "quasaar":
                return new Quasar();

            case "ram":
                return new Ram();

            case "rav":
            case "ravager":
                return new Ravager();

            case "recsta":
            case "recyclings":
            case "recyclingstation":
                return new RecyclingStation();

            case "repbot":
            case "repairb":
            case "repairbot":
                return new RepairBot();

            case "royred":
            case "royalredoubt":
                return new RoyalRedoubt();

            case "sco":
            case "s":
            case "scout":
                return new Scout();

            case "solski":
            case "solarskiff":
                return new SolarSkiff();

            case "spasta":
            case "spaces":
            case "spacestation":
                return new SpaceStation();

            case "spipod":
            case "spikep":
            case "spikepod":
                return new SpikePod();

            case "stabar":
            case "sbarge":
            case "starbarge":
                return new StarBarge();

            case "staome":
            case "somega":
            case "starbaseomega":
                return new StarbaseOmega();

            case "stafor":
            case "sfortress":
            case "starfortress":
                return new StarFortress();

            case "sta":
            case "stamar":
            case "smarket":
            case "starmarket":
                return new Starmarket();

            case "stenee":
            case "stealthneedle":
                return new StealthNeedle();

            case "stetow":
            case "stealthtower":
                return new StealthTower();

            case "steree":
            case "stellarreef":
                return new StellarReef();

            case "stosil":
            case "storagesilo":
                return new StorageSilo();

            case "sup":
            case "supernova":
                return new Supernova();

            case "supbot":
            case "sb":
            case "supplyb":
            case "supplybot":
                return new SupplyBot();

            case "supdep":
            case "sdepot":
            case "supplydepot":
                return new SupplyDepot();

            case "surshi":
            case "surveys":
            case "surveyship":
                return new SurveyShip();

            case "sw":
            case "swarmer":
                return new Swarmer();

            case "ark":
            case "theark":
                return new TheArk();

            case "thehiv":
            case "hive":
            case "thehive":
                return new TheHive();

            case "theinc":
            case "incinerator":
            case "theincinerator":
                return new TheIncinerator();

            case "theora":
            case "oracle":
            case "theoracle":
                return new TheOracle();

            case "thewre":
            case "wrecker":
            case "thewrecker":
                return new TheWrecker();

            case "trabot":
            case "tb":
            case "tbot":
            case "tradebot":
                return new TradeBot();

            case "traesc":
            case "tescort":
            case "tradeescort":
                return new TradeEscort();

            case "trahau":
            case "thauler":
            case "tradehauler":
                return new TradeHauler();

            case "tramis":
            case "tmission":
            case "trademission":
                return new TradeMission();

            case "trapod":
            case "tp":
            case "tpod":
            case "tradepod":
                return new TradePod();

            case "traraf":
            case "traft":
            case "traderaft":
                return new TradeRaft();

            case "trawhe":
            case "twheel":
            case "tradewheel":
                return new TradeWheel();

            case "trapos":
            case "tpost":
            case "tradingpost":
                return new TradingPost();

            case "vip":
            case "v":
            case "viper":
                return new Viper();

            case "warbea":
            case "warningbeacon":
                return new WarningBeacon();

            case "warjum":
            case "warpjump":
                return new WarpJump();

            case "warwor":
            case "warw":
            case "warworld":
                return new WarWorld();

            default:
                return null;
        }
    }

    public List<Hero> getHeroesFromHeroNames(String heroNames) {
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
