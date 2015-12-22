package starrealmssimulator.model;

import java.util.Map;

public class SimulationResults {
    private float winPercentage;

    private float averageNumTurns;

    private Map<Integer, Integer> playerAverageAuthorityByTurn;
    private Map<Integer, Integer> opponentAverageAuthorityByTurn;

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
}
