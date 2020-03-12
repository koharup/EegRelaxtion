package com.sano.picchi.muse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseManagerAndroid;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.sano.picchi.muse.helper.MuseConnectionHelper;
import com.sano.picchi.muse.helper.ShowDataHelper;

import java.util.List;

import static com.sano.picchi.muse.constants.AppConstants.SVM_MODEL_FN;


public class ShowMuseDataActivity extends Activity implements ShowDataHelper.ShowDataLister, MuseConnectionHelper.MuseConnectionLister {

    Handler handler = new Handler();
    MuseConnectionHelper museConnectionHelper;
    MuseManagerAndroid manager;
    ShowDataHelper sh;

    TextView museDataTextView;
    TextView museStatusTextView;
    TextView hsi1;
    TextView hsi2;
    TextView hsi3;
    TextView hsi4;

    int graphXValue = 0;
    LineGraphSeries<DataPoint> mSeries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eeg_realtime_classifier);
        museDataTextView = findViewById(R.id.muse_data);

        manager = MuseManagerAndroid.getInstance();
        manager.setContext(this);
        sh = new ShowDataHelper(this, SVM_MODEL_FN, this);
        museConnectionHelper = new MuseConnectionHelper(sh, this);

        Intent intent = getIntent();
        int position = intent.getIntExtra("pos", 0);
        List<Muse> availableMuse = manager.getMuses();
        connectToMuse(availableMuse.get(position));

        initView();
    }

    private void initView() {
        hsi1 = findViewById(R.id.hsi1);
        hsi2 = findViewById(R.id.hsi2);
        hsi3 = findViewById(R.id.hsi3);
        hsi4 = findViewById(R.id.hsi4);

        // Set up Graph
        GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(1);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(45);
        mSeries = new LineGraphSeries<>();
        graph.addSeries(mSeries);
        museStatusTextView = findViewById(R.id.tv_muse_status);
        handler.post(sh.processEEG);
    }

    private void connectToMuse(Muse muse) {
        museConnectionHelper.setMuse(muse);
        museConnectionHelper.connect_to_muse();
    }

    @Override
    public void onGetData(double progress) {
        mSeries.appendData(new DataPoint(graphXValue++, progress), true, 45);
        museDataTextView.setText(String.valueOf(progress));
    }

    @Override
    public void onGetMuseData(double[] hsiBuffer) {
        hsi1.setText(String.valueOf(hsiBuffer[0]));
        hsi2.setText(String.valueOf(hsiBuffer[1]));
        hsi3.setText(String.valueOf(hsiBuffer[2]));
        hsi4.setText(String.valueOf(hsiBuffer[3]));
    }

    @Override
    public void onGetMuseStatu(String museStatus) {
        museStatusTextView.setText(museStatus);
    }
}
