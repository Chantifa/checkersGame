package mvc.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import mvc.Main;

public class StartScreenController {

    private final String eight = "8 * 8";

    private final String ten = "10 * 10";

    private Main mainClassObject;

    @FXML
    private ComboBox<String> comboBoxPlayingPanelSize;

    @FXML
    private TextField textPanelPlayer1;

    @FXML
    private TextField textPanelPlayer2;


    @FXML
    private void initialize() {
        comboBoxPlayingPanelSize.getItems().add(eight);
        comboBoxPlayingPanelSize.getItems().add(ten);
        comboBoxPlayingPanelSize.setValue(eight);
    }

    @FXML
    private void handleSinglePlayer() {
        mainClassObject.startGame(true, textPanelPlayer1.getText(), textPanelPlayer2.getText());
    }

    @FXML
    private void handleMultiplayer() {
        mainClassObject.startGame(false, textPanelPlayer1.getText(), textPanelPlayer2.getText());
    }

    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    public int getPlayingPanelSize() {
        return comboBoxPlayingPanelSize.getValue().equals(eight) ? 8 : 10;
    }
}
