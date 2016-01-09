package starrealmssimulator.cards.ships.tradefederation;

import starrealmssimulator.model.*;

public class EmbassyYacht extends Ship
{
    public EmbassyYacht()
    {
        name = "Embassy Yacht";
        faction = Faction.TRADE_FEDERATION;
        cost = 3;
        set = CardSet.CORE;
        text = "Add 3 Authority; Add 2 Trade; If you have two or more bases in play, draw two cards";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addAuthority(3);
        player.addTrade(2);
        if (player.getBases().size() >= 2) {
            player.drawCards(2);
        }
    }
}
