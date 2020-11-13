package com.example.testwatermark;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.EditText;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    EditText upTestVideoName;
    EditText upTestImageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            upTestVideoName = findViewById(R.id.videoName);
        upTestImageName = findViewById(R.id.imageName);
        loadVideo();
        loadWaterMark();

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    124);
        } else {
            int rc = FFmpeg.execute("ffmpeg -i" +  upTestVideoName.getText() + "-i" +
                    upTestImageName.getText() + " test1.mp4");
        }
    }

    private void loadWaterMark() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 123);
    }

    private void loadVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 124);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 124 || requestCode == 123) {
                // Get the Video from data
                Uri selectedVideo = data.getData();

                Cursor cursor = getContentResolver().query(selectedVideo,null,null,null);
                if (cursor != null){
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    cursor.moveToFirst();
                    if (requestCode == 123){
                        upTestImageName.setText(cursor.getString(nameIndex));
                    }else {
                        upTestVideoName.setText(cursor.getString(nameIndex));
                    }
                    cursor.close();
                }
            }
        }
    }

}