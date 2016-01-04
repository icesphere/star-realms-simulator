package starrealmssimulator.model;

public abstract class Hero extends Card implements ScrappableCard {
    protected Hero() {
        faction = Faction.UNALIGNED;
    }

    @Override
    public void cardPlayed(Player player) {

    }

    public abstract Faction getAlliedFaction();
}
