package com.example.client;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientController implements Initializable {
    private static final int SOCKET_PORT = 8189;// порт
    public ListView<String> listView;
    public ListView<String> listView1;
    public Path clientDir;
    public final static String FILE_TO_SEND = "c:/temp/source.pdf"; //Расположение файла
    public TextField input;
    private IONet net;


    public void sendMsg(ActionEvent actionEvent) throws IOException {
        net.senMsg(input.getText());
        input.clear();
    }
    private void addMessage(String msg) {Platform.runLater(() -> listView.getItems().add(msg));// обрабатываем сообщение, добовляем в ListView
 }

  private void fillFileView() throws IOException {
        List<String> files = Files.list(clientDir)
                .map(p -> p.getFileName().toString())
      .collect(Collectors.toList());
      Platform.runLater(() -> listView1.getItems().addAll(files));


    }

//    public void _sendingp_resurs (ActionEvent actionEvent){ //дописать кнопку
//        Button source = (Button) actionEvent.getSource();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //поднимим сеть
        try {
            clientDir = Paths.get("Client_Files");
               fillFileView();

            Socket socket = new Socket("localhost",SOCKET_PORT); //поднимаем сокет

            net = new IONet(this::addMessage, socket); // получение сообщения (call back)

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
