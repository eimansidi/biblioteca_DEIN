module com.eiman.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires net.sf.jasperreports.core;
    requires java.sql;
    requires org.slf4j;


    opens com.eiman.biblioteca to javafx.fxml;
    exports com.eiman.biblioteca;
    exports com.eiman.biblioteca.controllers;
    exports com.eiman.biblioteca.models;
    opens com.eiman.biblioteca.controllers to javafx.fxml;
    opens com.eiman.biblioteca.models to javafx.base;
}