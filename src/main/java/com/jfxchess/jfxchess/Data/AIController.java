package com.jfxchess.jfxchess.Data;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AIController {

    boolean running = true;
    public final List<String> AIThoughts = new ArrayList<>();

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void makeMove(List<ChessGrid> gameBoard, ChessTeamColor sideToPlay){
        AIThoughts.add("Im gathering all possible moves for: " + sideToPlay.toString());
        List<Move> allResults = gatherAllPossibleResults(gameBoard, sideToPlay);
        AIThoughts.add("Ive found :" + allResults.size() + " results");

        for (Move result : allResults) {
            AIThoughts.add(result.toString());
        }

        makeBestMove(allResults);
    }



    private List<Move> gatherAllPossibleResults(List<ChessGrid> gameBoard, ChessTeamColor sideToPlay) {
        List<Move> tmpList = new ArrayList<>();
        int positionCounter = 0;
        for (ChessGrid grid: gameBoard) {
            if (grid.pieceOnGrid.teamColor == sideToPlay){
                tmpList.addAll(BoardManager.ruleBook.calculatePossibleMoves(gameBoard, new Move(positionCounter)));
            }
            positionCounter++;
        }
        return tmpList;
    }

    private void makeRandomMove(List<Move> moveList){
        Random rnd = new Random();
        Move rndMove = moveList.get(rnd.nextInt(moveList.size()));
        AIThoughts.add("Im picking at Random : " + rndMove.toString() + " out of a Total of:" + moveList.size());
        BoardManager.MovePiece(rndMove);
    }

    private void makeBestMove(List<Move> moveList){
        List<Move> moveToGoAheadWith = new ArrayList<>();
        int BestScore = 0;

        AIThoughts.add("Im thinking about the best move to make");

        for (Move bestMove : moveList) {
            if(EvaluateMovePoints(bestMove) > BestScore){
                moveToGoAheadWith.clear();
                moveToGoAheadWith.add(bestMove);
                AIThoughts.add("Ive found a GREAT move to make with a score("+EvaluateMovePoints(bestMove) +")"+ bestMove + " with a total of:" + moveToGoAheadWith.size());
                BestScore = EvaluateMovePoints(bestMove);
            }
            if(EvaluateMovePoints(bestMove) == BestScore){
                moveToGoAheadWith.add(bestMove);
                AIThoughts.add("Ive found a OK move to make with a score("+EvaluateMovePoints(bestMove) +")"+ bestMove + " with a total of:" + moveToGoAheadWith.size());
                BestScore = EvaluateMovePoints(bestMove);
            }

        }

        makeRandomMove(moveToGoAheadWith);
    }

    private int EvaluateMovePoints(Move moveToTest){
        int thisScore ;

        thisScore = BoardManager.getGameBoard().get(moveToTest.endPosition).pieceOnGrid.type.ordinal()+1;

        return thisScore;
    }

    public int EvaluateBoardPoints(ChessTeamColor colorToEvaluate, List<ChessGrid> gameBoard){

        int Score=0;
        for (ChessGrid grid: gameBoard) {

            if(grid.pieceOnGrid.teamColor == colorToEvaluate){
                Score+=grid.pieceOnGrid.type.ordinal();
            }
        }
        return Score;
    }

}
