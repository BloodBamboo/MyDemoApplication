package com.example.admin.myapplication.RxjavaSimple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class RxActivity extends AppCompatActivity {
    private static final String TAG = "RxActivity";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "subscribe 事件发射==" + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onComplete();
                emitter.onError(new Throwable(""));
            }
        }).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) {
                return integer + "aaaa";
            }
        })
                .subscribeOn()
                .observerOn()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe 成功==" + d.isDisposable() +"======"+ Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext===" + s + "==" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError=====" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete=====" + Thread.currentThread().getName());
                    }
                });

    }
}
