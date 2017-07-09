package com.example.marcinwlodarczyk.tabbed;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

//import static android.icu.text.RelativeDateTimeFormatter.Direction.THIS;


public class bluetoothManager{

    public Handler h;
    public TextView txtArduino;
    private Conn_info info = null;

    final int RECIEVE_MESSAGE = 1;

    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;


    private Activity activityContext = null; // v1


    public bluetoothManager(final Activity activityContext, Conn_info info) throws IOException {
        this.activityContext = activityContext;
        this.info = info;
        //txtArduino = (TextView) activityContext.findViewById(R.id.txtArduino);

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
                            sb.delete(0, sb.length());
                            //Log.d("TEST", "ODPOWIEDZ OD ARDUINO: -----> "+ sbprint); // и очищаем sb
                            Arduino.obtainSignal(sbprint);

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

        connect();
    }


    public void connect(){
        this.mConnectedThread = new ConnectedThread(info.getSocket());
        this.mConnectedThread.start();
    }

    public void rebootThread(){
        mConnectedThread.run();
    }

    public void stopThread(){
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

    public void sendData(Arduino a){
        //mConnectedThread.write(a.getMessage());
        mConnectedThread.write("300");
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
                    mConnectedThread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Отправляем в очередь сообщений Handler

                } catch (IOException e) {
                    stopThread();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            //Log.d(TAG, "...Данные для отправки: " + message + "...");
            //byte[] msgBuffer = message.getBytes();
            int msgBuffer = Integer.parseInt(message);
            Log.d(TAG, "I am going to write message");
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "Write message error" + e.getMessage() + "...");
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