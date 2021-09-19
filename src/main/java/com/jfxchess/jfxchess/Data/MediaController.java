package com.jfxchess.jfxchess.Data;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.io.File;

public class MediaController {
    final Image blackRook = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-rook.png");
    final Image whiteRook = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-rook.png");
    final Image blackBishop = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-bishop.png");
    final Image whiteBishop = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-bishop.png");
    final Image blackPawn = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-pawn.png");
    final Image whitePawn = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-pawn.png");
    final Image blackKnight = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-knight.png");
    final Image whiteKnight = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-knight.png");
    final Image blackKing = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-king.png");
    final Image whiteKing = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-king.png");
    final Image blackQueen = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-queen.png");
    final Image whiteQueen = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-queen.png");
    final AudioClip chessMoveSound = new AudioClip(new File("C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\Sounds\\UITight.wav").toURI().toString());
}

