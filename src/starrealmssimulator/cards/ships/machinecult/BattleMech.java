package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class BattleMech extends Ship implements AlliableCard
{
    public BattleMech()
    {
        name = "Battle Mech";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.CORE;
        text = "Add 4 Combat; You may scrap a card in your hand or discard pile; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player)
    {
        player.addCombat(4);
        player.scrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player)
    {
        player.drawCard();
    }
}
