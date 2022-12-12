module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires io.netty.buffer;
    requires io.netty.transport;

    opens com.example.client to javafx.fxml;
    exports com.example.client;
    exports com.example.client.ClientNetti.Nett;
    opens com.example.client.ClientNetti.Nett to javafx.fxml;
    exports com.example.client.ClientNetti;
    opens com.example.client.ClientNetti to javafx.fxml;
    exports com.example.client.ClientNetti.calibacks;
    opens com.example.client.ClientNetti.calibacks to javafx.fxml;
}