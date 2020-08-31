package mvc.controller;

import javafx.scene.image.ImageView;
import mvc.Main;
import mvc.model.Player;
import mvc.model.Tile;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import mvc.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is the Controller for the game Layout
 * It controls the user commands during a game.
 * */

public class LayoutController {

    /**
     * instance of the main Class
     */
    private Main mainClassObject;

    /**
     * The size of the board
     */
    private int size;

    /**
     * Amount of game pixels. How big is the game.
     */
    private int amount;

    /**
     * tile Radius {@link javafx.scene.shape.Circle}
     */
    private int tileRadius;

    /**
     * Flag(boolean): when a tile is being moved, the users actions are locked
     */
    private boolean graphicAction;

    /**
     * List with all Panel rectangles, for easier color change
     */
    private ArrayList<Rectangle> panel;

    /**
     * Pane, in which the Playing panel and the tiles will be generated
     */
    @FXML
    private Pane playingPanel;

    /**
     * Pane to put the removed tiles from player 1
     * @see # removeTile(stone)
     */
    @FXML
    private Pane panePlayer1RemovedTiles;

    /**
     * Pane to put the removed tiles from player 2
     * @see  # removeTile(stone)
     */
    @FXML
    private Pane panePlayer2RemovedTiles;

    /**
     * update Label for players1 name at his turn
     * @see #updatePlayer()
     */
    @FXML
    private Label labelPlayer1;

    /**
     /**
     * update Label for players2 name at his turn
     * @see #updatePlayer()
     */
    @FXML
    private Label labelPlayer2;

    /**
     * label for the announcements
     * @see  # setAnnouncement(String)
     */
    @FXML
    private Label labelAnnouncements;

    /**
     * Method that hands over objects
     * @param mainClassObject instance of Main Class
     */
    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    /**
     * creates the playing Panel and generates all the {@link Rectangle}
     * @param amount the size of the playing Panel ( 8 for 8x8 or 10 for 10x10)
     * @param size   the size of the playing Panel in Pixel (primarystage.getHeight - 200)
     * @param pf Object from {@link PlayingPanel} to read the Panel
     */
    public void buildPlayingPanel(int amount, int size, PlayingPanel pf) {
        this.amount = amount;
        this.size = size;
        this.tileRadius = size / amount / 3;

        panel = new ArrayList<>();
        clearPanel();
        playingPanel.setPrefSize(size, size);
        double a = (double)size / amount;
        for (int i = 0; i < amount; i++) {
            for (int j = 0; j < amount; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setArcHeight(0);
                rectangle.setArcWidth(0);
                rectangle.setHeight(a);
                rectangle.setWidth(a);
                rectangle.setLayoutX(i * a);
                rectangle.setLayoutY(size - j * a - a);
                rectangle.setOnMouseClicked(this::onPanelClick);
                panel.add(rectangle);
                playingPanel.getChildren().add(rectangle);
                pf.getPanel(i, j).setcRec(rectangle);
            }
        }
        colorPanel();
        setAnnouncement("");
    }

    /**
     * Colors the Panel, where the tiles are, at the standard color
     * */
    public void colorPanel() {
        int i = 0;
        for (Rectangle rectangle : panel) {
            if (((i / amount) + (i % amount)) % 2 == 0) {
                rectangle.setFill(Color.DARKGRAY);
            } else {
                rectangle.setFill(Color.BLACK);
            }
            i++;
        }
    }

    /**
     * removes all the tiles (and the removed tiles) from the screen
     */
    public void clearPanel() {
        panePlayer1RemovedTiles.getChildren().clear();
        panePlayer2RemovedTiles.getChildren().clear();
        playingPanel.getChildren().clear();
    }

    /**
     * Tile {@link Circle} generator.
     * @param p both players
     */
    public void createTiles(Player... p) {
        for (Player player : p) {
            for (Tile tile : player.getTiles()) {
                initTile(tile.getIndexX(), tile.getIndexY(), tile.getNodeCircle(), tile.getColor() == mvc.model.Color.BLACK ? Color.BLACK : Color.WHITESMOKE);
            }
        }
        setNames(p[0].getName(), p[1].getName());
        updatePlayer();
    }

    /**
     * generate a tile {@link Circle} and set it to the right position on the panel.
     * @param x X-Position
     * @param y Y-Position
     * @param c the circle
     * @param color color of the tile
     */
    private void initTile(int x, int y, Node c, Color color) {
        if (c instanceof Circle) {
            ((Circle)c).setRadius(tileRadius);
            ((Circle)c).setFill(color);
            ((Circle)c).setStroke(Color.BLACK);
            ((Circle)c).setStrokeWidth(1);
            placeTile(x, y, c);
            c.setOnMouseClicked(this::onPanelClick);
            playingPanel.getChildren().add(c);
        }
        else {
            System.err.println("Something went wrong");
        }
    }

