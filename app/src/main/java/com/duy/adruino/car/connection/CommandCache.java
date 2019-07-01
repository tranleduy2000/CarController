package com.duy.adruino.car.connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.adruino.car.DLog;

import java.io.IOException;

public class CommandCache {
    private static final String TAG = "CommandCache";
    private final int interval;

    @Nullable
    private String previousCommand = null;
    private long previousTime;

    public CommandCache(int interval) {
        this.interval = interval;
    }

    public void send(@NonNull String command, @NonNull IConnector connector,
                     @Nullable IConnectionListener listener) {

        if (System.currentTimeMillis() - previousTime < interval) {
            return;
        }
        previousTime = System.currentTimeMillis();
        if (command.equals(previousCommand)) {
            return;
        }

        if (DLog.DEBUG) {
            DLog.d(TAG, "send() called with: command = [" + command + "], connector = [" + connector + "]");
        }

        previousCommand = command;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connector.write(command + ";");
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onReceivedNewMessage(new Message(Message.Type.OUT, e.getMessage()));
                    }
                }
            }
        }).start();
    }
}
