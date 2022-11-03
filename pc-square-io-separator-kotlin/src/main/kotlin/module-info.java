module pc.square.io.separator.kotlin {
    requires javafx.controls;
    requires javafx.web;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.apache.poi.ooxml;
    requires com.jfoenix;
    requires lombok;

    opens com.mj.pc.square.io.separator.kotlin.controller to javafx.fxml;
    opens com.mj.pc.square.io.separator.kotlin.controller.layout to javafx.fxml;
    opens com.mj.pc.square.io.separator.kotlin.dialog to javafx.fxml;
    opens com.mj.pc.square.io.separator.kotlin to javafx.graphics;
}