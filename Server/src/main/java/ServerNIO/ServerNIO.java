package ServerNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
public class ServerNIO {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buf;
    private Path currentDir;

    public ServerNIO(int port) throws IOException {
        currentDir = Paths.get("./");
        buf = ByteBuffer.allocate(20);
        //Открытие каналов
        serverChannel = ServerSocketChannel.open();
        selector = Selector.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (serverChannel.isOpen()) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            try {
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        handleAccept();
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    iterator.remove();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();//  достаем канал
        StringBuilder msg = new StringBuilder();
        while (true) {
            int read = channel.read(buf);//чтение канала
            if (read == -1) {
                channel.close();
                return;
            }
            if (read == 0) {
                break;
            }
            buf.flip();
            while (buf.hasRemaining()) {
                msg.append((char) buf.get());
            }
            buf.clear();
        }
        processMessage(channel, msg.toString().trim());
//         String response = "Hello " + msg + key.attachment();
//         channel.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));
    }

    private void processMessage(SocketChannel channel, String msg) throws IOException {
        String[] tokens = msg.split(" +");
        TerminalCommabd type = null;
        try {
            type = TerminalCommabd.byCommand(tokens[0]);
            switch (type) {
                case DIR:
                    sendString(channel, getFilesList());
                    break;
                case CAT:
                    processCATRCommand(channel, tokens);
                    break;
                case CD:
                    processCdCommand(channel, tokens);
            }
        } catch (RuntimeException e) {
            String response = "Команда " + tokens[0] + " не существует!\n\r";
            sendString(channel, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCATRCommand(SocketChannel channel, String[] tokens) throws IOException {
        if (tokens == null || tokens.length != 2) {
            sendString(channel, "\n" +
                    "Команда CAT  должна иметь 2 аргумента");
        } else {
            String fileName = tokens[1];
            Path file = currentDir.resolve(fileName);// полуяаем файл
            if (!Files.isDirectory(file)) {
                String content = new String(Files.readAllBytes(file)) + "\n\r";// достаем файл
                sendString(channel, content);
            } else {
                sendString(channel, "Вы не можете использовать команду cat для DIR\n\r");
            }
        }
    }


    private String getFilesList() throws IOException {
        return Files.list(currentDir)
                .map(p -> p.getFileName().toString() + " " + getFileSuffix(p))
                .collect(Collectors.joining("\n")) + "\n\r";
    }

    private String getFileSuffix(Path path) {
        if (Files.isDirectory(path)) {
            return "[DIR]";
        } else {
            return "[FILE] " + path.toFile().length() + " bytes";
        }
    }

    private void sendString(SocketChannel channel, String msg) throws IOException {
        channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
        channel.write(ByteBuffer.wrap("Veto ->".getBytes(StandardCharsets.UTF_8)));
    }


    private void processCdCommand(SocketChannel channel, String[] tokens) throws IOException {
        if (tokens == null || tokens.length != 2) {
            sendString(channel, "Команда CD  должна иметь 2 аргумента");
        } else {
            String dir = tokens[1];
            if (Files.isDirectory(currentDir.resolve(dir))) {
                currentDir = currentDir.resolve(dir);
                channel.write(ByteBuffer.wrap("Veto -> ".getBytes(StandardCharsets.UTF_8)));
            } else {
                sendString(channel, "Вы не можете использовать команду cd для ФАЙЛА\n\r");
            }
        }
    }



    private void handleAccept() throws IOException {
        System.out.println("Подключение клиента...");
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, "Hello world!");
    }

    public static void main(String[] args) throws IOException {
        new ServerNIO(8189);
    }

}