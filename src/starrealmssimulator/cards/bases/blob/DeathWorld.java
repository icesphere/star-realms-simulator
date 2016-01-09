package starrealmssimulator.cards.bases.blob;

import starrealmssimulator.model.*;

public class DeathWorld extends Base
{
    public DeathWorld()
    {
        name = "Death World";
        faction = Faction.BLOB;
        cost = 7;
        set = CardSet.CRISIS_FLEETS_AND_FORTRESSES;
        shield = 6;
        text = "Add 4 Combat; You may scrap a Trade Federation, Machine Cult or Star Empire card from your hand or discard pile. If you do, draw a card";
    }

    @Override
    public void baseUsed(Player player)
    {
        player.addCombat(4);
        player.handleDeathWorld();
    }
}
