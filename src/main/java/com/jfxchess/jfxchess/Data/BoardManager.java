package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.MainUIController;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public  class BoardManager {
    static public List<ChessGrid> gameBoard = new ArrayList<ChessGrid>();
    static ChessTeamColor playerToMoveNext;

    public static final Rules ruleBook = new Rules();
    public static final AIController hal9000 = new AIController();

    public static String startPos;
    public static String endPos;
    public static Integer lastClickedPos;

    public static List<Rectangle> ImageGridList = new ArrayList<>();

    public static double TurnCounter ;

    static MediaController mediaController = new MediaController();
    public static List<ChessPiece> capturedPieces = new ArrayList<>();

    public static Color whiteSquares;
    public static  Color blackSquares;

    public static PGNController pgnController;


    public BoardManager() {

    }

    public static void SetupNewStandardBoard(){
        int counter=0;

        for(;counter <64; counter++){
            gameBoard.add(new  ChessGrid(new ChessPiece(ChessPieceTypes.NONE), counter ));
            gameBoard.get(counter).setGridLabel(BoardLabels.values()[counter]);
        }
        playerToMoveNext = ChessTeamColor.WHITE;
        TurnCounter =0;
        capturedPieces.clear();
        hal9000.AIThoughts.clear();
        hal9000.setRunning(true);
    }

    public static void printBoard(){
        for (ChessGrid currentGrid:gameBoard) {
            System.out.println(currentGrid);
        }
    }

    private static void printInfo(int Position){
        System.out.println("Grid ID:" + Position + " Piece:" + gameBoard.get(Position).pieceOnGrid.type.toString() + " Color:" + gameBoard.get(Position).pieceOnGrid.teamColor.toString());
    }


    public static int incrementX(int position, int delta){ return position + delta; }
    public static int decrementX(int position,int delta){ return position - delta; }
    public static int incrementY(int position,int delta){
        return position + (delta *8);
    }
    public static int decrementY(int position,int delta){
        return position - (delta *8);
    }


    public static Pane RenderCapturedPieces(int GridSize){
        Pane grapicContext = new Pane();
        int xPos=0,yPos=0;
        int currentSquare =0;

        for (ChessPiece gridSquare : capturedPieces){
            Rectangle r = new Rectangle(xPos,yPos, GridSize,GridSize);
            r.setFill(gridSquare.getGraphic());
            grapicContext.getChildren().add(r);

            xPos += GridSize;
            currentSquare++;
            if(currentSquare % 8 == 0){
                yPos += GridSize;
                xPos = 0;

            }
        }
        return grapicContext;
    }

    public static StrengthScore calculateBoardValues(List<ChessGrid> gameBoard){
        StrengthScore values = new StrengthScore();
        for (ChessGrid grid : gameBoard) {
            if (grid.pieceOnGrid.teamColor == ChessTeamColor.WHITE)
            {
                values.WhiteTeam += grid.pieceOnGrid.type.ordinal();
            }
            if (grid.pieceOnGrid.teamColor == ChessTeamColor.BLACK)
            {
                values.BlackTeam += grid.pieceOnGrid.type.ordinal();
            }
        }
        return  values;
    }

    public static Pane RenderBoard(int GridSize){
        Pane graphicContext = new Pane();

        int xPos=0,yPos=0;
        int currentSquare =0;

        for (ChessGrid gridSquare : gameBoard ) {

            Rectangle r = new Rectangle(xPos,yPos, GridSize,GridSize);
            r.setId(String.valueOf(currentSquare));
            Label idLabel = new Label(gridSquare.getGridLabel().toString());

            Rectangle grid = new Rectangle(xPos,yPos,GridSize,GridSize);
            grid.setId(String.valueOf(currentSquare));

            if(gameBoard.get(currentSquare).gridColor==GridColor.BLACK){
                if(whiteSquares!=null){
                    grid.setFill(blackSquares);
                }else grid.setFill(Color.LIGHTSLATEGRAY);

            }else
                if (whiteSquares !=null){
                    grid.setFill(whiteSquares);
                }else
                grid.setFill(Color.GREY);

            idLabel.setTranslateX(r.getX());
            idLabel.setTranslateY(r.getY());
            r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(playerToMoveNext == ChessTeamColor.WHITE) {
                        if(event.getButton() == MouseButton.SECONDARY){
                            printInfo(Integer.parseInt(r.getId()));
                        }
                        if (gameBoard.get(Integer.parseInt(r.getId())).pieceOnGrid.teamColor == ChessTeamColor.WHITE){
                            startPos = r.getId();
                        }

                        if (gameBoard.get(Integer.parseInt(r.getId())).pieceOnGrid.type == ChessPieceTypes.NONE){
                            endPos = r.getId();
                        }

                        if (gameBoard.get(Integer.parseInt(r.getId())).pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                            endPos = r.getId();
                        }
                    }else{
                        if (gameBoard.get(Integer.parseInt(r.getId())).pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                            startPos = r.getId();
                        }

                        if (gameBoard.get(Integer.parseInt(r.getId())).pieceOnGrid.type == ChessPieceTypes.NONE){
                            endPos = r.getId();
                        }

                        if (gameBoard.get(Integer.parseInt(r.getId())).pieceOnGrid.teamColor == ChessTeamColor.WHITE){
                            endPos = r.getId();
                        }
                    }

                }
            });

            grid.setOnMouseClicked(r.getOnMouseClicked());
            ImagePattern tmpImg = null;
            switch (gridSquare.pieceOnGrid.type){

                case ROOK -> {

                    if(gridSquare.pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                        tmpImg = new ImagePattern(mediaController.blackRook);
                    }
                    else{
                         tmpImg = new ImagePattern(mediaController.whiteRook);
                    }


                }

                case BISHOP -> {
                    if(gridSquare.pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                        tmpImg = new ImagePattern(mediaController.blackBishop);
                    }
                    else{
                        tmpImg = new ImagePattern(mediaController.whiteBishop);
                    }
                }

                case PAWN -> {
                    if(gridSquare.pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                        tmpImg = new ImagePattern(mediaController.blackPawn);
                    }
                    else{
                        tmpImg = new ImagePattern(mediaController.whitePawn);
                    }

                }

                case KNIGHT -> {
                    if(gridSquare.pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                        tmpImg = new ImagePattern(mediaController.blackKnight);
                    }
                    else{
                        tmpImg = new ImagePattern(mediaController.whiteKnight);
                    }
                }

                case KING -> {
                    if(gridSquare.pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                        tmpImg = new ImagePattern(mediaController.blackKing);
                    }
                    else{
                        tmpImg = new ImagePattern(mediaController.whiteKing);
                    }
                }

                case QUEEN -> {
                    if(gridSquare.pieceOnGrid.teamColor == ChessTeamColor.BLACK){
                        tmpImg = new ImagePattern(mediaController.blackQueen);
                    }
                    else{
                        tmpImg = new ImagePattern(mediaController.whiteQueen);
                    }
                }
            }

            gameBoard.get(currentSquare).pieceOnGrid.graphic = tmpImg;

            r.setAccessibleHelp(r.getId());
            r.setFill(tmpImg);

            r.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    TranslateTransition transition = new TranslateTransition(Duration.millis(500), r);
                    transition.setOnFinished(t -> {
                        r.setX(r.getTranslateX() + r.getX());
                        r.setY(r.getTranslateY() + r.getY());
                        r.setTranslateX(0);
                        r.setTranslateY(0);

                        transition.stop();
                        transition.setToX(event.getX() - 50 / 2 - r.getX());
                        transition.setToY(event.getY() - 50 / 2 - r.getY());
                        transition.playFromStart();
                    });
                }
            });
            ImageGridList.add(r);
            ImageGridList.add(grid);
            graphicContext.getChildren().add(grid);
            graphicContext.getChildren().add(r);
            graphicContext.getChildren().add(idLabel);
            currentSquare++;

            xPos += GridSize;
            if(currentSquare % 8 == 0){
                yPos += GridSize;
                xPos = 0;

            }
        }

        graphicContext.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return  graphicContext;
    }

    public static void MovePiece(Move move){
        if (ruleBook.isMoveValid(move, gameBoard, gameBoard.get(move.startPosition).pieceOnGrid)) {
            if(move.isWillResultInCapture()){
                capturedPieces.add(new ChessPiece(gameBoard.get(move.endPosition).pieceOnGrid.type,gameBoard.get(move.endPosition).pieceOnGrid.teamColor,gameBoard.get(move.endPosition).pieceOnGrid.graphic));
            }
            gameBoard.get(move.endPosition).pieceOnGrid = gameBoard.get(move.startPosition).pieceOnGrid;
            gameBoard.get(move.startPosition).pieceOnGrid = (ChessPiece) new ChessPiece();
            mediaController.chessMoveSound.play();

            incrementTurn();

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Move:  " + ruleBook.getFailureReason());
            alert.show();
        }
    }


    public static String SavePositionToFEN(){

        StringBuilder FENString = new StringBuilder();

        int countPosition =0;
        int emptySquareCount = 0;

        for (ChessGrid currentGrid : gameBoard) {

             switch (currentGrid.pieceOnGrid.type){
                case PAWN -> {
                    flushEmpty(FENString, emptySquareCount);
                    emptySquareCount = 0;
                    switch (currentGrid.pieceOnGrid.teamColor){
                        case WHITE -> {
                            FENString.append("P");
                        }
                        case BLACK -> {
                            FENString.append("p");
                        }
                    }


                }
                case BISHOP -> {
                    flushEmpty(FENString, emptySquareCount);
                    emptySquareCount = 0;
                    switch (currentGrid.pieceOnGrid.teamColor){
                        case WHITE -> {
                            FENString.append("B");
                        }

                        case BLACK -> {
                            FENString.append("b");
                        }
                    }

                }
                case ROOK -> {
                    flushEmpty(FENString, emptySquareCount);
                    emptySquareCount = 0;
                    switch (currentGrid.pieceOnGrid.teamColor){
                        case WHITE -> {
                            FENString.append("R");
                        }

                        case BLACK -> {
                            FENString.append("r");
                        }
                    }

                }
                case KNIGHT -> {
                    flushEmpty(FENString, emptySquareCount);
                    emptySquareCount = 0;
                    switch (currentGrid.pieceOnGrid.teamColor){
                        case WHITE -> {
                            FENString.append("N");
                        }

                        case BLACK -> {
                            FENString.append("n");
                        }
                    }

                }
                case QUEEN -> {
                    flushEmpty(FENString, emptySquareCount);
                    emptySquareCount = 0;
                    switch (currentGrid.pieceOnGrid.teamColor){
                        case WHITE -> {
                            FENString.append("Q");
                        }

                        case BLACK -> {
                            FENString.append("q");
                        }
                    }

                }
                case KING -> {
                    flushEmpty(FENString, emptySquareCount);
                    emptySquareCount = 0;
                    switch (currentGrid.pieceOnGrid.teamColor){
                        case WHITE -> {
                            FENString.append("K");
                        }

                        case BLACK -> {
                            FENString.append("k");
                        }
                    }

                }
                case NONE -> {
                    emptySquareCount++;
                }
            }

            countPosition++;

            if(countPosition % 8 == 0){
                flushEmpty(FENString, emptySquareCount);
                emptySquareCount=0;
                if (countPosition < 63)
                    FENString.append('/');
            }
        }


        switch (BoardManager.playerToMoveNext){
            case WHITE -> {
                FENString.append(" w ");
            }

            case BLACK -> {
                FENString.append(" b ");
            }
        }


        FENString.append("KQkq - 0 1");
        return FENString.toString();
    }

    private static void flushEmpty(StringBuilder input, int emptySquareCount){
        if(emptySquareCount > 0){
            input.append(emptySquareCount);

        }

    }


    public static void LoadPositionsFromFEN(String inputFEN) {

        gameBoard.clear();
        SetupNewStandardBoard();

        String[] processedString = inputFEN.split(" ");
        System.out.println(processedString[0]);

        int counter = 0;

        for (Character character:processedString[0].toCharArray()) {
            //White pieces in uppercase

           if(Character.isUpperCase(character)){
               ChessPiece tmpPiece = new ChessPiece();
                switch (character){
                    case 'P':
                        tmpPiece.teamColor = ChessTeamColor.WHITE;
                        tmpPiece.type = ChessPieceTypes.PAWN;
                        break;
                    case 'R':
                        tmpPiece.teamColor = ChessTeamColor.WHITE;
                        tmpPiece.type = ChessPieceTypes.ROOK;
                        break;
                    case 'N':
                        tmpPiece.teamColor = ChessTeamColor.WHITE;
                        tmpPiece.type = ChessPieceTypes.KNIGHT;
                        break;
                    case 'B':
                        tmpPiece.teamColor = ChessTeamColor.WHITE;
                        tmpPiece.type = ChessPieceTypes.BISHOP;
                        break;
                    case 'Q':
                        tmpPiece.teamColor = ChessTeamColor.WHITE;
                        tmpPiece.type = ChessPieceTypes.QUEEN;
                        break;
                    case 'K':
                        tmpPiece.teamColor = ChessTeamColor.WHITE;
                        tmpPiece.type = ChessPieceTypes.KING;
                        break;
                }
                switch (tmpPiece.type){
                    case PAWN -> {
                        if (tmpPiece.teamColor == ChessTeamColor.WHITE)
                            if(counter > 47 && counter < 56){
                            tmpPiece.setFirstMove(true);
                        }
                        else {
                            if (counter < 16 && counter > 7)
                                tmpPiece.setFirstMove(true);
                        }
                    }

                    case KING -> {
                        if(tmpPiece.teamColor == ChessTeamColor.WHITE){
                            if(counter == 59){
                                tmpPiece.setFirstMove(true);
                            }
                        }
                    }

                }
               gameBoard.get(counter).pieceOnGrid = tmpPiece;
           }

           //black pieces in lowercase
            if(Character.isLowerCase(character)){
                ChessPiece tmpPiece = new ChessPiece();
                switch (character){
                    case 'p':
                        tmpPiece.teamColor = ChessTeamColor.BLACK;
                        tmpPiece.type = ChessPieceTypes.PAWN;
                        break;
                    case 'r':
                        tmpPiece.teamColor = ChessTeamColor.BLACK;
                        tmpPiece.type = ChessPieceTypes.ROOK;
                        break;
                    case 'n':
                        tmpPiece.teamColor = ChessTeamColor.BLACK;
                        tmpPiece.type = ChessPieceTypes.KNIGHT;
                        break;
                    case 'b':
                        tmpPiece.teamColor = ChessTeamColor.BLACK;
                        tmpPiece.type = ChessPieceTypes.BISHOP;
                        break;
                    case 'q':
                        tmpPiece.teamColor = ChessTeamColor.BLACK;
                        tmpPiece.type = ChessPieceTypes.QUEEN;
                        break;
                    case 'k':
                        tmpPiece.teamColor = ChessTeamColor.BLACK;
                        tmpPiece.type = ChessPieceTypes.KING;
                        break;
                }

                switch (tmpPiece.type){
                    case PAWN -> {
                           if (counter < 16 && counter > 7)
                                    tmpPiece.setFirstMove(true);
                            }
                    }


                gameBoard.get(counter).pieceOnGrid = tmpPiece;
            }
            //spaces to skip
            if(Character.isDigit(character)){
                counter += Integer.parseInt(character.toString() ) -1;
            }

            if(character != '/'){
            counter++;
            }
        }

        switch (processedString[1]) {
            case "w" -> playerToMoveNext = ChessTeamColor.WHITE;
            case "b" -> playerToMoveNext = ChessTeamColor.BLACK;
        }

        MainUIController main = new MainUIController();

}

    public static ChessTeamColor getPlayerToMoveNext() {
        return playerToMoveNext;
    }

    public static void incrementTurn(){
        if(playerToMoveNext == ChessTeamColor.WHITE){
            playerToMoveNext = ChessTeamColor.BLACK;
        } else
        {
            playerToMoveNext = ChessTeamColor.WHITE;
        }
        TurnCounter++;
    }

    public static List<ChessGrid> getGameBoard() {
        return gameBoard;
    }
}
