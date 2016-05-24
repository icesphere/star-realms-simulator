package starrealmssimulator.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationInfo {
    private String playerName;

    private String opponentName;

    private Map<String, SimulationStats> simulationStats;

    private LinkedHashMap<String, Integer> winsByCard;

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

    public LinkedHashMap<String, Integer> getWinsByCard() {
        return winsByCard;
    }

    public void setWinsByCard(LinkedHashMap<String, Integer> winsByCard) {
        this.winsByCard = winsByCard;
    }
}
