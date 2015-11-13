package starrealmssimulator.model;

import java.util.List;

public class CardRules {
    private String defaultScore;

    private Integer defaultChoice;

    private List<CardRule> rules;

    private boolean useBaseAfterPlay;

    private boolean useAllyAfterPlay;

    public String getDefaultScore() {
        return defaultScore;
    }

    public void setDefaultScore(String defaultScore) {
        this.defaultScore = defaultScore;
    }

    public Integer getDefaultChoice() {
        return defaultChoice;
    }

    public void setDefaultChoice(Integer defaultChoice) {
        this.defaultChoice = defaultChoice;
    }

    public List<CardRule> getRules() {
        return rules;
    }

    public void setRules(List<CardRule> rules) {
        this.rules = rules;
    }

    public boolean isUseBaseAfterPlay() {
        return useBaseAfterPlay;
    }

    public void setUseBaseAfterPlay(boolean useBaseAfterPlay) {
        this.useBaseAfterPlay = useBaseAfterPlay;
    }

    public boolean isUseAllyAfterPlay() {
        return useAllyAfterPlay;
    }

    public void setUseAllyAfterPlay(boolean useAllyAfterPlay) {
        this.useAllyAfterPlay = useAllyAfterPlay;
    }
}
