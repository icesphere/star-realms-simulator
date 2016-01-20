package starrealmssimulator.model;

import java.util.Map;

public class SimulationResults {
    private float winPercentage;

    private float averageNumTurns;

    private int totalGamesCounted;

    private Map<Integer, Integer> playerAverageAuthorityByTurn;
    private Map<Integer, Integer> opponentAverageAuthorityByTurn;

    private String winGameLog;
    private String lossGameLog;

    public float getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(float winPercentage) {
        this.winPercentage = winPercentage;
    }

    public float getAverageNumTurns() {
        return averageNumTurns;
    }

    public void setAverageNumTurns(float averageNumTurns) {
        this.averageNumTurns = averageNumTurns;
    }

    public Map<Integer, Integer> getPlayerAverageAuthorityByTurn() {
        return playerAverageAuthorityByTurn;
    }

    public void setPlayerAverageAuthorityByTurn(Map<Integer, Integer> playerAverageAuthorityByTurn) {
        this.playerAverageAuthorityByTurn = playerAverageAuthorityByTurn;
    }

    public Map<Integer, Integer> getOpponentAverageAuthorityByTurn() {
        return opponentAverageAuthorityByTurn;
    }

    public void setOpponentAverageAuthorityByTurn(Map<Integer, Integer> opponentAverageAuthorityByTurn) {
        this.opponentAverageAuthorityByTurn = opponentAverageAuthorityByTurn;
    }

    public String getWinGameLog() {
        return winGameLog;
    }

    public void setWinGameLog(String winGameLog) {
        this.winGameLog = winGameLog;
    }

    public String getLossGameLog() {
        return lossGameLog;
    }

    public void setLossGameLog(String lossGameLog) {
        this.lossGameLog = lossGameLog;
    }

    public int getTotalGamesCounted() {
        return totalGamesCounted;
    }

    public void setTotalGamesCounted(int totalGamesCounted) {
        this.totalGamesCounted = totalGamesCounted;
    }
}
