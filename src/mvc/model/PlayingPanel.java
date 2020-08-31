package mvc.model;

public class PlayingPanel {
    private int PanelSize;
    private Panel cPanel[];

    public PlayingPanel(int size){
        PanelSize = size;
        createPanel();
    }

    public PlayingPanel(){
    }

    private void createPanel(){
        cPanel = new Panel[PanelSize*PanelSize];
        boolean black = false;
        for (int i = 0; i < PanelSize; i++){
            for (int j = 0; j < PanelSize; j++){
                Panel temp;
                if (black) {
                    temp = new Panel(Color.BLACK, i, j);
                }
                else{
                    temp = new Panel(Color.RED, i, j);
                }
                cPanel[i * PanelSize + j] = temp;
                black = !black;
            }
        }
    }

    public void rebuild(int PanelSize) {
        this.PanelSize = PanelSize;
        createPanel();
    }

    public Panel getPanel(int x, int y) {
        if (x * PanelSize + y >= 0 && x * PanelSize + y < PanelSize * PanelSize) {
            if (cPanel[x * PanelSize + y].getIndexX() == x && cPanel[x * PanelSize + y].getIndexY() == y) {
                return cPanel[x * PanelSize + y];
            }
            else {
                for (Panel f : cPanel) {
                    if (f.getIndexX() == x && f.getIndexY() == y) {
                        return f;
                    }
                }
            }
        }
        return null;
    }

    public int getSize(){
        return PanelSize;
    }

    public boolean isPositionInsidePanel(int x, int y){
        return x >= 0 && x < PanelSize && y >= 0 && y < PanelSize;
    }
}
