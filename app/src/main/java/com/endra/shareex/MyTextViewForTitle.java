package com.endra.shareex;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Endra on 10/7/2016.
 */
public class MyTextViewForTitle extends TextView {
    public MyTextViewForTitle(Context context) {
        super(context);
        init();
    }

    public MyTextViewForTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewForTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
            Typeface tf=Typeface.createFromAsset(getContext().getAssets(),"fonts/nevis.ttf");
            setTypeface(tf);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyTextViewForTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
}
