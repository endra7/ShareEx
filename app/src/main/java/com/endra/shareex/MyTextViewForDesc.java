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
public class MyTextViewForDesc extends TextView {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyTextViewForDesc(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public MyTextViewForDesc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyTextViewForDesc(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewForDesc(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface mf=Typeface.createFromAsset(getContext().getAssets(),"fonts/VAG-HandWritten.otf");
        setTypeface(mf);
    }

}
