package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class HighPriestLyle extends Hero {
    public HighPriestLyle() {
        name = "High Priest Lyle";
        set = CardSet.CRISIS_HEROES;
        cost = 2;
        text = "Scrap: Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card from your hand or discard pile";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.MACHINE_CULT;
    }

    @Override
    public void cardScrapped(Player player) {
        player.scrapCardFromHandOrDiscard();
        player.machineCultAlliedUntilEndOfTurn();
    }
}
