package com.example.yevgensydorenko.tabbed;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

//import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;


public class BluetoothManager {

    public Handler h;
    public TextView txtArduino;
    public TextView timeArduino;
    private Conn_info info = null;

    final int RECEIVE_MESSAGE = 1;

    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;


    private Activity activityContext = null; // v1


    @SuppressLint("HandlerLeak")
    public BluetoothManager(final Activity activityContext, Conn_info info) throws IOException {
        this.activityContext = activityContext;
        this.info = info;
        txtArduino = (TextView) activityContext.findViewById(R.id.txtArduino);
        timeArduino = (TextView) activityContext.findViewById(R.id.timeArduino);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECEIVE_MESSAGE:                                             // если приняли сообщение в Handler
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        String[] signal = Arduino.obtainSignal(strIncom);
                        if (!signal[0].equals("00 °C") && !signal[1].equals("00 m")) {
                            if (txtArduino != null) {
                                txtArduino.setText(signal[0]);
                            }
                            if (timeArduino != null) {
                                timeArduino.setText(signal[1]);
                            }
                        }

//
//                        sb.append(strIncom);                                                // формируем строку
//                        int endOfLineIndex = sb.indexOf("\r\n");                            // определяем символы конца строки
//                        if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
//                            String sbprint = sb.substring(0, endOfLineIndex);               // то извлекаем строку
//                            sb.delete(0, sb.length());
//                            //Log.d("TEST", "ODPOWIEDZ OD ARDUINO: -----> "+ sbprint); // и очищаем sb
//                            Arduino.obtainSignal(sbprint);
//
//                            if(txtArduino != null) {
//                                //txtArduino = (TextView) fragment_01.findViewById(R.id.txtArduino);
//                                txtArduino.setText(sbprint);
//                            }
//
//                            //txtArduino.setText("Ответ от Arduino: " + sbprint);             // обновляем TextViewtm
//                        }
                        //Log.d(TAG, "...Строка:"+ sb.toString() +  "Байт:" + msg.arg1 + "...");
                        break;
                }
            }
        };

        connect();
    }


    public void connect() {
        this.mConnectedThread = new ConnectedThread(info.getSocket());
        this.mConnectedThread.start();
    }

    public void rebootThread() {
        mConnectedThread.run();
    }

    public void stopThread() {
        mConnectedThread.interrupt();
    }
//    public boolean getStatus(){
//        try {
//            btSocket.isConnected();
//            return true;
//        }catch (NullPointerException e){
//            return false;
//        }
//    }

    public void sendData(Arduino a) {
        String [] result = a.getMessage();
        Log.d("sendData()", "message prepared: " + TextUtils.join(", ", result));
        mConnectedThread.write(new byte[]{
                Integer.valueOf(result[0]).byteValue(),
                Integer.valueOf(result[1]).byteValue(),
                Integer.valueOf(result[2]).byteValue()});
    }

    private void errorExit(String title, String message) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                    mConnectedThread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                    h.obtainMessage(RECEIVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Отправляем в очередь сообщений Handler

                } catch (IOException e) {
                    stopThread();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] message) {
            //Log.d(TAG, "...Данные для отправки: " + message + "...");
            //byte[] msgBuffer = message.getBytes();
            //int msgBuffer = Integer.parseInt(message);
            Log.d(TAG, "I am going to write message");
            try {
                //byte[] bytes = ByteBuffer.allocate(4).putInt().array();
                //ByteBuffer result = ByteBuffer.wrap(message);
                //Log.d(TAG, "I am going to write message:"+result);
                //byte[] bytes = new byte[]{1,00,01};
                mmOutStream.write(message);
            } catch (IOException e) {
                Log.d(TAG, "Write message error " + e.getMessage() + "...");
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

}