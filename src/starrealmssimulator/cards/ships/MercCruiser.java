package starrealmssimulator.cards.ships;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.Ship;

public class MercCruiser extends Ship
{
    public MercCruiser()
    {
        name = "Merc Cruiser";
        faction = Faction.UNALIGNED;
        cost = 3;
        set = CardSet.PROMO_YEAR_1;
        text = "Add 5 Combat; Choose a faction as you play Merc Cruiser. Merc Cruiser has that faction";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(5);
        player.chooseFactionForCard(this);
    }
}
