package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class MechWorld extends Outpost
{
    public MechWorld()
    {
        name = "Mech World";
        faction = Faction.MACHINE_CULT;
        cost = 5;
        set = CardSet.CORE;
        shield = 6;
        text = "Mech World counts as an ally for all factions";
    }

    @Override
    public void cardPlayed(Player player) {
        player.allFactionsAllied();
    }

    @Override
    public void baseUsed(Player player) {

    }

    @Override
    public boolean isBlob() {
        return true;
    }

    @Override
    public boolean isTradeFederation() {
        return true;
    }

    @Override
    public boolean isMachineCult() {
        return true;
    }

    @Override
    public boolean isStarEmpire() {
        return true;
    }
}
