package com.example.admin.myapplication.activity.old;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.appthemeengine.Config;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.bean.User;
import com.example.admin.myapplication.handler.EventHandler;

import ccom.example.admin.myapplication.databinding.DataBindingBinding;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/2.
 */

public class DataBindingActivity extends AppCompatActivity {

    public interface Html {
        @GET("baidu.com")
        Observable<ResponseBody> getIpInfo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ActivityBaseBinding 类是自动生成的
        DataBindingBinding binding = DataBindingUtil.setContentView(this, R.layout.data_binding);
        binding.setHandler(new EventHandler());
        ProgressBar progressBar = binding.progressBar;
        progressBar.setProgressDrawable(new ColorDrawable(Config.accentColor(this, null)));
        // 所有的 set 方法也是根据布局中 variable 名称生成的
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        Html html = retrofit.create(Html.class);
        html.getIpInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("*******", e.getMessage());
                        progressBar.setVisibility(View.GONE);
                        User user = new User("王", "明");
                        binding.setUser(user);
                    }

                    @Override
                    public void onNext(ResponseBody respone) {
                        progressBar.setVisibility(View.GONE);
                        User user = new User("Connor", "Lin");
                        binding.setUser(user);
                    }
                });
    }
}
