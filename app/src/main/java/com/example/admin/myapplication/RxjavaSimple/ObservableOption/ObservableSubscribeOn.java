package com.example.admin.myapplication.RxjavaSimple.ObservableOption;

import com.example.admin.myapplication.RxjavaSimple.Disposable;
import com.example.admin.myapplication.RxjavaSimple.Observable;
import com.example.admin.myapplication.RxjavaSimple.Observer;

public class ObservableSubscribeOn<T> extends Observable<T> {

    Observable source;


    public ObservableSubscribeOn(Observable<T> observable) {
        source = observable;
    }

    @Override
    protected void subscribeActual(Observer observer) {
        SubscribeOnObserver s = new SubscribeOnObserver(observer);
        observer.onSubscribe(s);
        new Thread(() -> {
            source.subscribe(s);
        }).start();
    }

    static public class SubscribeOnObserver<T> implements Observer<T>, Disposable {
        private Disposable dis;
        private Observer observer;
        public SubscribeOnObserver(Observer observer) {
            this.observer = observer;
        }

        @Override
        public boolean isDisposable() {
            return dis.isDisposable();
        }

        @Override
        public void dispos() {
            dis.dispos();
        }

        @Override
        public void onSubscribe(Disposable d) {
            dis = d;
        }

        @Override
        public void onNext(T o) {
            observer.onNext(o);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
