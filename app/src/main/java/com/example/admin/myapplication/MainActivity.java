package com.example.admin.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.admin.myapplication.Utils.ToastUtil;
import com.example.admin.myapplication.activity.AnimationActivity;
import com.example.admin.myapplication.activity.BluetoothActivity;
import com.example.admin.myapplication.activity.CustomViewFinderScannerActivity;
import com.example.admin.myapplication.activity.DrawLayoutActivity;
import com.example.admin.myapplication.activity.IOActivity;
import com.example.admin.myapplication.activity.LeadPagerActivity;
import com.example.admin.myapplication.activity.ListPopAnimationActivity;
import com.example.admin.myapplication.activity.LoveBezierActivity;
import com.example.admin.myapplication.activity.MapActivity;
import com.example.admin.myapplication.activity.PaletteActivity;
import com.example.admin.myapplication.activity.ReceiverActivity;
import com.example.admin.myapplication.activity.RecycleViewActivity;
import com.example.admin.myapplication.activity.RevealSearchViewActivity;
import com.example.admin.myapplication.activity.ScrollingActivity;
import com.example.admin.myapplication.activity.SearchViewActivity;
import com.example.admin.myapplication.activity.ServiceGuardActivity;
import com.example.admin.myapplication.activity.ShaderViewActivity;
import com.example.admin.myapplication.activity.SplashViewActivity;
import com.example.admin.myapplication.activity.TouchEventTransmitActivity;
import com.example.admin.myapplication.activity.TransitionAnimationActivity;
import com.example.admin.myapplication.activity.WaterActivity;
import com.example.admin.myapplication.activity.old.OldScrollingActivity;
import com.example.admin.myapplication.activity.theme.ThemeActivity;
import com.example.admin.myapplication.aop.AspectJAnnotation;
import com.example.admin.myapplication.ndk.NDKActivity;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 2017/2/6.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.nav)
    public NavigationView nav;

    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationInfo info = null;
        try {
            info = this.getPackageManager()
                    .getApplicationInfo(getPackageName(),
                            PackageManager.GET_META_DATA);

            String msg = info.metaData.getString("LAUNCHER");
            if (msg.equals("baidu") && !getIntent().getBooleanExtra("pass", false)) {
                Intent intent = new Intent();
                intent.setClassName(this, "com.example.admin.myapplication.HelloActivity");
                startActivity(intent);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.mainlayout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        switch (getResources().getString(R.string.app_name)) {
            case "baidu":
                getSupportActionBar().setTitle("baidu");
                break;
            case "sina":
                getSupportActionBar().setTitle("sina");
                break;
            default:
                getSupportActionBar().setTitle("main");
        }
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nav.setCheckedItem(R.id.call);
//        new Thread(() -> {
//            handler = new Handler(Looper.getMainLooper()) {
//                @Override
//                public void handleMessage(Message msg) {
//                    ToastUtil.show(MainActivity.this, "handler:" + Thread.currentThread().getName(), Toast.LENGTH_SHORT);
//                }
//            };
//            handler.sendEmptyMessage(0);
//        }).start();

        Thread thread = new Thread(() -> {
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            Log.e("tttttttttt", Thread.currentThread().getName());
                            ToastUtil.show(MainActivity.this, "handler---message", Toast.LENGTH_SHORT);
                            break;
                        case 1:
                            ToastUtil.show(MainActivity.this, "handler---stop", Toast.LENGTH_SHORT);
                            handler.getLooper().quit();
                            break;
                    }
                }
            };
            Looper.loop();
        });
        thread.start();
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribe(next -> {
                    if (handler == null) {
                        Log.e("tttttttttt", "handler is null");
                    }
                    handler.sendEmptyMessage(0);
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @RequiresPermission(Manifest.permission.WRITE_CONTACTS)
    @OnClick(R.id.button_old_demo)
    public void onClickOldDemo() {
        startActivity(new Intent(this, OldScrollingActivity.class));
    }

    @AspectJAnnotation(value = Manifest.permission.CAMERA)
    @OnClick(R.id.button_retrofit)
    public void onClickRetrofit() {
        startActivity(new Intent(this, ScrollingActivity.class));
    }

    @OnClick(R.id.button_animation_path)
    public void onClickAnimationPath() {
        startActivity(new Intent(this, AnimationActivity.class));
    }

    @OnClick(R.id.button_list_pop_animation)
    public void onClickListPopAnimation() {
        startActivity(new Intent(this, ListPopAnimationActivity.class));
    }

    @OnClick(R.id.button_lead_pager)
    public void onClick() {
        startActivity(new Intent(this, LeadPagerActivity.class));
    }

    @OnClick(R.id.button_service_guard)
    public void onClickServiceGuard() {
        startActivity(new Intent(this, ServiceGuardActivity.class));
    }

    @OnClick(R.id.button_view_touch_event_transmit)
    public void onClickTouchTransmit() {
        startActivity(new Intent(this, TouchEventTransmitActivity.class));
    }

    @OnClick(R.id.button_view_Shader_demo)
    public void onClickShader() {
        startActivity(new Intent(this, ShaderViewActivity.class));
    }

    @OnClick(R.id.button_view_reveal_search)
    public void onClickRevealSearch() {
        startActivity(new Intent(this, RevealSearchViewActivity.class));
    }

    @OnClick(R.id.button_view_water)
    public void onClickWater() {
        startActivity(new Intent(this, WaterActivity.class));
    }

    @OnClick(R.id.button_search_view)
    public void onClickSearchView() {
        startActivity(new Intent(this, SearchViewActivity.class));
    }

    @OnClick(R.id.button_transition_anim)
    public void onClickTransitionAnim() {
        startActivity(new Intent(this, TransitionAnimationActivity.class));
    }

    @OnClick(R.id.button_map)
    public void onClickMap() {
        startActivity(new Intent(this, MapActivity.class));
    }

    @OnClick(R.id.button_love_bezier)
    public void onClickLoveBeizer() {
        startActivity(new Intent(this, LoveBezierActivity.class));
    }

    @OnClick(R.id.button_scanner)
    public void onClickScanner() {
        startActivity(new Intent(this, CustomViewFinderScannerActivity.class));
    }
    @OnClick(R.id.button_splash_view)
    public void onClickSplashView() {
        startActivity(new Intent(this, SplashViewActivity.class));
    }

    @OnClick(R.id.button_recycle_view)
    public void onClickRecycleView() {
        startActivity(new Intent(this, RecycleViewActivity.class));
    }

    @OnClick(R.id.button_draw_layout)
    public void onClickDrawLayout() {
        startActivity(new Intent(this, DrawLayoutActivity.class));
    }

    @OnClick(R.id.button_palette)
    public void onClickPalette() {
        startActivity(new Intent(this, PaletteActivity.class));
    }

    @OnClick(R.id.button_ndk)
    public void onClickNDK() {
        startActivity(new Intent(this, NDKActivity.class));
    }

    @OnClick(R.id.button_theme)
    public void onClickTheme() {
        startActivity(new Intent(this, ThemeActivity.class));
    }

    @OnClick(R.id.button_io)
    public void onClickIO() {
        startActivity(new Intent(this, IOActivity.class));
    }
    @OnClick(R.id.button_receiver)
    public void onClickReceiver() {
        startActivity(new Intent(this, ReceiverActivity.class));
    }

    @OnClick(R.id.button_pay)
    public void onClickPay() {
        onPay1();
    }

    @OnClick(R.id.button_send_message)
    public void onClickSendMessage() {
        handler.sendEmptyMessage(0);
    }

    @OnClick(R.id.button_stop_looper)
    public void onClickStopMessage() {
        handler.sendEmptyMessage(1);
    }

    @OnClick(R.id.button_bluetooth)
    public void onClickStopLopper() {
        startActivity(new Intent(this, BluetoothActivity.class));
    }

    public void onPay1() {
        try {
            final String orderInfo = "app_id=2018091761447164&method=alipay.trade.app.pay&charset=utf-8&sign_type=RSA2&timestamp=2018-09-27+17%3A08%3A47&version=1.0&notify_url=http%3A%2F%2Filearning.edu-edu.com.cn%2Falipaytrad%2Fnotify_url.php&biz_content=%7B%22subject%22%3A%22%5Cu8d26%5Cu6237%5Cu8d2d%5Cu8bfe%22%2C%22out_trade_no%22%3A%22101234561234560002102%22%2C%22total_amount%22%3A%221.00%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&sign=hHrAH6vO7YSW3NmtzZcxnqWlBYgoDppplCvO48%2BBVQd7wszWSmPQEgqnVjmfiCGRHyeX%2B5OObVD6%2FuEBcVoRkJ%2F7jmPHa1dgipIQSF3hVZRzJUxkP7h9ukGQKlMSPbYdblimyi4j4TlbPD6B9lMYIvwIomdRVskFisK2ith5E34YVt19map8RyawQwt9ZS1Ax7tvjoXq32dwvHZfTwOqsQNcyctX0nDDLj7ytbX6tm%2BYRVMhITFPK9GYtrIXqVkB6g7Xib4W5esqmBv3xiZ%2B%2BRvMPtG2A%2FTfk%2FbSQ19WUXBc3k%2BOSKXc4YWcYnfKFWeqVZ4MbGO5KECjRhkMPUjAcg%3D%3D";
            Observable.create((ObservableEmitter<Map<String, String>> subscriber) -> {
                PayTask alipay = new PayTask(this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                subscriber.onNext(result);
            }).subscribeOn(Schedulers.newThread())
                    .map(new Function<Map<String, String>, Boolean>() {
                             @Override
                             public Boolean apply(Map<String, String> stringStringMap) throws Exception {
                                 try {
                                     if (!stringStringMap.get("resultStatus").equals("9000")) {
                                         return false;
                                     } else {
                                         return true;
                                     }
                                 } catch (Exception e) {
                                     e.printStackTrace();
                                 }

                                 return false;
                             }
                         }
                    )
                    .onErrorReturn((Throwable throwable) -> {
                        return false;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {

                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
