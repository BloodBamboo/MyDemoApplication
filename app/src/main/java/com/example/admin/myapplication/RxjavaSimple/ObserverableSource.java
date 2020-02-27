package com.example.admin.myapplication.RxjavaSimple;

public interface ObserverableSource<T> {
    //订阅抽象方法
    void subscribe(Observer<? super T> observer);
}
