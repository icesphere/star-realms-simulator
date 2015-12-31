package starrealmssimulator.bots;

import starrealmssimulator.cards.*;
import starrealmssimulator.model.Card;

public class HareBot extends AttackBot {

    @Override
    public int getBuyCardScore(Card card) {
        int deck = getCurrentDeckNumber();

        //Blob
        if (card instanceof BattleBlob) {

        } else if (card instanceof BattlePod) {

        } else if (card instanceof BattleScreecher) {

        } else if (card instanceof BlobCarrier) {

        } else if (card instanceof BlobDestroyer) {

        } else if (card instanceof BlobFighter) {

        } else if (card instanceof BlobWheel) {

        } else if (card instanceof BlobWorld) {
            return 100;
        } else if (card instanceof DeathWorld) {
            if (deck < 3) {
                return 95;
            }
            if (deck == 3) {
                return 85;
            }
            return 75;
        } else if (card instanceof BreedingSite) {

        } else if (card instanceof Mothership) {

        } else if (card instanceof Ram) {

        } else if (card instanceof TheHive) {

        } else if (card instanceof TradePod) {

        }

        //Trade Federation
        else if (card instanceof BarterWorld) {
            if (deck == 1) {
                return 30;
            }
            return 15;
        } else if (card instanceof CentralOffice) {
            if (deck < 3) {
                return 35;
            } else if (deck == 3) {
                return 20;
            }
            return 15;
        } else if (card instanceof CommandShip) {
            return 80;
        } else if (card instanceof ConstructionHauler) {

        } else if (card instanceof Cutter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 40;
            }
            return 10;
        } else if (card instanceof DefenseCenter) {
            return 10;
        } else if (card instanceof EmbassyYacht) {
            if (deck == 1) {
                return 15;
            } else if (getNumBases() > 3) {
                return 30;
            }
        } else if (card instanceof FederationShuttle) {
            if (deck == 1) {
                return 15;
            }
        } else if (card instanceof Flagship) {
            return 60;
        } else if (card instanceof Freighter) {
            if (deck == 1) {
                return 30;
            }
        } else if (card instanceof Megahauler) {

        } else if (card instanceof PortOfCall) {
            if (deck == 1) {
                return 30;
            }
            return 15;
        } else if (card instanceof Starmarket) {
            int bases = getNumBases();
            if (deck == 1 && bases > 0) {
                return 10;
            } else if (deck == 1) {
                return 5;
            } else if (deck == 2 && bases > 0) {
                return 5;
            }
            return 0;
        } else if (card instanceof TradeEscort) {
            return 15;
        } else if (card instanceof TradeRaft) {
            if (deck == 1) {
                return 15;
            }
        } else if (card instanceof TradingPost) {
            if (deck == 1) {
                return 20;
            }
            return 5;
        }

        //Star Empire
        else if (card instanceof BattleBarge) {

        } else if (card instanceof Battlecruiser) {
            return 70;
        } else if (card instanceof CargoLaunch) {
            return 10;
        } else if (card instanceof Corvette) {

        } else if (card instanceof Dreadnaught) {
            return 90;
        } else if (card instanceof FleetHQ) {

        } else if (card instanceof ImperialFighter) {

        } else if (card instanceof ImperialFrigate) {

        } else if (card instanceof RecyclingStation) {

        } else if (card instanceof RoyalRedoubt) {
            return 40;
        } else if (card instanceof SpaceStation) {
            if (deck == 1) {
                return 40;
            }
            return 30;
        } else if (card instanceof StarFortress) {
            if (deck <= 3) {
                return 90;
            }
            return 80;
        } else if (card instanceof StarbaseOmega) {

        } else if (card instanceof SurveyShip) {

        } else if (card instanceof WarWorld) {

        }

        //Machine Cult
        else if (card instanceof BattleMech) {
            return 40;
        } else if (card instanceof BattleStation) {
            if (deck < 3) {
                return 15;
            }
            return 30;
        } else if (card instanceof BorderFort) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 15;
            }
        } else if (card instanceof BrainWorld) {
            if (deck < 3) {
                return 80;
            }
            if (deck == 3) {
                return 50;
            }
            return 15;
        } else if (card instanceof FortressOblivion) {

        } else if (card instanceof Junkyard) {

        } else if (card instanceof MachineBase) {
            return 30;
        } else if (card instanceof MechWorld) {
            return 15;
        } else if (card instanceof MissileBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 30;
            }
            return 5;
        } else if (card instanceof MissileMech) {

        } else if (card instanceof PatrolBot) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 20;
            }
            return 10;
        } else if (card instanceof PatrolMech) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            }
            return 20;
        } else if (card instanceof SupplyBot) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof StealthNeedle) {

        } else if (card instanceof TheArk) {
            if (deck < 3) {
                return 90;
            }
            return 80;
        } else if (card instanceof TradeBot) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 10;
            }
            return 0;
        }

        //Other
        else if (card instanceof Explorer) {

        }
        else if (card instanceof MercCruiser) {

        }

        return super.getBuyCardScore(card);
    }
}
