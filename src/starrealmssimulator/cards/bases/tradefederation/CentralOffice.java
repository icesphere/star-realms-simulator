package starrealmssimulator.cards.bases.tradefederation;

import starrealmssimulator.model.*;

public class CentralOffice extends Base implements AlliableCard
{
    public CentralOffice()
    {
        name = "Central Office";
        faction = Faction.TRADE_FEDERATION;
        cost = 7;
        set = CardSet.CORE;
        shield = 6;
        text = "Add 2 Trade; You may put the next ship you acquire this turn on top of your deck; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(2);
        player.nextShipToTopOfDeck();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
