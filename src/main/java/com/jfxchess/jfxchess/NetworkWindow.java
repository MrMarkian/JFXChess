package com.jfxchess.jfxchess;
import com.jfxchess.jfxchess.Data.ChessTeamColor;
import com.jfxchess.jfxchess.Data.Player;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class NetworkWindow extends Application {

        @FXML
        private Button ConnectButton;

        @FXML
        private TextField address;

        @FXML
        private TextField hostname;
        @FXML
        private TextField clientname;

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
        private ComboBox<ChessTeamColor> hostColor ;

        @FXML
        void ConnectToServerClick(ActionEvent event) throws InterruptedException, IOException {
                MainUIController MainUi = Main.getMainUIController();
                MainUi.me = new Player(clientname.getText(), address.getText());

                MainUi.StartClient(MainUi.me);

                Stage stage = (Stage) ConnectButton.getScene().getWindow();
                stage.close();
        }

        @FXML
        void hostButtonClick(ActionEvent event) throws InterruptedException {
                MainUIController MainUi = Main.getMainUIController();
                MainUi.me = new Player(hostname.getText(),"localhost");
                MainUi.me.COLOR = ChessTeamColor.WHITE;
                Stage stage = (Stage) hostButton.getScene().getWindow();
                MainUi.StartServer(MainUi.me);
                stage.close();

        }

        @Override
        public void init() throws Exception {
                super.init();
                hostColor.getItems().setAll(ChessTeamColor.values());
        }

        @Override
        public void start(Stage stage) throws Exception {

        }
}



