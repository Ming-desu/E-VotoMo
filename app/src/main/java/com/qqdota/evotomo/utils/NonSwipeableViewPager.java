package com.qqdota.evotomo.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class NonSwipeableViewPager extends ViewPager {
    private boolean isEnabled;

    public NonSwipeableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnabled)
            return super.onTouchEvent(ev);
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isEnabled)
            return super.onInterceptTouchEvent(ev);
        return false;
    }

    public void setEnabled(boolean value) {
        isEnabled = value;
    }
}
