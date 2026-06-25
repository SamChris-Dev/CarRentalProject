module app.carrental {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.base;

    requires spark.core;

    requires com.google.gson;
    requires com.oracle.database.jdbc;

    opens dto to com.google.gson, javafx.base;
    opens server to spark.core, com.google.gson;



    opens app.carrental to com.google.gson, javafx.fxml;
    
    exports dto;
    exports app.carrental;
}