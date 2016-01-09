package starrealmssimulator.cards.bases.outposts.tradefederation;

import starrealmssimulator.model.*;

public class FactoryWorld extends Outpost
{
    public FactoryWorld()
    {
        name = "Factory World";
        faction = Faction.TRADE_FEDERATION;
        cost = 8;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Add 3 Trade. Put the next base or ship you acquire this turn into your hand.";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(3);
        player.nextShipOrBaseToHand();
    }
}
