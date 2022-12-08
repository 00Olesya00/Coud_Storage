import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Handler implements Runnable{
    private boolean running;
    private DataInputStream is;
    private DataOutputStream os;
    private byte[] buf;
    private Socket socket;
    private static final int SIZE = 9000;
    private Path ServerDir;

    public Handler(Socket socket) throws IOException {
        running = true;
        buf = new byte[SIZE];
        this.socket =socket;
        ServerDir = Paths.get("Server_Files");
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
    }
    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        try { // т.к, нет вариантов прокинуть на верх try -catch, обязательно вне цикла
            while (running) {
                //обрабатываем сообщение, приняли/отправили
//                int read = is.read(buf);
//                String messege = new String(buf,0,read)
//                        .trim();
                String command = is.readUTF();
                if (command.equals("выйти"))
                {os.writeUTF("Соединение с сервером прервано...\n");
                    close();
                    break;
                }else if (command.equals("#file#")){
                    String fileName = is.readUTF();
                    long size = is.readLong();
                    try (FileOutputStream fos = new FileOutputStream(ServerDir.resolve(fileName).toFile())){

                    for (int i = 0; i < (size + SIZE-1)/SIZE; i++) {
                        int read = is.read(buf);
                        fos.write(buf,0,read);
                    }
                    }
                }
                os.writeUTF("Файл получен");
//                System.out.println("Получено: " + messege );
//                os.write((messege+"\n").getBytes(StandardCharsets.UTF_8));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void close() throws IOException {// Закрытие соединения/разрыв связи
        os.close();
        is.close();
        socket.close();
    }
}