package com.example.admin.myapplication.RxjavaSimple.ObservableOption;

import com.example.admin.myapplication.RxjavaSimple.AbstractObservableWithUpstream;
import com.example.admin.myapplication.RxjavaSimple.BasicFuseableObserver;
import com.example.admin.myapplication.RxjavaSimple.Disposable;
import com.example.admin.myapplication.RxjavaSimple.Function;
import com.example.admin.myapplication.RxjavaSimple.Observable;
import com.example.admin.myapplication.RxjavaSimple.Observer;

public class ObservableMap<in, out> extends AbstractObservableWithUpstream<in, out> {

    final Function<? super in, ? extends out> function;

    public ObservableMap(Observable<in> source, Function<in, out> function) {
        super(source);
        this.function = function;
    }


    @Override
    protected void subscribeActual(Observer<? super out> observer) {
        MapObserver map  = new MapObserver(observer, function);
        source.subscribe(map);
    }


    static public class MapObserver<in, out> extends BasicFuseableObserver<in, out> implements Disposable {
        final Function<? super in, ? extends out> function;


        public MapObserver(Observer actual, Function<? super in, ? extends out> function) {
            super(actual);
            this.function = function;
        }


        @Override
        public void onNext(in i) {
            actual.onNext(function.apply(i));
        }
    }
}
