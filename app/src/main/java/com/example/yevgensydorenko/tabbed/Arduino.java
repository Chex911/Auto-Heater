package com.example.yevgensydorenko.tabbed;

import android.text.TextUtils;
import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by yevgensydorenko on 08.07.2017.
 */

public class Arduino {
    private static final String TAG = "Arduino";
    private String [] message = {"1","00","00"};
    private byte[] bytes = null;


    public Arduino() {

    }

    public static String[] obtainSignal(String s) {
        String[] output;
        Log.d(TAG, "ANY: obtainSignal() ----> " +s + " LENGTH: " + s.length());
        if (s.length() >= 3) {
//            Log.d(TAG, "obtainSignal() ----> " + s);
            try {
                output = s.split(",");
                output[0] += " °C";
                output[1] = output[1] + " m";
                return output;
            }
            catch (ArrayIndexOutOfBoundsException e){
                return new String[]{"00 °C", "00 m"};
            }
        }
        return new String[]{"00 °C", "00 m"};
    }

    public void sendSignal(String status, String time, String temp) {
        /*
            ("TRYB","TEMPERATURE","TIME")

            10001 - ON
            10000 - OFF
            2tt00 - ON profile_02 tt[temperature] : 00[time OFF]
            300TT - ON profile_03 00[time OFF]    : TT[time]
            4ttTT - ON profile_04 tt[temperature] : TT[time]
         */


        String output = "";

        switch (status) {
            case "00":
                status = "1";
                break;

            case "01":
                status = "2";
                break;

            case "10":
                status = "3";
                break;

            case "11":
                status = "4";
                break;

        }


        this.message = new String[]{status,time,temp};
//        int tmp = Integer.parseInt(output);
//        Log.d(TAG, "TMP:"+tmp);
//        bytes = ByteBuffer.allocate(4).putInt(tmp).array();

    }

    public String [] getMessage() {
        return message;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
