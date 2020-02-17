package com.example.admin.myapplication.okhttp_simple.chain;



import com.example.admin.myapplication.okhttp_simple.Response;

import java.io.IOException;

/**
 * @author Lance
 * @date 2018/4/17
 */

public interface Interceptor {

    Response intercept(InterceptorChain chain) throws IOException;
}
