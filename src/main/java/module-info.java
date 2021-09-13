module com.jfxchess.jfxchess {
    requires javafx.media;
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jfxchess.jfxchess to javafx.fxml;
    exports com.jfxchess.jfxchess;
}