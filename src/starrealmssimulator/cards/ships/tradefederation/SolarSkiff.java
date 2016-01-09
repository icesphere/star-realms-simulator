package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class SolarSkiff extends Ship implements AlliableCard
{
    public SolarSkiff()
    {
        name = "Solar Skiff";
        faction = Faction.TRADE_FEDERATION;
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
