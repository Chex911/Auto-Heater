package com.example.marcinwlodarczyk.tabbed;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import static com.example.marcinwlodarczyk.tabbed.R.id.container;

public class MainActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {

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
    static Dialog d;
    private BluetoothAdapter myBluetooth = null;
    //ImageView staus = (ImageView) findViewById(R.id.conn_status);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


//        try {
//            conn = new bluetoothManager(this);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
        premissions();





        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        dbHelper = new DBHelper(this);


    }

    @Override
    public void onResume() {
        super.onResume();


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, getIntent());
        if(resultCode==RESULT_OK && requestCode==1){
            new bluetooth_atask_conn().execute(); //Call the class to connect;
        }
        else{
            Log.d("TAG","DENY");
        }
    }


    public void onClickBT(View v) {
        premissions();
        txtArduino = (TextView) findViewById(R.id.txtArduino);
        //conn.setView(txtArduino);
        if (v.getId() == R.id.manual_con) {
            new bluetooth_atask_conn().execute(); //Call the class to connect;
        }
//        if (v.getId() == R.id.MainButton) {
//            if (conn.getStatus()) {
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
//        }

    }

    public void onClickInsert(View v) {
        String [][] str = {{"date","20 jun"},{"time","25"},{"temperature","18"}};
        String [][] str1 = {{"name","User1"},{"temp_bool","0"},{"temp","0"},{"time_bool","0"},{"time","0"}};
        dbHelper.insert(str,"statistic");
        dbHelper.insert(str1,"user");

    }

    public void onClickPref(View v) {
        final Dialog d = new Dialog(MainActivity.this);

        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final TextView temmpp;

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        switch (v.getId()) {
            case R.id.btn_temp:
                d.setTitle("Set Temperature");
                temmpp = (TextView) findViewById(R.id.temper);
                np.setMaxValue(33);
                np.setMinValue(10);
                try {

                    String path = temmpp.getText().toString();
                    String s[] = path.split(" ");
                    String result = s[0];
                    np.setValue(Integer.parseInt(result));
                } catch (Throwable cause) {
                    np.setValue(18);
                }
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(this);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        temmpp.setText(String.valueOf(np.getValue()) + " °C");
                        dbHelper.update_where(String.valueOf(np.getValue()),"temp","id","user","1");
                        //tv.setText(String.valueOf(np.getValue()));
                        d.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                break;


            case R.id.btn_time:
                d.setTitle("Set Time");
                temmpp = (TextView) findViewById(R.id.timeb);
                np.setMaxValue(90);
                np.setMinValue(5);
                try {
                    String path = temmpp.getText().toString();
                    String s[] = path.split(" ");
                    String result = s[0];
                    np.setValue(Integer.parseInt(result));
                } catch (Throwable cause) {
                    np.setValue(45);
                }

                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(this);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        temmpp.setText(String.valueOf(np.getValue()) + " m.");
                        dbHelper.update_where(String.valueOf(np.getValue()),"time","id","user","1");
                        //tv.setText(String.valueOf(np.getValue()));
                        d.dismiss();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                break;

        }

        d.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void premissions(){
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

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View myInflatedView = inflater.inflate(R.layout.fragment_sub_page02, container, false);
            //conn.setView(myInflatedView);


            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                View rootView = inflater.inflate(R.layout.fragment_sub_page01, container, false);
                return rootView;
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {

                View rootView = inflater.inflate(R.layout.fragment_sub_page02, container, false);
                return rootView;
            } else {
                View rootView = inflater.inflate(R.layout.fragment_sub_page03, container, false);
                return rootView;
            }

        }

    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // создаем таблицу с полями
            Log.d(TAG, "--- onCreate database ---");
            // создаем таблицу с полями

            db.execSQL("create table  statistic ("
                    + "id integer primary key autoincrement,"
                    + "date text,"
                    + "time text,"
                    + "temperature text"
                    + ");");

            db.execSQL("create table  image ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "source text"
                    + ");");
            db.execSQL("create table if not exists user ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "temp_bool integer,"
                    + "temp text,"
                    + "time_bool integer,"
                    + "time text,"
                    + "image integer"
//                    + "foreign key (image) references image(id)"
                    + ");");

        }
        public void insert(String[][] params,String table)
        {
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Log.d(TAG, "--- Insert in mytable: ---");
            for(int i=0; i<params.length;i++) {
                cv.put(params[i][0], params[i][1]);
            }


            long rowID = db.insert(table, null, cv);
            Log.d(TAG, "row inserted, ID = " + rowID);
            //       dbHelper.test_insert(db);
            //  int clearCount = db.delete("mytable", null, null);
            ;
            dbHelper.close();
        }

//        public void test_insert(SQLiteDatabase db)
//        {
//            ContentValues cv = new ContentValues();
////            cv.put("name","User 1");
////            cv.put("temp_bool",0);
////            cv.put("temp",25);
////            cv.put("time_bool",0);
////            cv.put("time",20);
//            cv.put("name","photo1");
//            cv.put("source","mat/ter/it");
//            db.insert("image",null,cv);
//        }

        public void update_where(String new_value, String params, String where,String database,String current)
        {
            ContentValues cv = new ContentValues();
            cv.put(params,new_value);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.update(database,cv,where + "=" + current,null);
            dbHelper.close();
            Log.d(TAG,"Molodec:)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}

