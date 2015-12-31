package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class CunningCaptain extends Hero {
    public CunningCaptain() {
        name = "Cunning Captain";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Until end of turn, you may use all of your Star Empire Ally abilities. Target player discards a card";
    }

    @Override
    public void cardScraped(Player player) {
        player.opponentDiscardsCard();
        player.starEmpireAlliedUntilEndOfTurn();
    }
}
