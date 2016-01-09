package starrealmssimulator.cards.bases.outposts.starempire;

import starrealmssimulator.model.*;

public class StarFortress extends Outpost implements AlliableCard
{
    public StarFortress()
    {
        name = "Star Fortress";
        faction = Faction.STAR_EMPIRE;
        cost = 7;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 3 Combat; Draw a card, then discard a card; Ally: Draw a card, then discard a card";
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(3);
        player.drawCard();
        player.discardCards(1, false);
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
        player.discardCards(1, false);
    }
}
