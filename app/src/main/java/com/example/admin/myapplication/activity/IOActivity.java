package com.example.admin.myapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.AppUtile;
import com.example.admin.myapplication.Utils.ToastUtil;
import com.example.admin.myapplication.bean.Article;
import com.example.admin.myapplication.bean.ElementData;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
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
    List<ElementData> articleList = new ArrayList<>();
    String content = "{'channelNum':1,'channelType':'T','endTime':0,'list':[{'time':1529249340,'value':26.381199},{'time':1529249345,'value':26.396938},{'time':1529249350,'value':26.36546},{'time':1529249355,'value':26.34972},{'time':1529249360,'value':26.34972},{'time':1529249365,'value':26.35759},{'time':1529249370,'value':26.35759},{'time':1529249375,'value':26.37333},{'time':1529249380,'value':26.396938},{'time':1529249385,'value':26.34185},{'time':1529249390,'value':26.36546},{'time':1529249395,'value':26.33398},{'time':1529249400,'value':26.37333},{'time':1529249405,'value':26.381199},{'time':1529249410,'value':26.32611},{'time':1529249415,'value':26.35759},{'time':1529249420,'value':26.34972},{'time':1529249425,'value':26.33398},{'time':1529249430,'value':26.34972},{'time':1529249435,'value':26.36546},{'time':1529249440,'value':26.35759},{'time':1529249445,'value':26.37333},{'time':1529249450,'value':26.34972},{'time':1529249455,'value':26.33398},{'time':1529249460,'value':26.34972},{'time':1529249465,'value':26.33398},{'time':1529249470,'value':26.34185},{'time':1529249475,'value':26.33398},{'time':1529249480,'value':26.33398},{'time':1529249485,'value':26.318241},{'time':1529249490,'value':26.36546},{'time':1529249495,'value':26.33398},{'time':1529249500,'value':26.34185},{'time':1529249505,'value':26.32611},{'time':1529249510,'value':26.34972},{'time':1529249515,'value':26.32611},{'time':1529249520,'value':26.34972},{'time':1529249525,'value':26.310371},{'time':1529249530,'value':26.32611},{'time':1529249535,'value':26.34185},{'time':1529249410,'value':26.32611},{'time':1529249415,'value':26.35759},{'time':1529249420,'value':26.34972},{'time':1529249425,'value':26.33398},{'time':1529249430,'value':26.34972},{'time':1529249435,'value':26.36546},{'time':1529249440,'value':26.35759},{'time':1529249445,'value':26.37333},{'time':1529249450,'value':26.34972},{'time':1529249455,'value':26.33398},{'time':1529249460,'value':26.34972},{'time':1529249465,'value':26.33398}],'maxv':0.0,'minv':0.0,'mode':0,'size':0,'startTime':0,'type':0}";
    File file;
    Charset charset = Charset.forName("ISO-8859-1");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        ButterKnife.bind(this);
        path = AppUtile.getInnerSDCardPath() + path;
        for (int i = 0; i < 1000; i++) {
            articleList.add(new ElementData());
        }
//        ActivityCompat.requestPermissions
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
        }
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
//            dos.write(content.getBytes());
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
//                    .writeString(content, charset)
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
        Observable.create((Subscriber<? super ElementData> subscriber) -> {
            ElementData data = null;
            try {
                file = new File(AppUtile.getInnerSDCardPath(), "1529249340_807b#T1.txt");
                if (!file.exists()) {
                    ToastUtil.showShort(IOActivity.this, "文件不存在" + file.getAbsolutePath());
                    return;
                }
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                //String content = Okio.buffer(Okio.source(file)).readString(charset);
                data = JSON.parseObject(content, new TypeReference<ElementData>() {});
//                list = JSON.parseArray(content, Article.class);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("----", "写入失败-=-----");
            }
            subscriber.onNext(data);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(data -> {
            if (data != null) {
                text.setText(data.toString());
                articleList.add(data);
            }
        });
    }

    @OnClick(R.id.button3)
    public void fileCopy(){
        String oldFilePath = path + fileName;
        String newFilePath = path + "/copy.txt";
        //如果原文件不存在
        File old = new File(oldFilePath);
        if (!old.exists()) {
            return ;
        }
        BufferedSource bufferedSource = null;
        BufferedSink bufferedSink = null;
        try {
            bufferedSource = Okio.buffer(Okio.source(old));
            bufferedSink = Okio.buffer(Okio.sink(new File(newFilePath)));
//            Okio.buffer(Okio.sink(new File(newFilePath))).writeAll(Okio.buffer(Okio.source(old)));
            bufferedSink.writeAll(bufferedSource);
            ToastUtil.showShort(IOActivity.this, "拷贝完成 ------");
            text.setText(newFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedSource != null) {
                    bufferedSource.close();
                }
                if (bufferedSink != null) {
                    bufferedSink.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.button4)
    public void onButton4(){
        try {
            file = new File(path,fileName);
            Okio.buffer(Okio.sink(file)).writeString("hello butter", charset).close();
            file = new File(path,fileName2);
            //用BufferedSink读文件
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(file));
            bufferedSink.buffer().writeAll(Okio.source(new File(path,fileName)));
            String s = bufferedSink.buffer().readString(charset);//读出之后buffer里没有数据所以md5结果不正确
            text.setText(s +"- md5:" + Okio.buffer(Okio.source(file)).buffer().md5().toString());
            bufferedSink.close();
            ToastUtil.showShort(IOActivity.this, "写入完成 ------" + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showShort(IOActivity.this, "写入失败-=-----" + path + fileName);
        }
    }
}
