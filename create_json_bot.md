###JSON file format

Bots are created using the JSON file format.

If you have never used JSON before there are several tutorials online. Here are a couple I found:
http://www.w3resource.com/JSON/introduction.php
http://www.w3schools.com/json/

###JSON bot structure

Each bot is required to have a name and an author and then define rules which will determine which cards to buy, discard, scrap, etc.

Here is a look at a bot with empty rules so you can see all the different rules:

    {
        "name": "AttackBot",
        "author": "icesphere",
        "buyRules": [],
        "discardRules": [],
        "scrapRules": [],
        "scrapForBenefitRules": [],
        "useBaseRules": [],
        "playRules": [],
        "makeChoiceRules": []
    }

"name" - this is the name of your bot
"author" - this is the name of the person who created the bot

The remaining fields are sets of rules.
Each set of rules can list one or more cards, and each card will have a score or a set of rules that will determine the score.
If a card is not listed in a set of rules or if none of the rules for the card match then it will use the default value for that rule set.

###Buy Rules

"buyRules" - when the bot buys a card it will look at all cards available to buy and will sort them based on these rules and then will buy the card with the highest score.
Default score is 0.
When the buy score is 0 that means the bot will not buy the card.

Example buyRules:

    "buyRules": [
        {
            "card": "Mothership",
            "score": 100
        },
        {
            "card": "Blob Wheel",
            "rules": [
                {
                    "deck": 1,
                    "score": 50
                },
                {
                    "deck": 2,
                    "score": 25
                },
                {
                    "deck": 3,
                    "score": 5
                }
            ]
        },
        {
            "card": "Corvette",
            "rules": [
                {
                    "deck": "< 3",
                    "score": 10
                },
                {
                    "score": 20
                }
            ]
        }
    ]

If you always want a card to have the same score you can just set the score without any rules, like I did above for Mothership.

Otherwise you can create a list of rules that will get applied in order.
The first rule that has all conditions matched will be used.
If one or more of the conditions for a rule do not match it will look at the next rule.
If none of the rules match it will use the default score of 0 and the card will not be bought.

In the Blob Wheel example there are 3 rules.
"deck" means which deck cycle you are in - this is equivalent to the number of times the deck has been shuffled + 1.
So if the deck has not been shuffled yet then you are in deck 1 and the score for Blob Wheel would be 50.
After your first shuffle you are in deck 2 and the score for Blob Wheel would be 25.
After your second shuffle you are in deck 3 and the score for Blob Wheel would be 5.
After your third shuffle you are in deck 4 and Blob Wheel would not be bought for the rest of the game.

In the Corvette example there are 2 rules.
Conditions are allowed to have the following operators: (<, >, <=, >=).
When using operators the value must be in "".
If you are in your first or second deck then the score for Corvette would be 10.
The second rule doesn't have any conditions.
A rule without any conditions means that if all the rules before it do not apply then this rule will always apply.
So in this example, once you are past your second deck the score for Corvette for the rest of the game would be 20.

###Discard Rules

"discardRules" - when a bot needs to discard a card, it will look at all of the cards available to discard and will sort them based on these rules and will discard cards with the highest score first.
When multiple cards are being discarded it will discard cards from highest score to lowest.
When discarding is optional a card with a score less than 20 will not be discarded.
The default score is (20 - card.cost). (Example: Mothership costs 7, so the default score for Mothership would be 13, and would not be discarded when discarding is optional)

Example discardRules:

    "discardRules": [
        {
            "card": "Viper",
            "score": 100
        },
        {
            "card": "Scout",
            "score": 90
        },
        {
            "card": "Explorer",
            "score": 80
        }
    ]

In this example a Viper would be discarded before a Scout which would be discarded before an Explorer.
All other cards would not be discarded when discarding is optional.
When discarding is not optional cheaper cards would be discarded before more expensive cards.

###Scrap Rules

"scrapRules" - follows the same rules as "discardRules" above

###Scrap for Benefit Rules

"scrapForBenefitRules" -  This is for cards that have the ability to scrap themselves for a benefit.
Example: Blob Wheel can be scrapped to gain 3 Trade.
Default score is 0.
When the score is 0 the card will not be scrapped.
If there are multiple cards that can be scrapped for benefit, cards will be scrapped from highest score to lowest.

