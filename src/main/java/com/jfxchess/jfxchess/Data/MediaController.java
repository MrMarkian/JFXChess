package com.jfxchess.jfxchess.Data;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaController {

    public Integer currentSetIndex = 0 ;

    private final List<Image> blackRooklist = new ArrayList<>();
    private final List<Image> whiteRookList = new ArrayList<>();
    private final List<Image> blackBishopList = new ArrayList<>();
    private final List<Image> whiteBishopList = new ArrayList<>();
    private final List<Image> blackPawnList = new ArrayList<>();
    private final List<Image> whitePawnList = new ArrayList<>();
    private final List<Image> blackKnightList = new ArrayList<>();
    private final List<Image> whiteKnightList = new ArrayList<>();
    private final List<Image> blackKingList = new ArrayList<>();
    private final List<Image> whiteKingList = new ArrayList<>();
    private final List<Image> blackQueenList = new ArrayList<>();
    private final List<Image> whiteQueenList = new ArrayList<>();

    public Image getBlackRook(){
        return blackRooklist.get(currentSetIndex);
    }
    public Image getWhiteRook(){
        return whiteRookList.get(currentSetIndex);
    }
    public Image getBlackBishop() {
        return blackBishopList.get(currentSetIndex);
    }

    public Image getWhiteBishop(){
        return whiteBishopList.get(currentSetIndex);
    }
    public Image getBlackPawn(){
        return blackPawnList.get(currentSetIndex);
    }
    public Image getWhitePawn(){
        return whitePawnList.get(currentSetIndex);
    }

    public Image getBlackKnight(){
        return blackKnightList.get(currentSetIndex);
    }
    public Image getWhiteKnight(){
        return whiteKnightList.get(currentSetIndex);
    }
    public Image getBlackKing(){
        return blackKingList.get(currentSetIndex);
    }
    public Image getWhiteKing(){
        return  whiteKingList.get(currentSetIndex);
    }

    public Image getBlackQueen(){
        return blackQueenList.get(currentSetIndex);
    }
    public Image getWhiteQueen(){
        return whiteQueenList.get(currentSetIndex);
    }

    /*
    final Image blackRook = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/black-rook.png")).toExternalForm());
    final Image whiteRook = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/white-rook.png")).toExternalForm());
    final Image blackBishop = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/black-bishop.png")).toExternalForm());
    final Image whiteBishop = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/white-bishop.png")).toExternalForm());
    final Image blackPawn = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/black-pawn.png")).toExternalForm());
    final Image whitePawn = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/white-pawn.png")).toExternalForm());
    final Image blackKnight = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/black-knight.png")).toExternalForm());
    final Image whiteKnight = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/white-knight.png")).toExternalForm());
    final Image blackKing = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/black-king.png")).toExternalForm());
    final Image whiteKing = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/white-king.png")).toExternalForm());
    final Image blackQueen = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/black-queen.png")).toExternalForm());
    final Image whiteQueen = new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/white-queen.png")).toExternalForm());
*/

    final AudioClip chessMoveSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/Sounds/UITight.wav")).toURI().toString());

    public MediaController() throws URISyntaxException {

        blackRooklist.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/black-rook.png")).toExternalForm()));
        whiteRookList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/white-rook.png")).toExternalForm()));
        blackBishopList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/black-bishop.png")).toExternalForm()));
        whiteBishopList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/white-bishop.png")).toExternalForm()));
        blackPawnList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/black-pawn.png")).toExternalForm()));
        whitePawnList.add( new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/white-pawn.png")).toExternalForm()));
        blackKnightList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/black-knight.png")).toExternalForm()));
        whiteKnightList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/white-knight.png")).toExternalForm()));
        blackKingList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/black-pawn.png")).toExternalForm()));
        whiteKingList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/white-king.png")).toExternalForm()));
        blackQueenList.add( new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/black-queen.png")).toExternalForm()));
        whiteQueenList.add(new Image(Objects.requireNonNull(getClass().getResource("/ChessImages/SET01/white-queen.png")).toExternalForm()));
    }
}

