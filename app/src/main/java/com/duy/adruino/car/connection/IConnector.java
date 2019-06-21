package com.duy.adruino.car.connection;

import java.io.IOException;

public interface IConnector {

    void write(String message) throws IOException;

    void connect() throws IOException;

    void disconnect() throws Exception;

    boolean isConnected();
}
