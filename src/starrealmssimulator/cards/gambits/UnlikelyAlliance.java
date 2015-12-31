package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class UnlikelyAlliance extends Gambit implements ScrappableGambit {
    public UnlikelyAlliance() {
        name = "Unlikely Alliance";
    }

    @Override
    public void scrapGambit(Player player) {
        player.drawCards(2);
    }
}
