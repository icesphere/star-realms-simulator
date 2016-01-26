package starrealmssimulator.model;

import java.util.Random;

public class GameState {

    //game info

    public int turn = 1;

    public String currentPlayer = "Y";

    public String includeBaseSet = "Y";

    public String includeColonyWars = "N";

    public String includeYearOnePromos = "N";

    public String includeCrisisBasesAndBattleships = "N";

    public String includeCrisisEvents = "N";

    public String includeCrisisFleetsAndFortresses = "N";

    public String includeCrisisHeroes = "N";

    public String includeGambits = "N";

    public String tradeRow;

    public String tradeRowCardsScrapped = "";


    //player info

    public String bot = "";

    public int authority = 50;

    public String hand = "";

    public String deck = "";

    public String discard = "";

    public String basesInPlay = "";

    public String gambits = "";

    public String heroesInPlay = "";

    public int shuffles = 0;

    //player info for partial turn

    public String played = "";

    public String inPlay = "";

    public int combat;

    public int trade;

    public boolean nextShipToTopOfDeck;

    public boolean nextShipOrBaseToTopOfDeck;

    public boolean nextShipOrBaseToHand;

    public boolean nextBaseToHand;

    public boolean allShipsAddOneCombat;

    public boolean allFactionsAllied;

    public boolean preventFirstDamage;

    //opponent info

    public String opponentBot = "";

    public int opponentAuthority = 50;

    public String opponentHandAndDeck = "";

    public String opponentDiscard = "";

    public String opponentBasesInPlay = "";

    public String opponentHeroesInPlay = "";

    public String opponentGambits = "";

    public int opponentShuffles = 0;

