package com.example.admin.myapplication.RxjavaSimple.ObservableOption;

import com.example.admin.myapplication.RxjavaSimple.Disposable;
import com.example.admin.myapplication.RxjavaSimple.Observable;
import com.example.admin.myapplication.RxjavaSimple.ObservableEmitter;
import com.example.admin.myapplication.RxjavaSimple.ObservableOnSubscribe;
import com.example.admin.myapplication.RxjavaSimple.Observer;


public class ObservableCreate<T> extends Observable<T> {
    ObservableOnSubscribe source;

    public ObservableCreate(ObservableOnSubscribe source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        CreateEmitter emitter = new CreateEmitter(observer);
        observer.onSubscribe(emitter);
        try {
            source.subscribe(emitter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static public class CreateEmitter<T> extends ObservableEmitter<T> implements Disposable {

        private boolean disposable = false;

        private Observer observer;

        public CreateEmitter(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T value) {
            if (disposable) {
                return;
            }
            observer.onNext(value);
        }

        @Override
        public void onError(Throwable throwable) {
            if (disposable) {
                return;
            }
            observer.onError(throwable);
        }

        @Override
        public void onComplete() {
            if (disposable) {
                return;
            }
            observer.onComplete();
        }

        @Override
        public boolean isDisposable() {
            return disposable;
        }

        @Override
        public void dispos() {
            disposable = true;
        }
    }
}
