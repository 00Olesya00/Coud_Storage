package com.example.client;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class IONet implements Closeable {
    private final CallBack callBack;
    private final Socket socket;
    private final DataInputStream is;
    private final DataOutputStream os;
    private final byte[] buf;

    public IONet(CallBack callBack,Socket socket) throws IOException {
        this.callBack = callBack;
        this.socket = socket;
        is = new DataInputStream(socket.getInputStream());
        os = new DataOutputStream(socket.getOutputStream());
        buf = new byte[9000];
        Thread readThread = new Thread(this::readMessages);//Чтение происходит в отдельном потоке
        readThread.setDaemon(true);
        readThread.start();
    }
    public void writeLong(Long size) throws IOException {
        os.writeLong(size);
        os.flush();
    }
    public void writeUTF(String msg) throws IOException {
        os.writeUTF(msg);
        os.flush();
    }
    public void writeBYTES(byte[] bytes, int off, int cnt) throws IOException {
        os.write(bytes,off,cnt);
        os.flush();
    }


    public IONet(CallBack addMessage, Socket socket, CallBack callBack, Socket socket1, DataInputStream is, DataOutputStream os, byte[] buf) {
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