    /**
     * set the names of both players.
     * @param name1 Name of Player1
     * @param name2 Name of Player2
     */
    private void setNames(String name1, String name2) {
        if (name1 != null && !name1.isEmpty()) {
            labelPlayer1.setText(name1);
        }
        if (name2 != null && !name2.isEmpty()) {
            labelPlayer2.setText(name2);
        }
    }

    /**
     * colors the name of the place, whose turn is now
     */
    public void updatePlayer() {
        if (mainClassObject.getPlayerController().isCurrentPlayer1()) {
            labelPlayer1.setId("playerNameOnTurn");
            labelPlayer2.setId("playerNameNotOnTurn");
        }
        else {
            labelPlayer1.setId("playerNameNotOnTurn");
            labelPlayer2.setId("playerNameOnTurn");
        }
    }

    /**
     * Warning on top of the screen.
     * @param message announcement, which will be displayed.
     */
    private void setAnnouncement(String message) {
        labelAnnouncements.setText(message);
    }

    /**
     * removes a tile from the panel
     * The removed tile will be broken into 2 pieces
     * @param tile tile that will be moved
     */
    private void removeTile(Tile tile) {
        playingPanel.getChildren().remove(tile.getNodeCircle());
        if (tile.getColor() == mvc.model.Color.BLACK) {
            panePlayer1RemovedTiles.getChildren().add(tile.getNodeCircle());
            double off = 0;
            if (tile.getNodeCircle() instanceof StackPane) {
                off = tileRadius;
            }
            tile.getNodeCircle().setLayoutX(panePlayer1RemovedTiles.getWidth() / 2 - off);
            tile.getNodeCircle().setLayoutY(panePlayer1RemovedTiles.getChildren().size() * tileRadius - off);
        }
        else {
            panePlayer2RemovedTiles.getChildren().add(tile.getNodeCircle());
            double off = 0;
            if (tile.getNodeCircle() instanceof StackPane) {
                off = tileRadius;
            }
            tile.getNodeCircle().setLayoutX(panePlayer2RemovedTiles.getWidth() / 2 - off);
            tile.getNodeCircle().setLayoutY(panePlayer2RemovedTiles.getChildren().size() * tileRadius - off);
        }
    }

