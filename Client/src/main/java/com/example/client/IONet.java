package com.example.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IONet implements Closeable {
    private final CallBack callBack;
    private final Socket socket;
    private final InputStream is;
    private final OutputStream os;
    private final byte[] buf;

    public IONet(CallBack callBack,Socket socket) throws IOException {
        this.callBack = callBack;
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        buf = new byte[9000];
        Thread readThread = new Thread(this::readMessages);//Чтение происходит в отдельном потоке
        readThread.setDaemon(true);
        readThread.start();
    }

    public IONet(CallBack addMessage, Socket socket, CallBack callBack, Socket socket1, InputStream is, OutputStream os, byte[] buf) {
        this.callBack = callBack;
        this.socket = socket1;
        this.is = is;
        this.os = os;
        this.buf = buf;
    }

    public void senMsg( String msg) throws IOException {
        os.write(msg.getBytes(StandardCharsets.UTF_8));
        os.flush();

    }
    private void readMessages()  {
        try {
            while (true) {
                int read = is.read(buf);//смотрим сколько прочитали
                String msg = new String(buf, 0, read).trim();// собираем строку
                callBack.onReceive(msg);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public void close() throws IOException {
        os.close();
        is.close();
        socket.close();


    }
}
