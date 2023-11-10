package cl.smapdev.curimapu.clases.utilidades;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
        private boolean swipeEnabled;

        public CustomViewPager(Context context) {
            super(context);
            this.swipeEnabled = true;
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.swipeEnabled = true;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            // If swipeEnabled is false, return false to disable swiping.
            return swipeEnabled && super.onInterceptTouchEvent(ev);
        }

        public void setSwipeEnabled(boolean swipeEnabled) {
            this.swipeEnabled = swipeEnabled;
        }
    }

