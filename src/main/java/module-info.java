module com.cliftonbartholomew.utilities {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.apache.poi.ooxml;
    requires java.datatransfer;
    requires MaterialFX;

    opens com.cliftonbartholomew.utilities to javafx.fxml;
    exports com.cliftonbartholomew.utilities;
}