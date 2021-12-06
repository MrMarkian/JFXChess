package com.jfxchess.jfxchess.Data;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

public class MediaController {

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


    final AudioClip chessMoveSound = new AudioClip(Objects.requireNonNull(getClass().getResource("/Sounds/UITight.wav")).toURI().toString());

    public MediaController() throws URISyntaxException {
    }
}

