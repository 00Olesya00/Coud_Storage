package com.example.client;

@FunctionalInterface
public interface CallBack { //Интерфейс оброботки сообщений

    void onReceive(String message);

}
