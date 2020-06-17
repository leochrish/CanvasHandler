package com.leo.canvashandler.Cropper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.leo.canvashandler.R;

import java.util.Objects;

public class CropperActivity extends AppCompatActivity {

    ImageCrop imageCrop;
    Button crop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_cropper);
        imageCrop = findViewById(R.id.sample);
        crop = findViewById(R.id.okay);
        imageCrop.setBitmap(BitmapFactory.decodeResource(getResources(),R.raw.sample));
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bitmapIntent = new Intent();
                bitmapIntent.putExtra("image",imageCrop.getBitmap());
                setResult(PolygonCrop.CROPPER_RESULT_OK,bitmapIntent);
                finish();
            }
        });

    }
}
