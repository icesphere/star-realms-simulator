package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class FederationShuttle extends Ship implements AlliableCard
{
    public FederationShuttle()
    {
        name = "Federation Shuttle";
        faction = Faction.TRADE_FEDERATION;
        cost = 1;
        set = CardSet.CORE;
        text = "Add 2 Trade; Ally: Add 4 Authority";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player) {
        player.addAuthority(4);
    }
}
