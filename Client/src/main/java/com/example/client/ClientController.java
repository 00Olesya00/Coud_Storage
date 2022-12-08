package com.example.client;

import java.io.FileInputStream;
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
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private static final int SOCKET_PORT = 8189;// порт
    public ListView<String> listView;
    public ListView<String> listView1;
    public Path clientDir;
    public final static String FILE_TO_LOC = "Client_Files"; //Расположение файлов
    public TextField input;
    private IONet net;
    private byte[] buf;


    public void sendMsg(ActionEvent actionEvent) throws IOException {
        File_Send(input.getText());
//        net.senMsg(input.getText());
//        input.clear();
    }
    private void File_Send(String fileName) throws IOException {
        Path file = clientDir.resolve(fileName);
        net.writeUTF("#file#");
        net.writeUTF(fileName.replaceAll(" +", "_"));
        net.writeLong(Files.size(file));
        try  (FileInputStream fis = new FileInputStream(file.toFile())){
            int read =0;
            while ((read = fis.read(buf)) !=-1){
                net.writeBYTES(buf,0,read);
            }
        }
    }
//    private void addMessage(String msg) {Platform.runLater(() -> listView.getItems().add(msg));// обрабатываем сообщение, добовляем в ListView
 //}
private void addMessage(String msg)
{Platform.runLater(() -> input.setText(msg));} // отправляем на сервер


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

            buf = new byte[9000];
               clientDir  = Paths.get(FILE_TO_LOC);
               System.out.println("1 - "+ clientDir.toAbsolutePath()); //проверяем где ищется нужный файл
                  fillFileView();
                  initClickListener();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
