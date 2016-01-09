package starrealmssimulator.bots;

import starrealmssimulator.cards.bases.blob.*;
import starrealmssimulator.cards.bases.outposts.machinecult.*;
import starrealmssimulator.cards.bases.outposts.starempire.*;
import starrealmssimulator.cards.bases.outposts.tradefederation.CapitolWorld;
import starrealmssimulator.cards.bases.outposts.tradefederation.DefenseCenter;
import starrealmssimulator.cards.bases.outposts.tradefederation.PortOfCall;
import starrealmssimulator.cards.bases.outposts.tradefederation.TradingPost;
import starrealmssimulator.cards.bases.starempire.FleetHQ;
import starrealmssimulator.cards.bases.starempire.StarbaseOmega;
import starrealmssimulator.cards.bases.tradefederation.BarterWorld;
import starrealmssimulator.cards.bases.tradefederation.CentralOffice;
import starrealmssimulator.cards.bases.tradefederation.Starmarket;
import starrealmssimulator.cards.heroes.*;
import starrealmssimulator.cards.ships.*;
import starrealmssimulator.cards.ships.blob.*;
import starrealmssimulator.cards.ships.machinecult.*;
import starrealmssimulator.cards.ships.starempire.*;
import starrealmssimulator.cards.ships.tradefederation.*;
import starrealmssimulator.model.Bot;
import starrealmssimulator.model.Card;

public class AttackBot extends Bot {

