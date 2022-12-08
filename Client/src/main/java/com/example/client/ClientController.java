package com.example.client;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
  import java.nio.file.*;
import java.util.List;
import java.util.ResourceBundle;
public class ClientController implements Initializable {
    private static final int SOCKET_PORT = 8189;// порт
    public ListView<String> listView;
    public ListView<String> listView1;
    public Path clientDir;
    public final static String FILE_TO_SEND = "com/example/client/Client_Files"; //Расположение файлов
    public TextField input;
    private IONet net;


    public void sendMsg(ActionEvent actionEvent) throws IOException {
        net.senMsg(input.getText());
        input.clear();
    }
    private void addMessage(String msg) {Platform.runLater(() -> listView.getItems().add(msg));// обрабатываем сообщение, добовляем в ListView
 }

 private void initClickListener(){
        listView1.setOnMouseClicked(event ->{
            if (event.getClickCount()==2) {
        String item = listView1.getSelectionModel().getSelectedItem();
        input.setText(item);
 }
 });
}
  private void fillFileView() throws IOException {
        List<String> files = Files.list(clientDir)
                .map(p -> p.getFileName().toString()).toList();
      Platform.runLater(() -> listView1.getItems().addAll(files));



    }

//    public void _sendingp_resurs (ActionEvent actionEvent){ //дописать кнопку
//        Button source = (Button) actionEvent.getSource();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { //поднимим сеть
        try {
              Socket socket = new Socket("localhost",SOCKET_PORT); //поднимаем сокет

            net = new IONet(this::addMessage, socket); // получение сообщения (call back)

//              
               clientDir  = Paths.get(FILE_TO_SEND);
               System.out.println("1 - "+ clientDir.toAbsolutePath()); //проверяем где ищется нужный файл
                  fillFileView();
                  initClickListener();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
