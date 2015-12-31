package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class CeoTorres extends Hero {
    public CeoTorres() {
        name = "CEO Torres";
        set = CardSet.CRISIS_HEROES;
        cost = 2;
        text = "Scrap: Add 7 Authority. Until end of turn, you may use all of your Trade Federation Ally abilities";
    }

    @Override
    public void cardScrapped(Player player) {
        player.addAuthority(4);
        player.tradeFederationAlliedUntilEndOfTurn();
    }
}
