package com.jfxchess.jfxchess.Data;

import java.util.ArrayList;
import java.util.List;

public class Rules {

    private  String failureReason;

    public boolean isMoveValid(Move move, List<ChessGrid> gameBoard){
        boolean moveValid = false;
        List<Move> allMoves = calculatePossibleMoves(gameBoard,move);

        ChessPiece piece = BoardManager.getPieceByPosition(move.startPosition);

        if (allMoves.size() <=0){
            failureReason = "No Move available for " + piece.type.toString();
            System.out.println("No moves available");
            return false;
        }
        else
        {
            for (Move mv: allMoves) {
                if (mv.getStartPosition() == move.startPosition && mv.getEndPosition() == move.getEndPosition()) {
                    moveValid = true;
                    piece.setFirstMove(false);
                    break;
                }
            }
            if (!moveValid)
                failureReason = "Not a valid Move for : " + piece.type.toString();
        }

        //todo: Improve check detection

        for (Move m: listAllMovesOnBoard(gameBoard)) {
            if(m.isWillResultInCapture()&& m.capturedPiece == ChessPieceTypes.KING){

                    failureReason = "Check Detected";
                    BoardManager.hal9000.setRunning(false);
                    return false;
            }
        }
        return moveValid;
    }

    public int MoveGenerationTest(int depth, List<ChessGrid> gameBoard){
        if (depth == 0)
        {
            return 1;
        }

        List<Move> allMoves = listAllMovesOnBoard(gameBoard);
        int numPositions = 0;

        for (Move move : allMoves) {
          numPositions+=MoveGenerationTest(depth -1,gameBoard);
        }
        System.out.println(numPositions);
    return numPositions;
    }

    public List<Move> listAllMovesOnBoard(List<ChessGrid> gameBoard){
        List<Move> allMoves = new ArrayList<>();

        int counter = 0;
        for (ChessGrid m: gameBoard  ) {
            allMoves.addAll(calculatePossibleMoves(gameBoard,new Move(m.location)));
        }
        return  allMoves;
    }

    public boolean isMoveSane(Move move, List<ChessGrid> gameBoard, ChessPiece piece){
        if(gameBoard.get(move.startPosition).pieceOnGrid.type == ChessPieceTypes.NONE){
           // System.out.println("RULE VIOLATION: EMPTY SQUARE ATTEMPTED TO MOVE");
            failureReason = "RULE VIOLATION: EMPTY SQUARE ATTEMPTED TO MOVE";
            return false;
        }

        if(move.startPosition < 0 || move.startPosition > 63 || move.endPosition < 0 || move.endPosition > 63){
          //  System.out.println("RULE VIOLATION: OUT OF BOUNDS");
            failureReason = "RULE VIOLATION: OUT OF BOUNDS";
            return false;
        }

        if(gameBoard.get(move.endPosition).pieceOnGrid.teamColor == gameBoard.get(move.startPosition).pieceOnGrid.teamColor){
         //   System.out.println("RULE VIOLATION: SAME TEAM");
            failureReason = "RULE VIOLATION: SAME TEAM";

            return false;
        }


        return true;
    }

    public Move TestForCapture(Move subjectUnderTest){
        if(BoardManager.gameBoard.get(subjectUnderTest.endPosition).pieceOnGrid.type !=ChessPieceTypes.NONE){
            subjectUnderTest.setWillResultInCapture(true);
            subjectUnderTest.setCapturedPiece(BoardManager.gameBoard.get(subjectUnderTest.endPosition).pieceOnGrid.type);
        }
        return subjectUnderTest;
    }


