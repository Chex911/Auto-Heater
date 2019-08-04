package com.example.yevgensydorenko.tabbed;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by yevgensydorenko on 08.07.2017.
 */

public class Conn_info {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:31:FB:4F:0F";
    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private BluetoothDevice device = null;

    private Activity activityContext = null; // v1

    public Conn_info(){
        this.btAdapter = BluetoothAdapter.getDefaultAdapter(); //BluetoothAdapter;

        try {
            this.device = btAdapter.getRemoteDevice(address);
        }catch (IllegalArgumentException e){
            //errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            //errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        btAdapter.cancelDiscovery();

        try {
            btSocket.connect();
            //Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
        } catch (IOException | NullPointerException e) {
            try {
                btSocket.close();
            } catch (IOException | NullPointerException e2) {
                //errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException | NullPointerException e) {
            //errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }

    }

    public BluetoothAdapter getAdapter(){
        return this.btAdapter;
    }

    public BluetoothSocket getSocket(){
        return this.btSocket;
    }

    public OutputStream getOutStream(){
        return this.outStream;
    }

    public BluetoothDevice getDevice() {
        return this.device;
    }

}
