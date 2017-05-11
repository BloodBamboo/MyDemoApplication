package com.example.admin.myapplication.anime;

import java.util.LinkedList;

/**
 * Created by admin on 2017/2/6.
 */

public class AnimationPath {
    private LinkedList<PathPoint> list = new LinkedList<>();


    public AnimationPath moveTo(float x, float y) {
        list.add(PathPoint.moveTo(x, y));
        return this;
    }

    public AnimationPath lineTo(float x, float y) {
        list.add(PathPoint.lineTo(x, y));
        return this;
    }

    public AnimationPath curveTo(float x, float y, float mControl0X, float mControl0Y, float mControl1X, float mControl1Y) {
        list.add(PathPoint.curveTo(x, y, mControl0X, mControl0Y, mControl1X, mControl1Y));
        return this;
    }

    public LinkedList<PathPoint> getList() {
        return list;
    }
}
