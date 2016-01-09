package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Moonwurm extends Ship implements AlliableCard
{
    public Moonwurm()
    {
        name = "Moonwurm";
        faction = Faction.BLOB;
        cost = 7;
        set = CardSet.COLONY_WARS;
        text = "Add 8 Combat; Draw a card; Ally: Acquire a card of two or less for free and put it into your hand";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(8);
        player.drawCard();
    }

    @Override
    public void cardAllied(Player player) {
        player.acquireFreeCardToHand(2);
    }
}
