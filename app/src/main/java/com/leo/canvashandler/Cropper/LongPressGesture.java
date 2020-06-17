package com.leo.canvashandler.Cropper;

import android.view.GestureDetector;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

public class LongPressGesture extends GestureDetector.SimpleOnGestureListener {

    LongPressListener listener;

    LongPressGesture(LongPressListener listener){
        this.listener = listener;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        List<Float> coOr = new ArrayList<>();
        coOr.add(e.getX());
        coOr.add(e.getY());
        listener.OnLongPress(coOr);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
}
