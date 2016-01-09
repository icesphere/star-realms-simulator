package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class MegaMech extends Ship implements AlliableCard
{
    public MegaMech()
    {
        name = "Mega Mech";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.CRISIS_BASES_AND_BATTLESHIPS;
        text = "Add 6 Combat; You may return target base from play to its owner's hand; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.returnTargetBaseToHand();
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
