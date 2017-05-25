package com.example.admin.myapplication.fragment;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.bean.User;
import com.example.admin.myapplication.databinding.fragment.FragmentDataBinding;
import com.example.admin.myapplication.handler.EventHandler;


/**
 * Created by Administrator on 2017/5/4.
 */

public class DataBindingFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_binding, container, false);
        User user = new User("Connor", "Lin");
        user.age = 20;
        binding.setUser(user);
        binding.setHandler(new EventHandler());
        return binding.getRoot();
    }

    @BindingAdapter({"font"})
    public static void setFont(TextView textView, int size) {
        textView.setTextSize(size);
        textView.setTextColor(Color.GREEN);
    }
}
