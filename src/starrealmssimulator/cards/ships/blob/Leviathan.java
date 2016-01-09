package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Leviathan extends Ship implements AlliableCard
{
    public Leviathan()
    {
        name = "Leviathan";
        faction = Faction.BLOB;
        cost = 8;
        set = CardSet.COLONY_WARS;
        text = "Add 9 Combat; Draw a card; You may destroy target base; Ally: Acquire a card of three or less for free and put it into your hand";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(9);
        player.drawCard();
        player.destroyTargetBase();
    }

    @Override
    public void cardAllied(Player player) {
        player.acquireFreeCardToHand(3);
    }
}
