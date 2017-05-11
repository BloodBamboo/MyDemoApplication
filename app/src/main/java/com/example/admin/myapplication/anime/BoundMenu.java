package com.example.admin.myapplication.anime;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by admin on 2017/2/9.
 */

public class BoundMenu {
    public interface BoundMenuListener {
        void showContent();
        void downContent();
    }

    private ListAnimationView background;
    private ViewGroup mContentView;
    private View mMenuView;

    public BoundMenu makeMenu(View view, final View menuView, final BoundMenuListener listener) {
        if (findContentView(view)) {
            background = new ListAnimationView(view.getContext());
            background.setLisetner(new ListAnimationView.ListAnimationViewListener() {
                @Override
                public void showContent() {
                    listener.showContent();
                }

                @Override
                public void downContent() {
                    listener.downContent();
                    mContentView.removeView(background);
                    mContentView.removeView(mMenuView);
                }

                @Override
                public void downContentAnimator() {
//                    ValueAnimator animator = ValueAnimator.ofInt(0 , menuView.getHeight());
//                    animator.setDuration(ListAnimationView.down_time);
//                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                        @Override
//                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                            menuView.setTranslationY((int) valueAnimator.getAnimatedValue());
//                        }
//                    });
                    ObjectAnimator animator = ObjectAnimator.ofFloat(menuView, "translationY",  0, menuView.getHeight());
                    animator.setDuration(ListAnimationView.down_time - 100);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            menuView.setVisibility(View.GONE);
                            menuView.setTranslationY(0);
                        }
                    });

                    animator.start();
                }
            });
            this.mMenuView = menuView;
        }

        return this;
    }

    public BoundMenu show() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mContentView.getHeight() / 2);
        lp.gravity = Gravity.BOTTOM;
        mContentView.addView(background, lp);
        FrameLayout.LayoutParams lp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mContentView.getHeight() / 2 - background.MaxHeightRadian);
        lp1.gravity = Gravity.BOTTOM;
        mContentView.addView(mMenuView, lp1);
        background.show();
        return this;
    }

    public void down() {
        background.down();
    }

    private boolean findContentView(View view) {
        do {
            view = (ViewGroup) view.getParent();
            if (FrameLayout.class.isInstance(view) && view.getId() == android.R.id.content) {
                mContentView = (ViewGroup) view;
                return true;
            }
        } while (view != null);
        return  false;
    }



}
