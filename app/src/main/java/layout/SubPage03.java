package layout;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import com.example.marcinwlodarczyk.tabbed.DBHelper;
import com.example.marcinwlodarczyk.tabbed.MainActivity;
import com.example.marcinwlodarczyk.tabbed.R;
import com.example.marcinwlodarczyk.tabbed.bluetooth_atask_conn;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubPage03.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubPage03#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubPage03 extends Fragment implements View.OnClickListener,NumberPicker.OnValueChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button PrButton1;
    Button PrButton2;
    Context context;
    Button ManConn;
    View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Switch swtemp;
    Switch swtime;
    TextView temmpp;
    TextView time;
    DBHelper dbHelper;

    private OnFragmentInteractionListener mListener;


    public SubPage03() {


        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubPage03.
     */
    // TODO: Rename and change types and number of parameters
    public static SubPage03 newInstance(String param1, String param2) {
        SubPage03 fragment = new SubPage03();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_sub_page03, container, false);
        time = (TextView) view.findViewById(R.id.timeb);
        time.setText(dbHelper.select(dbHelper,"user","time","")+" m");
        temmpp = (TextView) view.findViewById(R.id.temper);
        temmpp.setText(dbHelper.select(dbHelper,"user","temp","")+" °C");
        PrButton1 = (Button) view.findViewById(R.id.btn_temp);
        PrButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    final Dialog d = new Dialog(context);
                    d.setContentView(R.layout.dialog);
                    Button b1 = (Button) d.findViewById(R.id.button1);
                    Button b2 = (Button) d.findViewById(R.id.button2);


                    final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                            d.setTitle("Choose Temp.");
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
                            np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                    Log.i("value is", "" + newVal);

                                }
                            });
                            b1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    temmpp.setText(String.valueOf(np.getValue()) + " °C");
                                    dbHelper.update_where(dbHelper,String.valueOf(np.getValue()),"temp","id","user","1");
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


                    d.show();


            }
        });
        PrButton2 = (Button) view.findViewById(R.id.btn_time);
        PrButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(context);
                d.setContentView(R.layout.dialog);
                Button b1 = (Button) d.findViewById(R.id.button1);
                Button b2 = (Button) d.findViewById(R.id.button2);


                final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                d.setTitle("Set Time");
                np.setMaxValue(90);
                np.setMinValue(5);
                try {

                    String path = time.getText().toString();
                    String s[] = path.split(" ");
                    String result = s[0];
                    np.setValue(Integer.parseInt(result));
                } catch (Throwable cause) {
                    np.setValue(45);
                }
                np.setWrapSelectorWheel(false);
                np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        Log.i("value is", "" + newVal);

                    }
                });
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        time.setText(String.valueOf(np.getValue()) + " m");
                        dbHelper.update_where(dbHelper,String.valueOf(np.getValue()),"time","id","user","1");
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


                d.show();


            }
        });
        ManConn =(Button) view.findViewById(R.id.manual_con);
        ManConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //conn.setView(txtArduino);
                if (v.getId() == R.id.manual_con) {
                    Log.d("TAG", "DONE");
                    new bluetooth_atask_conn().execute(); //Call the class to connect;
                }
            }
          });
        swtemp= (Switch) view.findViewById(R.id.switch_temp);
        swtemp.setChecked(dbHelper.select(dbHelper,"user","temp_bool","").equals("1"));
        swtemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbHelper.update_where(dbHelper,isChecked ? "1" : "0","temp_bool","id","user","1");
            }
        });
        swtime= (Switch) view.findViewById(R.id.switch_time);
        swtime.setChecked(dbHelper.select(dbHelper,"user","time_bool","").equals("1"));
        swtime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dbHelper.update_where(dbHelper,isChecked ? "1" : "0","time_bool","id","user","1");
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void setContext(Context context,DBHelper dbHelper) {
        this.context=context;
        this.dbHelper=dbHelper;

        // Required empty public constructor
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
