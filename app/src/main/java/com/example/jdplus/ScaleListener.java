package com.example.jdplus;

import android.content.Context;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private ImageView imageView;
    Context context;
    private float scale=1f;

    public ScaleListener(Context context, ImageView imageView){
        this.context =context;
        this.imageView =imageView;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        scale *= detector.getScaleFactor();
        if (scale<1f) {
            imageView.setScaleX(1);
            imageView.setScaleY(1);
        }else {
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
        }


        return true;
    }
}
