package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.EveryTurnGambit;
import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;

public class FrontierFleet extends Gambit implements EveryTurnGambit {
    public FrontierFleet() {
        name = "Frontier Fleet";
    }

    @Override
    public void everyTurnAbility(Player player) {
        player.addCombat(1);
    }
}
