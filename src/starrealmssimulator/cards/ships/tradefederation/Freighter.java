package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class Freighter extends Ship implements AlliableCard
{
    public Freighter()
    {
        name = "Freighter";
        faction = Faction.TRADE_FEDERATION;
        cost = 4;
        set = CardSet.CORE;
        text = "Add 4 Trade; Ally: You may put the next ship you acquire this turn on top of your deck";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(4);
    }

    @Override
    public void cardAllied(Player player) {
        player.nextShipToTopOfDeck();
    }
}
