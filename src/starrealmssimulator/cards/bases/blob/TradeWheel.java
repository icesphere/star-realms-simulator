package starrealmssimulator.cards.bases.blob;

import starrealmssimulator.model.*;

public class TradeWheel extends Base implements AlliableCard
{
    public TradeWheel()
    {
        name = "Trade Wheel";
        faction = Faction.BLOB;
        cost = 3;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        shield = 5;
        text = "Add 1 Trade; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(1);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(2);
    }
}
