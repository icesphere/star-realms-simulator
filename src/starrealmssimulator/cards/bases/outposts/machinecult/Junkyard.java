package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class Junkyard extends Outpost
{
    public Junkyard()
    {
        name = "Junkyard";
        faction = Faction.MACHINE_CULT;
        cost = 6;
        set = CardSet.CORE;
        shield = 5;
        text = "Scrap a card in your hand or discard pile";
    }

    @Override
    public void baseUsed(Player player) {
        player.scrapCardFromHandOrDiscard();
    }
}
