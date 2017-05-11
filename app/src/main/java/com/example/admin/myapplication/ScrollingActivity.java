package com.example.admin.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScrollingActivity extends AppCompatActivity {

    public interface Html {
        @GET("article_{id}/index.html")
        Observable<ResponseBody> getHtml(@Path("id") String id);

        @GET("article_{id}/index.html")
        Call<ResponseBody> getHtml2(@Path("id") String id);
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://m.edu-edu.com.cn/edu/editor/_detail/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private WebView web_view;
    String id = "246";

    String url = "http://m.edu-edu.com.cn/edu/editor/_detail/article_249/index.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        web_view = (WebView) findViewById(R.id.web_view);

        web_view.getSettings().setJavaScriptEnabled(true);
        web_view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //创建okHttpClient对象
//        final OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request

//        web_view.loadUrl(url);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mOkHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.e("=====", response.toString());
//                        byte[] b = response.body().bytes(); //获取数据的bytes
//                        final String info = new String(b, "utf-8"); //
//
//                        web_view.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                web_view.loadDataWithBaseURL("http://m.edu-edu.com.cn/", info,"text/html","utf-8",null);
//                            }
//                        });
//
//                    }
//
//                });
//            }
//        }).start();


//        Html html = retrofit.create(Html.class);
//        Call<ResponseBody>  call = html.getHtml2(id);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                Log.e("=====",call.request().url().toString());
//                try {
//                    web_view.loadDataWithBaseURL("http://m.edu-edu.com.cn/", response.body().string(), "text/html", "utf-8", null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//                Log.e("*******",t.getMessage());
//            }
//        });

//        Call<ResponseBody>  call = html.getHtml2(id);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
////                    System.out.println(response.body().string());
//                    web_view.loadDataWithBaseURL("http://m.edu-edu.com.cn/", response.body().string(),"text/html","utf-8",null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });


        final Html html = retrofit.create(Html.class);
        html.getHtml(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("*******", e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody respone) {
                        try {
                            web_view.loadDataWithBaseURL("http://m.edu-edu.com.cn/", respone.string(), "text/html", "utf-8", null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


}
