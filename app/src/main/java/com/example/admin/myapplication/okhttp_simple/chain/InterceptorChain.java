package com.example.admin.myapplication.okhttp_simple.chain;

import com.example.admin.myapplication.okhttp_simple.Call;
import com.example.admin.myapplication.okhttp_simple.HttpConnection;
import com.example.admin.myapplication.okhttp_simple.Response;

import java.io.IOException;
import java.util.List;

public class InterceptorChain {

    List<Interceptor> interceptors;
    int index;
    Call call;
    HttpConnection connection;

    public InterceptorChain(List<Interceptor> interceptors, int index, Call call, HttpConnection connection) {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.connection = connection;
    }

    public Response process() throws IOException {
        if (index >= interceptors.size()) throw new IOException("Interceptor Chain Error");
        //获得拦截器 去执行
        Interceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors, index + 1, call, connection);
        Response response = interceptor.intercept(next);
        return response;
    }
}
