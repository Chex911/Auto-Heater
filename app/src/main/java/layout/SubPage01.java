
package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.marcinwlodarczyk.tabbed.DBHelper;
import com.example.marcinwlodarczyk.tabbed.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

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
    int centralIndex=3;
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
        final TextView txt_date=(TextView) view.findViewById(R.id.txt_date);
        final TextView txt_time=(TextView) view.findViewById(R.id.txt_time);
        final TextView txt_temp=(TextView) view.findViewById(R.id.txt_temp);
        final TextView txt_energy=(TextView) view.findViewById(R.id.txt_energy);
        new Graph().createGraph();
        new Graph().refreshInfo(txt_energy,txt_time,txt_date,txt_temp);
        final Button btnNext = (Button) view.findViewById(R.id.btnNext);
        final Button btnPrev = (Button) view.findViewById(R.id.btnPrev);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrev.setEnabled(true);
                centralIndex--;
                new Graph().refreshGraph();
                new Graph().createGraph();
                new Graph().refreshInfo(txt_energy,txt_time,txt_date,txt_temp);
                if(!checkLenght(true))
                    btnNext.setEnabled(false);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setEnabled(true);
                centralIndex++;
                new Graph().refreshGraph();
                new Graph().createGraph();
                new Graph().refreshInfo(txt_energy,txt_time,txt_date,txt_temp);
                if(!checkLenght(false))
                    btnPrev.setEnabled(false);
            }
        });

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
    public class Graph{
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        private Graph(){
        }
        private void createGraph(){

            String[]  dates=dbHelper.select(dbHelper,"statistic","date");
            String centralDate=dates[dates.length-centralIndex];
            double [] costs=findCost(dbHelper.select(dbHelper,"statistic","time"));
            DataPoint[] dp=new DataPoint[5];
            for (int i = 0; i<5 ; i++) {
                try {
                    String[] separated = dates[dates.length-centralIndex - 2 + i].split("-");
                    dp[i] = new DataPoint(Double.parseDouble(separated[2]), costs[dates.length-centralIndex - 2 + i]);
                }
                catch (IndexOutOfBoundsException e)
                {
                    dp[i]= new DataPoint(0,0);
                }

            }
            BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dp);
            graph.addSeries(series);
//        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(graph.getContext()));
//        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
//        graph.getGridLabelRenderer().setNumVerticalLabels(7);
            //       graph.getViewport().setXAxisBoundsManual(true);
            series.setSpacing(60);
            if(dp[0].getX()==0){
                graph.getViewport().setMinX(dp[4].getX()-4);
                graph.getViewport().setMaxX(dp[4].getX());
            }
            if(dp[4].getX()==0){
                graph.getViewport().setMinX(dp[0].getX());
                graph.getViewport().setMaxX(dp[0].getX()+4);
            }
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(getMaxY(costs));
            Log.d("Checkai", graph.getViewport().getMinX(false)+" "+ graph.getViewport().getMaxX(false));
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(setLabels(centralDate));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
//        graph.getViewport().setScalable(true);
//        series.setOnDataPointTapListener(new OnDataPointTapListener() {
//            @Override
//            public void onTap(Series series, DataPointInterface dataPoint) {
//                Toast.makeText(getActivity(), "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
//            }
//        });
        }
        public void refreshGraph()
        {
            graph.removeAllSeries();
        }
        private double getMaxY(double[] costs)
        {
            double max=0;
            for(int i=0;i<costs.length;i++)
            {
                if(costs[i]>max)
                    max=costs[i];
            }
            Log.d("MAX COST",max+"");
            return max;
        }
        private String[] setLabels(String centralDate)
        {
            String[] str= new String[5];
            String[] separated=centralDate.split("-");
            java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Integer.parseInt(separated[0]),Integer.parseInt(separated[1])-1,Integer.parseInt(separated[2]));
            calendar.add(Calendar.DATE, -2);
            for (int i=0;i<5;i++){
                str[i]= df.format(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
            return str;
        }
        public void refreshInfo(TextView energy,TextView time,TextView date,TextView temperature)
        {
            String[] dates=dbHelper.select(dbHelper,"statistic","date");
            double [] costs=findCost(dbHelper.select(dbHelper,"statistic","time"));
            String[] times=dbHelper.select(dbHelper,"statistic","time");
            String[] temperatures=dbHelper.select(dbHelper,"statistic","temperature");
            energy.setText(""+costs[dates.length-centralIndex]+" €");
            time.setText(times[dates.length-centralIndex]+" m");
            date.setText(correctDate(dates[dates.length-centralIndex]));
            temperature.setText(temperatures[dates.length-centralIndex]+" °C");

        }
        private double[] findCost(String[] time)
        {
            DecimalFormat df = new DecimalFormat("#.##");
            double[] cost = new double[time.length];
            for(int i=0;i<time.length;i++)
            {
                cost[i]=Double.parseDouble(time[i]);
                cost[i]=cost[i]/60*1.8*0.25;
                cost[i]= Double.parseDouble(df.format(cost[i]));
            }
            return cost;
        }


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
    public boolean checkLenght(boolean button)
    {
        String[] date = dbHelper.select(dbHelper,"statistic","date");
        if (button) {
            if (date.length - centralIndex > date.length - 2) {
                return false;
            }
            else {
                return true;
            }
        }
        if(!button){
            if (date.length - centralIndex <1) {
                return false;
            }
            else {
                return true;
            }
        }
        return false;

    }


}
