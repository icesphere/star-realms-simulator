package starrealmssimulator.bots.strategies;

import starrealmssimulator.cards.bases.blob.*;
import starrealmssimulator.cards.bases.outposts.machinecult.*;
import starrealmssimulator.cards.bases.outposts.starempire.*;
import starrealmssimulator.cards.bases.outposts.tradefederation.*;
import starrealmssimulator.cards.bases.starempire.FleetHQ;
import starrealmssimulator.cards.bases.starempire.OrbitalPlatform;
import starrealmssimulator.cards.bases.starempire.StarbaseOmega;
import starrealmssimulator.cards.bases.tradefederation.*;
import starrealmssimulator.cards.heroes.*;
import starrealmssimulator.cards.ships.Explorer;
import starrealmssimulator.cards.ships.MercCruiser;
import starrealmssimulator.cards.ships.blob.*;
import starrealmssimulator.cards.ships.machinecult.*;
import starrealmssimulator.cards.ships.starempire.*;
import starrealmssimulator.cards.ships.tradefederation.*;
import starrealmssimulator.model.BotStrategy;
import starrealmssimulator.model.Card;
import starrealmssimulator.model.Player;

public class VelocityStrategy implements BotStrategy {
    @Override
    public int getBuyCardScore(Card card, Player player) {
        int deck = player.getCurrentDeckNumber();
        int bases = player.getNumBases();
        Player opponent = player.getOpponent();
        int opponentBases = opponent.getNumBases();

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
                return 0;
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
                return 20;
            } else if (deck <= 3) {
                return 35;
            }
            return 10;
        } else if (card instanceof HighPriestLyle) {
            if (deck == 1) {
                return 30;
            } else if (deck <= 3) {
                return 45;
            }
            return 15;
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
            if (deck < 3) {
                return 40;
            }
            return 70;
        } else if (card instanceof BattlePod) {
            if (deck < 3) {
                return 20;
            }
            return 15;
        } else if (card instanceof BattleScreecher) {
            if (deck < 3) {
                return 50;
            }
            return 25;
        } else if (card instanceof Bioformer) {
            if (deck == 1) {
                return 55;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof BlobCarrier) {
            if (deck < 3) {
                return 60;
            }
            return 40;
        } else if (card instanceof BlobDestroyer) {
            if (opponentBases > 3) {
                return 60;
            } else if (opponentBases > 2) {
                return 50;
            } else if (deck < 3) {
                return 30;
            }
            return 40;
        } else if (card instanceof BlobFighter) {
            if (deck == 1) {
                return 5;
            } else if (deck == 2) {
                return 10;
            }
            return 40;
        } else if (card instanceof BlobWheel) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof BlobWorld) {
            if (bases > 3) {
                return 80;
            }
            return 60;
        } else if (card instanceof CargoPod) {
            if (deck == 1) {
                return 65;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 10;
            }
        } else if (card instanceof DeathWorld) {
            if (deck < 3) {
                return 80;
            }
            if (deck == 3) {
                return 70;
            }
            return 60;
        } else if (card instanceof BreedingSite) {
            if (deck < 3 && bases > 0) {
                return 55;
            } else if (deck < 3 || bases >= 2) {
                return 50;
            } else if (bases == 0) {
                return 10;
            }
            return 20;
        } else if (card instanceof Leviathan) {
            return 100;
        } else if (card instanceof Moonwurm) {
            return 80;
        } else if (card instanceof Mothership) {
            return 80;
        } else if (card instanceof Obliterator) {
            if (opponentBases > 3) {
                return 70;
            } else if (opponentBases > 2) {
                return 60;
            }
            return 50;
        } else if (card instanceof Parasite) {
            if (deck < 3) {
                return 50;
            }
            return 30;
        } else if (card instanceof PlasmaVent) {
            if (player.blobCardPlayedThisTurn()) {
                return 15;
            } else {
                if (deck <= 2) {
                    return 5;
                } else if (deck == 3) {
                    return 10;
                }
            }
        } else if (card instanceof Predator) {
            if (deck == 1) {
                return 10;
            } else if (deck == 2) {
                return 15;
            }
            return 45;
        } else if (card instanceof Ram) {
            if (deck < 3) {
                return 60;
            }
            return 30;
        } else if (card instanceof Ravager) {
            return 35;
        } else if (card instanceof SpikePod) {
            if (deck < 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof StellarReef) {
            if (deck <= 2) {
                return 5;
            }
        } else if (card instanceof Swarmer) {
            if (deck < 3) {
                return 15;
            }
            return 10;
        } else if (card instanceof TheHive) {
            if (bases > 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof TradePod) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof TradeWheel) {
            if (deck == 1) {
                return 5;
            }
        }

        //Trade Federation
        else if (card instanceof BarterWorld) {
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 30;
            }
            return 20;
        } else if (card instanceof CapitolWorld) {
            if (deck <= 3) {
                return 85;
            }
            return 60;
        } else if (card instanceof CentralOffice) {
            if (deck < 3) {
                return 80;
            } else if (deck == 3) {
                return 60;
            }
            return 50;
        } else if (card instanceof CentralStation) {
            if (deck == 1 && bases > 0) {
                return 35;
            } else if (deck == 1) {
                return 30;
            } else if (deck == 2 && bases > 0) {
                return 25;
            } else if (deck == 2) {
                return 15;
            } else if (deck == 3 && bases >= 3) {
                return 15;
            }
            if (bases >= 4) {
                return 10;
            }
        } else if (card instanceof ColonySeedShip) {
            if (player.tradeFederationCardPlayedThisTurn()) {
                if (deck == 1) {
                    return 95;
                } else if (deck == 2) {
                    return 85;
                } else if (deck == 3) {
                    return 60;
                }
                return 40;
            } else {
                if (deck == 1) {
                    return 90;
                } else if (deck == 2) {
                    return 80;
                } else if (deck == 3) {
                    return 45;
                }
                return 25;
            }
        } else if (card instanceof CommandShip) {
            return 95;
        } else if (card instanceof ConstructionHauler) {
            if (deck < 3) {
                return 80;
            } else if (deck == 3) {
                return 60;
            }
            return 40;
        } else if (card instanceof CustomsFrigate) {
            if (deck < 3) {
                return 30;
            } else if (deck == 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof Cutter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 70;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof DefenseCenter) {
            if (deck == 1) {
                return 5;
            } else if (deck == 2) {
                return 10;
            }
            return 20;
        } else if (card instanceof EmbassyYacht) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2 && bases > 1) {
                return 65;
            } else if (deck == 2) {
                return 50;
            } else if (bases > 3) {
                return 60;
            }
            return 20;
        } else if (card instanceof FactoryWorld) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 60;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof FederationShipyard) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof FederationShuttle) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof Flagship) {
            if (deck < 3) {
                return 45;
            }
            return 65;
        } else if (card instanceof Freighter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 20;
            }
        } else if (card instanceof FrontierFerry) {
            if (deck == 1) {
                return 90;
            } else if (deck == 2) {
                return 80;
            } else if (deck == 3) {
                return 50;
            }
            return 40;
        } else if (card instanceof LoyalColony) {
            if (deck < 3) {
                return 80;
            } else if (deck == 3) {
                return 60;
            }
            return 50;
        } else if (card instanceof Megahauler) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 10;
        } else if (card instanceof PortOfCall) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            }
            return 30;
        } else if (card instanceof PatrolCutter) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 70;
            } else if (deck == 3) {
                return 40;
            }
            return 30;
        } else if (card instanceof Peacekeeper) {
            if (deck < 3) {
                return 40;
            }
            return 60;
        } else if (card instanceof SolarSkiff) {
            if (deck == 1) {
                return 30;
            } else if (deck == 2) {
                return 15;
            }
        } else if (card instanceof Starmarket) {
            if (deck == 1 && bases > 0) {
                return 40;
            } else if (deck == 1) {
                return 35;
            } else if (deck == 2 && bases > 0) {
                return 30;
            } else if (deck == 2) {
                return 25;
            } else if (deck == 3) {
                return 15;
            }
            return 5;
        } else if (card instanceof StorageSilo) {
            if (deck == 1) {
                return 5;
            }
        } else if (card instanceof TradeEscort) {
            if (deck < 3) {
                return 25;
            }
            return 50;
        } else if (card instanceof TradeHauler) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof TradeRaft) {
            if (deck == 1) {
                return 35;
            } else if (deck == 2) {
                return 20;
            }
        } else if (card instanceof TradingPost) {
            if (deck == 1) {
                return 10;
            } else if (deck == 2) {
                return 5;
            }
        }

        //Star Empire
        else if (card instanceof AgingBattleship) {
            return 70;
        } else if (card instanceof BattleBarge) {
            if (bases >= 4) {
                return 50;
            } else if (bases >= 2) {
                return 40;
            }
            return 30;
        } else if (card instanceof Battlecruiser) {
            return 80;
        } else if (card instanceof CargoLaunch) {
            return 5;
        } else if (card instanceof CommandCenter) {
            int numberOfStarEmpireCards = player.countCardsByType(player.getAllCards(), Card::isStarEmpire);
            if (deck == 1) {
                return 40;
            } else if (deck == 2) {
                return 15 + (5 * numberOfStarEmpireCards);
            }
            return 5 * numberOfStarEmpireCards;
        } else if (card instanceof Corvette) {
            if (deck < 3) {
                return 5;
            }
            return 10;
        } else if (card instanceof Dreadnaught) {
            return 90;
        } else if (card instanceof EmperorsDreadnaught) {
            if (player.starEmpireCardPlayedThisTurn()) {
                return 115;
            } else {
                return 95;
            }
        } else if (card instanceof Falcon) {
            if (deck < 3) {
                return 5;
            }
            return 10;
        } else if (card instanceof FighterBase) {
            return 0;
        } else if (card instanceof FleetHQ) {
            if (deck < 3) {
                return 20;
            }
            return 10;
        } else if (card instanceof Gunship) {
            return 30;
        } else if (card instanceof HeavyCruiser) {
            return 70;
        } else if (card instanceof ImperialFighter) {
            if (deck <= 2) {
                return 0;
            }
            return 5;
        } else if (card instanceof ImperialFrigate) {
            if (deck < 3) {
                return 15;
            }
            return 35;
        } else if (card instanceof ImperialPalace) {
            if (player.countCardsByType(player.getAllCards(), Card::isStarEmpire) > 2) {
                return 50;
            }
            return 35;
        } else if (card instanceof ImperialTrader) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            } else if (deck == 3) {
                return 30;
            }
            return 20;
        } else if (card instanceof Lancer) {
            if (deck < 3) {
                if (opponentBases >= 1) {
                    return 5;
                }
                return 0;
            }
            if (opponentBases >= 3) {
                return 10;
            } else {
                return 5;
            }
        } else if (card instanceof OrbitalPlatform) {
            if (deck == 1) {
                return 0;
            } else if (deck <= 3) {
                return 5;
            }
        } else if (card instanceof RecyclingStation) {
            if (deck == 1) {
                return 30;
            }
            if (player.countCardsByType(player.getAllCards(), Card::isStarterCard) >= 4) {
                return 45;
            }
            return 30;
        } else if (card instanceof RoyalRedoubt) {
            if (bases > 2) {
                return 40;
            }
            return 20;
        } else if (card instanceof SpaceStation) {
            if (deck == 1) {
                return 50;
            } else if (deck == 2) {
                return 40;
            }
            return 25;
        } else if (card instanceof StarBarge) {
            if (deck < 3) {
                return 10;
            } else if (deck == 3) {
                return 5;
            }
        } else if (card instanceof StarFortress) {
            if (deck <= 3) {
                return 70;
            }
            return 60;
        } else if (card instanceof StarbaseOmega) {
            if (deck < 3 && bases > 0) {
                return 20;
            } else if (deck == 3 && bases >= 2) {
                return 15;
            } else if (deck < 3) {
                return 10;
            } else if (bases == 0) {
                return 0;
            }
            return 5;
        } else if (card instanceof SurveyShip) {
            return 5;
        } else if (card instanceof SupplyDepot) {
            if (deck < 3) {
                return 50;
            } else if (deck == 3) {
                return 30;
            }
            return 25;
        } else if (card instanceof WarWorld) {
            if (bases > 2) {
                return 30;
            }
            return 10;
        }

        //Machine Cult
        else if (card instanceof BattleBot) {
            if (deck == 1) {
                return 70;
            } else if (deck == 2) {
                return 50;
            } else if (deck == 3) {
                return 20;
            }
            if (player.countCardsByType(player.getAllCards(), Card::isStarterCard) >= 2) {
                return 5;
            }
        } else if (card instanceof BattleMech) {
            if (deck < 3) {
                return 80;
            }
            return 50;
        } else if (card instanceof BattleStation) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 20;
            }
            return 35;
        } else if (card instanceof BorderFort) {
            if (deck == 1) {
                return 70;
            } else if (deck == 2) {
                return 55;
            } else if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof BrainWorld) {
            if (deck < 3) {
                return 100;
            }
            if (deck == 3) {
                return 90;
            }
            return 85;
        } else if (card instanceof ConvoyBot) {
            if (deck == 1) {
                return 90;
            } else if (deck == 2) {
                return 70;
            } else if (deck == 3) {
                return 40;
            }
            return 20;
        } else if (card instanceof DefenseBot) {
            if (deck < 3 && bases > 0) {
                return 40;
            } else if (deck < 3) {
                return 35;
            } else if (deck == 3 && bases >= 2) {
                return 45;
            } else if (deck == 3 && bases > 0) {
                return 30;
            } else if (bases >= 2) {
                return 35;
            }
            return 10;
        } else if (card instanceof FortressOblivion) {
            if (deck < 3 && bases > 0) {
                return 40;
            } else if (deck < 3) {
                return 35;
            } else if (deck == 3 && bases >= 2) {
                return 30;
            } else if (deck ==3 && bases > 0) {
                return 20;
            }
            return 2;
        } else if (card instanceof FrontierStation) {
            if (deck < 3) {
                return 55;
            } else if (deck == 3) {
                return 35;
            }
            return 30;
        } else if (card instanceof Junkyard) {
            if (deck < 3) {
                return 40;
            } else if (bases > 3) {
                return 25;
            }
            return 5;
        } else if (card instanceof MachineBase) {
            if (deck < 3) {
                return 90;
            }
            if (deck == 3) {
                return 85;
            }
            return 80;
        } else if (card instanceof MechCruiser) {
            if (deck < 3) {
                return 75;
            }
            return 45;
        } else if (card instanceof MechWorld) {
            if (deck == 1) {
                return 20;
            }
            return 50;
        } else if (card instanceof MegaMech) {
            if (bases >= 4) {
                return 60;
            } else if (bases >= 2) {
                return 50;
            }
            return 40;
        } else if (card instanceof MiningMech) {
            if (deck == 1) {
                return 85;
            } else if (deck == 2) {
                return 65;
            } else if (deck == 3) {
                return 35;
            }
            return 10;
        } else if (card instanceof MissileBot) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 60;
            } else if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof MissileMech) {
            if (opponentBases > 3) {
                return 80;
            }
            return 60;
        } else if (card instanceof PatrolBot) {
            if (deck == 1) {
                return 60;
            } else if (deck == 2) {
                return 40;
            }
            return 15;
        } else if (card instanceof PatrolMech) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 60;
            } else if (deck == 3) {
                return 30;
            }
            return 15;
        } else if (card instanceof RepairBot) {
            if (deck == 1) {
                return 80;
            } else if (deck == 2) {
                return 60;
            } else if (deck == 3) {
                return 20;
            }
        } else if (card instanceof SupplyBot) {
            if (deck == 1) {
                return 90;
            } else if (deck == 2) {
                return 70;
            } else if (deck == 3) {
                return 30;
            }
        } else if (card instanceof StealthNeedle) {
            if (deck < 3) {
                return 30;
            }
            return 60;
        } else if (card instanceof StealthTower) {
            int totalBases = bases + opponentBases;
            if (deck < 3) {
                return 10 + (5 * totalBases);
            } else {
                return 5 * totalBases;
            }
        } else if (card instanceof TheArk) {
            if (deck < 3) {
                return 100;
            }
            return 95;
        } else if (card instanceof TheIncinerator) {
            if (deck < 3) {
                return 90;
            }
            if (deck == 3) {
                return 80;
            }
            return 75;
        } else if (card instanceof TheOracle) {
            if (deck == 1) {
                return 75;
            } else if (deck == 2) {
                return 55;
            } else if (deck == 3) {
                return 25;
            }
            return 5;
        } else if (card instanceof TheWrecker) {
            if (deck < 3) {
                return 90;
            }
            if (player.countCardsByType(player.getAllCards(), Card::isStarterCard) >= 3) {
                return 80;
            }
            return 70;
        } else if (card instanceof TradeBot) {
            if (deck == 1) {
                return 75;
            } else if (deck == 2) {
                return 55;
            } else if (deck == 3) {
                return 15;
            }
            return 5;
        } else if (card instanceof WarningBeacon) {
            if (player.machineCultCardPlayedThisTurn()) {
                return 20;
            } else {
                return 5;
            }
        }

        //Other
        else if (card instanceof Explorer) {
            if (deck == 1) {
                return 15;
            } else if (deck == 2) {
                return 5;
            } else if (deck == 3) {
                return 2;
            }
        }
        else if (card instanceof MercCruiser) {
            if (deck == 1) {
                return 5;
            } else if (deck == 2) {
                return 10;
            }
            return 20;
        }

        return 0;

    }
}
