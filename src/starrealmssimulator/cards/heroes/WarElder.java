package starrealmssimulator.cards.heroes;

import starrealmssimulator.model.CardSet;
import starrealmssimulator.model.Faction;
import starrealmssimulator.model.Hero;
import starrealmssimulator.model.Player;

public class WarElder extends Hero {
    public WarElder() {
        name = "War Elder";
        set = CardSet.CRISIS_HEROES;
        cost = 1;
        text = "Scrap: Until end of turn, you may use all of your Machine Cult Ally abilities. You may scrap a card from your hand";
    }

    @Override
    public Faction getAlliedFaction() {
        return Faction.MACHINE_CULT;
    }

    @Override
    public void cardScrapped(Player player) {
        player.scrapCardFromHand(true);
        player.machineCultAlliedUntilEndOfTurn();
    }
}
