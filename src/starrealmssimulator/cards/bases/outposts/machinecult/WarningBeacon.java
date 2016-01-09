package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Outpost;
import starrealmssimulator.model.Player;

public class WarningBeacon extends Outpost
{
    public WarningBeacon()
    {
        name = "Warning Beacon";
        faction = Faction.MACHINE_CULT;
        cost = 2;
        set = CardSet.COLONY_WARS;
        shield = 2;
        text = "Add 2 Combat. When you acquire this card, if you've played a Machine Cult card this turn, you may put this card directly into your hand.";
    }

    @Override
    public void cardPlayed(Player player) {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(2);
    }
}
