package com.example.admin.myapplication.okhttp_simple;

public interface Callback {
    void onFailure(Call call, Throwable throwable);

    void onResponse(Call call, Response response);
}
