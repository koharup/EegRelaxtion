package com.sano.picchi.muse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseListener;
import com.choosemuse.libmuse.MuseManagerAndroid;

import java.util.List;

public class ConnectMuseActivity extends Activity {

    String TAG = "ActivityConnectMuse";

    MuseManagerAndroid manager;

    private ArrayAdapter<String> listviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_muse);

        manager = MuseManagerAndroid.getInstance();
        manager.setContext(this);
        manager.setMuseListener(new MuseListener() {
            @Override
            public void museListChanged() {
                final List<Muse> list = manager.getMuses();
                listviewAdapter.clear();
                for (Muse m : list) {
                    listviewAdapter.add(m.getName() + " - " + m.getMacAddress());
                }
            }
        });
        manager.stopListening();
        manager.startListening();

        requestPermission();
        initUI();
    }

    private void initUI() {
        Button refresh = findViewById(R.id.start);
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listviewAdapter.clear();
                manager.stopListening();
                manager.startListening();
            }
        });

        ListView lv = findViewById(R.id.museList);
        listviewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(listviewAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                manager.stopListening(); // Stop listening
                List<Muse> availableMuse = manager.getMuses();
                if (availableMuse.size() < 1) {
                    Log.d(TAG, "No available muse to connect to!");
                } else {
                    Log.d(TAG, "pos:" + position);
                    Intent intent = new Intent(getApplicationContext(), ImageListActivity.class);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        manager.stopListening();
        finish();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // We don't have the ACCESS_COARSE_LOCATION permission so create the dialogs asking
            // the user to grant us the permission.
            DialogInterface.OnClickListener buttonListener =
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityCompat.requestPermissions(ConnectMuseActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    0);
                        }
                    };
            AlertDialog introDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_dialog_title)
                    .setMessage(R.string.permission_dialog_description)
                    .setPositiveButton(R.string.permission_dialog_understand, buttonListener)
                    .create();
            introDialog.show();
        }
    }
}
