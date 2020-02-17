package com.example.admin.myapplication.okhttp_simple.chain;

import android.util.Log;

import com.example.admin.myapplication.okhttp_simple.HttpClient;
import com.example.admin.myapplication.okhttp_simple.HttpConnection;
import com.example.admin.myapplication.okhttp_simple.HttpUrl;
import com.example.admin.myapplication.okhttp_simple.Request;
import com.example.admin.myapplication.okhttp_simple.Response;

import java.io.IOException;

public class ConnectionInterceptor implements Interceptor {
    @Override
    public Response intercept(InterceptorChain chain) throws IOException {
        Log.e("interceptor", "获取连接拦截器");
        Request request = chain.call.request();
        HttpClient client = chain.call.client();
        HttpUrl url = request.url();
        //从连接池中获得连接
        HttpConnection connection = client.connectionPool().get(url.getHost(), url.getPort());
        //没有可复用的连接
        if (null == connection) {
            connection = new HttpConnection();
        } else {
            Log.e("interceptor", "从连接池中获得连接");
        }

        connection.setRequest(request);
        //执行下一个拦截器
        try {
            chain.connection = connection;
            Response response = chain.process();
            if (response.isKeepAlive()) {
                client.connectionPool().put(connection);
            }else{
                connection.close();
            }
            return response;
        } catch (IOException e) {
            connection.close();
            throw e;
        }
    }
}
