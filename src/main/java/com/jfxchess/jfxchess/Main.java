package com.jfxchess.jfxchess;

import com.jfxchess.jfxchess.Data.BoardManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainWindow.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("JFXChess v.1 - Markian Rutkowskyj");
        stage.setScene(scene);

        stage.show();

    }

    public static Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}