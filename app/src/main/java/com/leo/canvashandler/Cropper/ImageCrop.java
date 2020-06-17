package com.leo.canvashandler.Cropper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

import java.util.ArrayList;
import java.util.List;

public class ImageCrop extends androidx.appcompat.widget.AppCompatImageView implements LongPressListener {

    private Bitmap bitmap;
    private Paint paint;
    private Context context;
    private List<List<Float>> coOrdinates;
    private Paint circlePaint;
    private Paint linePaint;
    private GestureDetectorCompat gestureDetectorCompat;
    private LongPressGesture longPressGesture;
    private Integer changePosition;
    private List<Float> tempPosition;
    private int x;
    private int canvasWidth, canvasHeight;
    private int CIRCLE_RADIUS = 20;
    private boolean isCropped;
    private Paint paintErase;
    private Path cropPath;

    public ImageCrop(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ImageCrop(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCrop(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ImageCrop(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);
        init();
    }

    private void init() {
        longPressGesture = new LongPressGesture(this);
        gestureDetectorCompat = new GestureDetectorCompat(context, longPressGesture);
        changePosition = null;
        tempPosition = new ArrayList<>();
        paint = new Paint();
        coOrdinates = new ArrayList<>();

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.WHITE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(5);
        isCropped = false;

        paintErase = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintErase.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        cropPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth = w;
        canvasHeight = h;
        bitmap = Bitmap.createScaledBitmap(bitmap, canvasWidth, canvasHeight, false);
        List<Float> point1= new ArrayList<>();
        List<Float> point2= new ArrayList<>();
        List<Float> point3= new ArrayList<>();
        point1.add((float)(w/2));
        point1.add((float)(h/4));
        point2.add((float)(w/4));
        point2.add((float)(h/2));
        point3.add((float)(w-w/4));
        point3.add((float)(h/2));
        coOrdinates.add(point1);
        coOrdinates.add(point2);
        coOrdinates.add(point3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isCropped) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
            for (List<Float> coOr : coOrdinates) {
                canvas.drawCircle(coOr.get(0), coOr.get(1), CIRCLE_RADIUS, circlePaint);
            }
            for (x = 0; x < coOrdinates.size(); x++) {
                if (x < (coOrdinates.size() - 1)) {
                    canvas.drawLine(coOrdinates.get(x).get(0), coOrdinates.get(x).get(1), coOrdinates.get(x + 1).get(0), coOrdinates.get(x + 1).get(1), linePaint);
                } else {
                    canvas.drawLine(coOrdinates.get(x).get(0), coOrdinates.get(x).get(1), coOrdinates.get(0).get(0), coOrdinates.get(0).get(1), linePaint);
                }
            }
        }else {
            cropPath.moveTo(coOrdinates.get(0).get(0),coOrdinates.get(0).get(1));
            for(int i=1; i<coOrdinates.size();i++){
                cropPath.lineTo(coOrdinates.get(i).get(0),coOrdinates.get(i).get(1));
            }
            canvas.drawPath(cropPath,paint);
            canvas.drawBitmap(bitmap, 0, 0, paintErase);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (changePosition != null) {
                tempPosition = new ArrayList<>();
                tempPosition.add(event.getX() <= 0 ? 0 : event.getX());
                tempPosition.add(event.getY() <= 0 ? 0 : event.getY());
                coOrdinates.set(changePosition, tempPosition);
                Log.e("temp", "" + tempPosition);
                postInvalidate();
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isCircleTouched(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            changePosition = null;
        }
        return true;
    }

    private void isCircleTouched(Float cx, Float cy) {
        for (int i = 0; i < coOrdinates.size(); i++) {
            if (((coOrdinates.get(i).get(0) - CIRCLE_RADIUS) < cx) && ((coOrdinates.get(i).get(0) + CIRCLE_RADIUS) > cx)) {
                if (((coOrdinates.get(i).get(1) - CIRCLE_RADIUS) < cy) && ((coOrdinates.get(i).get(1) + CIRCLE_RADIUS) > cy)) {
                    changePosition = i;
                    return;
                }
            }
        }
        changePosition = null;
    }

    private boolean isCircleTouched(List<Float> coOrdinate) {
        for (List<Float> coOr : coOrdinates) {
            if (((coOr.get(0) - 15) < coOrdinate.get(0)) && ((coOr.get(0) + 15) > coOrdinate.get(0))) {
                if (((coOr.get(1) - 15) < coOrdinate.get(1)) && ((coOr.get(1) + 15) > coOrdinate.get(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setBitmap(@NonNull Bitmap bitmap) {
        isCropped = false;
        coOrdinates = new ArrayList<>();
        this.bitmap = bitmap;
    }

    @Override
    public void OnLongPress(List<Float> coOr) {
        Log.e("LONG_TEST", "" + coOr);
        if (!isCircleTouched(coOr)) {
            addToCoOrdinates(coOr);
            postInvalidate();
        }
    }

    private void addToCoOrdinates(List<Float> coOr) {
        int finIndex = 0;
        float minDifference = 0f;
        for (int i = 0; i < coOrdinates.size(); i++) {
            if (i == 0) {
                minDifference = Math.abs((coOrdinates.get(i).get(0) - coOr.get(0))) +
                        Math.abs((coOrdinates.get(i).get(1) - coOr.get(1)));
            }
            if (Math.abs((coOrdinates.get(i).get(0) - coOr.get(0))) +
                    Math.abs((coOrdinates.get(i).get(1) - coOr.get(1))) < minDifference) {
                minDifference = Math.abs((coOrdinates.get(i).get(0) - coOr.get(0))) +
                        Math.abs((coOrdinates.get(i).get(1) - coOr.get(1)));
                finIndex = i;
            }
        }
        if (coOrdinates.size() > 2) {
            int suitableIndex = getSuitableIndex(finIndex, coOr);
            if (suitableIndex == 0 || suitableIndex == coOrdinates.size() - 1) {
                coOrdinates.add(coOr);
            } else {
                coOrdinates.add(suitableIndex, coOr);
            }
        } else {
            coOrdinates.add(coOr);
        }
    }

    private int getNextIndex(int currentIndex) {
        if (currentIndex == (coOrdinates.size() - 1)) {
            return 0;
        } else {
            return currentIndex + 1;
        }
    }

    private int getPreviousIndex(int currentIndex) {
        if (currentIndex == 0) {
            if (coOrdinates.size() > 0) {
                return coOrdinates.size() - 1;
            } else {
                return 0;
            }
        } else {
            return currentIndex - 1;
        }
    }

    private int getSuitableIndex(int currentIndex, List<Float> newPoint) {
        if (calculateDistance(coOrdinates.get(getNextIndex(currentIndex)), newPoint) <=
                calculateDistance(coOrdinates.get(getNextIndex(currentIndex)), coOrdinates.get(currentIndex))) {
            return getNextIndex(currentIndex);
        } else if (calculateDistance(coOrdinates.get(getPreviousIndex(currentIndex)), newPoint) <=
                calculateDistance(coOrdinates.get(getPreviousIndex(currentIndex)), coOrdinates.get(currentIndex))) {
            return currentIndex;
        } else {
            return currentIndex;
        }
    }

    private double calculateDistance(List<Float> start, List<Float> end) {
        return Math.sqrt(((int) (start.get(0) - end.get(0)) ^ 2) + ((int) (start.get(1) - end.get(1)) ^ 2));
    }

    public Bitmap getBitmap() {
        isCropped = true;
        invalidate();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
}