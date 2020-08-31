package mvc.controller;

import mvc.Main;
import mvc.model.*;
import mvc.model.Panel;

import java.util.ArrayList;
import java.util.List;

public class Game {
    
    private Main control;
    private LayoutController layoutController;
    private PlayerController playerController;

    private List<Panel> possiblePanel;
    private List<Panel> visitedPanel;
    private Move move;
    private Panel currentPanel;

    public Game(Main control, LayoutController layoutController, PlayerController playerController) {
        this.control = control;
        this.layoutController = layoutController;
        this.playerController = playerController;
        possiblePanel = new ArrayList<>();
        visitedPanel = new ArrayList<>();
        move = new Move();
    }

    public void reset() {
        possiblePanel.clear();
        move.setOutdated(true);
    }

    private boolean emptyPanel(Panel f) {
        return !playerController.getCurrentPlayer().hasTileAt(f.getIndexX(), f.getIndexY()) &&
                !playerController.getOtherPlayer().hasTileAt(f.getIndexX(), f.getIndexY());
    }

    /**
     * determines the Panel(x,y) that can be reached from this position.
     * @see #testPanelcope(Panel, Color, boolean, boolean)
     * @param x actual X-Coordinate
     * @param y actual Y-Coordinate
     * @param indexX distance from X
     * @param indexY distance from Y
     * @param indexX2 x + indexX2 is goal-coordinate, if x + indexX is an opponent tile.
     * @param indexY2 y + indexY2 ist goal-coordinate, if y + indexY is an opponent tile.
     * @param further if this the  first turn or not. At the first turn you can do only one movement.
     * @return search further Panel to test
     */
    private boolean testPanel(int x, int y, int indexX, int indexY, int indexX2, int indexY2, boolean further) {
        Panel Panel = Main.playingPanel.getPanel(x + indexX, y + indexY);
        Panel Panel2;
        if (Panel != null) {
            if (emptyPanel(Panel) && !further) {
                possiblePanel.add(Panel);
                return true;
            }
            else if (playerController.getOtherPlayer().hasTileAt(Panel.getIndexX(), Panel.getIndexY())) {
                Panel2 = Main.playingPanel.getPanel(x + indexX2, y + indexY2);
                if(!visitedPanel.contains(Panel)) {
                    if (Panel2 != null && emptyPanel(Panel2) || Panel2 != null && move.getFirstPanel() == Panel2) {
                        possiblePanel.add(Panel2);
                        currentPanel = Panel2;
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private void testPanelcope(Panel f, Color c, boolean further, boolean Queen) {
        if(Queen){
            for(int i = 1; i < Main.playingPanel.getSize(); i++) {
                if(!testPanel(f.getIndexX(), f.getIndexY(), i, i, i+1,i+1, further)){
                    break;
                }
            }
            for(int i = 1; i < Main.playingPanel.getSize(); i++) {
                if(!testPanel(f.getIndexX(), f.getIndexY(), i, -i,i+1,-i-1, further)){
                    break;
                }
            }
            for(int i = 1; i < Main.playingPanel.getSize(); i++) {
                if(!testPanel(f.getIndexX(), f.getIndexY(), -i, i,-i-1, i+1, further)){
                    break;
                }
            }
            for(int i = 1; i < Main.playingPanel.getSize(); i++) {
                if(!testPanel(f.getIndexX(), f.getIndexY(), -i, -i, -i-1, -i-1, further)){
                    break;
                }
            }
        }else {
            if (c == Color.BLACK) {
                testPanel(f.getIndexX(), f.getIndexY(), 1, 1,2,2, further);
                testPanel(f.getIndexX(), f.getIndexY(), -1, 1,-2,2, further);
            } else {
                testPanel(f.getIndexX(), f.getIndexY(), 1, -1,2,-2, further);
                testPanel(f.getIndexX(), f.getIndexY(), -1, -1,-2,-2, further);
            }
        }
    }

    public void selectTile(Tile t) {
        if (move != null && move.getTile() == t && !move.isOutdated() && Main.playingPanel.getPanel(t.getIndexX(), t.getIndexY()) != currentPanel) {
            layoutController.colorPanel();
            move.setOutdated(true);
            visitedPanel.clear();
            currentPanel = null;
            return;
        }
        if(currentPanel == null || Main.playingPanel.getPanel(t.getIndexX(), t.getIndexY()) != currentPanel) {
            move.init(t);
            Panel f = Main.playingPanel.getPanel(move.getTile().getIndexX(), move.getTile().getIndexY());
            move.addEnterPanel(f);
            possiblePanel.clear();
            testPanelcope(f, t.getColor(), false, t.isQueen());
            layoutController.highlightPanel(possiblePanel, move);
        }
        else  if (move != null && !move.isOutdated()) {
            Panel f = Main.playingPanel.getPanel(t.getIndexX(), t.getIndexY());
            selectPanel(f);
        }
    }

    public void selectPanel(Panel f) {
        if (move != null && !move.isOutdated()) {
            if (!possiblePanel.isEmpty() && possiblePanel.contains(f)) {
                move.addEnterPanel(f);
                if (Math.abs(move.getLastPanel().getIndexX() - f.getIndexX()) >= 2) {
                    int x,y;
                    for (int i = 1; i < Math.abs(move.getLastPanel().getIndexX() - f.getIndexX()); i++) {
                        if (f.getIndexX() > move.getLastPanel().getIndexX()) {
                            x = move.getLastPanel().getIndexX() + i;
                        }
                        else {
                            x = move.getLastPanel().getIndexX() - i;
                        }
                        if (f.getIndexY() > move.getLastPanel().getIndexY()) {
                            y = move.getLastPanel().getIndexY() + i;
                        }
                        else {
                            y = move.getLastPanel().getIndexY() - i;
                        }
                        if (playerController.getOtherPlayer().hasTileAt(x, y)) {
                            move.addSkipPanel(Main.playingPanel.getPanel(x, y));

                            visitedPanel.add(Main.playingPanel.getPanel(x, y));
                            layoutController.colorPanel();
                            possiblePanel.clear();

                            testPanelcope(move.getEndPanel(), move.getTile().getColor(), true, move.getTile().isQueen());
                            if (!possiblePanel.isEmpty()) {
                                layoutController.highlightPanel(possiblePanel, move);
                                return;
                            }
                        }
                    }
                }
                currentPanel = null;
                visitedPanel.clear();
                makeMove(move);
            }
            else if (move.getEndPanel().equals(f) && move.getEndPanel() != move.getFirstPanel()) {
                makeMove(move);
            }
            else if (playerController.getCurrentPlayer().hasTileAt(f.getIndexX(), f.getIndexY()) && currentPanel == null) {
                selectTile(playerController.getCurrentPlayer().getTileAt(f.getIndexX(), f.getIndexY()));
            }
            else if(currentPanel != null){
                currentPanel = null;
                visitedPanel.clear();
                possiblePanel.clear();
                move.setOutdated(true);
                selectTile(playerController.getCurrentPlayer().getTileAt(f.getIndexX(), f.getIndexY()));
            }
        }
        else if (playerController.getCurrentPlayer().hasTileAt(f.getIndexX(), f.getIndexY())) {
            selectTile(playerController.getCurrentPlayer().getTileAt(f.getIndexX(), f.getIndexY()));
        }
    }

    private void makeMove(Move move) {
        layoutController.colorPanel();
        layoutController.moveTile(move);
        move.update();
        testForQueen(move.getTile());
        possiblePanel.clear();
    }

    public void finishedMove() {
        if (!testForWinner()) {
            move.setOutdated(true);
            playerController.changePlayer();
            layoutController.updatePlayer();
            playAI();
        }
    }

    private void testForQueen(Tile t) {
        if (t.getColor() == Color.BLACK) {
            if (t.getIndexY() == Main.playingPanel.getSize() - 1) {
                t.setQueen();
            }
        }
        else if (t.getIndexY() == 0) {
            t.setQueen();
        }
    }

    private boolean testForWinner() {
        if (!isMovePossible(playerController.getOtherPlayer())) {
            control.winDialog(playerController.getCurrentPlayer().getName());
            return true;
        }
        return false;
    }

    private boolean isMovePossible(Player p) {
        for (Tile s : p.getTiles()) {
            if (!s.isEliminated()) {
                testPanelcope(Main.playingPanel.getPanel(s.getIndexX(), s.getIndexY()), s.getColor(), false, s.isQueen());
                if (!possiblePanel.isEmpty()) {
                    possiblePanel.clear();
                    return true;
                }
            }
        }
        return false;
    }

    private void playAI() {
        if(playerController.isSinglePlayerGame() && !playerController.isCurrentPlayer1()) {
            try {
                Move m = ((AI) playerController.getPlayer2()).getBestMove();
                makeMove(m);
            } catch (NoPossibleMoveException e) {
                control.winDialog(playerController.getPlayer1().getName());
            }
        }
    }
}
