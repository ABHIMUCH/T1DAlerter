package com.example.research;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import net.sf.javaml.classification.Classifier;

import java.util.ArrayList;
import java.util.Vector;


public class InitActivity extends Activity {

    public static Vector<String> last11;
    public static Vector<Classifier> SVMs;
    public static SVMMethods methodObject;
    public static setsMeanStdDev holdInfo;
    public static ArrayList<String> result;

    public static GraphView graph;
    public static Handler UIHandler;
    public static final int HIGH = 180;
    public static final int LOW = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        last11 = new Vector<String>();
        SVMs = new Vector<Classifier>();
        methodObject = new SVMMethods();

        graph = (GraphView) findViewById(R.id.graph);
        graph_init();

        result = new ArrayList<String>();

        WakefulBroadcastReceiver.startWakefulService(getApplicationContext());
    }

    private void graph_init(){
        graph.setTitle("Blood Glucose (mg/dl) over the last hour");

        Viewport display = graph.getViewport();
        display.setXAxisBoundsManual(true);
        display.setYAxisBoundsManual(true);
        display.setMaxX(60);
        display.setMaxY(300);
        display.setBackgroundColor(Color.GREEN);

        GridLabelRenderer labels = new GridLabelRenderer(graph);
        labels.setHorizontalLabelsVisible(true);
        labels.setVerticalLabelsVisible(true);
        labels.setVerticalAxisTitleTextSize(20);
        labels.setHorizontalAxisTitle("Last Hour");
        labels.setVerticalAxisTitle("Blood Glucose (mg/dl)");
    }


    static
    {
        UIHandler = new Handler(Looper.getMainLooper());
    }
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    public static void graph (final double data [], final int alertVal){
       runOnUI(new Runnable() {
            public void run() {

        DataPoint displayvals [] = new DataPoint[12];
        for(int i = 0; i < 12; i++){
            displayvals[i] = new DataPoint(i * 5, data[i]);
        }

//        DataPoint lowBound[] = new DataPoint[12];
//        for(int i =0; i < 12; i++)
//            lowBound[i] = new DataPoint(i*5, LOW);
//
//        DataPoint highBound[] = new DataPoint[12];
//        for(int i =0; i < 12; i++)
//            highBound[i] = new DataPoint(i*5, HIGH);

//        LineGraphSeries<DataPoint> high = new LineGraphSeries<DataPoint>(highBound);
//        LineGraphSeries<DataPoint> low = new LineGraphSeries<DataPoint>(lowBound);

        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(displayvals);

        graph.removeAllSeries();
        graph.addSeries(series);
//        graph.addSeries(low);
//        graph.addSeries(high);

        if(alertVal == 1)
            graph.getViewport().setBackgroundColor(Color.RED);
        else if (alertVal == -1){
            graph.getViewport().setBackgroundColor(Color.YELLOW);
        }
        else {
            graph.getViewport().setBackgroundColor(Color.GREEN);
        }
            }
       });
    }

}
