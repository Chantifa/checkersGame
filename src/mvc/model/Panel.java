package mvc.model;

import javafx.scene.shape.Rectangle;

public class Panel {
    private Color cColor;
    private int indexX, indexY;
    private Rectangle cRec;

    public Panel(Color c, int x, int y) {
        cColor = c;
        indexX = x;
        indexY = y;
    }

    public Rectangle getcRec() {
        return cRec;
    }

    public void setcRec(Rectangle temp) {
        cRec = temp;
    }

    public int getIndexX() {
        return indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public Color getcColor() {
        return cColor;
    }
}
