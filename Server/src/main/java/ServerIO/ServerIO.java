package ServerIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerIO {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8189);
        System.out.println("Запуск сервера...");
        while (true){
            try {
                Socket socket =server.accept();
                System.out.println("Присоединение пользователя...");//подключаем Users
                new Thread(new Handler(socket)).start();
            }catch (Exception e){
                e.printStackTrace();// Исключаем падение сервера.
            }
        }
    }
}

