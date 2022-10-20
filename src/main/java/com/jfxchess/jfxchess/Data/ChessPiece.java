package com.jfxchess.jfxchess.Data;

import com.jfxchess.jfxchess.MainUIController;
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

    public ChessPiece(Character createFromFENChar){
        if (!importChessPieceFromFENChar(createFromFENChar)){
            throw new RuntimeException();
        }

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

    public Character exportChessPieceToFENChar(){
            switch (type){
                case KING -> {
                    if (teamColor == ChessTeamColor.WHITE) return 'K'; else return 'k';
                }

                case QUEEN -> {
                    if (teamColor == ChessTeamColor.WHITE) return 'Q'; else return 'q';
                }

                case BISHOP -> {
                    if (teamColor == ChessTeamColor.WHITE) return 'B'; else return 'b';
                }

                case KNIGHT -> {
                    if (teamColor == ChessTeamColor.WHITE) return 'N'; else return 'n';
                }

                case ROOK -> {
                    if (teamColor == ChessTeamColor.WHITE) return 'R'; else return 'r';
                }

                case PAWN -> {
                    if (teamColor == ChessTeamColor.WHITE) return 'P'; else return 'p';
                }

                case NONE -> {
                    return null;
                }
            }
        return null;

    }

    public boolean importChessPieceFromFENChar(Character importChar){
        switch (importChar){
            case 'K'->{
                this.type = ChessPieceTypes.KING;
                this.teamColor = ChessTeamColor.WHITE;
                return true;
            }
            case 'k'->{
                this.type = ChessPieceTypes.KING;
                this.teamColor = ChessTeamColor.BLACK;
                return true;
            }
            case 'Q' ->{
                this.type = ChessPieceTypes.QUEEN;
                this.teamColor = ChessTeamColor.WHITE;
                return true;
            }
            case 'q' ->{
                this.type = ChessPieceTypes.QUEEN;
                this.teamColor = ChessTeamColor.BLACK;
                return true;
            }
            case 'B' ->{
                this.type = ChessPieceTypes.BISHOP;
                this.teamColor = ChessTeamColor.WHITE;
                return true;
            }
            case 'b' ->{
                this.type = ChessPieceTypes.BISHOP;
                this.teamColor = ChessTeamColor.BLACK;
                return true;
            }
            case 'N' ->{
                this.type = ChessPieceTypes.KNIGHT;
                this.teamColor = ChessTeamColor.WHITE;
                return true;
            }
            case 'n' ->{
                this.type = ChessPieceTypes.KNIGHT;
                this.teamColor = ChessTeamColor.BLACK;
                return true;
            }
            case 'R' ->{
                this.type = ChessPieceTypes.ROOK;
                this.teamColor = ChessTeamColor.WHITE;
                return true;
            }
            case 'r' ->{
                this.type = ChessPieceTypes.ROOK;
                this.teamColor = ChessTeamColor.BLACK;
                return true;
            }
            case 'P' ->{
                this.type = ChessPieceTypes.PAWN;
                this.teamColor = ChessTeamColor.WHITE;
                return true;
            }
            case 'p' ->{
                this.type = ChessPieceTypes.PAWN;
                this.teamColor = ChessTeamColor.BLACK;
                return true;
            }
            default ->{
                return false;
            }
        }
    }
}
