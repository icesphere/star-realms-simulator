package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class BrainWorld extends Outpost
{
    public BrainWorld()
    {
        name = "Brain World";
        faction = Faction.MACHINE_CULT;
        cost = 8;
        set = CardSet.CORE;
        shield = 6;
        text = "Scrap up to two cards from your hand and/or discard pile. Draw a card for each card scrapped this way.";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.scrapToDrawCards(2);
    }
}
