package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class TradePod extends Ship implements AlliableCard
{
    public TradePod()
    {
        name = "Trade Pod";
        faction = Faction.BLOB;
        cost = 2;
        set = CardSet.CORE;
        text = "Add 3 Trade; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}
