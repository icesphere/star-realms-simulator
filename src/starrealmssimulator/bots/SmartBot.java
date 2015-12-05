package starrealmssimulator.bots;

import starrealmssimulator.cards.*;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;

public class SmartBot extends Bot {
    @Override
    public String getPlayerName() {
        return "SmartBot";
    }

    @Override
    public int getBuyCardScore(Card card) {
        //todo

        //Blob
        if (card instanceof BattleBlob) {

        } else if (card instanceof BattlePod) {

        } else if (card instanceof BattleScreecher) {

        } else if (card instanceof BlobCarrier) {

        } else if (card instanceof BlobDestroyer) {

        } else if (card instanceof BlobFighter) {

        } else if (card instanceof BlobWheel) {

        } else if (card instanceof BlobWorld) {

        } else if (card instanceof BreedingSite) {

        } else if (card instanceof Mothership) {

        } else if (card instanceof Ram) {

        } else if (card instanceof TheHive) {

        } else if (card instanceof TradePod) {

        }

        //Trade Federation
        else if (card instanceof BarterWorld) {

        } else if (card instanceof CentralOffice) {

        } else if (card instanceof CommandShip) {

        } else if (card instanceof Cutter) {

        } else if (card instanceof DefenseCenter) {

        } else if (card instanceof EmbassyYacht) {

        } else if (card instanceof FederationShuttle) {

        } else if (card instanceof Flagship) {

        } else if (card instanceof Freighter) {

        } else if (card instanceof Megahauler) {

        } else if (card instanceof PortOfCall) {

        } else if (card instanceof Starmarket) {

        } else if (card instanceof TradeEscort) {

        } else if (card instanceof TradingPost) {

        }

        //Star Empire
        else if (card instanceof BattleBarge) {

        } else if (card instanceof Battlecruiser) {

        } else if (card instanceof Corvette) {

        } else if (card instanceof Dreadnaught) {

        } else if (card instanceof FleetHQ) {

        } else if (card instanceof ImperialFighter) {

        } else if (card instanceof ImperialFrigate) {

        } else if (card instanceof RecyclingStation) {

        } else if (card instanceof RoyalRedoubt) {

        } else if (card instanceof SpaceStation) {

        } else if (card instanceof StarbaseOmega) {

        } else if (card instanceof SurveyShip) {

        } else if (card instanceof WarWorld) {

        }

        //Machine Cult
        else if (card instanceof BattleMech) {

        } else if (card instanceof BattleStation) {

        } else if (card instanceof BrainWorld) {

        } else if (card instanceof FortressOblivion) {

        } else if (card instanceof Junkyard) {

        } else if (card instanceof MachineBase) {

        } else if (card instanceof MechWorld) {

        } else if (card instanceof MissileBot) {

        } else if (card instanceof MissileMech) {

        } else if (card instanceof PatrolMech) {

        } else if (card instanceof SupplyBot) {

        } else if (card instanceof StealthNeedle) {

        } else if (card instanceof TheArk) {

        } else if (card instanceof TradeBot) {

        }

        //Other
        else if (card instanceof Explorer) {

        }
        else if (card instanceof MercCruiser) {

        }

        return card.getCost();
    }
}
