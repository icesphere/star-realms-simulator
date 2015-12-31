package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class SurpriseAssault extends Gambit implements ScrappableGambit {
    public SurpriseAssault() {
        name = "Surprise Assault";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addCombat(8);
    }
}
