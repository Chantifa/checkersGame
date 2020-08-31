package mvc.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import mvc.Main;

public class MenuBarController {

    private Main mainClassObject;

    @FXML
    private MenuItem menuItemBack;

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    public void setObjects(Main mainClassObject) {
        this.mainClassObject = mainClassObject;
    }

    public void disableBackMenuItem(boolean disable) {
        menuItemBack.setDisable(disable);
    }

    /**
     * By pressing the {@link MenuItem} "Return", returns the user to the main menu.
     * */
    public void handleBack(){
        mainClassObject.returnToStart();
    }
}
