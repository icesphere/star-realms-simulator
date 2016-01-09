package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class MechCruiser extends Ship implements AlliableCard
{
    public MechCruiser()
    {
        name = "Mech Cruiser";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.COLONY_WARS;
        text = "Add 6 Combat; You may scrap a card in your hand or discard pile; Ally: Destroy target base";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(6);
        player.scrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.destroyTargetBase();
    }
}
