package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class RepairBot extends Ship implements ScrappableCard
{
    public RepairBot()
    {
        name = "Repair Bot";
        faction = Faction.MACHINE_CULT;
        cost = 2;
        set = CardSet.COLONY_WARS;
        text = "Add 2 Trade; You may scrap a card in your discard pile; Scrap: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addTrade(2);
        player.scrapCardFromDiscard(true);
    }

    @Override
    public void cardScrapped(Player player) {
        player.addCombat(2);
    }

    @Override
    public int getCombatWhenScrapped() {
        return 2;
    }
}
