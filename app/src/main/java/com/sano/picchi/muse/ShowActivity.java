package com.sano.picchi.muse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseManagerAndroid;
import com.sano.picchi.muse.helper.MuseConnectionHelper;
import com.sano.picchi.muse.helper.ShowDataHelper;

import java.util.ArrayList;
import java.util.List;

import static com.sano.picchi.muse.constants.AppConstants.SVM_MODEL_FN;


public class ShowActivity extends Activity implements ShowDataHelper.ShowDataLister, MuseConnectionHelper.MuseConnectionLister {


    int count = 0;
    int second = 15;
    CountDownTimer countDownTimer;
    ImageView showImage;
    ImageView flowerImage;
    TextView countText;
    ConstraintLayout showBack;
    ArrayList<Uri> imageURLList;

    MuseConnectionHelper museConnectionHelper;
    MuseManagerAndroid manager;
    ShowDataHelper sh;

    int devicePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent intent = getIntent();
        imageURLList = intent.getParcelableArrayListExtra("imageURLList");
        devicePosition = intent.getIntExtra("pos", 0);
        setUpCounDown();
        Log.d("memo", "setCOUnt");
        countDownTimer.start();
        showImage = findViewById(R.id.showImage);
        countText = findViewById(R.id.countText);
        showImage.setImageResource(R.drawable.flower_w_co3);
        museInit();
    }

    public void museInit(){
        manager = MuseManagerAndroid.getInstance();
        manager.setContext(this);
        sh = new ShowDataHelper(this, SVM_MODEL_FN, this);
        museConnectionHelper = new MuseConnectionHelper(sh, this);
        List<Muse> availableMuse = manager.getMuses();
        connectToMuse(availableMuse.get(devicePosition));
    }

    private void connectToMuse(Muse muse) {
        museConnectionHelper.setMuse(muse);
        museConnectionHelper.connectTomMuse();
    }


    public void setUpCounDown() {
        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long l) {
                second = second - 1;
                Log.d("memo", String.valueOf(second));
                countText.setText(String.valueOf(second));
            }
            @Override
            public void onFinish() {
                second = 15;
                repeatIfNeeded();
            }
        };
    }

    public void repeatIfNeeded() {

        //枚数分にする
        if (count <= 4) {
            //写真を変える処理をかく
            countDownTimer.cancel();
            countDownTimer.start();
            Log.d("Test", String.valueOf(count));
            count += 1;
        } else if (count == 5) {
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
            countDownTimer.cancel();
            Log.d("Test", String.valueOf(count));
            finish();
        }

        switch (count) {
            case 0:
                showImage.setImageResource(R.drawable.flower_w_co3);
                break;
            case 1:
                showImage.setImageURI(imageURLList.get(0));
                break;
            case 2:
                showImage.setImageURI(imageURLList.get(1));
                break;
            case 3:
                showImage.setImageURI(imageURLList.get(2));
                break;
            case 4:
                showImage.setImageURI(imageURLList.get(3));
                break;
            case 5:
                showImage.setImageURI(imageURLList.get(4));
                break;
        }
    }

    @Override
    public void onGetMuseRawData(double[] hsiBuffer) {
        Log.d("onGetMuseRawData",String.valueOf(hsiBuffer[0]));
        Log.d("onGetMuseRawData",String.valueOf(hsiBuffer[1]));
        Log.d("onGetMuseRawData",String.valueOf(hsiBuffer[2]));
        Log.d("onGetMuseRawData",String.valueOf(hsiBuffer[3]));

    }

    @Override
    public void onChangeMuseStatu(String museStatus) {
        //Log.d("onGetMuseRawData",String.valueOf(museStatus));

    }

    @Override
    public void onGetData(double museData) {
        Log.d("onGetData",String.valueOf(museData));
        flowerImage = findViewById(R.id.flowerImage);

        //0.6以上(@@未満)
        if (museData >= 0.50){
            flowerImage.setImageResource(R.drawable.flower_r_0);
            Log.d("onGetDatadata","0");

        }else if(museData >= 0.45 && museData < 0.50){
            flowerImage.setImageResource(R.drawable.flower_r_1);
            Log.d("onGetDatadata","1");

        }else if(museData >= 0.40 && museData < 0.45){
            flowerImage.setImageResource(R.drawable.flower_r_2);
            Log.d("onGetDatadata","2");

        }else if(museData >= 0.35 && museData < 0.40){
            flowerImage.setImageResource(R.drawable.flower_r_3);
            Log.d("onGetDatadata","3");

        }else if (museData >= 0.30 && museData < 0.35){
            flowerImage.setImageResource(R.drawable.flower_r_4);
            Log.d("onGetDatadata","4");

        }else if(museData >= 0.25 && museData < 0.30){
            flowerImage.setImageResource(R.drawable.flower_r_5);
            Log.d("onGetDatadata","5");

        }else if (museData >= 0.20 && museData < 0.25){
            flowerImage.setImageResource(R.drawable.flower_r_6);
            Log.d("onGetDatadata","6");

        }else if (museData >= 0.15 && museData < 0.20){
            flowerImage.setImageResource(R.drawable.flower_r_7);
            Log.d("onGetDatadata","7");

        }else if (museData >= 0.10 && museData < 0.15){
            flowerImage.setImageResource(R.drawable.flower_r_8);
            Log.d("onGetDatadata","8");

        }else if (museData >= 0.05 && museData < 0.10){
            flowerImage.setImageResource(R.drawable.flower_r_9);
            Log.d("onGetDatadata","9");


        }else if(museData < 0.05){
            flowerImage.setImageResource(R.drawable.flower_r_10);
            Log.d("onGetDatadata","10");



        }





    }
}