    @Override
    public int getBuyCardScore(Card card) {
        int deck = getCurrentDeckNumber();
        int bases = getNumBases();

        //Heroes
        if (card instanceof RamPilot) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof BlobOverlord) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 40;
            }
            return 20;
        } else if (card instanceof SpecialOpsDirector) {
            if (deck == 1) {
                return 1;
            } else if (deck <= 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof CeoTorres) {
            if (deck == 1) {
                return 5;
            } else if (deck <= 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof WarElder) {
            if (deck == 1) {
                return 10;
            } else if (deck <= 3) {
                return 25;
            }
            return 5;
        } else if (card instanceof HighPriestLyle) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof CunningCaptain) {
            if (deck == 1) {
                return 15;
            } else if (deck <= 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof AdmiralRasmussen) {
            if (deck == 1) {
                return 20;
            } else if (deck <= 3) {
                return 40;
            }
            return 20;
        }

        //Blob
        if (card instanceof BattleBlob) {
            return 85;
        } else if (card instanceof BattlePod) {
            if (deck < 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof BattleScreecher) {
            if (deck < 3) {
                return 50;
            }
            return 40;
        } else if (card instanceof BlobCarrier) {
            if (deck < 3) {
                return 90;
            }
            return 70;
        } else if (card instanceof BlobDestroyer) {
            int opponentBases = getOpponent().getNumBases();
            if (opponentBases > 3) {
                return 80;
            } else if (opponentBases > 2) {
                return 70;
            }
            return 60;
        } else if (card instanceof BlobFighter) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 50;
            } else {
                return 60;
            }
        } else if (card instanceof BlobWheel) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof BlobWorld) {
            return 80;
        } else if (card instanceof BreedingSite) {
            if (deck < 3 && bases > 0) {
                return 55;
            } else if (deck < 3 || bases >= 2) {
                return 50;
            } else if (bases == 0) {
                return 30;
            }
            return 40;
        } else if (card instanceof DeathWorld) {
            if (deck < 3) {
                return 90;
            }
            if (deck == 3) {
                return 80;
            }
            return 70;
        } else if (card instanceof Mothership) {
            return 100;
        } else if (card instanceof Obliterator) {
            int opponentBases = getOpponent().getNumBases();
            if (opponentBases > 3) {
                return 95;
            } else if (opponentBases > 2) {
                return 85;
            }
            return 75;
        } else if (card instanceof Ram) {
            if (deck < 3) {
                return 70;
            }
            return 55;
        } else if (card instanceof SpikePod) {
            if (deck < 3) {
                return 35;
            }
            return 25;
        } else if (card instanceof TheHive) {
            if (deck <= 2) {
                return 20;
            }
            return 50;
        } else if (card instanceof TradePod) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 10;
            } else {
                return 5;
            }
        } else if (card instanceof TradeWheel) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
        }

        //Trade Federation
        else if (card instanceof BarterWorld) {
            if (deck == 1) {
                return 25;
            }
            return 10;
        } else if (card instanceof CapitolWorld) {
            if (deck <= 3) {
                return 60;
            }
            return 40;
        } else if (card instanceof CentralOffice) {
            return 15;
        } else if (card instanceof CommandShip) {
            return 75;
        } else if (card instanceof ConstructionHauler) {
            if (deck < 3) {
                return 30;
            } else if (deck == 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof CustomsFrigate) {
            if (deck < 3) {
                return 10;
            }
            return 5;
        } else if (card instanceof Cutter) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof DefenseCenter) {
            return 5;
        } else if (card instanceof EmbassyYacht) {
            return 0;
        } else if (card instanceof FederationShuttle) {
            return 0;
        } else if (card instanceof Flagship) {
            return 50;
        } else if (card instanceof Freighter) {
            return 0;
        } else if (card instanceof Megahauler) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof PortOfCall) {
            return 10;
        } else if (card instanceof Starmarket) {
            if (deck == 1) {
                return 10;
            }
        } else if (card instanceof TradeEscort) {
            return 10;
        } else if (card instanceof TradeRaft) {
            return 0;
        } else if (card instanceof TradingPost) {
            if (deck == 1) {
                return 15;
            }
        }

        //Star Empire
        else if (card instanceof BattleBarge) {
            if (bases >= 4) {
                return 60;
            } else if (bases >= 2) {
                return 50;
            }
            return 40;
        } else if (card instanceof Battlecruiser) {
            return 75;
        } else if (card instanceof CargoLaunch) {
            return 10;
        } else if (card instanceof Corvette) {
            if (deck < 3) {
                return 10;
            }
            return 20;
        } else if (card instanceof Dreadnaught) {
            return 95;
        } else if (card instanceof FighterBase) {
            if (deck == 1) {
                return 10;
            }
            return 20;
        } else if (card instanceof FleetHQ) {
            if (deck < 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof ImperialFighter) {
            if (deck == 1) {
                return 10;
            }
            return 15;
        } else if (card instanceof ImperialFrigate) {
            if (deck < 3) {
                return 30;
            }
            return 40;
        } else if (card instanceof ImperialTrader) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 60;
            } else if (deck == 3) {
                return 50;
            }
            return 40;
        } else if (card instanceof RecyclingStation) {
            return 30;
        } else if (card instanceof RoyalRedoubt) {
            return 30;
        } else if (card instanceof SpaceStation) {
            if (deck == 1) {
                return 30;
            }
            return 10;
        } else if (card instanceof StarFortress) {
            if (deck <= 3) {
                return 85;
            }
            return 75;
        } else if (card instanceof StarbaseOmega) {
            if (deck < 3 && bases > 0) {
                return 5;
            } else if (deck < 3) {
                return 0;
            } else if (bases >= 2) {
                return 5;
            } else if (bases == 0) {
                return 0;
            }
            return 5;
        } else if (card instanceof SurveyShip) {
            return 5;
        } else if (card instanceof WarWorld) {
            return 30;
        }

        //Machine Cult
        else if (card instanceof BattleMech) {
            return 30;
        } else if (card instanceof BattleStation) {
            if (deck < 3) {
                return 10;
            }
            return 20;
        } else if (card instanceof BorderFort) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof BrainWorld) {
            if (deck < 3) {
                return 80;
            }
            if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof DefenseBot) {
            if (deck < 3 && bases > 0) {
                return 10;
            } else if (deck < 3) {
                return 5;
            }
        } else if (card instanceof FortressOblivion) {
            if (deck < 3 && bases > 0) {
                return 10;
            } else if (deck < 3) {
                return 5;
            }
        } else if (card instanceof Junkyard) {
            return 0;
        } else if (card instanceof MachineBase) {
            if (deck < 3) {
                return 30;
            }
            if (deck == 3) {
                return 10;
            }
        } else if (card instanceof MechWorld) {
            return 5;
        } else if (card instanceof MegaMech) {
            if (bases >= 4) {
                return 60;
            } else if (bases >= 2) {
                return 50;
            }
            return 35;
        } else if (card instanceof MissileBot) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 10;
            }
        } else if (card instanceof MissileMech) {
            if (getOpponent().getNumBases() > 3) {
                return 70;
            }
            return 50;
        } else if (card instanceof PatrolBot) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 10;
            }
        } else if (card instanceof PatrolMech) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof SupplyBot) {
            if (deck == 1) {
                return 20;
            } else if (deck == 2) {
                return 10;
            }
        } else if (card instanceof StealthNeedle) {
            if (deck < 3) {
                return 30;
            }
            return 50;
        } else if (card instanceof TheArk) {
            if (deck < 3) {
                return 80;
            }
            return 75;
        } else if (card instanceof TradeBot) {
            if (deck == 1) {
                return 10;
            }
        }

        //Other
        else if (card instanceof Explorer) {
            if (deck <= 2) {
                return 5;
            }
        } else if (card instanceof MercCruiser) {
            if (deck == 1) {
                return 20;
            }
            return 40;
        }

        return 0;
    }
}
