package mvc.model;

import mvc.Main;

import java.util.ArrayList;
import java.util.List;


public class AI extends Player {

    private Player opponent;

    /**
     * Lists of all the possible moves, that can be done from the AI.
     */
    private List<PanelCrossed> allMoves;

    public AI(Color c, String name, int size, Player opponent) {
        super(c, name, size);
        this.opponent = opponent;
    }

    public Move getBestMove() throws NoPossibleMoveException{
        allMoves = new ArrayList<>();

        //A possible move for every not eliminated tile
        for (Tile t : getTiles()) {
            if (!t.isEliminated()) {
                ai(t, t.getIndexX(), t.getIndexY(), 0, true, new ArrayList<Panel>(), new ArrayList<Panel>());
            }
        }

        if(allMoves.size() == 0){
            throw new NoPossibleMoveException();
        }
        return PanelCrossed.getBestTurn(allMoves);
    }

    private int diagonalcheck(Tile t, Direction d, int x, int y, boolean firstCall, List<Panel> skipped, List<Panel> entered) {

        // possible turn right down
        if (d.equals(Direction.RIGHTDOWN)) {
            /*  If t is a /Queen, the for-loop will be used many times to find a Tile which is far from the current Panel,
                otherwise check where a nearby opponent is */
            for(int i = 0; i < (t.isQueen() ?  (Main.playingPanel.getSize()-2) : 1); i++) {

                // The moment an opponent is on the way it stops or when an opponent is behind this Panel.
                if(Main.playingPanel.isPositionInsidePanel(x + i + 1, y - i - 1) && hasTileAt(x + i + 1, y - i - 1) ||
                        opponent.hasTileAt(x + i + 1, y - i - 1) && (opponent.hasTileAt(x + i + 2, y - i - 2) || hasTileAt(x + i + 2, y - i - 2))) {
                    return 0;
                }

                /*  the next 2 diagonal Panel, if the skipped Panel wasn't visited the opponent should be empty*/
                if (Main.playingPanel.isPositionInsidePanel(x + i + 2, y - i - 2) && Main.playingPanel.isPositionInsidePanel(x + i + 1, y - i - 1)
                        && !skipped.contains(Main.playingPanel.getPanel(x + i + 1, y - i - 1))
                        && opponent.hasTileAt(x + i + 1, y - i - 1)) {
                    //skipped tile
                    skipped.add(Main.playingPanel.getPanel(x + i + 1, y - i - 1));
                    //see again if there are other possible moves to be done
                    entered.add(Main.playingPanel.getPanel(x + i + 2, y - i - 2));
                    //save these scores to be used later.
                    return 2+i;
                }

            }
            /*  if the next diagonal Panel is empty, it can be used */
            if (firstCall && Main.playingPanel.isPositionInsidePanel(x + 1, y - 1) && !(opponent.hasTileAt(x + 1, y - 1) || hasTileAt(x + 1, y - 1))) {
                entered.add(Main.playingPanel.getPanel(x + 1, y - 1));
                return 1;
            }
        }
        /*  the decision for the move*/
        else if (d.equals(Direction.LEFTDOWN)) {
            for(int i = 0; i < (t.isQueen() ?  (Main.playingPanel.getSize()-2) : 1); i++) {
                if(Main.playingPanel.isPositionInsidePanel(x - i - 1, y - i - 1) && hasTileAt(x - i - 1, y - i - 1) ||
                        opponent.hasTileAt(x - i - 1, y - i - 1) && (opponent.hasTileAt(x - i - 2, y - i - 2) || hasTileAt(x - i - 2, y - i - 2))) {
                    return 0;
                }
                if (Main.playingPanel.isPositionInsidePanel(x - i - 2, y - i - 2) && Main.playingPanel.isPositionInsidePanel(x - i - 1, y - i - 1)
                        && !skipped.contains(Main.playingPanel.getPanel(x - i - 1, y - i - 1))
                        && opponent.hasTileAt(x - i - 1, y - i - 1)) {
                    skipped.add(Main.playingPanel.getPanel(x - i - 1, y - i - 1));
                    entered.add(Main.playingPanel.getPanel(x - i - 2, y - i - 2));
                    return 2+i;
                }

            }
            if (firstCall && Main.playingPanel.isPositionInsidePanel(x - 1, y - 1) && !(opponent.hasTileAt(x - 1, y - 1) || hasTileAt(x - 1, y - 1))) {
                entered.add(Main.playingPanel.getPanel(x - 1, y - 1));
                return 1;
            }
        } else if (t.isQueen() && d.equals(Direction.RIGHTUP)) {
            for(int i = 0; i < (t.isQueen() ?  (Main.playingPanel.getSize()-2) : 1); i++) {
                if(Main.playingPanel.isPositionInsidePanel(x + i + 1, y + i + 1) && hasTileAt(x + i + 1, y + i + 1) ||
                        opponent.hasTileAt(x + i + 1, y + i + 1) && (opponent.hasTileAt(x + i + 2, y + i + 2) || hasTileAt(x + i + 2, y + i + 2))) {
                    return 0;
                }
                if (Main.playingPanel.isPositionInsidePanel(x + i + 2, y + i + 2) && Main.playingPanel.isPositionInsidePanel(x + i + 1, y + i + 1)
                        && !skipped.contains(Main.playingPanel.getPanel(x + i + 1, y + i + 1))
                        && opponent.hasTileAt(x + i + 1, y + i + 1)) {
                    skipped.add(Main.playingPanel.getPanel(x + i + 1, y + i + 1));
                    entered.add(Main.playingPanel.getPanel(x + i + 2, y + i + 2));
                    return 2+i;
                }

            }
            if (firstCall && Main.playingPanel.isPositionInsidePanel(x + 1, y + 1) && !(opponent.hasTileAt(x + 1, y + 1) || hasTileAt(x + 1, y + 1))) {
                entered.add(Main.playingPanel.getPanel(x + 1, y + 1));
                return 1;
            }
        } else if (t.isQueen() && d.equals(Direction.LEFTUP)) {
            for(int i = 0; i < (t.isQueen() ?  (Main.playingPanel.getSize()-2) : 1); i++) {
                if(Main.playingPanel.isPositionInsidePanel(x - i - 1, y + i + 1) && hasTileAt(x - i - 1, y + i + 1) ||
                        opponent.hasTileAt(x - i - 1, y + i + 1) && (opponent.hasTileAt(x - i - 2, y + i + 2) || hasTileAt(x - i - 2, y + i + 2))) {
                    return 0;
                }
                if (Main.playingPanel.isPositionInsidePanel(x - i - 2, y + i + 2) && Main.playingPanel.isPositionInsidePanel(x - i - 1, y + i + 1)
                        && !skipped.contains(Main.playingPanel.getPanel(x - i - 1, y + i + 1))
                        && opponent.hasTileAt(x - i - 1, y + i + 1)) {
                    skipped.add(Main.playingPanel.getPanel(x - i - 1, y + i + 1));
                    entered.add(Main.playingPanel.getPanel(x - i - 2, y + i + 2));
                    return 2+i;
                }

            }
            if (firstCall && Main.playingPanel.isPositionInsidePanel(x - 1, y + 1) && !(opponent.hasTileAt(x - 1, y + 1) || hasTileAt(x - 1, y + 1))) {
                entered.add(Main.playingPanel.getPanel(x - 1, y + 1));
                return 1;
            }
        }
        return 0;
    }

