package com.example.marcinwlodarczyk.tabbed;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class bluetoothManager{

    public Handler h;
    public TextView txtArduino;
    //public ImageView sts;

    final int RECIEVE_MESSAGE = 1;

    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    BluetoothDevice device;


    private Activity activityContext = null; // v1
    private View fragment_01;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC - adress
    private static String address = "98:D3:31:FB:4F:0F";

    public bluetoothManager(final Activity activityContext) throws IOException {
        this.activityContext = activityContext;
        //txtArduino = (TextView) activityContext.findViewById(R.id.txtArduino);
//         sts=(ImageView) activityContext.findViewById(R.id.conn_status);
//         sts.setColorFilter(Color.parseColor("#ff0000"));
        connect();
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // если приняли сообщение в Handler
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);                                                // формируем строку
                        int endOfLineIndex = sb.indexOf("\r\n");                            // определяем символы конца строки
                        if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
                            String sbprint = sb.substring(0, endOfLineIndex);               // то извлекаем строку
                            sb.delete(0, sb.length());                                      // и очищаем sb
                            Log.d("TEST", "ODPOWIEDZ OD ARDUINO: -----> "+ sbprint);
                            if(txtArduino != null) {
                                //txtArduino = (TextView) fragment_01.findViewById(R.id.txtArduino);
                                txtArduino.setText(sbprint);

                            }

                            //txtArduino.setText("Ответ от Arduino: " + sbprint);             // обновляем TextViewtm
                        }

                        //Log.d(TAG, "...Строка:"+ sb.toString() +  "Байт:" + msg.arg1 + "...");
                        break;
                }
            };
        };


    }

    public bluetoothManager() throws IOException {

        btAdapter = BluetoothAdapter.getDefaultAdapter(); //BluetoothAdapter

        /*BluetoothDevice device = btAdapter.getRemoteDevice(address);
        btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        outStream = btSocket.getOutputStream();
        btSocket.connect();*/
    }

    public void setActivityContext(Activity activityContext){
        this.activityContext=activityContext;
    }

    public void setView(TextView v){
        //fragment_01 = v;
        txtArduino = v;
    }


    public void connect(){

        btAdapter = BluetoothAdapter.getDefaultAdapter(); //BluetoothAdapter
        checkBTState();

        try {
            device = btAdapter.getRemoteDevice(address);
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

        this.mConnectedThread = new ConnectedThread(btSocket);
        this.mConnectedThread.start();

    }

    public boolean getStatus(){
        try {
            btSocket.isConnected();
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }

    public void sendData(String message) {

        mConnectedThread.write(message);

        /*byte[] msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (address.equals("00:00:00:00:00:00"))
                msg = msg + ".\n\nВ переменной address у вас прописан 00:00:00:00:00:00, вам необходимо прописать реальный MAC-адрес Bluetooth модуля";
            msg = msg +  ".\n\nПроверьте поддержку SPP UUID: " + MY_UUID.toString() + " на Bluetooth модуле, к которому вы подключаетесь.\n\n";
        }*/
    }

    private boolean checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth не поддерживается");
            return false;
        } else {
            if (btAdapter.isEnabled()) {
                //Log.d(TAG, "...Bluetooth включен...");
                return true;
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                activityContext.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return false;

            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(activityContext, title + " - " + message, Toast.LENGTH_LONG).show();
        activityContext.finish();
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.d("THREAD_RUN", "-------> Thread run() method is called");
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Отправляем в очередь сообщений Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            //Log.d(TAG, "...Данные для отправки: " + message + "...");
            //byte[] msgBuffer = message.getBytes();
            int msgBuffer = Integer.parseInt(message);
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                //Log.d(TAG, "...Ошибка отправки данных: " + e.getMessage() + "...");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}

