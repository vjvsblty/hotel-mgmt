module com.app.hotel.hotelmgmtfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;


    opens com.app.hotel.hotelmgmtfx to javafx.fxml;
    exports com.app.hotel.hotelmgmtfx;
    exports com.app.hotel.hotelmgmtfx.model;
    exports com.app.hotel.hotelmgmtfx.utils;
    opens com.app.hotel.hotelmgmtfx.utils to javafx.fxml;
    exports com.app.hotel.hotelmgmtfx.screens;
    opens com.app.hotel.hotelmgmtfx.screens to javafx.fxml;
}