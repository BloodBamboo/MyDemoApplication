package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.view.ui.RevealDrawable;
import com.example.admin.myapplication.view.ui.TrashCanView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/22.
 */

public class RevealSearchViewActivity extends AppCompatActivity {

    RevealDrawable rd;
    Subscription subscribe;
    Thread thread;
    boolean isCancel = false;
    int level = 0;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.trash_can_view)
    TrashCanView trash_can_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_search);
        ButterKnife.bind(this);
        rd = new RevealDrawable(
                getResources().getDrawable(R.mipmap.avft),
                getResources().getDrawable(R.mipmap.avft_active),
                RevealDrawable.HORIZONTAL);
        image.setImageDrawable(rd);
        image.setImageLevel(level);
        image.setOnClickListener((v) -> {
				level += 1000;
                if (level > 10000) {
                    level = 0;
                }
                image.setImageLevel(level);
		});
    }

    @OnClick(R.id.button1)
    public void onClcik1() {
        isCancel = false;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!isCancel) {
                        Thread.sleep(20);
                        if (level > 10000) {
                            level = 0;
                        } else {
                            level += 10;
                        }
                        if (image != null) {
                            image.post(new Runnable() {
                                @Override
                                public void run() {
                                    image.setImageLevel(level);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        thread.start();

//        subscribe = Observable.interval(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
//                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        if (level > 10000) {
//                            level = 0;
//                        }
//                        image.setImageLevel(level++);
//                    }
//                });
    }

    @OnClick(R.id.button2)
    public void onClcik2() {
        if (subscribe != null && !subscribe.isUnsubscribed()) {
            subscribe.unsubscribe();
        }

        if (thread != null) {
            isCancel = true;
        }
    }

    @OnClick(R.id.button3)
    public void onClcik3() {
        trash_can_view.startAnim();
    }
}
