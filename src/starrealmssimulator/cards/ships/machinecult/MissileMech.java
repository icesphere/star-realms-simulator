package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class MissileMech extends Ship implements AlliableCard
{
    public MissileMech()
    {
        name = "Missile Mech";
        faction = Faction.MACHINE_CULT;
        cost = 6;
        set = CardSet.CORE;
        text = "Add 6 Combat; You may destroy target base; Ally: Draw a card";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(6);
        player.destroyTargetBase();
    }

    @Override
    public void cardAllied(Player player) {
        player.drawCard();
    }
}