    /**
     * The AI Methods determine {@link #diagonalcheck(Tile, Direction, int, int, boolean, List, List)}, if an opponent't tile can be hit(skipped) and calls
     * itself in the new coordinates(X,Y) again recursive. Then again from the new position diagonal.
     */
    private void ai(Tile t, int x, int y, int PanelCrossed, boolean firstCall, List<Panel> skipped, List<Panel> entered) {

        // if it is the first path(entry) the Startfeld will be added
        if (firstCall) {
            entered.add(Main.playingPanel.getPanel(x, y));
        }

        // skipped und entered Lists were copied for all the directions.
        List<Panel> skippedLeftDown = new ArrayList<>(skipped);
        List<Panel> enteredLeftDown = new ArrayList<>(entered);

        List<Panel> skippedRightDown = new ArrayList<>(skipped);
        List<Panel> enteredRightDown = new ArrayList<>(entered);

        List<Panel> skippedLeftUp = new ArrayList<>(skipped);
        List<Panel> enteredLeftUp = new ArrayList<>(entered);

        List<Panel> skippedRightUp = new ArrayList<>(skipped);
        List<Panel> enteredRightUp = new ArrayList<>(entered);

        int a;
        if ((a = diagonalcheck(t, Direction.RIGHTDOWN, x, y, firstCall, skippedLeftDown, enteredLeftDown)) > 1) {
            /*  recursive AI call, if an opponent is skipped (hit) to see if there is another one to continue doing so */
            ai(t, x + a, y - a, PanelCrossed + a, false, new ArrayList<Panel>(skippedLeftDown), new ArrayList<Panel>(enteredLeftDown));
        } else {
            if (PanelCrossed + a > 0) {
                PanelCrossed z = new PanelCrossed(PanelCrossed + a, t);
                z.addEnterPanel(enteredLeftDown);
                z.addSkipPanel(skippedLeftDown);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(t, Direction.LEFTDOWN, x, y, firstCall, skippedRightDown, enteredRightDown)) > 1) {
            ai(t, x - a, y - a, PanelCrossed + a, false, new ArrayList<Panel>(skippedRightDown), new ArrayList<Panel>(enteredRightDown));
        } else {
            if (PanelCrossed + a > 0) {
                PanelCrossed z = new PanelCrossed(PanelCrossed + a, t);
                z.addEnterPanel(enteredRightDown);
                z.addSkipPanel(skippedRightDown);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(t, Direction.RIGHTUP, x, y, firstCall, skippedLeftUp, enteredLeftUp)) > 1) {
            ai(t, x + a, y + a, PanelCrossed + a, false, new ArrayList<Panel>(skippedLeftUp), new ArrayList<Panel>(enteredLeftUp));
        } else {
            if (PanelCrossed + a > 0) {
                PanelCrossed z = new PanelCrossed(PanelCrossed + a, t);
                z.addEnterPanel(enteredLeftUp);
                z.addSkipPanel(skippedLeftUp);
                allMoves.add(z);
            }
        }

        if ((a = diagonalcheck(t, Direction.LEFTUP, x, y, firstCall, skippedRightUp, enteredRightUp)) > 1) {
            ai(t, x - a, y + a, PanelCrossed + a, false, new ArrayList<Panel>(skippedRightUp), new ArrayList<Panel>(enteredRightUp));
        } else {
            if (PanelCrossed + a > 0) {
                PanelCrossed z = new PanelCrossed(PanelCrossed + a, t);
                z.addEnterPanel(enteredRightUp);
                z.addSkipPanel(skippedRightUp);
                allMoves.add(z);
            }
        }
    }
}