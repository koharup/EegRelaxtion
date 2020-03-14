package com.sano.picchi.muse;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;

import com.choosemuse.libmuse.MuseManagerAndroid;


public class ImageActivity extends Activity{
    MuseManagerAndroid manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        manager = MuseManagerAndroid.getInstance();
        manager.setContext(this);

    }


    public void start(View v){
        Intent intent = new Intent(this, ConnectMuseActivity.class);
        startActivity(intent);
    }
}