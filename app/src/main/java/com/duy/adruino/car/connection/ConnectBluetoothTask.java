package com.duy.adruino.car.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

public class ConnectBluetoothTask extends AsyncTask<Void, Void, BluetoothSocket> {
    @NonNull
    private BluetoothDevice bluetoothDevice;
    @Nullable
    private IConnectionListener<BluetoothSocket> callback;
    @Nullable
    private Exception exception;

    public ConnectBluetoothTask(@NonNull BluetoothDevice bluetoothDevice,
                                @Nullable IConnectionListener<BluetoothSocket> resultCallback) {
        this.bluetoothDevice = bluetoothDevice;
        this.callback = resultCallback;
    }

    @Override
    protected BluetoothSocket doInBackground(Void... voids) {
        try {
            // SPP UUID service
            final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            BluetoothSocket socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
            socket.connect();
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(BluetoothSocket bluetoothSocket) {
        super.onPostExecute(bluetoothSocket);
        if (isCancelled()) {
            return;
        }
        if (callback != null) {
            if (bluetoothSocket != null) {
                callback.onConnected(bluetoothSocket);
            } else {
                callback.onConnectFailed(exception);
            }
        }
    }
}
