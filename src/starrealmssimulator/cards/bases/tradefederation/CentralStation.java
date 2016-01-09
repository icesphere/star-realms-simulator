package starrealmssimulator.cards.bases.tradefederation;

import starrealmssimulator.model.*;

public class CentralStation extends Base
{
    public CentralStation()
    {
        name = "Central Station";
        faction = Faction.TRADE_FEDERATION;
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Add 2 Trade. If you have three or more bases in play (including this one), gain 4 Authority and draw a card.";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addTrade(2);
        if (player.getBases().size() >= 3) {
            player.addAuthority(4);
            player.drawCard();
        }
    }
}
