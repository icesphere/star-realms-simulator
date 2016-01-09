package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class BattleStation extends Outpost implements ScrappableCard
{
    public BattleStation()
    {
        name = "Battle Station";
        faction = Faction.MACHINE_CULT;
        cost = 3;
        set = CardSet.CORE;
        shield = 5;
        text = "Scrap: Add 5 Combat";
    }

    @Override
    public void cardScrapped(Player player)
    {
        player.addCombat(5);
    }

    @Override
    public void baseUsed(Player player) {

    }

    @Override
    public int getCombatWhenScrapped() {
        return 5;
    }
}
