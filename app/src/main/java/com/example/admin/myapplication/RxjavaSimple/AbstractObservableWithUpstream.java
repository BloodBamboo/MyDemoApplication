package com.example.admin.myapplication.RxjavaSimple;


//被观察者
public abstract class AbstractObservableWithUpstream<in, out> extends Observable<out> {
    protected final ObserverableSource<in> source;

    public AbstractObservableWithUpstream(ObserverableSource<in> source) {
        this.source = source;
    }
}
