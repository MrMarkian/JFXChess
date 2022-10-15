package com.jfxchess.jfxchess.Data;

import javafx.scene.paint.ImagePattern;

public class ChessPiece {

    ChessPieceTypes type;
    ChessTeamColor teamColor;
    boolean firstMove = false;
    ImagePattern graphic;

    public ChessPiece(ChessPieceTypes type, ChessTeamColor teamColor) {
        this.type = type;
        this.teamColor = teamColor;
    }

    public ChessPiece(ChessPieceTypes type){
        this.type = type;
    }

    public ChessPiece(ChessPieceTypes type, ChessTeamColor teamColor, ImagePattern graphic) {
        this.type = type;
        this.teamColor = teamColor;
        this.graphic = graphic;
    }

    public ChessPiece(){
        this.type = ChessPieceTypes.NONE;
    }

    public ImagePattern getGraphic() {
        return graphic;
    }

    public void setGraphic(ImagePattern graphic) {
        this.graphic = graphic;
    }

    public ChessPieceTypes getType() {
        return type;
    }

    public void setType(ChessPieceTypes type) {
        this.type = type;
    }

    public ChessTeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(ChessTeamColor teamColor) {
        this.teamColor = teamColor;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
}
