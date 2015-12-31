package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class BoldRaid extends Gambit implements ScrappableGambit {
    public BoldRaid() {
        name = "Bold Raid";
    }

    @Override
    public void scrapGambit(Player player) {
        player.destroyTargetBase();
        player.drawCard();
    }
}