Example scrapForBenefitRules:

    "scrapForBenefitRules": [
        {
            "card": "Barter World",
            "rules": [
                {
                    "opponent.authority": "< 6",
                    "score": 10
                }
            ]
        },
        {
            "card": "Battlecruiser",
            "rules": [
                {
                    "opponent.outposts.inplay": "> 0",
                    "opponent.authority": "<= combat",
                    "score": 10
                },
                {
                    "opponent.authority": "< 6",
                    "score": 5
                }
            ]
        },
        {
            "card": "Blob Wheel",
            "rules": [
                {
                    "deck": "< 3",
                    "buy.score.increase": "> 20",
                    "score": 5
                },
                {
                    "buy.score.increase": "> 40",
                    "score": 5
                }
            ]
        }
    ]

In this example a Barter World would be scrapped when the opponent's authority is less than 6.
The Battlecruiser would be scrapped if the opponent has an outpost in play and the opponent's authority is <= your combat.
The Battlecruiser would also be scrapped if the opponent's authority is less than 6.
The Blob Wheel would be scrapped if you are in your first or second deck and the buy score of the card you could buy with the extra trade is at least 20 higher than what you would be able to buy without the extra trade.
If you are past your second deck then the buy score increase would need be at least 40 in order for Blob Wheel to be scrapped.

###Use Base Rules

"useBaseRules" - Determines when to use base abilities.
Default score is 20.
When score is 0 then the base will not be used.
If there are multiple bases with a score greater than 0 then bases will be used from highest score to lowest.

Example useBaseRules:

    "useBaseRules": [
        {
            "card": "Recycling Station",
            "score": 100
        },
        {
            "card": "Machine Base",
            "score": 90
        },
        {
            "card": "Blob World",
            "score": 10
        }
    ]

In this example Recycling Station would be used first, followed by Machine Base, followed by all other bases, and then Blob World would be used last.

###Play Rules

"playRules" - Determines which order cards from your hand will be played in.
Default score is 20.
Cards will be played from highest score to lowest.
Additional options:
"useBaseAfterPlay" - Base will be used immediately after being played.
"useAllyAfterPlay" - Allied ability will be used (if there is an ally) immediately after being played.

Example playRules:

    "playRules": [
        {
            "card": "Recycling Station",
            "score": 100,
            "useBaseAfterPlay": true
        },
        {
            "card": "Supply Bot",
            "score": 95
        },
        {
            "card": "Patrol Mech",
            "score": 80,
            "useAllyAfterPlay": true
        },
        {
            "card": "Stealth Needle",
            "score": 10
        },
        {
            "card": "Explorer",
            "score": 9
        },
        {
            "card": "Scout",
            "score": 8
        },
        {
            "card": "Viper",
            "score": 7
        }
    ]

In this example Recycling Station would be played first and would immediately use it's ability.
Supply Bot would be played next.
Patrol Mech would be played next and would immediately use it's allied ability if there was an ally available.
All other cards not listed would be played.
Then Stealth Needle would be played followed by Explorer, Scout, and Viper.

###Make Choice Rules

"makeChoiceRules" - Determines which choice will be made for each card.
Defaults to the first choice on the card.
Each card and each rule for the card needs a choice instead of a score.
Choice should be set to 1 for the first choice on the card, 2 for the second choice, etc.

Example makeChoiceRules:

    "makeChoiceRules": [
        {
            "card": "Blob World",
            "rules": [
                {
                    "cards.played.blob": "> 1",
                    "opponent.authority": "> 5",
                    "choice": 2
                },
                {
                    "choice": 1
                }
            ]
        },
        {
            "card": "Patrol Mech",
            "rules": [
                {
                    "deck": "> 2",
                    "choice": 2
                },
                {
                    "opponent.authority": "< 30",
                    "choice": 2
                },
                {
                    "choice": 1
                }
            ]
        },
        {
            "card": "Recycling Station",
            "choice": 2
        }
    ]

