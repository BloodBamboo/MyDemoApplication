package com.example.admin.myapplication.activity.old;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.ATEActivity;
import com.afollestad.appthemeengine.Config;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.example.admin.myapplication.R;

/**
 * Created by Administrator on 2017/5/2.
 */

@SuppressLint("NewApi")
public class SettingsActivity extends ATEActivity
        implements ColorChooserDialog.ColorCallback {


    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        final Config config = ATE.config(this, getATEKey());
        switch (dialog.getTitle()) {
            case R.string.primary_color:
                config.primaryColor(selectedColor);
                break;
            case R.string.accent_color:
                config.accentColor(selectedColor);
                // We've overridden the navigation view selected colors in the default config,
                // which means we are responsible for keeping those colors up to date.
                config.navigationViewSelectedIcon(selectedColor);
                config.navigationViewSelectedText(selectedColor);
                break;
            case R.string.primary_text_color:
                config.textColorPrimary(selectedColor);
                break;
            case R.string.secondary_text_color:
                config.textColorSecondary(selectedColor);
                break;
        }
        config.commit();
        recreate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        Button button4 = (Button) findViewById(R.id.button);
        button4.setOnClickListener((v) -> {
            new ColorChooserDialog.Builder(SettingsActivity.this, R.string.accent_color)
                    .preselect(Config.accentColor(SettingsActivity.this, null))
                    .show();
        });

    }
}
