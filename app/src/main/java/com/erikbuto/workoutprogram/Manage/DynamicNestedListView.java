package com.erikbuto.workoutprogram.Manage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

/**
 * Created by Utilisateur on 17/07/2015.
 */
public class DynamicNestedListView extends DynamicListView implements NestedScrollingChild {

    public DynamicNestedListView(@NonNull Context context){
        super(context);
    }

    public DynamicNestedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicNestedListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private final NestedScrollingChildHelper scrollingChildHelper =
            new NestedScrollingChildHelper(this);


    public void setNestedScrollingEnabled(boolean enabled) {
        scrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return scrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return scrollingChildHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        scrollingChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return scrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {

        return scrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return scrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return scrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }
}
