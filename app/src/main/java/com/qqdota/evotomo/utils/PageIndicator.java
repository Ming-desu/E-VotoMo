package com.qqdota.evotomo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.qqdota.evotomo.R;

/**
 * The page indicator class
 */
public class PageIndicator extends LinearLayout {
    private Context context;
    private TextView[] pageIndicators;
    private int pageCount;
    private int page = 0;

    public PageIndicator(Context context) {
        super(context);
        this.context = context;
    }

    public PageIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        applyStyle(context, attrs);
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        if (pageCount == 0)
            return;
        this.pageCount = pageCount;
        setupPages();
        invalidate();
        requestLayout();
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page < 1 || page > getPageCount())
            return;
        this.page = page;
        changePage(page);
    }

    private void applyStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PageIndicator,
                0, 0
        );

        try {
            setPageCount(typedArray.getInteger(R.styleable.PageIndicator_pageCount, 0));
            setPage(typedArray.getInteger(R.styleable.PageIndicator_currentPage, 0));
        }
        finally {
            typedArray.recycle();
        }

    }

    /**
     * The method that will generate the icons
     */
    private void setupPages() {
        pageIndicators = new TextView[getPageCount()];
        for (int i = 0; i < getPageCount(); i++) {
            pageIndicators[i] = new TextView(context);
            pageIndicators[i].setLayoutParams(setPageLayoutParams(i));

            Drawable drawable = context.getApplicationContext().getResources().getDrawable(R.drawable.page_indicator, null);

            pageIndicators[i].setBackground(drawable);
            pageIndicators[i].setEnabled(false);

            addView(pageIndicators[i]);
        }
    }

    /**
     * The method that will change the page
     * @param page
     */
    private void changePage(int page) {
        for (int i = 0; i < getPageCount(); i++) {
            if (i == page - 1)
                pageIndicators[i].setEnabled(true);
            else
                pageIndicators[i].setEnabled(false);
            pageIndicators[i].invalidate();
            pageIndicators[i].requestLayout();
        }
    }

    /**
     * The method that will set up the layout params
     * @param i
     * @return
     */
    private LayoutParams setPageLayoutParams(int i) {
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        int px = dpToPixel(4);

        if (i == 0)
            layoutParams.setMarginEnd(px);
        else if (i == getPageCount() - 1)
            layoutParams.setMarginStart(px);
        else
            layoutParams.setMargins(px, 0, px, 0);

        return layoutParams;
    }

    /**
     * The method that will convert dp to px
     * @param dp
     * @return
     */
    private int dpToPixel(int dp) {
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );

        return px;
    }
}
