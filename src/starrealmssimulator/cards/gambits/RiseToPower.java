package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class RiseToPower extends Gambit implements ScrappableGambit {
    public RiseToPower() {
        name = "Rise to Power";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addAuthority(8);
        player.drawCard();
    }
}