In this example Blob World would use the second option (Draw a card for each Blob card that you've played this turn) if it has played 2 or more Blob cards and if the opponent's authority is greater than 5.
Otherwise Blob World will use the first option (5 combat).
Patrol Mech will use the second option (Add 5 combat) if they are past their second deck or if the opponent's authority is < 30.
Otherwise Patrol Mech will use the first option (3 trade).
Recycling Station will always use the second option (Discard up to two cards, then draw that many cards)

###Attack Base Rules

"attackBaseRules" - Determines which base to attack first.
Default score is the cost of the base.
If score is 0 the base will not be attacked.

###Destroy Base Rules

"destroyBaseRules" - Determines which base to destroy when a card has a destroy base ability.
Default score is the cost of the base.
If score is 0 the base will not be destroyed.

###Copy Ship Rules

"copyShipRules" - Determines which ship to copy for Stealth Needle.
Default score is the cost of the ship.
If score is 0 the ship will not be copied.
Score for Stealth Needle is always 0 so that it cannot be copied.

###Scrap Trade Row Rules

"scrapTradeRowRules" - Determines which ship to scrap from the trade row.
Default score is 0.
If score is 0 then the card will not be scrapped from the trade row.

###Card to Top of Deck Rules

"cardToTopOfDeckRules" - Determines which card to put on top of your deck.
Default score is the cost of the card.
If score is 0 then the card will not be put on top of the deck.

###Card Info

"cardInfo" - Adds additional info about cards which your bot can then use in any of the rule conditions.
Each card can have an any number of info values.
Each info key can be anything.
Each info value can be a number or it can also resolve based on any of the options available when creating a rule condition.
Alternatively the info value can also have a set of rules to decide what the value will be.
If an info value is not found for a card it will use a value of 0.

Example cardInfo:

    "cardInfo": [
        {
            "card": "Cutter",
            "info": {
                "aggressive": 1,
                "defensive": 5,
                "velocity": 0,
                "economy": 3,
                "startGame": [
                    {
                        "cards.all.trade_federation": "> 0",
                        "score": 10
                    },
                    {
                        "score": 9
                    }
                ],
                "midGame": 5,
                "endGame": 2
            }
        },
        {
            "card": "Recycling Station",
            "info": {
                "aggressive": 3,
                "defensive": 2,
                "protection": 4,
                "velocity": 3,
                "economy": 1,
                "startGame": [
                    {
                        "cards.all.star_empire": "> 0",
                        "score": 3
                    },
                    {
                        "score": 2
                    }
                ],
                "midGame": 5,
                "endGame": 4
            }
        },
        {
            "card": "Imperial Frigate",
            "info": {
                "aggressive": 5,
                "defensive": 0,
                "velocity": 1,
                "economy": 0,
                "startGame": [
                    {
                        "opponent.bases": "> 0",
                        "score": 5
                    },
                    {
                        "card_info_avg_aggressive": "> 2",
                        "score": 4
                    },
                    {
                        "cards.all.star_empire": "> 0",
                        "score": 4
                    },
                    {
                        "trade_row_card_info_avg_protection": "> 0",
                        "score": 3
                    },
                    {
                        "score": 2
                    }
                ],
                "midGame": 5,
                "endGame": 7
            }
        }
    ]

Available keys:

"card_info_" + info key: this will return the value you have defined for the info key for the current card.

"card_info_avg_" + info key: this will return the avg value for the info key across all of your bot's cards. (played, bases, hand, deck, discard).

"opponent_card_info_avg_" + info key: this will return the avg value for the info key across all of your opponent's cards. (bases, hand, deck, discard).

"trade_row_card_info_avg_" + info key: this will return the avg value for the info key for all cards currently in the trade row.


Example buyRules using card info instead of listing each card:

    "buyRules": [
        {
            "card": "*",
            "rules": [
                {
                    "deck": "<= 2",
                    "score": "card_info_startGame"
                },
                {
                    "deck": 3,
                    "score": "card_info_midGame"
                },
                {
                    "score": "card_info_endGame"
                }
            ]
        }
    ]

In this example the value for each card for the buyRules comes from the card info defined above.

Imperial Frigate: When an opponent has bases, or if you have already bought an aggressive card or a yellow card, or if there are bases in the trade row that will provide your opponent protection, then the Imperial Frigate will have a higher value in the start of the game.

###Rule Conditions

There are several options available to check against when creating conditions for your rules:

deck (deck cycle - number of times shuffled + 1 - see HomerJr’s post on four and a half decks)
turn (current turn in the game - this is not per player so the second player's first turn would be turn 2)
card.cost (the cost to buy the card - Example: card.cost for Mothership would be 7).

trade (current trade amount)
combat (current combat amount)
authority (current authority for your bot)
opponent.authority (opponent's current authority)

deck.size (number of cards in your deck)
discard.size (number of cards in your discard)
hand.size (number of cards in your hand)

opponent.deck.size (number of cards in your opponent's deck)
opponent.discard.size (number of cards in your opponent's discard)
opponent.hand.size (number of cards in your opponent's hand)

ships (number of ships - in play, deck, discard, hand)
opponent.ships (see above)

bases (number of bases - includes all bases and outposts (in play, in deck, in discard, in hand)
outposts (number of outposts - includes all outposts (in play, in deck, in discard, in hand)

opponent.bases (see above)
opponent.outposts (see above)

bases.inplay (number of bases/outposts in play)
opponent.bases.inplay (see above)

outposts.inplay (number of outposts in play)
opponent.outposts.inplay (see above)

bases.unused (number of bases/outposts that have not been used this turn)

opponent.bases.smallest (smallest shield value of opponents bases/outposts in play)
opponent.bases.biggest (biggest shield value of opponents bases/outposts in play)
opponent.outposts.smallest (smallest shield value of opponents outposts in play)
opponent.outposts.biggest (biggest shield value of opponents outposts in play)

buyScoreIncrease (only applies to cards that can scrap for trade - increase in highest buy score if you scrapped this card - see example in scrapForBenefitRules example above)
combat.plus.card.scrap.combat (current combat plus amount of combat the card can be scrapped for)

trade_row.blob (number of blob cards in the trade row)
trade_row.trade_federation (number of trade federation cards in the trade row)
trade_row.machine_cult (number of machine cult cards in the trade row)
trade_row.star_empire (number of star empire cards in the trade row)

cards.played.blob (number of blob cards played this turn)
cards.played.trade_federation (number of trade federation cards played this turn)
cards.played.machine_cult (number of machine cult cards played this turn)
cards.played.star_empire (number of star empire cards played this turn)
cards.played.starter_cards (number of Scouts and Vipers played this turn)

cards.hand.blob (number of blob cards in your hand)
cards.hand.trade_federation
cards.hand.machine_cult
cards.hand.star_empire
cards.hand.starter_cards (number of Scouts and Vipers in your hand)

cards.discard.blob (number of blob cards in your discard)
cards.discard.trade_federation
cards.discard.machine_cult
cards.discard.star_empire
cards.discard.starter_cards (number of Scouts and Vipers in your discard)

cards.deck.blob (number of blob cards in your deck)
cards.deck.trade_federation
cards.deck.machine_cult
cards.deck.star_empire
cards.deck.starter_cards (number of Scouts and Vipers in your deck)

cards.all.blob (number of blob cards in play, hand, deck, discard)
cards.all.trade_federation
cards.all.machine_cult
cards.all.star_empire
cards.all.starter_cards (number of Scouts and Vipers in play, hand, deck, discard)

opponent.cards.discard.blob (number of blob cards in your opponent's discard)
opponent.cards.discard.trade_federation
opponent.cards.discard.machine_cult
opponent.cards.discard.star_empire
opponent.cards.discard.starter_cards (number of Scouts and Vipers in your opponent's discard)

opponent.cards.deck.blob (number of blob cards in your opponent's deck)
opponent.cards.deck.trade_federation
opponent.cards.deck.machine_cult
opponent.cards.deck.star_empire
opponent.cards.deck.starter_cards (number of Scouts and Vipers in your opponent's deck)

opponent.cards.all.blob (number of blob cards in play, hand, deck, discard)
opponent.cards.all.trade_federation
opponent.cards.all.machine_cult
opponent.cards.all.star_empire
opponent.cards.all.starter_cards (number of Scouts and Vipers in play, hand, deck, discard)

###Rule Condition Values

The above options can also be used in a condition value.
In the "scrapForBenefitRules" example above the Battlecruiser has the following rule that checks the opponent's authority against your current combat:

    {
        "opponent.outposts.inplay": "> 0",
        "opponent.authority": "<= combat",
        "score": 10
    }

###Card Matchers

In addition to the "card" matcher which matches on the card name - there are 3 other matchers:

* "outpost" - this will match any outpost
* "base" - this will match any base or outpost
* "ship" - this will match any ship
* "*" - this will match any card

These matchers will only be used if there is not a match found for the card name.

###Bot Take Turn Logic

Current bot logic when it takes a turn:

- Check to see if you want to scrap any cards for benefit.

- Check to see if you want to use any base abilities.

- Play cards from your hand.

- Use allied abilities.

- Buy Cards.

- If a base was used, or a card was played, or a card was bought
    - Repeat the above steps

- Otherwise
    - Apply combat to opponent's outposts
    - If combat >= opponent's authority
        - Finish off opponent
    - Otherwise
        - Attack bases
        - Apply remaining combat to opponent's authority
    - End Turn

###Current Bots

ExpensiveBot - Always buys the most expensive card

VelocityBot - Favors scrap

AttackBot - Favors attack

DefenseBot - Favors lifegain/defense

HareBot - Combination of attack and scrap

TortoiseBot - Combination of defense and scrap

ExplorerBot - Only buys Explorers and always scraps Explorers for Benefit.

DoNothingBot - Does not buy any cards.

UnrulyBot - Created by gmc14 - Rules are empty or have a score of 100.

BlueGreenBot, BlueYellowBot, GreenYellowBot, RedBlueBot, RedGreenBot, YellowRedBot - Favor buying cards in their two colors.

SmartBot - (Work in progress) - the idea is to have a bot that can adapt it's strategy based on trade row, current cards, current opponent cards, etc.



