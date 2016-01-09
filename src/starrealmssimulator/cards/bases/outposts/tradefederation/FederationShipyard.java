package starrealmssimulator.cards.bases.outposts.tradefederation;

import starrealmssimulator.model.*;

public class FederationShipyard extends Outpost implements AlliableCard
{
    public FederationShipyard()
    {
        name = "Federation Shipyard";
        faction = Faction.TRADE_FEDERATION;
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 2 Trade. Ally: Put the next ship or base you acquire this turn on top of your deck.";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player)
    {
        player.addTrade(2);
        player.nextShipOrBaseToTopOfDeck();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(2);
    }
}
