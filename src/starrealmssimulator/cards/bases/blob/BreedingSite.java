package starrealmssimulator.cards.bases.blob;

import starrealmssimulator.model.*;

public class BreedingSite extends Base
{
    public BreedingSite()
    {
        name = "Breeding Site";
        faction = Faction.BLOB;
        cost = 4;
        set = CardSet.PROMO_YEAR_1;
        shield = 7;
        text = "If you played a base this turn (including this one), gain 5 Combat";
    }

    @Override
    public void baseUsed(Player player) {
        player.addCombat(5);
    }

    @Override
    public boolean baseCanBeUsed(Player player) {
        return player.basePlayedThisTurn();
    }
}
