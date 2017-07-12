package layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.marcinwlodarczyk.tabbed.DBHelper;
import com.example.marcinwlodarczyk.tabbed.R;

import java.util.ArrayList;

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
    Button EditButton;
    Context context;
    ImageView imgProfile;
    Button ManConn;
    View view;
    private static final String TAG = "SubPage3";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Switch swtemp;
    Switch swtime;
    TextView temmpp;
    TextView time;
    DBHelper dbHelper;
    Spinner spinner;

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
        if(getUser()!=0) {
            time = (TextView) view.findViewById(R.id.timeb);
            time.setText(dbHelper.select(dbHelper, "user", "time", "WHERE id=" + getUser()) + " m");
            temmpp = (TextView) view.findViewById(R.id.temper);
            temmpp.setText(dbHelper.select(dbHelper, "user", "temp", "WHERE id=" + getUser()) + " °C");
            PrButton1 = (Button) view.findViewById(R.id.btn_temp);
            PrButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog d = new Dialog(context);
                    d.setContentView(R.layout.dialog_params);
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
                            dbHelper.update_where(dbHelper, String.valueOf(np.getValue()), "temp", "id", "user", ""+getUser());
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
//            Button testbtn=(Button) view.findViewById(R.id.test);
//            testbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dbHelper.insert(dbHelper, new String[][]{{"time", "20"}, {"temperature", "20"}}, "statistic");
//                    Log.d("Insert Date", "" + dbHelper.select(dbHelper, "statistic", "date"));
//                }
//                });
            PrButton2 = (Button) view.findViewById(R.id.btn_time);
            PrButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog d = new Dialog(context);
                    d.setContentView(R.layout.dialog_params);
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
                            dbHelper.update_where(dbHelper, String.valueOf(np.getValue()), "time", "id", "user", ""+getUser());
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


            spinner = (Spinner) view.findViewById(R.id.user_spinner);

            spinner.setAdapter(new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_dropdown_item, dbHelper.select(dbHelper, "user", "name")));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // показываем позиция нажатого элемента
//                SharedPreferences sPref = getPreferences(MODE_PRIVATE);
//                SharedPreferences.Editor ed = sPref.edit();
//                ed.putString(SAVED_TEXT, "1");
//                ed.commit();
                    setUser(spinner.getSelectedItemPosition() + 1);
                    time.setText(dbHelper.select(dbHelper, "user", "time", "WHERE id=" + getUser()) + " m");
                    temmpp.setText(dbHelper.select(dbHelper, "user", "temp", "WHERE id=" + getUser()) + " °C");
                    swtemp.setChecked(dbHelper.select(dbHelper, "user", "temp_bool", "WHERE id=" + getUser()).equals("1"));
                    swtime.setChecked(dbHelper.select(dbHelper, "user", "time_bool", "WHERE id=" + getUser()).equals("1"));
                    imgProfile.setImageResource(Integer.parseInt(dbHelper.select(dbHelper, "user", "image", "where id=" + getUser())));
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            spinner.setSelection(getUser() - 1);
            swtemp = (Switch) view.findViewById(R.id.switch_temp);
            swtemp.setChecked(dbHelper.select(dbHelper, "user", "temp_bool", "WHERE id=" + getUser()).equals("1"));
            swtemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dbHelper.update_where(dbHelper, isChecked ? "1" : "0", "temp_bool", "id", "user", ""+getUser());
                }
            });
            swtime = (Switch) view.findViewById(R.id.switch_time);
            swtime.setChecked(dbHelper.select(dbHelper, "user", "time_bool", "WHERE id=" + getUser()).equals("1"));
            swtime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    dbHelper.update_where(dbHelper, isChecked ? "1" : "0", "time_bool", "id", "user", ""+getUser());
                }
            });
            EditButton = (Button) view.findViewById(R.id.btn_editName);
            EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Rename User");

// Set up the input
                    final EditText input = new EditText(context);

// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setText(dbHelper.select(dbHelper, "user", "name", "WHERE id=" + getUser()));
                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                    builder.setView(input);

// Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, input.getText().toString());
                            dbHelper.update_where(dbHelper, input.getText().toString(), "name", "id", "user",""+ getUser());
                            spinner.setAdapter(new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_dropdown_item, dbHelper.select(dbHelper, "user", "name")));

                            spinner.setSelection(getUser() - 1);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }
            });
            imgProfile = (ImageView) view.findViewById(R.id.profile_image);
            imgProfile.setClickable(true);
            imgProfile.setImageResource(Integer.parseInt(dbHelper.select(dbHelper, "user", "image", "where id=" + getUser())));
            imgProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog d = new Dialog(context);
                    d.setTitle("Choose a new image");
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
// Set up the input
                    String[] images = dbHelper.select(dbHelper, "image", "source");
                    for (int i = 0; i < images.length; i += 3) {
                        LinearLayout layout_v = new LinearLayout(context);
                        layout_v.setOrientation(LinearLayout.VERTICAL);
                        for (int j = 0; j < 3; j++) {
                            if ((i + j) != images.length) {
                                final ImageView input2 = new ImageView(context);
                                final int source = Integer.parseInt(images[i + j]);
                                input2.setImageResource(source);
                                input2.setMinimumHeight(275);
                                input2.setMinimumWidth(275);
                                input2.setClickable(true);
                                input2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dbHelper.update_where(dbHelper, "" + source, "image", "id", "user", ""+getUser());
                                        imgProfile.setImageResource(source);
                                        d.dismiss();
                                    }
                                });

                                layout_v.addView(input2);
                            }

                        }
                        layout.addView(layout_v);
                    }


                    d.setContentView(layout);
                    d.show();

                }
            });
        }
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
    private int getUser()
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int savedText = sharedPref.getInt("Current User",0);
       return savedText;
    }
    private void setUser(int value)
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Current User",value);
        editor.commit();
        Log.d(TAG,"New Current User: "+value);
    }

}
