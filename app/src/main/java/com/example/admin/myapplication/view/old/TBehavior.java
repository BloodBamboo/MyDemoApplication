package com.example.admin.myapplication.view.old;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class TBehavior extends CoordinatorLayout.Behavior<RelativeLayout>  {


    public TBehavior() {
    }

    public TBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RelativeLayout child, View dependency) {
//        float translationY = getFabTranslationYForSnackbar(parent, dependency);
//        float percentComplete = -translationY / dependency.getHeight();

        float heifht = dependency.getHeight();
        float t1 = dependency.getTop();
        child.setAlpha(-t1 / heifht);
        Log.e("======aaa=", -t1 + "");
        return false;
    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                View fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab);
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof AppBarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
            }
        }

        return minOffset;
    }
}
