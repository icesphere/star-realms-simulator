package starrealmssimulator.cards.gambits;

import starrealmssimulator.model.Gambit;
import starrealmssimulator.model.Player;
import starrealmssimulator.model.ScrappableGambit;

public class SalvageOperation extends Gambit implements ScrappableGambit {
    public SalvageOperation() {
        name = "Salvage Operation";
    }

    @Override
    public void scrapGambit(Player player) {
        player.addCardFromDiscardToTopOfDeck();
    }
}