    /**
     * moves a tile across the panel.
     * skip-panel will be removed
     * during the tile movements, the players are locked {@link #graphicAction}
     * @param move Move, that the tile does
     */
    public void moveTile(Move move) {
        graphicAction = true;
        double value = (double)size / amount;
        updateTile(move.getTile().getNodeCircle());
        Tile tile = move.getTile();
        Timer t = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (!isTileNearPanel(tile, move.getNextPanel(), value)) {
                            calculateTileLocation(tile.getNodeCircle(), value, move);
                            if (move.getFirstSkipedPanel() != null && isTileNearPanel(tile, move.getFirstSkipedPanel(), value)) {
                                Tile tile = mainClassObject.getPlayerController().getOtherPlayer()
                                        .getTileAt(move.getFirstSkipedPanel().getIndexX(), move.getFirstSkipedPanel().getIndexY());
                                if (tile != null) {
                                    tile.setEliminated();
                                    removeTile(tile);
                                    move.nextSkipedPanel();
                                }
                            }
                        }
                        else {
                            if (!move.nextPanel()) {
                                placeTile(move.getEndPanel().getIndexX(), move.getEndPanel().getIndexY(), tile.getNodeCircle());
                                t.cancel();
                                t.purge();
                                if (tile.isQueen()) {
                                    visualizedQueen(tile);
                                }
                                mainClassObject.getGame().finishedMove();
                                graphicAction = false;
                            }
                        }
                    }
                    catch (Exception e) {
                        System.err.println("Something went wrong: " + e.getClass().getName());
                        e.printStackTrace();
                    }
                });
            }
        };
        t.schedule(task, 0, 40);
    }

    /**
     * set the Node to a closer value near the direction.
     * The node will will be every time (1/12) from a  a Panel replaced.
     * @param n Objekt, which should be moved
     * @param value size from a panel
     * @param move Der actuall move to lock the destination.
     */
    private void calculateTileLocation(Node n, double value, Move move) {
        n.setLayoutX(n.getLayoutX() + (value / 12)
                * (move.getNextPanel().getIndexX() >= move.getCurrentPanel().getIndexX() ? 1 : -1));
        n.setLayoutY(n.getLayoutY() + (value / 12)
                * (move.getNextPanel().getIndexY() >= move.getCurrentPanel().getIndexY() ? -1 : 1));
    }

    /**
     * test if a tile is there graphical.
     * An offset of +/- 2% will be taken into consideration.
     * @param t tile will be tested
     * @param f Panel that will be tested if the tile is on it.
     * @param value distance that the tile moved
     * @return true, if the Tile is on the panel.
     */
    private boolean isTileNearPanel(Tile t, Panel f, double value) {
        double off = 0;
        if (t.getNodeCircle() instanceof StackPane) {
            off = tileRadius;
        }
        return (f.getIndexX() + 0.48) * value <= t.getNodeCircle().getLayoutX() + off && (f.getIndexX() + 0.52) * value >= t.getNodeCircle().getLayoutX() + off &&
                size - (f.getIndexY() + 0.52) * value <= t.getNodeCircle().getLayoutY() + off && size - (f.getIndexY() + 0.48) * value >= t.getNodeCircle().getLayoutY() + off;
    }

    /**
     * has all visited panel and the possible panel to be moved into.
     * @param panel possible panel.
     * @param move Move (visited panel).
     */
    public void highlightPanel(List<Panel> panel, Move move) {
        colorPanel();
        for (Panel f : move.getEnteredPanel()) {
            f.getcRec().setFill(Color.GREEN);
        }
        for (Panel p : panel) {
            p.getcRec().setFill(Color.DARKRED);
        }
    }

    /**
     * set the x- and the y-coordinates from a Node, and check the Queen.
     * Normal tiles are round and have a  ZeroSpot in der Middle ({@link Circle}).
     * A Queen has a crown. Has a zeroPoint up and left.
     * @param x Index x from the panel that the tile should be
     * @param y Index y from the panel that the tile should be
     * @param node Node that will be placed.
     */
    private void placeTile(int x, int y, Node node) {
        double a = (double)size / amount;
        double off = 0;
        if (node instanceof StackPane) {
            off = tileRadius;
        }
        node.setLayoutX((x + 0.5) * a - off);
        node.setLayoutY(size - (y + 0.5) * a - off);
    }

    /**
     * update a Tile, so that it will be all over up.
     * the node will be for a short time removed from the panel and then added again (movement animation).
     * check {@link #moveTile(Move)}
     * @param c circle
     */
    private void updateTile(Node c) {
        playingPanel.getChildren().remove(c);
        playingPanel.getChildren().add(c);
    }

    /**
     * controls a normal tile in a queen-mode
     * a stackPane will be made, which will help us with {@link Circle} and {@link javafx.scene.image.Image}
     * The Image has 75% from the Circles size.
     *
     * @param t the Queen tile.
     */
    private void visualizedQueen(Tile t) {
        if (t.isQueen() && t.getNodeCircle() instanceof Circle) {
            Circle c = (Circle)t.getNodeCircle();
            c.setOnMouseClicked(null);
            StackPane sp = new StackPane();
            sp.setPrefSize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
            sp.getChildren().add(c);

            ImageView iw = new ImageView(mainClassObject.getQueenImage());
            iw.setFitHeight(c.getRadius() * 1.5);
            iw.setFitWidth(c.getRadius() * 1.5);

            sp.getChildren().add(iw);
            sp.setLayoutX(c.getLayoutX() - c.getRadius());
            sp.setLayoutY(c.getLayoutY() - c.getRadius());
            sp.setOnMouseClicked(this::onPanelClick);
            playingPanel.getChildren().remove(c);
            playingPanel.getChildren().add(sp);
            t.changeNode(sp);
        }
    }

    /**
     * processes with mouse clicks on the game Panel.
     * The things that can be clicked at a{@link Rectangle} from a {@link Panel},
     * a {@link Circle} from a {@link Tile} or a {@link StackPane} from a Queen.
     * @param e MouseEvent
     * @see Game#selectTile(Tile)
     * @see Game#selectPanel(Panel)
     */
    private void onPanelClick(MouseEvent e) {
        if (!graphicAction && (!mainClassObject.getPlayerController().isSinglePlayerGame() || mainClassObject.getPlayerController().isCurrentPlayer1())) {
            if (e.getSource() instanceof Rectangle || e.getSource() instanceof Circle || e.getSource() instanceof StackPane) {
                setAnnouncement("");
                if (e.getSource() instanceof Rectangle) {
                    Rectangle temp = (Rectangle) e.getSource();
                    int index = panel.indexOf(temp);
                    int x = (index / amount);
                    int y = index % amount;
                    Panel pressedPanel = Main.playingPanel.getPanel(x, y);
                    mainClassObject.getGame().selectPanel(pressedPanel);
                }
                else {
                    Tile t = mainClassObject.getPlayerController().getCurrentPlayer().getTilesOfClickedCircle((Node)e.getSource());
                    if (t == null) {
                        setAnnouncement("This is NOT your tile");
                        return;
                    }
                    else if (t.isEliminated()) {
                        setAnnouncement("This tile was already eliminated");
                        return;
                    }
                    mainClassObject.getGame().selectTile(t);
                }
            }
        }
    }
}



