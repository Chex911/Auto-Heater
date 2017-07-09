package com.example.marcinwlodarczyk.tabbed;

import android.util.Log;

/**
 * Created by marcinwlodarczyk on 08.07.2017.
 */

public class Arduino {
    private static final String TAG = "Arduino";
    private  String message = "10000";


    public Arduino(){

    }

    public static String[] obtainSignal(String s){
        String[] output = new String[2];
        if(s.length() == 4) {
            Log.d(TAG,"obtainSignal()            ----> "+s);
            output[0] = s.substring(0, 2); // Time of work
            output[1] = s.substring(2, 4); // Temperature
            Log.d(TAG,"obtainSignal(Time)        ----> "+output[0]);
            Log.d(TAG,"obtainSignal(Temperature) ----> "+output[1]);
            return output;
        }
        return new String[] {"000","00"};
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

    }

    public String getMessage(){
        return message;
    }
}
