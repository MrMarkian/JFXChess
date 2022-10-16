package com.jfxchess.jfxchess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NetworkWindow {

        @FXML
        private Button ConnectButton;

        @FXML
        private TextField address;

        @FXML
        private RadioButton black;

        @FXML
        private CheckBox enablePassword;

        @FXML
        private Button hostButton;

        @FXML
        private PasswordField password;

        @FXML
        private PasswordField srvPassword;

        @FXML
        private RadioButton white;

        @FXML
        void ConnectToServerClick(ActionEvent event) throws InterruptedException {
                MainUIController MainUi = Main.getMainUIController();
                MainUi.StartClient();
                Stage stage = (Stage) ConnectButton.getScene().getWindow();
                stage.close();
        }

        @FXML
        void hostButtonClick(ActionEvent event) throws InterruptedException {
                MainUIController MainUi = Main.getMainUIController();
                Stage stage = (Stage) hostButton.getScene().getWindow();
                MainUi.StartServer();
                stage.close();

        }

}



