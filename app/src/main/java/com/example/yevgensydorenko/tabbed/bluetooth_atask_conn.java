package com.example.yevgensydorenko.tabbed;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by yevgensydorenko on 09.06.2017.
 */

public class bluetooth_atask_conn extends AsyncTask<Boolean, Boolean, Boolean> {
    public BluetoothAdapter myBluetooth = null;
    public BluetoothSocket btSocket = null;
    public BluetoothDevice btDevice = null;
    private boolean isBtConnected = false;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static String address = "98:D3:31:FB:4F:0F";
    private boolean ConnectSuccess = true; //if it's here, it's almost connected
    public AsyncResponse_conn delegate = null;

    @Override
    protected void onPreExecute()
    {
        Log.d("onPreExecute", "--- onPreExecute ARDUINO ---");
    }

    @Override
    protected Boolean doInBackground(Boolean... devices) //while the progress dialog is shown, the connection is done in background
    {
        try
        {
            if (btSocket == null || !isBtConnected)
            {
                myBluetooth = BluetoothAdapter.getDefaultAdapter(); //BluetoothAdapter
                btDevice = myBluetooth.getRemoteDevice(address);
                btSocket = btDevice.createRfcommSocketToServiceRecord(MY_UUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connectione
                ConnectSuccess = true;
            }
        }
        catch (IOException e)
        {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }
        return null;
    }
    @Override
    protected void onPostExecute(Boolean result) //after the doInBackground, it checks if everything went fine
    {

        //MainActivity.conn=5;
        if (!ConnectSuccess)
        {
            Log.d("onPostExecute", "Connection Failed. Is it a SPP Bluetooth? Try again.");

            //finish();
        }
        else
        {
            Log.d("onPostExecute", "Conenected.");
            isBtConnected = true;
            delegate.processConn_result(isBtConnected);
        }
    }
}