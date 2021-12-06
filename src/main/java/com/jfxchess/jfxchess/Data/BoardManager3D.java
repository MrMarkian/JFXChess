package com.jfxchess.jfxchess.Data;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.VertexFormat;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class BoardManager3D extends Stage {

    public BoardManager3D() throws Exception {
        start(this);
    }

    public void start(Stage stage) {
        int X=100,Y=100,Z = 100;

        final PhongMaterial blackSquares  = new PhongMaterial();
        blackSquares.setDiffuseColor(Color.BLACK);
        blackSquares.setSpecularColor(Color.GREY);

        final PhongMaterial whiteSquares  = new PhongMaterial();
        whiteSquares.setDiffuseColor(Color.WHITE);
        whiteSquares.setSpecularColor(Color.GREY);

        boolean is3DSupported = Platform.isSupported(ConditionalFeature.SCENE3D);
        if(!is3DSupported) {
            System.out.println("Sorry, 3D is not supported in JavaFX on this platform.");
            return;
        }

        List<Box> boardGrid = new ArrayList<>();

        int GridSize = 100;
        int xPos = 0;
        int yPos = 0;
        int currentSquare =0;
        int offset = 800;

        for (ChessGrid grid: BoardManager.gameBoard ) {
            Box box = new Box(GridSize,GridSize,GridSize);
            box.setCullFace(CullFace.BACK);

            box.setTranslateX(xPos + offset);
            box.setTranslateY(yPos );
            box.setTranslateZ(0);

            if(grid.gridColor == GridColor.BLACK)
                box.setMaterial(blackSquares);
            else
                box.setMaterial(whiteSquares);

            currentSquare++;

            xPos += GridSize;
            if(currentSquare % 8 == 0){
                yPos += 100;
                xPos = 0;
            }
            boardGrid.add(box);
        }

        boolean fixedEyeAtCameraZero = false;
        PerspectiveCamera camera = new PerspectiveCamera(fixedEyeAtCameraZero);
        camera.setTranslateX(0);
        camera.setTranslateY(-0);
        camera.setTranslateZ(0);


        Group root = new Group();
        root.getChildren().addAll(boardGrid);
        root.setRotationAxis(Rotate.X_AXIS);
        root.setRotate(30);
        Scene scene = new Scene(root, 500, 300, true);
        scene.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.W){
                camera.setTranslateY(camera.getTranslateY()-20);
            }
            if(keyEvent.getCode() == KeyCode.S){
                camera.setTranslateY(camera.getTranslateY()+20);
            }
            if(keyEvent.getCode() == KeyCode.A){
                camera.setTranslateX(camera.getTranslateX()-20);
            }
            if(keyEvent.getCode() == KeyCode.D){
                camera.setTranslateX(camera.getTranslateX()+20);
            }
            if(keyEvent.getCode() == KeyCode.Z){
                camera.setTranslateZ(camera.getTranslateZ()+20);
            }
            if(keyEvent.getCode() == KeyCode.X){
                camera.setTranslateZ(camera.getTranslateZ()-20);
            }

         });

        scene.setOnMousePressed(event -> {
        });

        scene.setOnScroll(scrollEvent -> camera.setTranslateZ(scrollEvent.getDeltaX()));

        scene.setCamera(camera);
        stage.setScene(scene);
        stage.setTitle("3D Board View");
        stage.show();
    }
}
