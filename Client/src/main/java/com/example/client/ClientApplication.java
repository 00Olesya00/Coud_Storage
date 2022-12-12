package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml")); //  запускается без сервера
//        Scene scene = new Scene(fxmlLoader.load(), 460, 540);
        scene = new Scene(loadFXML("hello-view"),460, 540);// fxml запускается только с запуском сервера
        stage.setTitle("Cloud Storage");
        stage.setScene(scene);
        stage.show();
    }
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }


    public static void main(String[] args) {
        launch();
    }
}