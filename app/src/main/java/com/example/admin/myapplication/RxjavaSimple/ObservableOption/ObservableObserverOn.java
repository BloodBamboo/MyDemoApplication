package com.example.admin.myapplication.RxjavaSimple.ObservableOption;

import android.os.Handler;
import android.os.Looper;

import com.example.admin.myapplication.RxjavaSimple.AbstractObservableWithUpstream;
import com.example.admin.myapplication.RxjavaSimple.Disposable;
import com.example.admin.myapplication.RxjavaSimple.Observer;
import com.example.admin.myapplication.RxjavaSimple.ObserverableSource;


public class ObservableObserverOn<T> extends AbstractObservableWithUpstream<T, T> {


    public ObservableObserverOn(ObserverableSource<T> source) {
        super(source);
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        ObserverObserverOn s = new ObserverObserverOn(observer);
        observer.onSubscribe(s);
        source.subscribe(s);
    }

    static public class ObserverObserverOn<T> implements Observer<T>, Disposable {
        Observer observer;
        Handler handler;
        Disposable disposable;

        public ObserverObserverOn(Observer<? super T> observer) {
            handler = new Handler(Looper.getMainLooper());
            this.disposable = new Disposable() {
                @Override
                public boolean isDisposable() {
                    return true;
                }

                @Override
                public void dispos() {

                }
            };
            this.observer = observer;
        }

        @Override
        public boolean isDisposable() {
            return disposable.isDisposable();
        }

        @Override
        public void dispos() {
            disposable.dispos();
        }

        @Override
        public void onSubscribe(Disposable d) {
            disposable = d;
        }

        @Override
        public void onNext(T t) {
            handler.post(() -> {
                observer.onNext(t);
            });
        }

        @Override
        public void onError(Throwable e) {
            handler.post(() -> {
                observer.onError(e);
            });
        }

        @Override
        public void onComplete() {
            handler.post(() -> {
                observer.onComplete();
            });
        }
    }
}
