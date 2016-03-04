package starrealmssimulator.model;

public class SimulationStats {
    private int wins;
    private int firstPlayerWins;
    private int authorityDifferentialTotal;

    public void addWin() {
        wins++;
    }

    public void addFirstPlayerWin() {
        firstPlayerWins++;
    }

    public void addAuthorityDifferential(int authorityDifferential) {
        authorityDifferentialTotal += authorityDifferential;
    }

    public int getWins() {
        return wins;
    }

    public int getFirstPlayerWins() {
        return firstPlayerWins;
    }

    public int getAuthorityDifferentialTotal() {
        return authorityDifferentialTotal;
    }
}
