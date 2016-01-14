package starrealmssimulator.cards.bases.blob;

import starrealmssimulator.model.*;

public class PlasmaVent extends Base
{
    public PlasmaVent()
    {
        name = "Plasma Vent";
        faction = Faction.BLOB;
        cost = 6;
        set = CardSet.COLONY_WARS;
        shield = 5;
        text = "Add 4 Combat. When you acquire this card, if you've played a Blob card this turn, you may put this card directly into your hand.";
    }

    @Override
    public void cardPlayed(Player player)
    {
        this.useBase(player);
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(4);
    }
}
