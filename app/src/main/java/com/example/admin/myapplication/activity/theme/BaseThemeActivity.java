package com.example.admin.myapplication.activity.theme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.SharedPreferencesHelper;

/**
 * Created by 12 on 2017/10/12.
 */

public class BaseThemeActivity extends AppCompatActivity {

    public static final String THEME = "theme";
    public static final int theme1 = 1;
    public static final int theme2 = 2;
//    public static final int theme3= 3;

    protected Toolbar toolbar;
    private int theme = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SharedPreferencesHelper(this);
        switch ((int) SharedPreferencesHelper.get(THEME, theme1)) {
            case theme1:
                theme = theme1;
                setTheme(R.style.AppLightTheme);
                SharedPreferencesHelper.put(THEME, theme1);
                break;
            case theme2:
                theme = theme2;
                setTheme(R.style.AppDarkTheme);
                SharedPreferencesHelper.put(THEME, theme2);
                break;
            default:
                theme = theme1;
                setTheme(R.style.AppLightTheme);
                SharedPreferencesHelper.put(THEME, theme1);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (theme != (int)SharedPreferencesHelper.get(THEME, theme)) {
            recreate();
        }
    }

    protected void initToolBar() {
        toolbar.inflateMenu(R.menu.menu_scrolling);
        toolbar.setOnMenuItemClickListener((MenuItem item) -> {
            switch (item.getItemId()) {
                case R.id.action_theme1:
                    setTheme(R.style.AppLightTheme);
                    SharedPreferencesHelper.put(THEME, theme1);
                    recreate();
                    return true;
                case R.id.action_theme2:
                    setTheme(R.style.AppDarkTheme);
                    SharedPreferencesHelper.put(THEME, theme2);
                    recreate();
                    return true;
            }
            return false;
        });
    }
}
