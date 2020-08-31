package mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PanelCrossed extends Move {
    private int turnDuration;

    public PanelCrossed(int turnDuration, Tile s){
        super(s);
        this.turnDuration = turnDuration;
    }

    private int getPanelCrossed(){return turnDuration;}

    public String toString(){
        return "Duration: " + turnDuration + "; fromPos: " + super.getFirstPanel().getIndexX() + ", " + super.getFirstPanel().getIndexY() +
                " toPos: " + super.getEndPanel().getIndexX() + ", " + super.getEndPanel().getIndexY();
    }

    public void print(){
        System.out.println("Entered Panel:");
        for(Panel f : getEnteredPanel()){
            System.out.println(f.getIndexX() + ", " + f.getIndexY());
        }
        System.out.println("Skipped Panel:");
        for(Panel f : getSkipedPanel()){
            System.out.println(f.getIndexX() + ", " + f.getIndexY());
        }
        System.out.println(toString());
    }

    /**
     * From a possible moves list, a random turn with maximum duration will be chosen.
     */
    public static PanelCrossed getBestTurn(List<PanelCrossed> turns){
        if(turns != null) {
            if (turns.size() > 0) {
                //the highest duration turns will be searched and a random move will be chosen.
                int max = turns.get(0).getPanelCrossed();
                List<PanelCrossed> PanelCrossed = new ArrayList<>();

                for (PanelCrossed z : turns) {
                    if (max < z.getPanelCrossed()) {
                        max = z.getPanelCrossed();
                    }
                }
                for (PanelCrossed z : turns) {
                    if (max == z.getPanelCrossed()) {
                        PanelCrossed.add(z);
                    }
                }
                return PanelCrossed.get(new Random().nextInt(PanelCrossed.size()));
            }
        }
        return null;
    }
}
