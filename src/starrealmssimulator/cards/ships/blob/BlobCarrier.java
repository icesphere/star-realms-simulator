package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class BlobCarrier extends Ship implements AlliableCard
{
    public BlobCarrier()
    {
        name = "Blob Carrier";
        faction = Faction.BLOB;
        cost = 6;
        set = CardSet.CORE;
        text = "Add 7 Combat; Ally: Acquire any ship without paying its cost and put it on top of your deck";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(7);
    }

    @Override
    public void cardAllied(Player player)
    {
        player.acquireFreeShipAndPutOnTopOfDeck();
    }
}
