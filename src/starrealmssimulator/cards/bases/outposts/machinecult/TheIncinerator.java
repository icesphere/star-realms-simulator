package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class TheIncinerator extends Outpost implements AlliableCard
{
    public TheIncinerator()
    {
        name = "The Incinerator";
        faction = Faction.MACHINE_CULT;
        cost = 8;
        set = CardSet.COLONY_WARS;
        shield = 6;
        text = "Scrap up to two cards from your hand and/or discard pile. Ally: Gain 2 Combat for each card scrapped from your hand and/or discard pile this turn.";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.scrapCardsFromHandOrDiscard(2);
    }

    @Override
    public void cardAllied(Player player) {
        //todo
    }
}
