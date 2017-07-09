package com.example.marcinwlodarczyk.tabbed;

import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by marcinwlodarczyk on 08.07.2017.
 */

public class bluetooth_atask_conn_info extends AsyncTask<Conn_info, Conn_info, Conn_info> {
    private Conn_info info = null;
    public AsyncResponse_info delegate = null;

    public Conn_info getInfo(){
        return info;
    }

    @Override
    protected Conn_info doInBackground(Conn_info... params) {
        info = new Conn_info();
        Log.d("POST","Jobs undone");
        return info;
    }

    @Override
    protected void onPostExecute(Conn_info result) {
        delegate.processInfo_result(info);
        Log.d("POST","Jobs done");
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Conn_info... values) {
    }
}

