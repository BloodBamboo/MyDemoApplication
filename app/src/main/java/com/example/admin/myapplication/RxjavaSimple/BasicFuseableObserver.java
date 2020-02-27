package com.example.admin.myapplication.RxjavaSimple;


//观察者
public abstract class BasicFuseableObserver<T, R> implements Observer<T>, Disposable {
    //观察者
    protected final Observer<? super R> actual;

    protected Disposable disposeble;
    boolean dispos = false;

    public BasicFuseableObserver(Observer<? super R> actual) {
        this.actual = actual;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposeble = d;
        actual.onSubscribe(d);
    }

    @Override
    public void onError(Throwable e) {
        actual.onError(e);
    }

    @Override
    public void onComplete() {
        actual.onComplete();
    }

    @Override
    public void dispos() {
        dispos = true;
    }

    @Override
    public boolean isDisposable() {
        return dispos;
    }
}
