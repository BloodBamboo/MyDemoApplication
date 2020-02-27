package com.example.admin.myapplication.RxjavaSimple;

import com.example.admin.myapplication.RxjavaSimple.ObservableOption.ObservableCreate;
import com.example.admin.myapplication.RxjavaSimple.ObservableOption.ObservableMap;
import com.example.admin.myapplication.RxjavaSimple.ObservableOption.ObservableObserverOn;
import com.example.admin.myapplication.RxjavaSimple.ObservableOption.ObservableSubscribeOn;

public abstract class Observable<T> implements ObserverableSource<T> {

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);

    public static <T> Observable<T> create(ObservableOnSubscribe<T> observableOnSubscribe) {
        return new ObservableCreate(observableOnSubscribe);
    }

    public <T, out> Observable<out> map(Function<T, out> function) {
        return new ObservableMap(this, function);
    }

    public Observable<T> subscribeOn() {
        return new ObservableSubscribeOn(this);
    }

    public Observable<T> observerOn() {
        return new ObservableObserverOn(this);
    }
}
