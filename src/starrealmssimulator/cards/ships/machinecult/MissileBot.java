package starrealmssimulator.cards.ships.machinecult;

import starrealmssimulator.model.*;

public class MissileBot extends Ship implements AlliableCard
{
    public MissileBot()
    {
        name = "Missile Bot";
        faction = Faction.MACHINE_CULT;
        cost = 2;
        set = CardSet.CORE;
        text = "Add 2 Combat; You may scrap a card in your hand or discard pile; Ally: Add 2 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(2);
        player.scrapCardFromHandOrDiscard();
    }

    @Override
    public void cardAllied(Player player) {
        player.addCombat(2);
    }
}
