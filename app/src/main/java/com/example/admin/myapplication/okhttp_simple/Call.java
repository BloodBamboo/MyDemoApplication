package com.example.admin.myapplication.okhttp_simple;

import com.example.admin.myapplication.okhttp_simple.chain.CallServiceInterceptor;
import com.example.admin.myapplication.okhttp_simple.chain.ConnectionInterceptor;
import com.example.admin.myapplication.okhttp_simple.chain.HeaderInterceptor;
import com.example.admin.myapplication.okhttp_simple.chain.Interceptor;
import com.example.admin.myapplication.okhttp_simple.chain.InterceptorChain;
import com.example.admin.myapplication.okhttp_simple.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Call {

    private Request request;
    private HttpClient httpClient;
    //是否执行过
    boolean executed;
    //是否取消
    private boolean canceled;

    public Call(Request request, HttpClient httpClient) {
        this.request = request;
        this.httpClient = httpClient;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void canceled() {
        this.canceled = true;
    }

    public Request request() {
        return request;
    }

    public void enqueue(Callback callback) {
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("已经执行过了。");
            }
            executed = true;
        }
        httpClient.dispatcher().enqueue(new AsyncCall(callback, request, httpClient));
    }

    public HttpClient client() {
        return httpClient;
    }

    /**
     * 执行网络请求的线程
     */
    class AsyncCall implements Runnable {

        private Callback callback;
        private Request request;
        private HttpClient client;

        public AsyncCall(Callback callback, Request request, HttpClient client) {
            this.callback = callback;
            this.request = request;
            this.client = client;
        }

        @Override
        public void run() {
            //信号 是否回调过
            boolean signalledCallbacked = false;
            try {
                Response response = getResponse();
                if (canceled) {
                    signalledCallbacked = true;
                    callback.onFailure(Call.this, new IOException("Canceled"));
                } else {
                    signalledCallbacked = true;
                    callback.onResponse(Call.this, response);
                }
            } catch (Exception e) {
                if (!signalledCallbacked) {
                    callback.onFailure(Call.this, e);
                }
            } finally {
                //将这个任务从调度器移除
                client.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.url().host;
        }
    }

    private Response getResponse() throws IOException {
        List<Interceptor> interceptors = new ArrayList<>();
        //重试拦截器
        interceptors.add(new RetryInterceptor());
        //请求头拦截器
        interceptors.add(new HeaderInterceptor());
        //连接拦截器
        interceptors.add(new ConnectionInterceptor());
        //通信拦截器
        interceptors.add(new CallServiceInterceptor());
        InterceptorChain chain = new InterceptorChain(interceptors, 0, this, null);
        return chain.process();
    }
}
