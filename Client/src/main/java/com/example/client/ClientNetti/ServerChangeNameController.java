package com.example.client.ClientNetti;

import com.example.client.ClientController;
import com.example.client.ClientNetti.Nett.NettyClient;
import com.example.client.ClientNetti.Utils.Sender;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerChangeNameController {
    public TextField newNameTextField;
    private ClientController clientController;
    private Stage stage;
    private String currentFileName;

    public void renameButton(ActionEvent actionEvent) {
        if (!newNameTextField.getText().equals("")) {
            Sender.sendRenameRequest(NettyClient.getInstance().getCurrentChannel(), currentFileName, newNameTextField.getText());
            this.stage.close();
        }
    }

    public void setMainController(ClientController mainController) {
        this.clientController = mainController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentName(String currentFileName) {
        this.currentFileName = currentFileName;
    }
}

