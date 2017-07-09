
package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcinwlodarczyk.tabbed.DBHelper;
import com.example.marcinwlodarczyk.tabbed.MainActivity;
import com.example.marcinwlodarczyk.tabbed.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubPage01.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubPage01#newInstance} factory method to
 * create an instance of this fragment.
 */


public class SubPage01 extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SubPage01";
    // TODO: Rename and change types of parameters
    private String mParam1;
    DBHelper dbHelper;
    Context context;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public SubPage01() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubPage01.
     */
    // TODO: Rename and change types and number of parameters

    public static SubPage01 newInstance(String param1, String param2) {
        SubPage01 fragment = new SubPage01();
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


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_page01, container, false);
        TextView txt_date=(TextView) view.findViewById(R.id.txt_date);
        txt_date.setText(correctDate(dbHelper.select(dbHelper,"statistic","date","where id=1")));
        TextView txt_time=(TextView) view.findViewById(R.id.txt_time);
        txt_time.setText(dbHelper.select(dbHelper,"statistic","time","where id=1")+" m");
        TextView txt_temp=(TextView) view.findViewById(R.id.txt_temp);
        txt_temp.setText(dbHelper.select(dbHelper,"statistic","temperature","where id=1")+" °C");
        createGraph();
        return view;
    }



    @Override
    public void onClick(View v) {
        Log.i("MyActivity","MSG");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG,"Switch ON");
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
    public void setContext(Context context,DBHelper dbHelper) {
        this.context=context;
        this.dbHelper=dbHelper;

        // Required empty public constructor
    }
    public String correctDate(String date)
    {
        String[] separated=date.split("-");
            switch (separated[1]) {
                case "01":
                    date = "JAN";
                    break;
                case "02":
                    date = "FEB";
                    break;
                case "03":
                    date = "MAR";
                    break;
                case "04":
                    date = "APR";
                    break;
                case "05":
                    date = "MAY";
                    break;
                case "06":
                    date = "JUN";
                    break;
                case "07":
                    date = "JUL";
                    break;
                case "08":
                    date = "AUG";
                    break;
                case "09":
                    date = "SEP";
                    break;
                case "10":
                    date = "OCT";
                    break;
                case "11":
                    date = "NOV";
                    break;
                case "12":
                    date = "DEC";
                    break;

            }
        return separated[2]+"-"+date;
    }
    private void createGraph(){
        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        String[] dates=dbHelper.select(dbHelper,"statistic","date");
        double [] costs=findCost(dbHelper.select(dbHelper,"statistic","time"));
//
//        graph.addSeries(series);
//        Calendar calendar =Calendar.getInstance();
//
//        DataPoint[] dp=new DataPoint[6];
//        for (int i = 0; i < dates.length||i<5 ; i++) {
//            dp[i]=new DataPoint(i,costs[i]);
//            calendar.add(Calendar.DATE, 1);
//        }
//        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dp);
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date d6 = calendar.getTime();

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 2),
                new DataPoint(d3, 8),
                new DataPoint(d4, 5),
                new DataPoint(d5, 9),
        });
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
//        graph.getGridLabelRenderer().setNumVerticalLabels(7);
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d6.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        series.setSpacing(10);
//        series.setAnimated(true);
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(5);
        graph.getViewport().setScalable(true);
//        series.setOnDataPointTapListener(new OnDataPointTapListener() {
//            @Override
//            public void onTap(Series series, DataPointInterface dataPoint) {
//                Toast.makeText(getActivity(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    private double[] findCost(String[] time)
    {
        DecimalFormat df = new DecimalFormat("#.###");
        double[] cost = new double[time.length];
       for(int i=0;i<time.length;i++)
       {
           cost[i]=Double.parseDouble(time[i]);
           cost[i]=cost[i]/60*1.8*0.17;
           cost[i]= Double.parseDouble(df.format(cost[i]));
       }
       return cost;
    }
}
