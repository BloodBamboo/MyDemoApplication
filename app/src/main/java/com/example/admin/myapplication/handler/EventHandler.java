package com.example.admin.myapplication.handler;

import android.content.Context;
import android.widget.Toast;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.bean.User;


/**
 * Created by Administrator on 2017/5/2.
 */

public class EventHandler {
    public String getText(int age) {
        return "dsfasdfsdafa 按文档发生的发生  :" + age;
    }

    public int getColor(Context context) {
        return context.getResources().getColor(R.color.colorAccent);
    }

    public String loadString(Context context) {
        // 使用生成的context变量
        return context.getResources().getString(R.string.string_from_context);
    }

    public void upData(User user) {
        user.setFirstName("阿打算的");
        user.setLastName("adsfsadfas");
        user.setAge(25);
    }

    public void onClick(Context context, User user) {
        Toast.makeText(context, user.firstName, Toast.LENGTH_LONG).show();
    }
}