    public List<Move> calculatePossibleMoves(List<ChessGrid> gameBoard, Move StartPosition){

        ChessPiece piece = BoardManager.getPieceByPosition(StartPosition.startPosition);

        List<Move> possibilities = new ArrayList<>();

        switch (piece.type) {
            case PAWN -> {
                Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);

                if(piece.isFirstMove()) {
                    if (piece.teamColor == ChessTeamColor.WHITE) { //Test moving forward 2 squares
                        testPosition.setEndPosition(BoardManager.decrementY(StartPosition.startPosition, 2));
                    } else {
                        testPosition.setEndPosition(BoardManager.incrementY(StartPosition.startPosition, 2));
                    }
                    if (isMoveSane(testPosition, gameBoard, piece)) {
                        if (gameBoard.get(testPosition.endPosition).pieceOnGrid.type == ChessPieceTypes.NONE)
                            possibilities.add(testPosition);
                    }

                }
                //test moving forward 1 square
                Move testPosition2 = new Move(StartPosition.startPosition, StartPosition.endPosition);


                if(piece.teamColor==ChessTeamColor.WHITE) {
                    testPosition2.setEndPosition(BoardManager.decrementY(StartPosition.startPosition, 1));
                } else{
                    testPosition2.setEndPosition(BoardManager.incrementY(StartPosition.startPosition, 1));
                }

                if (isMoveSane(testPosition2, gameBoard, piece)) {
                    if(gameBoard.get(testPosition2.endPosition).pieceOnGrid.type== ChessPieceTypes.NONE)
                        possibilities.add(testPosition2);
                }

                //check for attack one way
                Move testPosition3 = new Move(StartPosition.startPosition, StartPosition.endPosition);
                if(piece.teamColor == ChessTeamColor.WHITE){
                    testPosition3.setEndPosition(BoardManager.decrementX(testPosition3.startPosition,1));
                    testPosition3.setEndPosition(BoardManager.decrementY(testPosition3.getEndPosition(),1));

                    if(isMoveSane(testPosition3,gameBoard,piece) && testPosition3.getEndX()< testPosition3.getStartX()){
                        if(gameBoard.get(testPosition3.endPosition).pieceOnGrid.teamColor==ChessTeamColor.BLACK)
                            possibilities.add(testPosition3);
                    }

                } else {
                    testPosition3.setEndPosition(BoardManager.incrementX(testPosition3.startPosition,1));
                    testPosition3.setEndPosition(BoardManager.incrementY(testPosition3.getEndPosition(),1));
                    if(isMoveSane(testPosition3,gameBoard,piece) && testPosition3.getEndX() > testPosition3.getStartX()){
                       if(gameBoard.get(testPosition3.endPosition).pieceOnGrid.teamColor==ChessTeamColor.WHITE)
                            possibilities.add(testPosition3);
                    }
                }
                //check for attack the other way
                Move testPosition4 = new Move(StartPosition.startPosition, StartPosition.endPosition);
                if(piece.teamColor == ChessTeamColor.WHITE){
                    testPosition4.setEndPosition(BoardManager.incrementX(testPosition4.startPosition,1));
                    testPosition4.setEndPosition(BoardManager.decrementY(testPosition4.getEndPosition(),1));
                    if(isMoveSane(testPosition4,gameBoard,piece )&& testPosition4.getEndX() > testPosition4.getStartX()){
                        if(gameBoard.get(testPosition4.endPosition).pieceOnGrid.teamColor==ChessTeamColor.BLACK)
                            possibilities.add(testPosition4);
                    }

                } else {
                    testPosition4.setEndPosition(BoardManager.decrementX(testPosition4.startPosition,1));
                    testPosition4.setEndPosition(BoardManager.incrementY(testPosition4.getEndPosition(),1));
                    if(isMoveSane(testPosition4,gameBoard,piece)&& testPosition4.getEndY() > testPosition4.getStartY()){
                        if(gameBoard.get(testPosition4.endPosition).pieceOnGrid.teamColor==ChessTeamColor.WHITE)
                            possibilities.add(testPosition4);
                    }
                }
           }

            case ROOK -> {

                checkLeftHorizontal(piece, gameBoard, StartPosition, possibilities);
                checkRightHorizontal(piece, gameBoard, StartPosition, possibilities);
                checkStraightDown(piece, gameBoard, StartPosition, possibilities);
                checkStraightUp(piece, gameBoard, StartPosition, possibilities);

            }

            case BISHOP -> {
                checkDiagonalLeftUp(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalRightUp(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalRightDown(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalLeftDown(piece, gameBoard, StartPosition, possibilities);
            }

            case QUEEN -> {
                checkLeftHorizontal(piece, gameBoard, StartPosition, possibilities);


                checkRightHorizontal(piece, gameBoard, StartPosition, possibilities);
                                //check straight down
                checkStraightDown(piece, gameBoard, StartPosition, possibilities);
                //check straight up
                checkStraightUp(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalLeftUp(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalRightUp(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalRightDown(piece, gameBoard, StartPosition, possibilities);
                checkDiagonalLeftDown(piece, gameBoard, StartPosition, possibilities);

            }

            case KING -> {
                Move testPosition = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition.setEndPosition(BoardManager.decrementX(testPosition.startPosition,1));
                if(isMoveSane(testPosition,gameBoard,piece) && testPosition.getEndX() < testPosition.getStartX() && testPosition.getEndY() == testPosition.getStartY()){
                    possibilities.add(testPosition);
                }
                Move testPosition2 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition2.setEndPosition(BoardManager.incrementX(testPosition2.startPosition,1));
                if(isMoveSane(testPosition2,gameBoard,piece) && testPosition2.getEndX() > testPosition2.getStartX() && testPosition2.getEndY() == testPosition2.getStartY()){
                    possibilities.add(testPosition2);
                }

                Move testPosition3 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition3.setEndPosition(BoardManager.incrementY(testPosition3.startPosition,1));
                if(isMoveSane(testPosition3,gameBoard,piece) && testPosition3.getEndY() > testPosition3.getStartY() && testPosition3.getEndX() == testPosition3.getStartX()){
                    possibilities.add(testPosition3);
                }

                Move testPosition4 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition4.setEndPosition(BoardManager.decrementY(testPosition4.startPosition,1));
                if(isMoveSane(testPosition4,gameBoard,piece) && testPosition4.getEndY() < testPosition4.getStartY() && testPosition4.getEndX() == testPosition4.getStartX()){
                    possibilities.add(testPosition4);
                }

                Move testPosition5 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition5.setEndPosition(BoardManager.incrementX(testPosition5.startPosition,1));
                testPosition5.setEndPosition(BoardManager.incrementY(testPosition5.endPosition,1));

                if(isMoveSane(testPosition5,gameBoard,piece) && (testPosition5.getStartX() + 1) == testPosition5.getEndX()){
                    possibilities.add(testPosition5);
                }

                Move testPosition6 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition6.setEndPosition(BoardManager.decrementX(testPosition6.startPosition,1));
                testPosition6.setEndPosition(BoardManager.incrementY(testPosition6.endPosition,1));

                if(isMoveSane(testPosition6,gameBoard,piece)&& testPosition6.getStartX() > testPosition6.getEndX()){
                    possibilities.add(testPosition6);
                }

                Move testPosition7 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition7.setEndPosition(BoardManager.incrementX(testPosition7.startPosition,1));
                testPosition7.setEndPosition(BoardManager.decrementY(testPosition7.endPosition,1));

                if(isMoveSane(testPosition7,gameBoard,piece)&& testPosition7.getEndX() > testPosition7.getStartX()){
                    possibilities.add(testPosition7);
                }

                Move testPosition8 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition8.setEndPosition(BoardManager.decrementX(testPosition8.startPosition,1));
                testPosition8.setEndPosition(BoardManager.decrementY(testPosition8.endPosition,1));

                if(isMoveSane(testPosition8,gameBoard,piece) && testPosition8.getEndX() < testPosition8.getStartX()){
                    possibilities.add(testPosition8);
                }

            }
            case KNIGHT -> {

                //todo: Some funky swizzle going on here. fix when awake

                //test L Left top
                Move testPosition = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition.setEndPosition(BoardManager.decrementX(testPosition.startPosition,1));
                testPosition.setEndPosition(BoardManager.decrementY(testPosition.endPosition,2));

                if(isMoveSane(testPosition,gameBoard,piece) && testPosition.getEndX() < testPosition.getStartX() && testPosition.getEndY() < testPosition.getStartY()){
                        possibilities.add(testPosition);
                }

                //test L Right top
                Move testPosition2 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition2.setEndPosition(BoardManager.incrementX(testPosition2.startPosition,1));
                testPosition2.setEndPosition(BoardManager.decrementY(testPosition2.endPosition,2));

                if(isMoveSane(testPosition2,gameBoard,piece)&& testPosition.getEndY() < testPosition.getStartY()){
                    possibilities.add(testPosition2);
                }

                //test L Top on right side
                Move testPosition3 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition3.setEndPosition(BoardManager.incrementX(testPosition3.startPosition,2));
                testPosition3.setEndPosition(BoardManager.decrementY(testPosition3.endPosition,1));

                if(isMoveSane(testPosition3,gameBoard,piece)&& testPosition.getEndY() < testPosition.getStartY()){
                    possibilities.add(testPosition3);
                }

                //test L Bottom on right side
                Move testPosition4 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition4.setEndPosition(BoardManager.incrementX(testPosition4.startPosition,2));
                testPosition4.setEndPosition(BoardManager.incrementY(testPosition4.endPosition,1));

                if(isMoveSane(testPosition4,gameBoard,piece)&& (testPosition4.getStartX() + 1) == testPosition4.getEndX()){
                    possibilities.add(testPosition4);
                }

                //test L Right on bottom side
                Move testPosition5 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition5.setEndPosition(BoardManager.incrementX(testPosition5.startPosition,1));
                testPosition5.setEndPosition(BoardManager.incrementY(testPosition5.endPosition,2));

                if(isMoveSane(testPosition5,gameBoard,piece) && (testPosition5.getStartX() + 1) == testPosition5.getEndX()){
                    possibilities.add(testPosition5);
                }

                //test L Left on bottom side
                Move testPosition6 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition6.setEndPosition(BoardManager.decrementX(testPosition6.startPosition,1));
                testPosition6.setEndPosition(BoardManager.incrementY(testPosition6.endPosition,2));

                if(isMoveSane(testPosition6,gameBoard,piece)&& testPosition6.getStartX() > testPosition6.getEndX()){
                    possibilities.add(testPosition6);
                }

                //test L Left on bottom side
                Move testPosition7 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition7.setEndPosition(BoardManager.decrementX(testPosition7.startPosition,2));
                testPosition7.setEndPosition(BoardManager.incrementY(testPosition7.endPosition,1));

                if(isMoveSane(testPosition7,gameBoard,piece)&& testPosition7.getEndX() < testPosition7.getStartX()){
                    possibilities.add(testPosition7);
                }
                //test L Left on bottom side
                Move testPosition8 = new Move(StartPosition.startPosition,StartPosition.endPosition);
                testPosition8.setEndPosition(BoardManager.decrementX(testPosition8.startPosition,2));
                testPosition8.setEndPosition(BoardManager.decrementY(testPosition8.endPosition,1));

                if(isMoveSane(testPosition8,gameBoard,piece) && testPosition8.getEndX() < testPosition8.getStartX()){
                    possibilities.add(testPosition8);
                }

            }

        }

        for (Move possibleMoves : possibilities) {
            possibleMoves = TestForCapture(possibleMoves);
        }

        return possibilities;
    }

    private void checkDiagonalLeftDown(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;
        //check diagonal left / down
        counter=1;
        for (; counter < 8; counter ++){
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.incrementY(testPosition.getStartPosition(),counter));
            testPosition.setEndPosition(BoardManager.decrementX(testPosition.getEndPosition(),counter));
            if(isMoveSane(testPosition, gameBoard, piece) && testPosition.getStartX() > testPosition.getEndX()) {
                possibilities.add(testPosition);
                if(gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE){
                    counter =8;
                }
            }else{
                counter = 8;
            }
        }
    }

    private void checkDiagonalRightDown(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;
        //check diagonal right / down
        counter=1;
        for (; counter < 8; counter ++){
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.incrementY(testPosition.getStartPosition(),counter));
            testPosition.setEndPosition(BoardManager.incrementX(testPosition.getEndPosition(),counter));
            if(isMoveSane(testPosition, gameBoard, piece) && testPosition.getStartX() < testPosition.getEndX()) {
                possibilities.add(testPosition);
                if(gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE){
                    counter =8;
                }
            }else{
                counter = 8;
            }
        }
    }

    private void checkDiagonalRightUp(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;
        //check diagonal right / up
        counter=1;
        for (; counter < 8; counter ++){
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.decrementY(testPosition.getStartPosition(),counter));
            testPosition.setEndPosition(BoardManager.incrementX(testPosition.getEndPosition(),counter));
            if(isMoveSane(testPosition, gameBoard, piece)&& testPosition.getStartX() < testPosition.getEndX()) {
                possibilities.add(testPosition);
                if(gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE){
                    counter =8;
                }
            }else{
                counter = 8;
            }
        }
    }

    private void checkDiagonalLeftUp(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;
        //check diagonal left / up
        counter=1;
        for (; counter < 8; counter ++){
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.decrementY(testPosition.getStartPosition(),counter));
            testPosition.setEndPosition(BoardManager.decrementX(testPosition.getEndPosition(),counter));
            if(isMoveSane(testPosition, gameBoard, piece) && testPosition.getEndX() < testPosition.getStartX()) {
                possibilities.add(testPosition);
                if(gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE){
                    counter =8;
                }
            }else{
                counter = 8;
            }
        }
    }

    private void checkStraightUp(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;
        counter = 1;
        for (; counter < 8; counter++) {
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.decrementY(testPosition.getStartPosition(), counter));
            if (isMoveSane(testPosition, gameBoard, piece)) {
                possibilities.add(testPosition);
                if (gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE) {
                    counter = 8;
                }
            } else {
                counter = 8;
            }
        }
    }

