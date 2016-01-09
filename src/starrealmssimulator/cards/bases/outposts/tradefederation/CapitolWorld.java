package starrealmssimulator.cards.bases.outposts.tradefederation;

import starrealmssimulator.model.*;

public class CapitolWorld extends Outpost
{
    public CapitolWorld()
    {
        name = "Capitol World";
        faction = Faction.TRADE_FEDERATION;
        cost = 8;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 6 Authority; Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addAuthority(6);
        player.drawCard();
    }
}
