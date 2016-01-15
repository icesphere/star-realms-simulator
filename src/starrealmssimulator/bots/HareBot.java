package starrealmssimulator.bots;

import starrealmssimulator.cards.bases.blob.BlobWorld;
import starrealmssimulator.cards.bases.blob.DeathWorld;
import starrealmssimulator.cards.bases.outposts.machinecult.*;
import starrealmssimulator.cards.bases.outposts.starempire.RoyalRedoubt;
import starrealmssimulator.cards.bases.outposts.starempire.SpaceStation;
import starrealmssimulator.cards.bases.outposts.starempire.StarFortress;
import starrealmssimulator.cards.bases.outposts.tradefederation.DefenseCenter;
import starrealmssimulator.cards.bases.outposts.tradefederation.PortOfCall;
import starrealmssimulator.cards.bases.outposts.tradefederation.TradingPost;
import starrealmssimulator.cards.bases.tradefederation.BarterWorld;
import starrealmssimulator.cards.bases.tradefederation.CentralOffice;
import starrealmssimulator.cards.bases.tradefederation.Starmarket;
import starrealmssimulator.cards.heroes.*;
import starrealmssimulator.cards.ships.machinecult.*;
import starrealmssimulator.cards.ships.starempire.Battlecruiser;
import starrealmssimulator.cards.ships.starempire.CargoLaunch;
import starrealmssimulator.cards.ships.starempire.Dreadnaught;
import starrealmssimulator.cards.ships.tradefederation.*;
import starrealmssimulator.model.Card;

public class HareBot extends AttackBot {

    //todo override more cards

    @Override
    public int getBuyCardScore(Card card) {
        int deck = getCurrentDeckNumber();

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
        if (card instanceof BlobWorld) {
            return 100;
        } else if (card instanceof DeathWorld) {
            if (deck < 3) {
                return 95;
            }
            if (deck == 3) {
                return 85;
            }
            return 75;
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
        else if (card instanceof Battlecruiser) {
            return 70;
        } else if (card instanceof CargoLaunch) {
            return 10;
        } else if (card instanceof Dreadnaught) {
            return 90;
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

        return super.getBuyCardScore(card);
    }
}
