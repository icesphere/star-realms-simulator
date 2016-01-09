package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class TradeHauler extends Ship implements AlliableCard
{
    public TradeHauler()
    {
        name = "Trade Hauler";
        faction = Faction.TRADE_FEDERATION;
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Trade; Ally: Add 3 Authority";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(3);
    }

    @Override
    public void cardAllied(Player player) {
        player.addAuthority(3);
    }
}
