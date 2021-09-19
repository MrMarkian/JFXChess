package com.jfxchess.jfxchess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    static Scene scene;
    static Stage mainStage;
    static MainUIController mainUIController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainWindow.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);
        mainUIController = fxmlLoader.getController();
        stage.setTitle("JFXChess v.1 - Markian Rutkowskyj");
        stage.setScene(scene);
        mainStage = stage;

        stage.show();

    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static MainUIController getMainUIController() {
        return mainUIController;
    }

    public static void main(String[] args) {
        launch();
    }
}