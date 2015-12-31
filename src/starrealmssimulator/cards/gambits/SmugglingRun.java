package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class SmugglingRun extends Gambit implements ScrappableGambit {
    public SmugglingRun() {
        name = "Smuggling Run";
    }

    @Override
    public void scrapGambit(Player player) {
        player.acquireFreeCard(4);
    }
}
