package com.duy.adruino.car.connection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface IConnectionListener<T> {

    void onConnected(@NonNull T socket);

    void onConnectFailed(@Nullable Exception error);

    void onDisconnected();

    void onReceivedNewMessage(@NonNull Message message);

}
