package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class TheOracle extends Outpost implements AlliableCard
{
    public TheOracle()
    {
        name = "The Oracle";
        faction = Faction.MACHINE_CULT;
        cost = 4;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Scrap a card in your hand. Ally: Add 3 Combat.";
    }

    @Override
    public void baseUsed(Player player) {
        player.scrapCardFromHand(true);
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(3);
    }
}
