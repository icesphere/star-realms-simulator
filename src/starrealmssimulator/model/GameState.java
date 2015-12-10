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

    public String bot = "";

    public int authority;

    public String hand = "";

    public String deck = "";

    public String discard = "";

    public String basesInPlay = "";

    public String gambits = "";


    //opponent info

    public String opponentBot = "";

    public int opponentAuthority;

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
}
