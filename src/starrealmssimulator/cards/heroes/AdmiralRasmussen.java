package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class AdmiralRasmussen extends Hero {
    public AdmiralRasmussen() {
        name = "Cunning Captain";
        set = CardSet.CRISIS_HEROES;
        cost = 2;
        text = "Scrap: Until end of turn, you may use all of your Star Empire Ally abilities. Draw a card";
    }

    @Override
    public void cardScraped(Player player) {
        player.drawCard();
        player.starEmpireAlliedUntilEndOfTurn();
    }
}
