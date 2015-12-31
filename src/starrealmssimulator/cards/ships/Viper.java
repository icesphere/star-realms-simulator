package starrealmssimulator.cards.ships;

import starrealmssimulator.model.*;

public class Viper extends Ship
{
    public Viper()
    {
        name = "Viper";
        faction = Faction.UNALIGNED;
        cost = 0;
        set = CardSet.CORE;
        text = "Add 1 Combat";
    }

    @Override
    public void cardPlayed(Player player) {
        player.addCombat(1);
    }
}
