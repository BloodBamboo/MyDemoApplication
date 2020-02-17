package com.example.admin.myapplication.okhttp_simple.chain;

import android.util.Log;

import com.example.admin.myapplication.okhttp_simple.Request;
import com.example.admin.myapplication.okhttp_simple.RequestBody;
import com.example.admin.myapplication.okhttp_simple.Response;

import java.io.IOException;
import java.util.Map;

public class HeaderInterceptor implements Interceptor {



    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.e("interceptor", "请求头拦截器");
        Request request = chain.call.request();
        Map<String, String> headers = request.headers();
        //如果使用者没有配置 Connection请求头
        if (!headers.containsKey("Connection")) {
            headers.put("Connection", "Keep-Alive");
        }
        headers.put("Host", request.url().getHost());
        //是否有请求体
        if (null != request.body()) {
            RequestBody body = request.body();
            long contentLength = body.contentLength();
            //请求体长度
            if (contentLength != 0) {
                headers.put("Content-Length",String.valueOf(contentLength));
            }
            String contentType = body.contentType();
            if (null != contentType){
                headers.put("Content-Type",contentType);
            }
        }

        //责任链中的下一个
        return chain.process();
    }
}
