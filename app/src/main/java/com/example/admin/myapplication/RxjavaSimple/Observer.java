package com.example.admin.myapplication.RxjavaSimple;

public interface Observer<T> {

    void onSubscribe(Disposable d);

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
