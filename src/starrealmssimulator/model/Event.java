package starrealmssimulator.model;

public abstract class Event extends Card {
    protected Event() {
        faction = Faction.UNALIGNED;
    }

    @Override
    public void cardPlayed(Player player) {

    }

    public void eventTriggered(Player player) {
        player.getGame().gameLog(name + " event triggered");
        handleEvent(player);
        player.getGame().scrapCardFromTradeRow(this);
    }

    public abstract void handleEvent(Player player);
}
