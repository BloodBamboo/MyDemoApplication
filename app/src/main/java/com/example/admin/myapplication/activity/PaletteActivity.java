package com.example.admin.myapplication.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SweepGradient;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.example.admin.myapplication.R;
import com.example.admin.myapplication.view.ui.PaletteRelativeLayout;

/**
 * Created by 12 on 2017/7/9.
 */

public class PaletteActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_main);
        PaletteRelativeLayout paletteRelativeLayout = (PaletteRelativeLayout) findViewById(R.id.bg);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_two);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                //拿到鲜艳的颜色
                Palette.Swatch vibreant = palette.getVibrantSwatch();
                if (vibreant == null) {
                    for (Palette.Swatch swatch : palette.getSwatches()) {
                        vibreant = swatch;
                        break;
                    }
                }
//                GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
//                v.setBackground(bg);
                paletteRelativeLayout.setBgColor(vibreant.getRgb());
            }
        });

//        SweepGradient mSweepGradient = new SweepGradient(300, 300, mColors, null);
    }


}