    @Override
    public String toString() {
        return
                "--game info--" +
                "\nturn: " + turn +
                "\ncurrentPlayer: " + currentPlayer +
                "\nincludeBaseSet: " + includeBaseSet +
                "\nincludeColonyWars: " + includeColonyWars +
                "\nincludeYearOnePromos: " + includeYearOnePromos +
                "\nincludeCrisisBasesAndBattleships: " + includeCrisisBasesAndBattleships +
                "\nincludeCrisisEvents: " + includeCrisisEvents +
                "\nincludeCrisisFleetsAndFortresses: " + includeCrisisFleetsAndFortresses +
                "\nincludeGambits: " + includeGambits +
                "\ntradeRow: '" + tradeRow + '\'' +

                "\n\n--player info--" +
                "\nbot: '" + bot + '\'' +
                "\nauthority: '" + authority + '\'' +
                "\nhand: '" + hand + '\'' +
                "\ndeck: '" + deck + '\'' +
                "\ndiscard: '" + discard + '\'' +
                "\nbasesInPlay: '" + basesInPlay + '\'' +
                "\nheroesInPlay: '" + heroesInPlay + '\'' +
                "\ngambits: '" + gambits + '\'' +

                "\n\n--opponent info--" +
                "\nopponentBot: '" + opponentBot + '\'' +
                "\nopponentAuthority: '" + opponentAuthority + '\'' +
                "\nopponentHandAndDeck: '" + opponentHandAndDeck + '\'' +
                "\nopponentDiscard: '" + opponentDiscard + '\'' +
                "\nopponentBasesInPlay: '" + opponentBasesInPlay + '\'' +
                "\nopponentHeroesInPlay: '" + opponentHeroesInPlay + '\'' +
                "\nopponentGambits: '" + opponentGambits + '\'';
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getIncludeBaseSet() {
        return includeBaseSet;
    }

    public void setIncludeBaseSet(String includeBaseSet) {
        this.includeBaseSet = includeBaseSet;
    }

    public String getIncludeColonyWars() {
        return includeColonyWars;
    }

    public void setIncludeColonyWars(String includeColonyWars) {
        this.includeColonyWars = includeColonyWars;
    }

    public String getIncludeYearOnePromos() {
        return includeYearOnePromos;
    }

    public void setIncludeYearOnePromos(String includeYearOnePromos) {
        this.includeYearOnePromos = includeYearOnePromos;
    }

    public String getIncludeCrisisBasesAndBattleships() {
        return includeCrisisBasesAndBattleships;
    }

    public void setIncludeCrisisBasesAndBattleships(String includeCrisisBasesAndBattleships) {
        this.includeCrisisBasesAndBattleships = includeCrisisBasesAndBattleships;
    }

    public String getIncludeCrisisEvents() {
        return includeCrisisEvents;
    }

    public void setIncludeCrisisEvents(String includeCrisisEvents) {
        this.includeCrisisEvents = includeCrisisEvents;
    }

    public String getIncludeCrisisFleetsAndFortresses() {
        return includeCrisisFleetsAndFortresses;
    }

    public void setIncludeCrisisFleetsAndFortresses(String includeCrisisFleetsAndFortresses) {
        this.includeCrisisFleetsAndFortresses = includeCrisisFleetsAndFortresses;
    }

    public String getIncludeCrisisHeroes() {
        return includeCrisisHeroes;
    }

    public void setIncludeCrisisHeroes(String includeCrisisHeroes) {
        this.includeCrisisHeroes = includeCrisisHeroes;
    }

    public String getIncludeGambits() {
        return includeGambits;
    }

    public void setIncludeGambits(String includeGambits) {
        this.includeGambits = includeGambits;
    }

    public String getTradeRow() {
        return tradeRow;
    }

    public void setTradeRow(String tradeRow) {
        this.tradeRow = tradeRow;
    }

    public String getBot() {
        return bot;
    }

    public void setBot(String bot) {
        this.bot = bot;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getHand() {
        return hand;
    }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public String getDiscard() {
        return discard;
    }

    public void setDiscard(String discard) {
        this.discard = discard;
    }

    public String getBasesInPlay() {
        return basesInPlay;
    }

    public void setBasesInPlay(String basesInPlay) {
        this.basesInPlay = basesInPlay;
    }

    public String getHeroesInPlay() {
        return heroesInPlay;
    }

    public void setHeroesInPlay(String heroesInPlay) {
        this.heroesInPlay = heroesInPlay;
    }

    public String getGambits() {
        return gambits;
    }

    public void setGambits(String gambits) {
        this.gambits = gambits;
    }

    public String getOpponentBot() {
        return opponentBot;
    }

    public void setOpponentBot(String opponentBot) {
        this.opponentBot = opponentBot;
    }

    public int getOpponentAuthority() {
        return opponentAuthority;
    }

    public void setOpponentAuthority(int opponentAuthority) {
        this.opponentAuthority = opponentAuthority;
    }

    public String getOpponentHandAndDeck() {
        return opponentHandAndDeck;
    }

    public void setOpponentHandAndDeck(String opponentHandAndDeck) {
        this.opponentHandAndDeck = opponentHandAndDeck;
    }

    public String getOpponentDiscard() {
        return opponentDiscard;
    }

    public void setOpponentDiscard(String opponentDiscard) {
        this.opponentDiscard = opponentDiscard;
    }

    public String getOpponentBasesInPlay() {
        return opponentBasesInPlay;
    }

    public void setOpponentBasesInPlay(String opponentBasesInPlay) {
        this.opponentBasesInPlay = opponentBasesInPlay;
    }

    public String getOpponentHeroesInPlay() {
        return opponentHeroesInPlay;
    }

    public void setOpponentHeroesInPlay(String opponentHeroesInPlay) {
        this.opponentHeroesInPlay = opponentHeroesInPlay;
    }

    public String getOpponentGambits() {
        return opponentGambits;
    }

    public void setOpponentGambits(String opponentGambits) {
        this.opponentGambits = opponentGambits;
    }

    public boolean getRandomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public boolean determineBoolean(String stringValue) {
        if ("Y".equalsIgnoreCase(stringValue)) {
            return true;
        } else {
            return stringValue.equals("R") && getRandomBoolean();
        }
    }

    public boolean determineCurrentPlayer() {
        return determineBoolean(currentPlayer);
    }

    public boolean determineIncludeBaseSet() {
        return determineBoolean(includeBaseSet);
    }

    public boolean determineIncludeColonyWars() {
        return determineBoolean(includeColonyWars);
    }

    public boolean determineIncludeYearOnePromos() {
        return determineBoolean(includeYearOnePromos);
    }

    public boolean determineIncludeCrisisBasesAndBattleships() {
        return determineBoolean(includeCrisisBasesAndBattleships);
    }

    public boolean determineIncludeCrisisEvents() {
        return determineBoolean(includeCrisisEvents);
    }

    public boolean determineIncludeCrisisFleetsAndFortresses() {
        return determineBoolean(includeCrisisFleetsAndFortresses);
    }

    public boolean determineIncludeCrisisHeroes() {
        return determineBoolean(includeCrisisHeroes);
    }

    public boolean determineIncludeGambits() {
        return determineBoolean(includeGambits);
    }

    public int getShuffles() {
        return shuffles;
    }

    public void setShuffles(int shuffles) {
        this.shuffles = shuffles;
    }

    public int getOpponentShuffles() {
        return opponentShuffles;
    }

    public void setOpponentShuffles(int opponentShuffles) {
        this.opponentShuffles = opponentShuffles;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getInPlay() {
        return inPlay;
    }

    public void setInPlay(String inPlay) {
        this.inPlay = inPlay;
    }
}
