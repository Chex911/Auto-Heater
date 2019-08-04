package com.example.yevgensydorenko.tabbed;

import android.os.AsyncTask;


/**
 * Created by yevgensydorenko on 08.07.2017.
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
        return info;
    }

    @Override
    protected void onPostExecute(Conn_info result) {
        delegate.processInfo_result(info);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Conn_info... values) {
    }
}

