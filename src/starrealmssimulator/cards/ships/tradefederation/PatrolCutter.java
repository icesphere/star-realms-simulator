package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class PatrolCutter extends Ship implements AlliableCard
{
    public PatrolCutter()
    {
        name = "Patrol Cutter";
        faction = Faction.TRADE_FEDERATION;
        cost = 3;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; Add 3 Combat; Ally: Add 4 Authority";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addTrade(2);
        player.addCombat(3);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addAuthority(4);
    }
}