    private void checkStraightDown(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;//check straight down
        counter = 1;
        for (; counter < 8; counter++) {
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.incrementY(testPosition.getStartPosition(), counter));
            if (isMoveSane(testPosition, gameBoard, piece)) {
                possibilities.add(testPosition);
                if (gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE) {
                    counter = 8;
                }
            } else {
                counter = 8;
            }
        }
    }

    private void checkRightHorizontal(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        int counter;

        //check right horizontal
        counter = 1;
        for (; counter < 8; counter++) {
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.incrementX(testPosition.getStartPosition(), counter));
            if (isMoveSane(testPosition, gameBoard, piece) && testPosition.getStartY() == testPosition.getEndY()) {
                possibilities.add(testPosition);
                if (gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE) {
                    counter = 8;
                }
            } else {
                counter = 8;
            }
        }
    }

    private void checkLeftHorizontal(ChessPiece piece, List<ChessGrid> gameBoard, Move StartPosition, List<Move> possibilities) {
        //check left horizontal
        int counter = 1;
        for (; counter < 8; counter++) {
            Move testPosition = new Move(StartPosition.startPosition, StartPosition.endPosition);
            testPosition.setEndPosition(BoardManager.decrementX(testPosition.getStartPosition(), counter));
            if (isMoveSane(testPosition, gameBoard, piece) && testPosition.getStartY() == testPosition.getEndY()) {
                possibilities.add(testPosition);
                if (gameBoard.get(testPosition.getEndPosition()).pieceOnGrid.type != ChessPieceTypes.NONE) {
                    counter = 8;
                }
            } else {
                counter = 8;
            }
        }
    }

}
