package starrealmssimulator.cards.bases.outposts.machinecult;

import starrealmssimulator.model.*;

public class MachineBase extends Outpost
{
    public MachineBase()
    {
        name = "Machine Base";
        faction = Faction.MACHINE_CULT;
        cost = 7;
        set = CardSet.CORE;
        shield = 6;
        text = "Draw a card, then scrap a card from your hand";
    }

    @Override
    public void baseUsed(Player player) {
        player.drawCard();
        player.scrapCardFromHand(false);
    }
}
