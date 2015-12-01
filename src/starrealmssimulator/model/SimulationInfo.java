package starrealmssimulator.model;

import java.util.Map;

public class SimulationInfo {
    private String playerName;

    private String opponentName;

    private Map<String, SimulationStats> simulationStats;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public Map<String, SimulationStats> getSimulationStats() {
        return simulationStats;
    }

    public void setSimulationStats(Map<String, SimulationStats> simulationStats) {
        this.simulationStats = simulationStats;
    }
}
