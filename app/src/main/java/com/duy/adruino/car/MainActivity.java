package com.duy.adruino.car;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.adruino.car.connection.BluetoothCommunicator;
import com.duy.adruino.car.connection.IConnectionListener;
import com.duy.adruino.car.connection.Message;
import com.duy.adruino.car.controller.Direction;

import java.io.IOException;
import java.util.Arrays;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity implements IConnectionListener<BluetoothSocket> {

    private static final int RC_ENABLE_BLUETOOTH = 567;
    private static final String TAG = "MainActivity";
    @Nullable
    private BluetoothCommunicator bluetoothCommunicator;

    private JoystickView joystickView;
    private TextView txtSpeed;
    private TextView txtDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupView();
    }

    private void setupView() {

        setSupportActionBar(findViewById(R.id.toolbar));

        txtSpeed = findViewById(R.id.txt_speed);
        txtDirection = findViewById(R.id.txt_direction);

        joystickView = findViewById(R.id.joystick_view);
        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                updateControllerUI(angle, strength);
            }
        }, 17);
    }

    @SuppressLint("SetTextI18n")
    private void updateControllerUI(int angle, int strength) {
        Direction direction = Direction.getDirection(angle);
        Log.d(TAG, "direction = " + direction);
        Log.d(TAG, "strength = " + strength);

        txtSpeed.setText("Speed: " + strength);
        txtDirection.setText("Direction: " + direction);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_connect) {
            connectBluetooth();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.message_bluetooth_not_avaiable), Toast.LENGTH_LONG).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), RC_ENABLE_BLUETOOTH);
            return;
        }

        final int size = bluetoothAdapter.getBondedDevices().size();
        final BluetoothDevice[] bondedDevices = bluetoothAdapter.getBondedDevices().toArray(new BluetoothDevice[size]);
        final String[] deviceNames = Arrays.stream(bondedDevices)
                .map(device -> device.getName() + " - " + device.getAddress()).toArray(String[]::new);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_device);
        builder.setSingleChoiceItems(deviceNames, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BluetoothDevice toBeConnect = bondedDevices[which];
                Log.d(TAG, "toBeConnect = " + toBeConnect);

                connectBluetoothWith(toBeConnect);

                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void connectBluetoothWith(BluetoothDevice device) {
        //update UI
        onReceivedNewMessage(new Message(Message.Type.OUT, "Connecting with device " + device));

        try {
            bluetoothCommunicator = new BluetoothCommunicator(device, this);
            bluetoothCommunicator.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tryToConnectWithPreviousDevice() {
//        if (DLog.DEBUG) {
//            DLog.d(TAG, "tryToConnectWithPreviousDevice() called");
//        }
//
//
//        String address = AppSettings.getLastConnectedDevice(this);
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
//        BluetoothDevice connectedDevice
//                = bondedDevices.stream().filter(device -> device.getAddress().equals(address)).findFirst().orElse(null);
//        if (connectedDevice != null) {
//            this.connectBluetoothWith(connectedDevice);
//
//            onNewMessage(new MessageItem(MessageItem.TYPE_OUT, "tryToConnectWithPreviousDevice "
//                    + connectedDevice.getName() + " - " + connectedDevice.getAddress()));
//        }

    }

    @Override
    public void onConnected(@NonNull BluetoothSocket socket) {
        onReceivedNewMessage(new Message(Message.Type.OUT, "LOCAL: connected with socket " + socket));
    }

    @Override
    public void onConnectFailed(@Nullable Exception error) {
        Message message = new Message(Message.Type.ERROR, error == null ? "Unknown error" : error.getMessage());
        onReceivedNewMessage(message);
    }

    @Override
    public void onDisconnected() {
        onReceivedNewMessage(new Message(Message.Type.OUT, "LOCAL: Disconnected"));

    }

    @Override
    public void onReceivedNewMessage(@NonNull Message message) {


    }
}
