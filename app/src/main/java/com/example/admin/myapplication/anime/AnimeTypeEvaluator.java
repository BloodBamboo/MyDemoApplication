package com.example.admin.myapplication.anime;

import android.animation.TypeEvaluator;

/**
 * Created by admin on 2017/2/6.
 */

public class AnimeTypeEvaluator implements TypeEvaluator<PathPoint> {
    @Override
    public PathPoint evaluate(float v, PathPoint start, PathPoint end) {
        float x = 0, y = 0;

        if (end.getType() == PathPoint.LINE) {
            x = start.getX() + v * (end.getX() - start.getX());
            y = start.getY() + v * (end.getY() - start.getY());
        } else if (end.getType() == PathPoint.CURVE) {
            float oneMiinusT = 1 - v;
            x = oneMiinusT * oneMiinusT * oneMiinusT * start.getX() +
                    3 * oneMiinusT * oneMiinusT * v * end.getControl0X() +
                    3 * oneMiinusT * v * v * end.getControl1X() +
                    v * v * v * end.getX();
            y = oneMiinusT * oneMiinusT * oneMiinusT * start.getY() +
                    3 * oneMiinusT * oneMiinusT * v * end.getControl0Y() +
                    3 * oneMiinusT * v * v * end.getControl1Y() +
                    v * v * v * end.getY();
        } else {
            x = end.getX();
            y = end.getY();
        }

        return new PathPoint(x, y);
    }
}
