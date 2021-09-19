package com.jfxchess.jfxchess.Data;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;



public class BoardRenderFXGL extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("FXGL Render View");
        System.out.println("Starting FXGL");
    }



    public static void main(String[] args){
        launch(args);
    }
}
