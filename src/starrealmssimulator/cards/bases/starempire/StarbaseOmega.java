package starrealmssimulator.cards.bases.starempire;

import starrealmssimulator.model.Base;
import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Player;

public class StarbaseOmega extends Base
{
    public StarbaseOmega()
    {
        name = "Starbase Omega";
        faction = Faction.STAR_EMPIRE;
        cost = 4;
        set = CardSet.PROMO_YEAR_1;
        shield = 6;
        text = "If you played a base this turn (including this one), draw a card";
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCard();
    }

    @Override
    public boolean baseCanBeUsed(Player player) {
        return player.basePlayedThisTurn();
    }
}
