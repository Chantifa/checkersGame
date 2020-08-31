package mvc.model;

import javafx.scene.Node;

public class Player {

    private Tile cTile[];

    private String cName;

    private Color cColor;

    public Player() {}

    public Player(Color c, String name) {
        cName = name;
        cColor = c;
    }

    public Player(Color c, String name, int size) {
        this(c, name);
        createTiles(size);
    }

    private void createTiles(int size) {
        int x, y;
        if (cColor == Color.BLACK) {
            x = 0;
            y = 0;
        } else {
            y = size - (size / 2 - 1);
            if (y % 2 != 0) {
                x = 1;
            } else {
                x = 0;
            }
        }
        int f = (int) ((((double) size / 4) - 0.5) * size);
        cTile = new Tile[f];
        for (int i = 0; i < f; i++) {
            cTile[i] = new Tile(cColor, x, y, false);
            if ((x += 2) >= size) {
                y += 1;
                if ((y % 2) != 0) {
                    x = 1;
                } else {
                    x = 0;
                }
            }
        }
    }

        public void replaceTile (int indexTile, int x, int y) {
            cTile[indexTile].setIndexX(x);
            cTile[indexTile].setIndexY(y);
        }

        public Tile[] getTiles() {
            return cTile;
        }

    public int getActiveTiles() {
        int value = 0;
        for (Tile s : getTiles()) {
            if (!s.isEliminated()) {
                value++;
            }
        }
        return value;
    }

    public String getName(){
        return cName;
    }

    public Color getColor() {
        return cColor;
    }

    public Tile getTilesOfClickedCircle(Node n) {
        for (Tile s : cTile) {
            if (s.getNodeCircle().equals(n)) {
                return s;
            }
        }
        return null;
    }

    public boolean hasTileAt(int x, int y) {
        for (Tile s : cTile) {
            if (s.getIndexX() == x && s.getIndexY() == y && !s.isEliminated()) {
                return true;
            }
        }
        return false;
    }

    public Tile getTileAt(int x, int y) {
        for (Tile s : cTile) {
            if (s.getIndexX() == x && s.getIndexY() == y && !s.isEliminated()) {
                return s;
            }
        }
        return null;
    }
}
