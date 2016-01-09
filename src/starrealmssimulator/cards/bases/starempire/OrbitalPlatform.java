package starrealmssimulator.cards.bases.starempire;

import starrealmssimulator.model.Base;
import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Player;

public class OrbitalPlatform extends Base
{
    public OrbitalPlatform()
    {
        name = "Orbital Platform";
        faction = Faction.STAR_EMPIRE;
        cost = 3;
        set = CardSet.COLONY_WARS;
        shield = 4;
        text = "Discard a card. If you do, draw a card.";
    }

    @Override
    public void baseUsed(Player player) {
        int cardsDiscarded = player.discardCards(1, false);
        if (cardsDiscarded > 0) {
            player.drawCard();
        }
    }
}
