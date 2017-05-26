package com.example.admin.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.view.ui.SearchView;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/5/26.
 */

public class SearchViewActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.search_view)
    public void onClick(SearchView searchView) {
        searchView.start();
    }
}
