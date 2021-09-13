package com.jfxchess.jfxchess.Data;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class MediaController {
    Image blackRook = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-rook.png");
    Image whiteRook = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-rook.png");
    Image blackBishop = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-bishop.png");
    Image whiteBishop = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-bishop.png");
    Image blackPawn = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-pawn.png");
    Image whitePawn = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-pawn.png");
    Image blackKnight = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-knight.png");
    Image whiteKnight = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-knight.png");
    Image blackKing = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-king.png");
    Image whiteKing = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-king.png");
    Image blackQueen = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\black-queen.png");
    Image whiteQueen = new Image("FILE:C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\ChessImages\\white-queen.png");
    AudioClip chessMoveSound = new AudioClip(new File("C:\\Users\\Manic\\IdeaProjects\\JFXChess\\src\\main\\resources\\Sounds\\UITight.wav").toURI().toString());


}

