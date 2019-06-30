package com.duy.adruino.car.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class BluetoothCommunicator extends Thread implements IConnector, IConnectionListener<BluetoothSocket> {

    private static final String TAG = "BluetoothCommunicator";

    @Nullable
    private BluetoothSocket socket;
    private BluetoothDevice bluetoothDevice;
    @Nullable
    private IConnectionListener<BluetoothSocket> listener;

    @Nullable
    private BufferedReader reader;
    @Nullable
    private BufferedWriter writer;


    private boolean connecting = false;
    @Nullable
    private ConnectBluetoothTask connectingTask;

    public BluetoothCommunicator(@NonNull BluetoothDevice bluetoothDevice,
                                 @Nullable IConnectionListener<BluetoothSocket> listener) {
        this.bluetoothDevice = bluetoothDevice;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        try {
            while (!isInterrupted()) {
                String line = reader.readLine();
                if (listener != null) {
                    listener.onReceivedNewMessage(new Message(Message.Type.IN, line));
                }
                Thread.sleep(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @WorkerThread
    @Override
    public void write(String message) throws IOException {
        Log.d(TAG, "write() called with: message = [" + message + "]");
        if (connecting) {
            throw new IOException("Bluetooth is connecting");
        }
        if (!isConnected()) {
            throw new IOException("Bluetooth socket is not connected");
        }
        if (writer != null) {
            writer.write(message);
            writer.flush();
        }
    }

    @Override
    public void connect() {
        if (connecting) {
            Log.d(TAG, "connect: Bluetooth is connecting");
            return;
        }
        if (isConnected()) {
            Log.d(TAG, "connect: Connection has already established");
            return;
        }
        connecting = true;

        connectingTask = new ConnectBluetoothTask(bluetoothDevice, this);
        connectingTask.execute();
    }

    @Override
    public void disconnect() throws Exception {
        if (connectingTask != null) {
            connectingTask.cancel(true);
        }
        if (connectingTask != null || !isConnected()) {
            Log.d(TAG, "disconnect: Bluetooth is not connected.");
            return;
        }

        interrupt();

        if (writer != null) {
            writer.flush();
            writer.close();
        }
        if (reader != null) {
            reader.close();
        }
        if (socket != null) {
            socket.close();
        }

        if (listener != null) {
            listener.onDisconnected();
        }
    }

    @Override
    public boolean isConnected() {
        if (connecting) {
            return false;
        }
        try {
            if (socket != null) {
                return socket.isConnected();
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public void onConnected(@NonNull BluetoothSocket socket) {
        connecting = false;
        this.socket = socket;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            onConnectFailed(e);

            e.printStackTrace();
            try {
                disconnect();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return;
        }

        if (listener != null) {
            listener.onConnected(socket);
        }
    }

    @Override
    public void onConnectFailed(@Nullable Exception error) {
        if (listener != null) {
            listener.onConnectFailed(error);
        }
    }

    @Override
    public void onDisconnected() {
        if (listener != null) {
            listener.onDisconnected();
        }
    }

    @WorkerThread
    @Override
    public void onReceivedNewMessage(@NonNull Message message) {
        if (listener != null) {
            listener.onReceivedNewMessage(message);
        }
    }
}
