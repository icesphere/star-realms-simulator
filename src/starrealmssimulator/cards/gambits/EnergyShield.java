package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.EveryTurnGambit;
import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class EnergyShield extends Gambit implements ScrappableGambit, EveryTurnGambit {
    public EnergyShield() {
        name = "Energy Shield";
    }

    @Override
    public void everyTurnAbility(Player player) {
        player.setPreventFirstDamage(true);
    }

    @Override
    public void scrapGambit(Player player) {
        player.setPreventFirstDamage(false);
        player.drawCard();
    }
}
