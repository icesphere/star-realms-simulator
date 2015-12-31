package starrealmssimulator.model;

public abstract class Hero extends Card implements ScrapableCard {
    protected Hero() {
        faction = Faction.UNALIGNED;
    }

    @Override
    public void cardPlayed(Player player) {

    }
}
