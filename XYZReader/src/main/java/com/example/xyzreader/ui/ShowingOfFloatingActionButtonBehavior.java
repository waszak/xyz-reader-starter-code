package com.example.xyzreader.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * https://guides.codepath.com/android/Floating-Action-Buttons
 * https://stackoverflow.com/questions/41761736/android-design-library-25-1-0-causes-floatingactionbutton-behavior-to-stop-worki/42082313#42082313
 */

public class ShowingOfFloatingActionButtonBehavior extends FloatingActionButton.Behavior {

    public ShowingOfFloatingActionButtonBehavior(){

    }
    public ShowingOfFloatingActionButtonBehavior(Context context, AttributeSet attributeSet){
        super();
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        axes,type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.setVisibility(View.INVISIBLE);
            //since 25.1.0 CoordinatorLayout is skipping views set to GONE
            //child.hide() wouldn't work anymore
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }
}
