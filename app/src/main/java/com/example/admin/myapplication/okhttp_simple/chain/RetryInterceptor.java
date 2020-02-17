package com.example.admin.myapplication.okhttp_simple.chain;

import android.util.Log;

import com.example.admin.myapplication.okhttp_simple.Call;
import com.example.admin.myapplication.okhttp_simple.HttpClient;
import com.example.admin.myapplication.okhttp_simple.Response;

import java.io.IOException;

public class RetryInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.e("拦截器", "重试拦截器.....");
        IOException exception = null;
        Call call = chain.call;
        HttpClient client = call.client();

        for (int i = 0; i < client.retrys() + 1; i++) {
            //如果取消了
            if (call.isCanceled()) {
                throw new IOException("Canceled");
            }
            try {
                return chain.process();
            } catch (IOException e) {
                exception = e;
            }
        }
        throw exception;
    }
}
