module com.jfxchess.jfxchess {
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;


    opens com.jfxchess.jfxchess to javafx.fxml;
    opens com.jfxchess.jfxchess.Data to com.almasb.fxgl.core;
    exports com.jfxchess.jfxchess;
}