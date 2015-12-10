package starrealmssimulator.model;

public class GameState {

    //game info

    public int turn;

    public boolean currentPlayer = true;

    public boolean includeYearOnePromos;

    public boolean includeBasesAndBattleships;

    public boolean includeGambits;

    public String tradeRow;


    //player info

    public String bot;

    public String hand;

    public String deck;

    public String discard;

    public String basesInPlay;

    public String gambits;


    //opponent info

    public String opponentBot;

    public String opponentHandAndDeck;

    public String opponentDiscard;

    public String opponentBasesInPlay;

    public String opponentGambits;
}
