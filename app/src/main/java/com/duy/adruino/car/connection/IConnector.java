package com.duy.adruino.car.connection;

import android.support.annotation.WorkerThread;

import java.io.IOException;

public interface IConnector {
    @WorkerThread
    void write(String message) throws IOException;

    void connect() throws IOException;

    void disconnect() throws Exception;

    boolean isConnected();
}
