package com.example.petproject.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private boolean scrollable;

    public CustomViewPager(Context context) {
        super(context);
        scrollable = false;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scrollable = false;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return scrollable && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return scrollable && super.onInterceptTouchEvent(event);
    }
}
