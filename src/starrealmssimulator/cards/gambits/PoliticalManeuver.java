package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class PoliticalManeuver extends Gambit implements ScrappableGambit {
    public PoliticalManeuver() {
        name = "Political Maneuver";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addTrade(2);
    }
}
