package starrealmssimulator.bots;

import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;
import starrealmssimulator.model.Gambit;

public class SimulateToEndBot extends Bot {
    private String playerName;

    @Override
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public int getBuyCardScore(Card card) {
        //don't buy anything
        return 0;
    }

    @Override
    public int getScrapForBenefitScore(Card card) {
        //always scrap for benefit
        return 1;
    }

    @Override
    public int getUseGambitScore(Gambit gambit) {
        //use all gambits
        return 1;
    }
}
