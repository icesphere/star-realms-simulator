package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class Cutter extends Ship implements AlliableCard
{
    public Cutter()
    {
        name = "Cutter";
        faction = Faction.TRADE_FEDERATION;
        cost = 2;
        set = CardSet.CORE;
        text = "Add 4 Authority; Add 2 Trade; Ally: Add 4 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addAuthority(4);
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(4);
    }
}
