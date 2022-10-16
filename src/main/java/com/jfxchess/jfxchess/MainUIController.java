package com.jfxchess.jfxchess;

import com.jfxchess.jfxchess.Data.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class MainUIController {
//region UIElements
    @FXML
    private Label welcomeText;
    @FXML
    private Pane BoardRenderPane;
    @FXML
    private Pane captureRenderer;
    @FXML
    private TextField FENInputBox;
    @FXML
    private TextField moveStartInputBox;
    @FXML
    private TextField moveEndInputBox;
    @FXML
    private Slider gridSizeSlider;
    @FXML
    private ListView<Move> moveListView;
    @FXML
    private ListView<String> moveHistoryListView;
    @FXML
    private ListView<String> AIThoughtsView;
    @FXML
    private TextField textEntry;
    @FXML
    private ListView<String> chatHistory;
    @FXML
    private CheckMenuItem AIMenuEnabled;
    @FXML
    private CheckMenuItem WhiteAI;
    @FXML
    private CheckMenuItem BlackAI;
    @FXML
    private CheckBox AIEnabledCheckBox;
    @FXML
    private CheckBox AIPlayWhiteCheckbox;
    @FXML
    private CheckBox ALPlayBlackCheckbox;
    @FXML
    private Label BlackScoreLabel;
    @FXML
    private Label WhiteScoreLabel;
    @FXML
    private Label TurnCountLabel;
    @FXML
    private BarChart StatsBarChart ;
    @FXML
    private ColorPicker WhiteSquareColorChooser;
    @FXML
    private ColorPicker BlackSquareColorChooser;
    @FXML
    private ListView<PGNGame> PGNGameList;
    @FXML
    private TableView<PGNGame> PGNTableView;
    @FXML
    private CheckMenuItem DarkModeMenuChecked;
    @FXML
    private ListView<String> NetworkingListView;

    private final PGNController pgnController = new PGNController();
//endregion
    final ClientNetworkingController client = new ClientNetworkingController();
    ServerNetworkingController server;

    {
        server = new ServerNetworkingController();
    }


    @FXML
    protected void newGameClicked() throws InterruptedException {
        BoardManager.gameBoard.clear();
        BoardManager.SetupNewStandardBoard();
        BoardManager.LoadPositionsFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        AIThoughtsView.getItems().clear();
        moveHistoryListView.getItems().clear();
        moveHistoryListView.getItems().add(BoardManager.SavePositionToFEN());
        StatsBarChart.getData().clear();
        UpdateUI();
    }

    @FXML
    protected void onHelloButtonClick() throws InterruptedException {
        BoardManager.SetupNewStandardBoard();
        BoardManager.printBoard();
        BoardManager.LoadPositionsFromFEN(FENInputBox.getText());
        UpdateUI();
    }

    @FXML
    private void forceAIMoveClick() throws InterruptedException {
        BoardManager.hal9000.makeMove(BoardManager.gameBoard, BoardManager.getPlayerToMoveNext());
        moveHistoryListView.getItems().add(BoardManager.SavePositionToFEN());

        UpdateUI();

    }

    @FXML
    protected void Show3DWindow() throws Exception {
        BoardManager3D Window3D = new BoardManager3D();
        Window3D.show();
    }

    @FXML
    protected  void updateGridSquareColor() throws InterruptedException {

        BoardManager.whiteSquares = WhiteSquareColorChooser.getValue();
        BoardManager.blackSquares = BlackSquareColorChooser.getValue();
        UpdateUI();

    }

    @FXML
    protected void StartServer() throws InterruptedException {

         if (BoardManager.gameBoard.isEmpty()){
            newGameClicked();
         }
        Thread runServer = new Thread(server);
        runServer.start();
        server.uiController = this;
        Main.getMainStage().setTitle("SERVER RUNNING");

        UpdateUI();
    }

    @FXML
    protected void StartClient() throws InterruptedException {
        if (BoardManager.gameBoard.isEmpty()){
            newGameClicked();
        }
        Thread runClient = new Thread(client);
        runClient.start();
        client.uiController = this;

        Main.getMainStage().setTitle("CLIENT RUNNING");
        UpdateUI();
    }

    @FXML
    protected void sendChatMessage() throws IOException {

        String txtToSend = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute();
        txtToSend += " //  " + textEntry.getText();

        if (server.runServer){
            server.SendMessage(txtToSend);


        }else if(client.runClient){
            client.SendMessage(txtToSend);

        }
        chatHistory.getItems().add(txtToSend);
        textEntry.clear();

    }

    public void addChatMessage(String message){

            chatHistory.getItems().add(message);

    }

    @FXML
    protected void SendBoardStateClicked() throws IOException {
        server.SendFENString(moveHistoryListView.getSelectionModel().getSelectedItem());
    }


    @FXML
    private void MoveButtonClick() throws InterruptedException, IOException {
        Move tmpMove = new Move();

        tmpMove.setStartPosition(Integer.parseInt(moveStartInputBox.getText()));
        tmpMove.setEndPosition(Integer.parseInt(moveEndInputBox.getText()));
        tmpMove.calculateXY();

            if(client.runClient) {
                client.SendMove(tmpMove);
            }else if(server.runServer){
                server.SendMove(tmpMove);
            }else{BoardManager.MovePiece(tmpMove);}


        moveHistoryListView.getItems().add(BoardManager.SavePositionToFEN());
        UpdateUI();

      //  Alert calc = new Alert(Alert.AlertType.INFORMATION, "Number of positions: "+ BoardManager.ruleBook.MoveGenerationTest(4,BoardManager.gameBoard));

    }

    @FXML
    protected void FlatBeeClicked(){
        welcomeText.getScene().getStylesheets().clear();
        welcomeText.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/FlatBee.css")).toExternalForm());
    }

    @FXML
    protected void DarkModeClicked(){
        if(DarkModeMenuChecked.isSelected()) {
            welcomeText.getScene().getStylesheets().clear();
            welcomeText.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/DarkMode.css")).toExternalForm());
        }
        else
        {
            welcomeText.getScene().getStylesheets().clear();
        }
    }



    @FXML
    protected void SilverModeClicked()
    {
        welcomeText.getScene().getStylesheets().clear();
        welcomeText.getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MistSilverSkin.css")).toExternalForm());
    }



    @FXML
    protected  void importPNGMenuClick( ) {
        FileChooser fileChooser = new FileChooser();

        Platform.runLater(() -> {
            try {
                pgnController.LoadFullDatabase(fileChooser.showOpenDialog(null).getAbsoluteFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            PGNGameList.getItems().addAll(FXCollections.observableList(pgnController.getPgnGamesDataBase()));
        });
    }

    @FXML
    protected void RenderFXGLView(){
        BoardRenderFXGL fxgl3DView = new BoardRenderFXGL();


    }

    @FXML
    protected void ShowNetworkWindow() throws IOException {
        NetworkWindow networkWindow = new NetworkWindow();

        Stage dialog = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("NetworkWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 200);
        dialog.setTitle("Network Multiplayer");
        dialog.setScene(scene);
        dialog.initOwner(Main.getMainStage());
        dialog.initModality(Modality.APPLICATION_MODAL);

        dialog.showAndWait();

// process result of dialog operation.

    }

    @FXML
    private void updateGridSize() throws InterruptedException {
        UpdateUI();
    }

    @FXML
    public void initialize(){
        welcomeText.setText("JFXChess v1");
        FENInputBox.setText("r6r/7p/3bb2n/pPp5/4QP2/2N5/P1P4P/R3K1NR w KQ - 0 1");

        gridSizeSlider.valueProperty().addListener((observableValue, number, t1) -> {
            try {
                UpdateUI();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        AIPlayWhiteCheckbox.setOnMouseClicked(event -> WhiteAI.setSelected(AIPlayWhiteCheckbox.isSelected()));

        ALPlayBlackCheckbox.setOnMouseClicked(event -> BlackAI.setSelected(ALPlayBlackCheckbox.isSelected()));

        AIEnabledCheckBox.setOnMouseClicked(event -> AIMenuEnabled.setSelected(AIEnabledCheckBox.isSelected()));

        moveHistoryListView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                FENInputBox.setText(moveHistoryListView.getSelectionModel().getSelectedItem());
                try {
                    onHelloButtonClick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        BoardRenderPane.setOnMouseClicked(event -> {


            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    try {
                        MoveButtonClick();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            moveStartInputBox.setText(BoardManager.startPos);
            moveEndInputBox.setText(BoardManager.endPos);
            moveListView.setItems(FXCollections.observableList(BoardManager.ruleBook.calculatePossibleMoves(BoardManager.gameBoard, new Move(Integer.parseInt(BoardManager.startPos), 0))));
            NetworkingListView.setItems(FXCollections.observableList(NetworkingCommon.networkingLog));

            for (Rectangle r : BoardManager.ImageGridList) {
                r.setStroke(null);
                r.setEffect(null);
                for (Move m : moveListView.getItems()) {
                    if (m.getEndPosition() == Integer.parseInt(r.getId())) {
                        DropShadow borderGlow = new DropShadow();
                        borderGlow.setOffsetY(0f);
                        borderGlow.setOffsetX(0f);
                        borderGlow.setColor(Color.BLUE);
                        borderGlow.setWidth(10);
                        borderGlow.setHeight(10);
                        // r.setFill(Color.BLUE);
                        if (BoardManager.gameBoard.get(m.getEndPosition()).pieceOnGrid.getType() != ChessPieceTypes.NONE) {
                            borderGlow.setWidth(100);
                            borderGlow.setHeight(100);
                            borderGlow.setColor(Color.RED);
                            r.setStroke(Color.RED);

                        } else {
                            r.setStroke(Color.RED);
                            r.setEffect(borderGlow);
                        }
                    }
                    if (Objects.equals(BoardManager.startPos, r.getId())) {
                        DropShadow borderGlow = new DropShadow();
                        borderGlow.setOffsetY(0f);
                        borderGlow.setOffsetX(0f);
                        borderGlow.setColor(Color.GREEN);
                        borderGlow.setWidth(30);
                        borderGlow.setHeight(30);
                        r.setEffect(borderGlow);
                        r.setStroke(Color.GREEN);
                    }
                    if (Objects.equals(BoardManager.endPos, r.getId())) {
                        DropShadow borderGlow = new DropShadow();
                        borderGlow.setOffsetY(0f);
                        borderGlow.setOffsetX(0f);
                        borderGlow.setColor(Color.CYAN);
                        borderGlow.setWidth(10);
                        borderGlow.setHeight(10);
                        r.setEffect(borderGlow);
                        r.setStroke(Color.CYAN);
                    }
                }
            }
        });
    }

    @FXML
    private void CopyFENButtonClicked(){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();

        content.putString(moveHistoryListView.getSelectionModel().getSelectedItem());

        clipboard.setContent(content);

        Alert showFEN = new Alert(Alert.AlertType.INFORMATION,moveHistoryListView.getSelectionModel().getSelectedItem());
        showFEN.show();
    }

    public void UpdateUI() throws InterruptedException {
        welcomeText.setText("Next Move: " + BoardManager.getPlayerToMoveNext().name());
        AIThoughtsView.getItems().addAll(BoardManager.hal9000.AIThoughts);

        RenderBoardinWindow();
        StrengthScore score;
        score = BoardManager.calculateBoardValues(BoardManager.gameBoard);
        BlackScoreLabel.setText("Black Score: "+ score.getBlackText());
        WhiteScoreLabel.setText("White Score: "+ score.getWhiteText());
        TurnCountLabel.setText("Turn Count:" + (int) BoardManager.TurnCounter);


/*            XYChart.Series set1 = new XYChart.Series();

            set1.getData().add(new XYChart.Data("White",score.getWhiteTeam()));
            set1.getData().add(new XYChart.Data("Black",score.getBlackTeam()));
            StatsBarChart.getData().addAll(set1);*/

        if(AIMenuEnabled.isSelected()){

              if(WhiteAI.isSelected() && BoardManager.getPlayerToMoveNext() == ChessTeamColor.WHITE ){
                if(BoardManager.hal9000.isRunning()) {

                    AIMenuEnabled.setSelected(BoardManager.hal9000.isRunning());
                    forceAIMoveClick();
                }
            }
            if(BlackAI.isSelected() && BoardManager.getPlayerToMoveNext() == ChessTeamColor.BLACK ){
                if(BoardManager.hal9000.isRunning()) {

                    AIMenuEnabled.setSelected(BoardManager.hal9000.isRunning());
                    forceAIMoveClick();
                }
            }

            if(BlackAI.isSelected() && WhiteAI.isSelected()){
                if (BoardManager.hal9000.isRunning()) {

                    AIMenuEnabled.setSelected(BoardManager.hal9000.isRunning());
                    forceAIMoveClick();

                }
            }
        }
    }

    private void RenderBoardinWindow(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BoardRenderPane.getChildren().clear();
                BoardRenderPane.getChildren().add(BoardManager.RenderBoard((int) Math.round(gridSizeSlider.getValue())));

                BoardRenderPane.setLayoutX(BoardRenderPane.getBoundsInParent().getCenterX() - (BoardRenderPane.getWidth()/2));
                BoardRenderPane.setLayoutY(BoardRenderPane.getBoundsInParent().getCenterY() - (BoardRenderPane.getHeight()/2));

                captureRenderer.getChildren().clear();
                captureRenderer.getChildren().add(BoardManager.RenderCapturedPieces(50));
            }
        });


    }
}