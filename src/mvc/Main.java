package mvc;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import mvc.controller.*;
import mvc.model.*;

import java.io.IOException;
import java.util.Optional;

public class Main extends Application {

    private Stage primaryStage;
    private Parent startLayout;
    private Parent layout;
    private BorderPane menuLayout;

    public static PlayingPanel playingPanel;

    private LayoutController paneController;
    private StartScreenController startPaneController;
    private MenuBarController menuPaneController;
    private PlayerController playerController;
    private Game game;
    private Image queen;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("Checkers Game");
        primaryStage.setOnCloseRequest(event -> Platform.exit());

        loadRootMenuLayout();
        loadStartLayout();
        loadlayout();
        setStartLayout();

        playingPanel = new PlayingPanel();
        playerController = new PlayerController();
        game = new Game(this, paneController, playerController);

        primaryStage.setMinHeight(menuLayout.getPrefHeight());
        primaryStage.setMinWidth(menuLayout.getPrefWidth());

        primaryStage.show();
    }

    private void loadRootMenuLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MenuBar.fxml"));
            menuLayout = loader.load();

            menuPaneController = loader.getController();
            menuPaneController.setObjects(this);

            primaryStage.setScene(new Scene(menuLayout));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStartLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/startScreen.fxml"));
            startLayout = loader.load();

            startPaneController = loader.getController();
            startPaneController.setObjects(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStartLayout() {
        menuLayout.setCenter(startLayout);
        this.primaryStage.setResizable(true);
        menuPaneController.disableBackMenuItem(true);
    }

    private void loadlayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/layout.fxml"));
            layout = loader.load();
            paneController = loader.getController();
            paneController.setObjects(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setlayout() {
        menuLayout.setCenter(layout);
        this.primaryStage.setResizable(false);
        menuPaneController.disableBackMenuItem(false);
    }

    public void startGame(boolean ai, String name1, String name2) {
        playingPanel.rebuild(startPaneController.getPlayingPanelSize());
        paneController.buildPlayingPanel(startPaneController.getPlayingPanelSize(), (int) primaryStage.getHeight() - 200, playingPanel);
        playerController.init(ai, startPaneController.getPlayingPanelSize(), name1, name2);
        paneController.createTiles(playerController.getPlayer1(), playerController.getPlayer2());
        setlayout();
    }

    public void winDialog(String name) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getStylesheets().add(Main.class.getResource("view/styles.css").toExternalForm());
        dialog.setHeaderText(name + " has won the Game! Now choose if you want to start the game again,\ngo back to the main Menu or close the game.");

        ButtonType restartButton = new ButtonType("restart");
        ButtonType closeButton = new ButtonType("Close");

        dialog.getButtonTypes().setAll(restartButton, closeButton);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == restartButton) {
            startGame(playerController.isSinglePlayerGame(), playerController.getPlayer1().getName(), playerController.getPlayer2().getName());
        } else {
            Platform.exit();
        }
    }

    public void returnToStart() {
        setStartLayout();
        paneController.clearPanel();
        game.reset();
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public Image getQueenImage() {
        return queen;
    }

    public Game getGame() {
        return game;
    }

    public static void main(String[] args) {
        launch(args);
    }
}