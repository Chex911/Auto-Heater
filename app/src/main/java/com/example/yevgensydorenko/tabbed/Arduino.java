package com.example.yevgensydorenko.tabbed;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by yevgensydorenko on 08.07.2017.
 */

public class Arduino {
    private static final String TAG = "Arduino";
    private  String message = "10000";
    private byte[] bytes = null;


    public Arduino(){

    }

    public static String[] obtainSignal(String s){
        String[] output;
        if(s.length() > 6) {
            Log.d(TAG,"obtainSignal() ----> "+s);
            output = s.split(",");
            output[0] += " °C";
            output[1] =output[1].substring(0,2)+" m";
            return output;
        }
        return new String[] {"00 °C","00 m"};
    }

    public void sendSignal(String... s){
        /*
            ("TRYB","TEMPERATURE","TIME")

            10001 - ON
            10000 - OFF
            2tt00 - ON profile_02 tt[temperature] : 00[time OFF]
            300TT - ON profile_03 00[time OFF]    : TT[time]
            4ttTT - ON profile_04 tt[temperature] : TT[time]
         */


        String output = "";

        switch (s[0]){
            case "00":
                s[0] = "1";
                break;

            case "01":
                s[0] = "2";
                break;

            case "10":
                s[0] = "3";
                break;

            case "11":
                s[0] = "4";
                break;

        }

        for (String parameter : s) {
            output += parameter;
        }

        this.message = output;
        int tmp = Integer.parseInt(output);
        bytes = ByteBuffer.allocate(4).putInt(tmp).array();

    }

    public String getMessage(){
        return message;
    }

    public byte[] getBytes(){
        return bytes;
    }
}
