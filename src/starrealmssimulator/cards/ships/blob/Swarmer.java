package starrealmssimulator.cards.ships.blob;

import starrealmssimulator.model.*;

public class Swarmer extends Ship implements AlliableCard
{
    public Swarmer()
    {
        name = "Swarmer";
        faction = Faction.BLOB;
        cost = 1;
        set = CardSet.COLONY_WARS;
        text = "Add 3 Combat; You may scrap a card in the trade row; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(3);
        player.scrapCardInTradeRow();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.addCombat(2);
    }
}
