package com.jfxchess.jfxchess;

import com.jfxchess.jfxchess.Data.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import java.io.IOException;
import java.util.Objects;

public class MainUIController {
    @FXML
    private Label welcomeText;
    @FXML
    private Pane BoardRenderPane;
    @FXML
    private Pane captureRenderer;
    @FXML
    private TextField FENInputBox;
    @FXML
    private Button MoveButton;
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
    private CheckMenuItem AIMenuEnabled;
    @FXML
    private CheckMenuItem WhiteAI;
    @FXML
    private CheckMenuItem BlackAI;
    @FXML
    private CheckBox AIEnabledCheckBox;
    @FXML
    private Spinner<ChessTeamColor> AISideSpinner;
    @FXML
    private CheckBox AIPlayWhiteCheckbox;
    @FXML
    private CheckBox ALPlayBlackCheckbox;
    @FXML
    private Label BlackScoreLabel;
    @FXML
    private Label WhiteScorelabel;
    @FXML
    private Label TurnCountLabel;

    @FXML
    private CategoryAxis XAxsis;
    @FXML
    private NumberAxis YAxsis;
    @FXML
    private BarChart StatsBarChart ;
    @FXML
    private ColorPicker WhiteSquareColorChooser;

    @FXML
    private ColorPicker BlackSquareColorChooser;

    private PGNController pgnController;

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
    protected  void updateGridSquareColor(ActionEvent event) throws InterruptedException {

        BoardManager.whiteSquares = WhiteSquareColorChooser.getValue();
        BoardManager.blackSquares = BlackSquareColorChooser.getValue();
        UpdateUI();

    }

    @FXML
    private void MoveButtonClick() throws InterruptedException {
        Move tmpMove = new Move();

        tmpMove.setStartPosition(Integer.parseInt(moveStartInputBox.getText()));
        tmpMove.setEndPosition(Integer.parseInt(moveEndInputBox.getText()));

        tmpMove.calculateXY();
        BoardManager.MovePiece(tmpMove);
        moveHistoryListView.getItems().add(BoardManager.SavePositionToFEN());
        UpdateUI();

      //  Alert calc = new Alert(Alert.AlertType.INFORMATION, "Number of positions: "+ BoardManager.ruleBook.MoveGenerationTest(4,BoardManager.gameBoard));

    }

    @FXML
    protected  void importPNGMenuClick(ActionEvent event ) throws IOException {

        FileChooser fileChooser = new FileChooser();


        Alert clicked = new Alert(Alert.AlertType.INFORMATION, pgnController.importSingleGame(fileChooser.showOpenDialog(null).getAbsoluteFile().toPath()).getUtcDate());
        clicked.show();

    }

    @FXML
    private void updateGridSize(ActionEvent event) throws InterruptedException {
        UpdateUI();
    }

    @FXML
    public void initialize(){
        welcomeText.setText("JFXChess v1");
        FENInputBox.setText("r6r/7p/3bb2n/pPp5/4QP2/2N5/P1P4P/R3K1NR w KQ - 0 1");

        gridSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                try {
                    UpdateUI();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        AIPlayWhiteCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                WhiteAI.setSelected(AIPlayWhiteCheckbox.isSelected());
            }
        });

        ALPlayBlackCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BlackAI.setSelected(ALPlayBlackCheckbox.isSelected());
            }
        });

        AIEnabledCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AIMenuEnabled.setSelected(AIEnabledCheckBox.isSelected());
            }
        });

        moveHistoryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    FENInputBox.setText(moveHistoryListView.getSelectionModel().getSelectedItem());
                    try {
                        onHelloButtonClick();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        BoardRenderPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {



                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        try {
                            MoveButtonClick();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }

                moveStartInputBox.setText(BoardManager.startPos);
                moveEndInputBox.setText(BoardManager.endPos);
                moveListView.setItems(FXCollections.observableList(BoardManager.ruleBook.calculatePossibleMoves(BoardManager.gameBoard.get(Integer.parseInt(BoardManager.startPos)).pieceOnGrid,BoardManager.gameBoard,new Move(Integer.parseInt(BoardManager.startPos),0))));


                for ( Rectangle r : BoardManager.ImageGridList ) {
                    r.setStroke(null);
                    r.setEffect(null);
                    for (Move m:moveListView.getItems()) {
                        if (m.getEndPosition()== Integer.parseInt(r.getId())){
                            DropShadow borderGlow= new DropShadow();
                            borderGlow.setOffsetY(0f);
                            borderGlow.setOffsetX(0f);
                            borderGlow.setColor(Color.BLUE);
                            borderGlow.setWidth(10);
                            borderGlow.setHeight(10);
                           // r.setFill(Color.BLUE);
                            if(BoardManager.gameBoard.get(m.getEndPosition()).pieceOnGrid.getType() != ChessPieceTypes.NONE){
                                borderGlow.setWidth(100);
                                borderGlow.setHeight(100);
                                borderGlow.setColor(Color.RED);
                                r.setStroke(Color.RED);

                            }else {
                                r.setStroke(Color.RED);
                                r.setEffect(borderGlow);
                            }
                        }
                        if (Objects.equals(BoardManager.startPos, r.getId())){
                            DropShadow borderGlow= new DropShadow();
                            borderGlow.setOffsetY(0f);
                            borderGlow.setOffsetX(0f);
                            borderGlow.setColor(Color.GREEN);
                            borderGlow.setWidth(30);
                            borderGlow.setHeight(30);
                            r.setEffect(borderGlow);
                            r.setStroke(Color.GREEN);
                        }
                        if (Objects.equals(BoardManager.endPos, r.getId())){
                            DropShadow borderGlow= new DropShadow();
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

    private void UpdateUI() throws InterruptedException {
        welcomeText.setText("Next Move: " + BoardManager.getPlayerToMoveNext().name());


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
        AIThoughtsView.getItems().addAll(BoardManager.hal9000.AIThoughts);

        RenderBoardinWindow();
        StrengthScore score;
        score = BoardManager.calculateBoardValues(BoardManager.gameBoard);
        BlackScoreLabel.setText("Black Score: "+ score.getBlackText());
        WhiteScorelabel.setText("White Score: "+ score.getWhiteText());
        TurnCountLabel.setText("Turn Count:" + (int) BoardManager.TurnCounter);

            XYChart.Series set1 = new XYChart.Series();

            set1.getData().add(new XYChart.Data("White",score.getWhiteTeam()));
             set1.getData().add(new XYChart.Data("Black",score.getBlackTeam()));
            StatsBarChart.getData().addAll(set1);




    }

    private void RenderBoardinWindow(){
        BoardRenderPane.getChildren().clear();
        BoardRenderPane.getChildren().add(BoardManager.RenderBoard((int) Math.round(gridSizeSlider.getValue())));
        captureRenderer.getChildren().clear();
        captureRenderer.getChildren().add(BoardManager.RenderCapturedPieces(50));

    }
}