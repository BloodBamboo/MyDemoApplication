package com.example.admin.myapplication.RxjavaSimple;


public interface ObservableOnSubscribe<T> {

    //为每个订阅的观察者调用
    void subscribe(ObservableEmitter<T> emitter) throws Exception;
}
