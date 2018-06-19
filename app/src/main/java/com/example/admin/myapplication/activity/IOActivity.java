package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.AppUtile;
import com.example.admin.myapplication.Utils.ToastUtil;
import com.example.admin.myapplication.bean.Article;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.Okio;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IOActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView text;

    String path = "/demo/";
    String fileName = "demo.txt";
    String fileName2 = "demo2.txt";
    List<Article> articleList = new ArrayList<>();
    File file;
    Charset charset = Charset.forName("ISO-8859-1");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        ButterKnife.bind(this);
        for (int i = 0; i < 10000; i++) {
            articleList.add(new Article());
        }
        path = AppUtile.getInnerSDCardPath() + path;

    }

    @OnClick(R.id.button)
    public void onButton(View v) {
        try {
            file = new File(path, fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            FileOutputStream fos = new FileOutputStream(file);
            OutputStream os = new BufferedOutputStream(fos);
            DataOutputStream dos = new DataOutputStream(os);
            dos.write(JSON.toJSONString(articleList).getBytes());
            dos.flush();
            fos.close();
            ToastUtil.showShort(this, "写入完成 ------" + path + fileName);
            text.setText(file.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(this, "写入失败-=-----" + path + fileName);
        }
    }

    @OnClick(R.id.button1)
    public void onButton1(View v) {
        try {
            file = new File(path, fileName2);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            Okio.buffer(Okio.sink(file))
                    .writeString(JSON.toJSONString(articleList), charset)
                    .close();
            ToastUtil.showShort(IOActivity.this, "写入完成 ------" + file.getAbsolutePath());
            text.setText(file.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(IOActivity.this, "写入失败-=-----" + path + fileName);
        }
    }

    @OnClick(R.id.button2)
    public void onButton2(View v) {
        ToastUtil.showShort(this, "开始读取------");
        Observable.create((Subscriber<? super List<Article>> subscriber) -> {
            List<Article> list = null;
            try {
                file = new File(path, fileName2);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                String content = Okio.buffer(Okio.source(file))
                        .readString(charset);
                list = JSON.parseArray(content, Article.class);

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("----", "写入失败-=-----");
            }
            subscriber.onNext(list);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(list -> {
            if (list != null) {
                ToastUtil.showShort(this, "读取完成 ------" + list.size());
                text.setText(list.get(0).toString());
            }
        });

    }

}
