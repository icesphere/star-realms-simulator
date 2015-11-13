package starrealmssimulator.model;

public class SimulationStats {
    private int wins;
    private int firstPlayerWins;

    public void addWin() {
        wins++;
    }

    public void addFirstPlayerWin() {
        firstPlayerWins++;
    }

    public int getWins() {
        return wins;
    }

    public int getFirstPlayerWins() {
        return firstPlayerWins;
    }
}
