package starrealmssimulator.model;

public class GameState {

    //game info

    public int turn;

    public boolean currentPlayer;

    public boolean includeYearOnePromos;

    public boolean includeBasesAndBattleships;

    public boolean includeGambits;

    public String tradeRow;


    //player info

    public String bot = "";

    public int authority = 50;

    public String hand = "";

    public String deck = "";

    public String discard = "";

    public String basesInPlay = "";

    public String gambits = "";


    //opponent info

    public String opponentBot = "";

    public int opponentAuthority = 50;

    public String opponentHandAndDeck = "";

    public String opponentDiscard = "";

    public String opponentBasesInPlay = "";

    public String opponentGambits = "";

    @Override
    public String toString() {
        return
                "--game info--" +
                "\nturn: " + turn +
                "\ncurrentPlayer: " + currentPlayer +
                "\nincludeYearOnePromos: " + includeYearOnePromos +
                "\nincludeBasesAndBattleships: " + includeBasesAndBattleships +
                "\nincludeGambits: " + includeGambits +
                "\ntradeRow: '" + tradeRow + '\'' +

                "\n\n--player info--" +
                "\nbot: '" + bot + '\'' +
                "\nauthority: '" + authority + '\'' +
                "\nhand: '" + hand + '\'' +
                "\ndeck: '" + deck + '\'' +
                "\ndiscard: '" + discard + '\'' +
                "\nbasesInPlay: '" + basesInPlay + '\'' +
                "\ngambits: '" + gambits + '\'' +

                "\n\n--opponent info--" +
                "\nopponentBot: '" + opponentBot + '\'' +
                "\nopponentAuthority: '" + opponentAuthority + '\'' +
                "\nopponentHandAndDeck: '" + opponentHandAndDeck + '\'' +
                "\nopponentDiscard: '" + opponentDiscard + '\'' +
                "\nopponentBasesInPlay: '" + opponentBasesInPlay + '\'' +
                "\nopponentGambits: '" + opponentGambits + '\'';
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public boolean isCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isIncludeYearOnePromos() {
        return includeYearOnePromos;
    }

    public void setIncludeYearOnePromos(boolean includeYearOnePromos) {
        this.includeYearOnePromos = includeYearOnePromos;
    }

    public boolean isIncludeBasesAndBattleships() {
        return includeBasesAndBattleships;
    }

    public void setIncludeBasesAndBattleships(boolean includeBasesAndBattleships) {
        this.includeBasesAndBattleships = includeBasesAndBattleships;
    }

    public boolean isIncludeGambits() {
        return includeGambits;
    }

    public void setIncludeGambits(boolean includeGambits) {
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

    public String getOpponentGambits() {
        return opponentGambits;
    }

    public void setOpponentGambits(String opponentGambits) {
        this.opponentGambits = opponentGambits;
    }
}
