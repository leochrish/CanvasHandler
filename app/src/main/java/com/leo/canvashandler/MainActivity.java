package com.leo.canvashandler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.leo.canvashandler.Cropper.CropperActivity;
import com.leo.canvashandler.Cropper.ImageCrop;
import com.leo.canvashandler.Cropper.PolygonCrop;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivityForResult(new Intent(this, CropperActivity.class),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == PolygonCrop.CROPPER_RESULT_OK){
                Log.e("test",""+data.getStringExtra("image"));
            }
        }
    }
}
