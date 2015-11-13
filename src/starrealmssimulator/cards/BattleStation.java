package starrealmssimulator.cards;

import starrealmssimulator.model.*;

public class BattleStation extends Outpost implements ScrapableCard
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
    public void cardScraped(Player player)
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
