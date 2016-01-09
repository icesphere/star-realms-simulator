package starrealmssimulator.bots.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import starrealmssimulator.cards.ships.machinecult.StealthNeedle;
import starrealmssimulator.model.*;
import starrealmssimulator.service.GameService;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonBot extends Bot {
    private String botName;
    private String author;
    private String botFile;

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

    public JsonBot(String botFile) {
        this.botFile = botFile;

        JsonBotCache jsonBotCache = GameService.getBotCache(botFile);

        if (jsonBotCache != null) {
            botName = jsonBotCache.getBotName();
            author = jsonBotCache.getAuthor();
            buyRulesMap = jsonBotCache.getBuyRulesMap();
            discardRulesMap = jsonBotCache.getDiscardRulesMap();
            scrapRulesMap = jsonBotCache.getScrapRulesMap();
            scrapForBenefitRulesMap = jsonBotCache.getScrapForBenefitRulesMap();
            useBaseRulesMap = jsonBotCache.getUseBaseRulesMap();
            playRulesMap = jsonBotCache.getPlayRulesMap();
            makeChoiceRulesMap = jsonBotCache.getMakeChoiceRulesMap();
            destroyBaseRulesMap = jsonBotCache.getDestroyBaseRulesMap();
            attackBaseRulesMap = jsonBotCache.getAttackBaseRulesMap();
            copyShipRulesMap = jsonBotCache.getCopyShipRulesMap();
            scrapTradeRowRulesMap = jsonBotCache.getScrapTradeRowRulesMap();
            cardToTopOfDeckRulesMap = jsonBotCache.getCardToTopOfDeckRulesMap();
            returnBaseToHandRulesMap = jsonBotCache.getReturnBaseToHandRulesMap();
            cardInfoMap = jsonBotCache.getCardInfoMap();
        } else {
            loadBotFile(botFile);
        }
    }

    @Override
    public String getPlayerName() {
        return botName;
    }

    public String getAuthor() {
        return author;
    }

    private void loadBotFile(String botFile) {
        try {
            JsonBotCache cache = new JsonBotCache();
            InputStream is = JsonBot.class.getResourceAsStream(botFile);
            String jsonString = convertStreamToString(is);
            JSONObject obj = new JSONObject(jsonString);
            botName = obj.getString("name");
            author = obj.getString("author");
            buyRulesMap = loadCardRules(obj, "buyRules");
            discardRulesMap = loadCardRules(obj, "discardRules");
            scrapRulesMap = loadCardRules(obj, "scrapRules");
            scrapForBenefitRulesMap = loadCardRules(obj, "scrapForBenefitRules");
            useBaseRulesMap = loadCardRules(obj, "useBaseRules");
            playRulesMap = loadCardRules(obj, "playRules");
            makeChoiceRulesMap = loadCardRules(obj, "makeChoiceRules");
            destroyBaseRulesMap = loadCardRules(obj, "destroyBaseRules");
            attackBaseRulesMap = loadCardRules(obj, "attackBaseRules");
            copyShipRulesMap = loadCardRules(obj, "copyShipRules");
            scrapTradeRowRulesMap = loadCardRules(obj, "scrapTradeRowRules");
            cardToTopOfDeckRulesMap = loadCardRules(obj, "cardToTopOfDeckRules");
            returnBaseToHandRulesMap = loadCardRules(obj, "returnBaseToHandRules");
            cardInfoMap = loadCardInfo(obj, "cardInfo");
            cache.setBotName(botName);
            cache.setAuthor(author);
            cache.setBuyRulesMap(buyRulesMap);
            cache.setDiscardRulesMap(discardRulesMap);
            cache.setScrapRulesMap(scrapRulesMap);
            cache.setScrapForBenefitRulesMap(scrapForBenefitRulesMap);
            cache.setUseBaseRulesMap(useBaseRulesMap);
            cache.setPlayRulesMap(playRulesMap);
            cache.setMakeChoiceRulesMap(makeChoiceRulesMap);
            cache.setDestroyBaseRulesMap(destroyBaseRulesMap);
            cache.setAttackBaseRulesMap(attackBaseRulesMap);
            cache.setCopyShipRulesMap(copyShipRulesMap);
            cache.setScrapTradeRowRulesMap(scrapTradeRowRulesMap);
            cache.setCardToTopOfDeckRulesMap(cardToTopOfDeckRulesMap);
            cache.setReturnBaseToHandRulesMap(returnBaseToHandRulesMap);
            cache.setCardInfoMap(cardInfoMap);
            GameService.addBotToCache(botFile, cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private Map<String, CardInfo> loadCardInfo(JSONObject obj, String infoKey) {
        Map<String, CardInfo> infoMap = new HashMap<>();

        try {
            JSONArray json = obj.getJSONArray(infoKey);
            for (int i = 0; i < json.length(); i++) {
                CardInfo cardInfo = new CardInfo();
                JSONObject card = json.getJSONObject(i);
                String cardName = getCardNameKey(card.getString("card"));

                try {
                    JSONObject info = card.getJSONObject("info");
                    for (String key : info.keySet()) {
                        CardRules cardRules = new CardRules();
                        Object infoObject = info.get(key);
                        if (infoObject instanceof JSONArray) {
                            JSONArray rulesArray = (JSONArray) infoObject;
                            List<CardRule> rules = new ArrayList<>();
                            for (int k = 0; k < rulesArray.length(); k++) {
                                JSONObject cardRule = rulesArray.getJSONObject(k);
                                CardRule rule = new CardRule();
                                for (String infoRuleKey : cardRule.keySet()) {
                                    rule.addRuleValue(infoRuleKey, getJsonValue(cardRule, infoRuleKey));
                                }
                                rules.add(rule);
                            }
                            cardRules.setRules(rules);
                        } else {
                            cardRules.setDefaultScore(getJsonValue(info, key));
                        }
                        cardInfo.addInfoValue(key, cardRules);
                    }
                } catch (JSONException ignored) {}

                infoMap.put(cardName, cardInfo);
            }
        } catch (JSONException ignored) {
        }

        return infoMap;
    }

    private Map<String, CardRules> loadCardRules(JSONObject obj, String rulesKey) {
        Map<String, CardRules> rulesMap = new HashMap<>();

        try {
            JSONArray json = obj.getJSONArray(rulesKey);
            for (int i = 0; i < json.length(); i++) {
                CardRules buyRules = new CardRules();
                JSONObject card = json.getJSONObject(i);
                String cardName = getCardNameKey(card.getString("card"));

                buyRules.setDefaultScore(getJsonValue(card, "score"));
                buyRules.setDefaultChoice(getJsonInteger(card, "choice"));
                buyRules.setUseAllyAfterPlay(getJsonBoolean(card, "useAllyAfterPlay"));
                buyRules.setUseBaseAfterPlay(getJsonBoolean(card, "useBaseAfterPlay"));

                try {
                    JSONArray cardRules = card.getJSONArray("rules");
                    List<CardRule> rules = new ArrayList<>();
                    for (int j = 0; j < cardRules.length(); j++) {
                        JSONObject cardRule = cardRules.getJSONObject(j);
                        CardRule rule = new CardRule();
                        for (String key : cardRule.keySet()) {
                            rule.addRuleValue(key, getJsonValue(cardRule, key));
                        }
                        rules.add(rule);
                    }
                    buyRules.setRules(rules);
                } catch (JSONException ignored) {}

                rulesMap.put(cardName, buyRules);
            }
        } catch (JSONException ignored) {
        }

        return rulesMap;
    }

    private String getCardNameKey(String cardName) {
        return cardName.replaceAll("\\s", "").toLowerCase();
    }

    public String getJsonValue(JSONObject object, String key) {
        try {
            Object value = object.get(key);
            if (value != null) {
                if (value instanceof Integer) {
                    return String.valueOf(value);
                } else if (value instanceof String) {
                    return (String) value;
                }
            }
        } catch (JSONException ignore) {}

        return null;
    }

    public Integer getJsonInteger(JSONObject object, String key) {
        try {
            Object value = object.get(key);
            if (value != null) {
                if (value instanceof Integer) {
                    return (Integer) value;
                }
            }
        } catch (JSONException ignore) {}

        return null;
    }

    public boolean getJsonBoolean(JSONObject object, String key) {
        try {
            Object value = object.get(key);
            if (value != null) {
                if (value instanceof Boolean) {
                    return (Boolean) value;
                } else if (value instanceof String) {
                    return Boolean.parseBoolean((String) value);
                }
            }
        } catch (JSONException ignore) {}

        return false;
    }

    @Override
    public int getBuyCardScore(Card card) {
        return getCardScore(card, buyRulesMap, 0);
    }

    @Override
    public int getDiscardCardScore(Card card) {
        int defaultScore = 20 - card.getCost();
        return getCardScore(card, discardRulesMap, defaultScore);
    }

    @Override
    public int getScrapCardScore(Card card) {
        int defaultScore = 20 - card.getCost();
        return getCardScore(card, scrapRulesMap, defaultScore);
    }

    @Override
    public int getScrapForBenefitScore(Card card) {
        int defaultScore = 0;
        return getCardScore(card, scrapForBenefitRulesMap, defaultScore);
    }

    @Override
    public int getDestroyBaseScore(Base card) {
        int defaultScore = card.getCost();
        return getCardScore(card, destroyBaseRulesMap, defaultScore);
    }

    @Override
    public int getAttackBaseScore(Base card) {
        int defaultScore = card.getCost();
        return getCardScore(card, attackBaseRulesMap, defaultScore);
    }

    @Override
    public int getCopyShipScore(Ship card) {
        int defaultScore = card.getCost();
        if (card instanceof StealthNeedle) {
            return 0;
        }
        return getCardScore(card, copyShipRulesMap, defaultScore);
    }

    @Override
    public int getChoice(Card card) {
        return getCardChoice(card, 1);
    }

    @Override
    public int getUseBaseScore(Base card) {
        int defaultScore = 20;
        return getCardScore(card, useBaseRulesMap, defaultScore);
    }

    @Override
    public int getPlayCardScore(Card card) {
        int defaultScore = 20;
        return getCardScore(card, playRulesMap, defaultScore);
    }

    @Override
    public int getScrapCardFromTradeRowScore(Card card) {
        int defaultScore = 0;
        return getCardScore(card, scrapTradeRowRulesMap, defaultScore);
    }

    @Override
    public int getCardToTopOfDeckScore(Card card) {
        int defaultScore = getBuyCardScore(card);
        return getCardScore(card, cardToTopOfDeckRulesMap, defaultScore);
    }

    @Override
    public int getReturnBaseToHandScore(Base card) {
        int defaultScore = getBuyCardScore(card);
        return getCardScore(card, returnBaseToHandRulesMap, defaultScore);
    }

    @Override
    public boolean useAllyAfterPlay(Card card) {
        CardRules playRules = getCardRules(card, playRulesMap);
        return playRules != null && playRules.isUseAllyAfterPlay();
    }

    @Override
    public boolean useBaseAfterPlay(Base base) {
        CardRules playRules = getCardRules(base, playRulesMap);
        return playRules != null && playRules.isUseBaseAfterPlay();
    }

    private CardRules getCardRules(Card card, Map<String, CardRules> cardRuleMap) {
        CardRules cardRules = cardRuleMap.get(getCardNameKey(card.getName()));

        if (cardRules == null && card.isOutpost()) {
            cardRules = cardRuleMap.get("outpost");
        } else if (cardRules == null && card.isBase()) {
            cardRules = cardRuleMap.get("base");
        } else if (cardRules == null && card.isShip()) {
            cardRules = cardRuleMap.get("ship");
        }

        if (cardRules == null) {
            cardRules = cardRuleMap.get("*");
        }

        return cardRules;
    }

    public int getCardScore(Card card, Map<String, CardRules> cardRuleMap, int defaultScore) {
        CardRules cardRules = getCardRules(card, cardRuleMap);

        if (cardRules != null) {
            return getCardScore(card, cardRules, defaultScore);
        }

        return defaultScore;
    }

    public int getCardScore(Card card, CardRules cardRules, int defaultScore) {
        if (cardRules.getRules() != null) {
            for (CardRule cardRule : cardRules.getRules()) {
                if (ruleMatches(card, cardRule)) {
                    return calculateValue(card, cardRule.getScore());
                }
            }
        }
        if (cardRules.getDefaultScore() != null) {
            return calculateValue(card, cardRules.getDefaultScore());
        }

        return defaultScore;
    }

    public int getCardChoice(Card card, int defaultChoice) {
        CardRules cardRules = getCardRules(card, makeChoiceRulesMap);

        if (cardRules != null) {
            if (cardRules.getRules() != null) {
                for (CardRule cardRule : cardRules.getRules()) {
                    if (ruleMatches(card, cardRule)) {
                        return cardRule.getChoice();
                    }
                }
            }
            if (cardRules.getDefaultChoice() != null) {
                return cardRules.getDefaultChoice();
            }
        }

        return defaultChoice;
    }

    private int calculateValue(Card card, String stringValue) {
        stringValue = stringValue.trim();
        int value;

        try {
            value = Integer.parseInt(stringValue);
        } catch (Exception e) {
            value = getValueForKey(stringValue, card);
        }

        return value;
    }

    private int getValueForKey(String key, Card card) {
        if (key.startsWith("card_info_avg_")) {

            String cardInfoKey = key.substring("card_info_avg_".length());

            int totalValue = 0;

            for (Card c : getAllCards()) {
                totalValue += getCardInfoValue(c, cardInfoKey);
            }

            if (totalValue > 0) {
                return totalValue / getAllCards().size();
            }

            return 0;

        } else if (key.startsWith("card_info_")) {

            String cardInfoKey = key.substring("card_info_".length());
            return getCardInfoValue(card, cardInfoKey);

        } else if (key.startsWith("opponent_card_info_avg_")) {

            if (!getOpponent().getAllCards().isEmpty()) {
                String cardInfoKey = key.substring("opponent_card_info_avg_".length());

                int totalValue = 0;

                for (Card c : getOpponent().getAllCards()) {
                    totalValue += getCardInfoValue(c, cardInfoKey);
                }

                if (totalValue > 0) {
                    return totalValue / getOpponent().getAllCards().size();
                }
            }

            return 0;

        } else {

            switch (key) {
                case "card.cost":
                    return card.getCost();

                case "authority":
                    return getAuthority();
                case "opponent.authority":
                    return getOpponent().getAuthority();

                case "trade":
                    return getTrade();

                case "combat":
                    return getCombat();

                case "turn":
                    return getGame().getTurn();

                case "deck.size":
                    return getDeck().size();
                case "discard.size":
                    return getDiscard().size();
                case "hand.size":
                    return getHand().size();

                case "opponent.deck.size":
                    return getOpponent().getDeck().size();
                case "opponent.discard.size":
                    return getOpponent().getDiscard().size();
                case "opponent.hand.size":
                    return getOpponent().getHand().size();

                case "deck":
                    return getCurrentDeckNumber();

                case "ships":
                    return getNumShips();
                case "opponent.ships":
                    return getOpponent().getNumShips();

                case "bases":
                    return getNumBases();
                case "outposts":
                    return getNumOutposts();

                case "opponent.bases":
                    return getOpponent().getNumBases();
                case "opponent.outposts":
                    return getOpponent().getNumOutposts();

                case "bases.unused":
                    return getUnusedBasesAndOutposts().size();

                case "bases.inplay":
                    return getBases().size();
                case "opponent.bases.inplay":
                    return getOpponent().getBases().size();

                case "outposts.inplay":
                    return getOutposts().size();
                case "opponent.outposts.inplay":
                    return getOpponent().getOutposts().size();

                case "opponent.bases.smallest":
                    return getOpponent().getSmallestBaseShield();
                case "opponent.bases.biggest":
                    return getOpponent().getBiggestBaseShield();
                case "opponent.outposts.smallest":
                    return getOpponent().getSmallestOutpostShield();
                case "opponent.outposts.biggest":
                    return getOpponent().getBiggestOutpostShield();

                case "combat.plus.card.scrap.combat":
                    return getCombat() + card.getCombatWhenScrapped();

                case "buy.score.increase":
                    return getBuyScoreIncrease(card.getTradeWhenScrapped());

                case "trade_row.blob":
                    return countCardsByType(getGame().getTradeRow(), Card::isBlob);
                case "trade_row.trade_federation":
                    return countCardsByType(getGame().getTradeRow(), Card::isTradeFederation);
                case "trade_row.machine_cult":
                    return countCardsByType(getGame().getTradeRow(), Card::isMachineCult);
                case "trade_row.star_empire":
                    return countCardsByType(getGame().getTradeRow(), Card::isStarEmpire);

                case "cards.played.blob":
                    return countCardsByType(getPlayed(), Card::isBlob);
                case "cards.played.trade_federation":
                    return countCardsByType(getPlayed(), Card::isTradeFederation);
                case "cards.played.machine_cult":
                    return countCardsByType(getPlayed(), Card::isMachineCult);
                case "cards.played.star_empire":
                    return countCardsByType(getPlayed(), Card::isStarEmpire);
                case "cards.played.starter_cards":
                    return countCardsByType(getPlayed(), Card::isStarterCard);

                case "cards.hand.blob":
                    return countCardsByType(getHand(), Card::isBlob);
                case "cards.hand.trade_federation":
                    return countCardsByType(getHand(), Card::isTradeFederation);
                case "cards.hand.machine_cult":
                    return countCardsByType(getHand(), Card::isMachineCult);
                case "cards.hand.star_empire":
                    return countCardsByType(getHand(), Card::isStarEmpire);
                case "cards.hand.starter_cards":
                    return countCardsByType(getHand(), Card::isStarterCard);

                case "cards.discard.blob":
                    return countCardsByType(getDiscard(), Card::isBlob);
                case "cards.discard.trade_federation":
                    return countCardsByType(getDiscard(), Card::isTradeFederation);
                case "cards.discard.machine_cult":
                    return countCardsByType(getDiscard(), Card::isMachineCult);
                case "cards.discard.star_empire":
                    return countCardsByType(getDiscard(), Card::isStarEmpire);
                case "cards.discard.starter_cards":
                    return countCardsByType(getDiscard(), Card::isStarterCard);

                case "cards.deck.blob":
                    return countCardsByType(getDeck(), Card::isBlob);
                case "cards.deck.trade_federation":
                    return countCardsByType(getDeck(), Card::isTradeFederation);
                case "cards.deck.machine_cult":
                    return countCardsByType(getDeck(), Card::isMachineCult);
                case "cards.deck.star_empire":
                    return countCardsByType(getDeck(), Card::isStarEmpire);
                case "cards.deck.starter_cards":
                    return countCardsByType(getDeck(), Card::isStarterCard);

                case "cards.all.blob":
                    return countCardsByType(getAllCards(), Card::isBlob);
                case "cards.all.trade_federation":
                    return countCardsByType(getAllCards(), Card::isTradeFederation);
                case "cards.all.machine_cult":
                    return countCardsByType(getAllCards(), Card::isMachineCult);
                case "cards.all.star_empire":
                    return countCardsByType(getAllCards(), Card::isStarEmpire);
                case "cards.all.starter_cards":
                    return countCardsByType(getAllCards(), Card::isStarterCard);

                case "opponent.cards.discard.blob":
                    return countCardsByType(getOpponent().getDiscard(), Card::isBlob);
                case "opponent.cards.discard.trade_federation":
                    return countCardsByType(getOpponent().getDiscard(), Card::isTradeFederation);
                case "opponent.cards.discard.machine_cult":
                    return countCardsByType(getOpponent().getDiscard(), Card::isMachineCult);
                case "opponent.cards.discard.star_empire":
                    return countCardsByType(getOpponent().getDiscard(), Card::isStarEmpire);
                case "opponent.cards.discard.starter_cards":
                    return countCardsByType(getOpponent().getDiscard(), Card::isStarterCard);

                case "opponent.cards.deck.blob":
                    return countCardsByType(getOpponent().getDeck(), Card::isBlob);
                case "opponent.cards.deck.trade_federation":
                    return countCardsByType(getOpponent().getDeck(), Card::isTradeFederation);
                case "opponent.cards.deck.machine_cult":
                    return countCardsByType(getOpponent().getDeck(), Card::isMachineCult);
                case "opponent.cards.deck.star_empire":
                    return countCardsByType(getOpponent().getDeck(), Card::isStarEmpire);
                case "opponent.cards.deck.starter_cards":
                    return countCardsByType(getOpponent().getDeck(), Card::isStarterCard);

                case "opponent.cards.all.blob":
                    return countCardsByType(getOpponent().getAllCards(), Card::isBlob);
                case "opponent.cards.all.trade_federation":
                    return countCardsByType(getOpponent().getAllCards(), Card::isTradeFederation);
                case "opponent.cards.all.machine_cult":
                    return countCardsByType(getOpponent().getAllCards(), Card::isMachineCult);
                case "opponent.cards.all.star_empire":
                    return countCardsByType(getOpponent().getAllCards(), Card::isStarEmpire);
                case "opponent.cards.all.starter_cards":
                    return countCardsByType(getOpponent().getAllCards(), Card::isStarterCard);

                default:
                    System.out.println("Key not found: " + key);
                    return 0;
            }
        }
    }

    private int getCardInfoValue(Card card, String cardInfoKey) {
        CardInfo cardInfo = cardInfoMap.get(getCardNameKey(card.getName()));
        if (cardInfo != null) {
            CardRules cardRules = cardInfo.getInfoCardRules(cardInfoKey);
            if (cardRules != null) {
                return getCardScore(card, cardRules, 0);
            }
        }

        return 0;
    }

    private boolean ruleMatches(Card card, CardRule rule) {
        for (String key : rule.getRuleKeys()) {
            if (!ruleValueMatches(rule, key, card)) {
                return false;
            }
        }

        return true;
    }

    private String findOperator(String value) {
        if (value.contains(">=")) {
            return ">=";
        } else if (value.contains("<=")) {
            return "<=";
        } else if (value.contains("<")) {
            return "<";
        } else if (value.contains(">")) {
            return ">";
        } else {
            return null;
        }
    }

    private int findNumber(String value) {
        String numberOnly = value.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

    private int findKeyValue(String value, Card card) {
        String keyOnly = value.replaceAll("[^a-zA-Z\\._]", "").toLowerCase();
        return getValueForKey(keyOnly, card);
    }

    private boolean ruleValueMatches(CardRule rule, String key, Card card) {
        int value = getValueForKey(key, card);
        String ruleStringValue = rule.getRuleValue(key);
        int ruleValue;
        try {
            ruleValue = findNumber(ruleStringValue);
        } catch(Exception e) {
            ruleValue = findKeyValue(ruleStringValue, card);
        }

        String operator = findOperator(ruleStringValue);

        return valueMatches(ruleValue, value, operator);
    }

    private boolean valueMatches(int ruleValue, int value, String operator) {
        if (operator == null) {
            return value == ruleValue;
        }

        switch (operator) {
            case "<":
                return value < ruleValue;
            case ">":
                return value > ruleValue;
            case "<=":
                return value <= ruleValue;
            case ">=":
                return value >= ruleValue;
            default:
                return value == ruleValue;
        }
    }

    public String getBotFile() {
        return botFile;
    }
}
