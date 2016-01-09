package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Predator extends Ship implements AlliableCard
{
    public Predator()
    {
        name = "Predator";
        faction = Faction.BLOB;
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 4 Combat; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
