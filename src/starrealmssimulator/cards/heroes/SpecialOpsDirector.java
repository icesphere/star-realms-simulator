package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class SpecialOpsDirector extends Hero {
    public SpecialOpsDirector() {
        name = "Special Ops Director";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Add 4 Authority. Until end of turn, you may use all of your Trade Federation Ally abilities";
    }

    @Override
    public void cardScraped(Player player) {
        player.addAuthority(4);
        player.tradeFederationAlliedUntilEndOfTurn();
    }
}
