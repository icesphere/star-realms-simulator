package starrealmssimulator.model;

import java.util.Map;

public class JsonBotCache {
    private String botName;
    private String author;
    private Map<String, CardRules> buyRulesMap;
    private Map<String, CardRules> discardRulesMap;
    private Map<String, CardRules> scrapRulesMap;
    private Map<String, CardRules> scrapForBenefitRulesMap;
    private Map<String, CardRules> useBaseRulesMap;
    private Map<String, CardRules> playRulesMap;
    private Map<String, CardRules> makeChoiceRulesMap;
    private Map<String, CardRules> destroyBaseRulesMap;
    private Map<String, CardRules> attackBaseRulesMap;
    private Map<String, CardRules> copyShipRulesMap;
    private Map<String, CardRules> scrapTradeRowRulesMap;
    private Map<String, CardRules> cardToTopOfDeckRulesMap;
    private Map<String, CardRules> returnBaseToHandRulesMap;
    private Map<String, CardInfo> cardInfoMap;

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Map<String, CardRules> getBuyRulesMap() {
        return buyRulesMap;
    }

    public void setBuyRulesMap(Map<String, CardRules> buyRulesMap) {
        this.buyRulesMap = buyRulesMap;
    }

    public Map<String, CardRules> getDiscardRulesMap() {
        return discardRulesMap;
    }

    public void setDiscardRulesMap(Map<String, CardRules> discardRulesMap) {
        this.discardRulesMap = discardRulesMap;
    }

    public Map<String, CardRules> getScrapRulesMap() {
        return scrapRulesMap;
    }

    public void setScrapRulesMap(Map<String, CardRules> scrapRulesMap) {
        this.scrapRulesMap = scrapRulesMap;
    }

    public Map<String, CardRules> getScrapForBenefitRulesMap() {
        return scrapForBenefitRulesMap;
    }

    public void setScrapForBenefitRulesMap(Map<String, CardRules> scrapForBenefitRulesMap) {
        this.scrapForBenefitRulesMap = scrapForBenefitRulesMap;
    }

    public Map<String, CardRules> getUseBaseRulesMap() {
        return useBaseRulesMap;
    }

    public void setUseBaseRulesMap(Map<String, CardRules> useBaseRulesMap) {
        this.useBaseRulesMap = useBaseRulesMap;
    }

    public Map<String, CardRules> getPlayRulesMap() {
        return playRulesMap;
    }

    public void setPlayRulesMap(Map<String, CardRules> playRulesMap) {
        this.playRulesMap = playRulesMap;
    }

    public Map<String, CardRules> getMakeChoiceRulesMap() {
        return makeChoiceRulesMap;
    }

    public void setMakeChoiceRulesMap(Map<String, CardRules> makeChoiceRulesMap) {
        this.makeChoiceRulesMap = makeChoiceRulesMap;
    }

    public Map<String, CardRules> getDestroyBaseRulesMap() {
        return destroyBaseRulesMap;
    }

    public void setDestroyBaseRulesMap(Map<String, CardRules> destroyBaseRulesMap) {
        this.destroyBaseRulesMap = destroyBaseRulesMap;
    }

    public Map<String, CardRules> getAttackBaseRulesMap() {
        return attackBaseRulesMap;
    }

    public void setAttackBaseRulesMap(Map<String, CardRules> attackBaseRulesMap) {
        this.attackBaseRulesMap = attackBaseRulesMap;
    }

    public Map<String, CardRules> getCopyShipRulesMap() {
        return copyShipRulesMap;
    }

    public void setCopyShipRulesMap(Map<String, CardRules> copyShipRulesMap) {
        this.copyShipRulesMap = copyShipRulesMap;
    }

    public Map<String, CardRules> getScrapTradeRowRulesMap() {
        return scrapTradeRowRulesMap;
    }

    public void setScrapTradeRowRulesMap(Map<String, CardRules> scrapTradeRowRulesMap) {
        this.scrapTradeRowRulesMap = scrapTradeRowRulesMap;
    }

    public Map<String, CardRules> getCardToTopOfDeckRulesMap() {
        return cardToTopOfDeckRulesMap;
    }

    public void setCardToTopOfDeckRulesMap(Map<String, CardRules> cardToTopOfDeckRulesMap) {
        this.cardToTopOfDeckRulesMap = cardToTopOfDeckRulesMap;
    }

    public Map<String, CardInfo> getCardInfoMap() {
        return cardInfoMap;
    }

    public void setCardInfoMap(Map<String, CardInfo> cardInfoMap) {
        this.cardInfoMap = cardInfoMap;
    }

    public Map<String, CardRules> getReturnBaseToHandRulesMap() {
        return returnBaseToHandRulesMap;
    }

    public void setReturnBaseToHandRulesMap(Map<String, CardRules> returnBaseToHandRulesMap) {
        this.returnBaseToHandRulesMap = returnBaseToHandRulesMap;
    }
}
