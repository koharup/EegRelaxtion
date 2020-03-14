package com.sano.picchi.muse;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageListActivity extends Activity {

    final int REQUEST_CODE_PERMISSION = 100;
    final int REQUEST_CODE_CAMERA = 200;
    final int REQUEST_CODE_PHOTO = 123;

    Uri pictureUri;
    ArrayList<Uri> imageURLList = new ArrayList<>();
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView image4;
    ImageView image5;

    int devicePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3= findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        image5 = findViewById(R.id.image5);
        albums();

        Intent intent = getIntent();
        devicePosition = intent.getIntExtra("pos", 0);

    }

    public void start(View v){
        Intent intent = new Intent(this,ShowActivity.class);
        intent.putParcelableArrayListExtra("imageURLList",imageURLList);
        intent.putExtra("pos", devicePosition);
        startActivity(intent);
    }

    public void albums(){
        Toast.makeText(this, "写真を5枚選択してください", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_PICK);
        this.startActivityForResult(Intent.createChooser(intent, "Choose Photo"), REQUEST_CODE_PHOTO);
    }

    public void camera(View v){
        cameraTask();
    }

    public void cameraTask(){
        Log.d("camera","cameratask");
        checkPermissionList(new ArrayList<String>(Arrays.asList(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)));
        if (checkPermission(Manifest.permission.CAMERA) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            takePicture();
        }
    }

    public void checkPermissionList(ArrayList<String> tergerPermissionArray){
        Log.d("checkpermissionue","checkpermissionue");
        ArrayList<String> chechNeededPermissionList = new ArrayList();

        for (String tergerPermission : tergerPermissionArray) {
            if (!checkPermission(tergerPermission)) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, tergerPermission)) {
                    // すでに１度パーミッションのリクエストが行われていて、
                    // ユーザーに「許可しない（二度と表示しないは非チェック）」をされていると
                    // この処理が呼ばれます。
                    Toast.makeText(this, "パーミッションが許可されていません。設定から許可してください。", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    chechNeededPermissionList.add(tergerPermission);
                }
            }
        }

        if (!chechNeededPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, chechNeededPermissionList.toArray(new String[chechNeededPermissionList.size()]),REQUEST_CODE_PERMISSION);
        }
    }
    public boolean checkPermission(String tergerPermission){
        Log.d("checkpermissionshita","checkpermissionue");
        return ContextCompat.checkSelfPermission(
                this,
                tergerPermission
        ) == PackageManager.PERMISSION_GRANTED;
    }

    public void takePicture(){
        Log.d("takepicture","checkpermissionue");
        String fileName = "${System.currentTimeMillis()}.jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        pictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityresult","onActivityresult");

        if (requestCode == REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK) {
            /*pictureUri = data?.data!!
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, pictureUri)
            Log.d("test",pictureUri.toString())
            imageView4.setImageBitmap(bitmap)*/

            int itemCount = data.getClipData().getItemCount();
            for (int i = 0;i<itemCount;i++){
                Uri uri = data.getClipData().getItemAt(i).getUri();
                imageURLList.add(uri);
            }

            Log.d("datatest",data.toString());
            Log.d("test",imageURLList.toString());
            image1.setImageURI(imageURLList.get(0));
            image2.setImageURI(imageURLList.get(1));
            image3.setImageURI(imageURLList.get(2));
            image4.setImageURI(imageURLList.get(3));
            image5.setImageURI(imageURLList.get(4));
        }

        //カメラを選んだ時に表示するとこ(今回はカメラ使わないからいらない)
        /*if (requestCode == MainActivity.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, pictureUri)
            intent = Intent(this,ShowActivity::class.java)
            imageView4.setImageBitmap(bitmap)
        }*/
    }
}