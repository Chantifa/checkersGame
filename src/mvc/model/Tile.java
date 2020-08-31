package mvc.model;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class Tile {

    private int indexX , indexY;
    private boolean eliminated;
    private Color circleColor;
    private boolean queen;
    private Node nodeCircle;

    public Tile(Color c, int x, int y, boolean queen){
        circleColor = c;
        indexX = x;
        indexY = y;
        this.queen = queen;
        eliminated = false;
        nodeCircle = new Circle();
    }

    public Node getNodeCircle(){
        return nodeCircle;
    }

    public void changeNode(Node n) {
        nodeCircle = n;
    }
    public Color getColor(){
        return circleColor;
    }
    public boolean isQueen(){
        return queen;
    }
    public void setQueen(){
        queen = true;
    }
    public int getIndexX(){
        return indexX;
    }
    public int getIndexY(){
        return indexY;
    }
    public void setIndexX(int x){
        indexX = x;
    }
    public void setIndexY(int y){
        indexY = y;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated() {
        eliminated = true;
    }
}
