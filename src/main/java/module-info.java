module app.carrental {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.base;

    opens app.carrental to org.junit.platform.commons, javafx.fxml;

    exports app.carrental;
}