package com.example.marcinwlodarczyk.tabbed;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import layout.SubPage01;
import layout.SubPage02;
import layout.SubPage03;

import static com.example.marcinwlodarczyk.tabbed.R.id.container;

public class MainActivity extends AppCompatActivity {

    public static bluetoothManager conn;
    private static final String TAG = "MyActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    DBHelper dbHelper;
    boolean flag = false;
    TextView txtArduino;

    private BluetoothAdapter myBluetooth = null;
    //ImageView staus = (ImageView) findViewById(R.id.conn_status);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            conn = new bluetoothManager(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // устанавливаем переключатель программно в значение ON
       // mSwitch.setChecked(true);
        // добавляем слушателя
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                                               @Override
//                                               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                                   // в зависимости от значения isChecked выводим нужное сообщение
//                                                   if (isChecked) {
//                                                      //dbHelper.update_where("1","temp_bool","id","user","1");
//                                                       Log.d(TAG,"Switch ON");
//
//                                                   } else {
//                                                      // dbHelper.update_where("0","temp_bool","id","user","1");
//                                                       Log.d(TAG,"Switch OFF");
//                                                   }
//                                               }
//                                           }
//    );

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

       // permissions();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        dbHelper = new DBHelper(this);
        mViewPager.setCurrentItem(1);


    }

    @Override
    public void onResume() {
        super.onResume();


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, getIntent());
        if(resultCode==RESULT_OK && requestCode==1){
            //new bluetooth_atask_conn().execute(); //Call the class to connect;
        }
        else{
            Log.d("TAG","DENY");
        }
    }


    public void onClickBT(View v) {
        permissions();
        txtArduino = (TextView) findViewById(R.id.txtArduino);
        //conn.setView(txtArduino);
//        if (v.getId() == R.id.manual_con) {
//            Log.d("TAG","DONE");
//            new bluetooth_atask_conn().execute(); //Call the class to connect;
//        }
       if (v.getId() == R.id.MainButton) {

//           if (conn.getStatus()) {
//                if (!flag) {
//                    conn.sendData("227");
//                } else {
//                    conn.sendData("100");
//                }
//                flag = !flag;
//            } else {
//                conn.connect();
//
//                if (!flag) {
//                    conn.sendData("1");
//                } else {
//                    conn.sendData("0");
//                }
//                flag = !flag;
//            }
        }

    }

    public void onClickInsert(View v) {
//        String [][] str = {{"date","20 jun"},{"time","25"},{"temperature","18"}};
//        String [][] str1 = {{"name","User1"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"}};
//        dbHelper.insert(dbHelper,str,"statistic");
//        dbHelper.insert(dbHelper,str1,"user");
        String [][] str1 = {{"name","apple"},{"source",""+R.mipmap.apple}};
        String [][] str2 = {{"name","diamond"},{"source",""+R.mipmap.diamond}};
        String [][] str3= {{"name","flash"},{"source",""+R.mipmap.flash}};
        String [][] str4 = {{"name","guitar"},{"source",""+R.mipmap.guitar}};
        String [][] str5= {{"name","gentleman"},{"source",""+R.mipmap.gentleman}};
        String [][] str6 = {{"name","love"},{"source",""+R.mipmap.love}};
        String [][] str7= {{"name","macos"},{"source",""+R.mipmap.macos}};
        String [][] str8 = {{"name","meter"},{"source",""+R.mipmap.meter}};
        String [][] str9= {{"name","skull"},{"source",""+R.mipmap.skull}};
        String [][] str10= {{"name","User1"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"},{"image",""+R.mipmap.macos}};
        String [][] str11= {{"name","User2"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"},{"image",""+R.mipmap.macos}};
        String [][] str12= {{"name","User3"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"},{"image",""+R.mipmap.macos}};
        String [][] str14= {{"name","User4"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"},{"image",""+R.mipmap.macos}};
        String [][] str13= {{"name","User5"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"},{"image",""+R.mipmap.macos}};

        dbHelper.insert(dbHelper,str1,"image");
        dbHelper.insert(dbHelper,str2,"image");
        dbHelper.insert(dbHelper,str3,"image");
        dbHelper.insert(dbHelper,str4,"image");
        dbHelper.insert(dbHelper,str5,"image");
        dbHelper.insert(dbHelper,str6,"image");
        dbHelper.insert(dbHelper,str7,"image");
        dbHelper.insert(dbHelper,str8,"image");
        dbHelper.insert(dbHelper,str9,"image");
        dbHelper.insert(dbHelper,str10,"user");
        dbHelper.insert(dbHelper,str11,"user");
        dbHelper.insert(dbHelper,str12,"user");
        dbHelper.insert(dbHelper,str13,"user");
        dbHelper.insert(dbHelper,str14,"user");
        setUser(1);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void permissions(){
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }
        else
            {
                new bluetooth_atask_conn().execute();
            }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    SubPage01 tab1 = new SubPage01();
                    return tab1;
                case 1:
                    SubPage02 tab2 =new SubPage02();
                    return tab2;
                case 2:
                    SubPage03 tab3 = new SubPage03();
                    tab3.setContext(MainActivity.this,dbHelper);
                    return tab3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Statistics";
                case 1:
                    return "Heater";
                case 2:
                    return "Preferences";
            }
            return null;
        }
    }
    private int getUser()
    {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        int savedText = sharedPref.getInt("Current User",0);
        return savedText;
    }
    private void setUser(int value)
    {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Current User",value);
        editor.commit();
        Log.d(TAG,"New Current User: "+value);
    }
}

