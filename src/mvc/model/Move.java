package mvc.model;

import java.util.List;
import java.util.Vector;

public class Move {

    private Tile tile;
    private Vector<Panel> enteredPanel;
    private Vector<Panel> skippedPanel;
    private int index;
    private boolean outdated;

    public Move() {
        enteredPanel = new Vector<>();
        skippedPanel = new Vector<>();
        outdated = true;
    }

    public Move(Tile t) {
        this();
        tile = t;
        index = 1;
        outdated = false;
    }

    public void init(Tile t) {
        tile = t;
        enteredPanel.clear();
        skippedPanel.clear();
        index = 1;
        outdated = false;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public Tile getTile() {
        return tile;
    }

    public Panel getFirstPanel() {
        if (enteredPanel != null && !enteredPanel.isEmpty()) {
            return enteredPanel.get(0);
        }
        return null;
    }

    public Panel getEndPanel() {
        if (enteredPanel != null && !enteredPanel.isEmpty()) {
            return enteredPanel.get(enteredPanel.size() - 1);
        }
        return null;
    }

    public Panel getLastPanel() {
        if (enteredPanel != null && !enteredPanel.isEmpty() && enteredPanel.size() > 1) {
            return enteredPanel.get(enteredPanel.size() - 2);
        }
        return null;
    }

    public Panel getNextPanel() {
        return enteredPanel.get(index);
    }

    public Panel getCurrentPanel() {
        return enteredPanel.get(index - 1);
    }

    public boolean nextPanel() {
        if (index < enteredPanel.size() - 1) {
            index++;
            return true;
        }
        return false;
    }

    public void addEnterPanel(Panel f) {
        enteredPanel.add(f);
    }

    public void addSkipPanel(List<Panel> p) {
        skippedPanel.addAll(p);
    }

    public void addEnterPanel(List<Panel> p) {
        enteredPanel.addAll(p);
    }

    public void addSkipPanel(Panel p) {
        skippedPanel.add(p);
    }

    public List<Panel> getEnteredPanel() {
        return enteredPanel;
    }

    public List<Panel> getSkipedPanel() {
        return skippedPanel;
    }

    public Panel getFirstSkipedPanel() {
        if (!skippedPanel.isEmpty()) {
            return skippedPanel.get(0);
        }
        return null;
    }

    public void nextSkipedPanel() {
        if (!skippedPanel.isEmpty()) {
            skippedPanel.remove(0);
        }
    }

    public void update() {
        tile.setIndexX(getEndPanel().getIndexX());
        tile.setIndexY(getEndPanel().getIndexY());
    }
}
