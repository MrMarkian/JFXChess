package com.jfxchess.jfxchess.Data;

public class Move {
    int startPosition;
    int endPosition;

    int startX, startY;
    int endX, endY;

    boolean willResultInCapture = false;
    ChessPieceTypes capturedPiece;

    public Move() {

    }

    public Move(String networkText){
        String[] processedText = networkText.split("!");
        startPosition = Integer.parseInt(processedText[0]);
        endPosition = Integer.parseInt(processedText[1]);
        calculateXY();
    }

    public Move(int StartPosition) {
        this.startPosition = StartPosition;

    }

    public Move(int StartPosition, int EndPosition) {
        this.startPosition = StartPosition;
        this.endPosition = EndPosition;
        calculateXY();
    }

    public String printNetworkTextValues(){
        return ""+startPosition+"!"+endPosition;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int position) {
        this.startPosition = position;
    }


    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {

        this.endPosition = endPosition;
        calculateXY();
    }


    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }


    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }


    public void calculateXY() {
        startX = startPosition % 8;
        startY = startPosition / 8;

        endX = endPosition % 8;
        endY = endPosition / 8;
    }

    public boolean isWillResultInCapture() {
        return willResultInCapture;
    }

    public void setWillResultInCapture(boolean willResultInCapture) {
        this.willResultInCapture = willResultInCapture;
    }

    public ChessPieceTypes getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(ChessPieceTypes capturedPiece) {
        this.capturedPiece = capturedPiece;
    }


    @Override
    public String toString() {
        String tmpString =
         "Move {" +
                ChessGrid.getLabelFromInt(startPosition).toString() + " - " + ChessGrid.getLabelFromInt(endPosition) + "} ";
              //  ", startX=" + startX +
              //  ", startY=" + startY +
              //  ", endX=" + endX +
              //  ", endY=" + endY +
                if(willResultInCapture) {
                    tmpString = tmpString +
                    " Capture=" + capturedPiece;
                }
                return tmpString;
    }
}