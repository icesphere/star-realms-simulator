package starrealmssimulator.model;

public class GameState {
    public int turn;

    public boolean currentPlayer = true;

    public boolean includeYearOnePromos;

    public boolean includeBasesAndBattleships;

    public boolean includeGambits;

    public String tradeRow;

    public String hand;

    public String deck;

    public String discard;

    public String basesInPlay;

    public String gambits;

    public String opponentHandAndDeck;

    public String opponentDiscard;

    public String opponentBasesInPlay;

    public String opponentGambits;
}
