package com.example.admin.myapplication.anime;

/**
 * Created by admin on 2017/2/6.
 */

public class PathPoint {

    public static int MOVE = 0;
    public static int LINE = 1;
    public static int CURVE = 2;

    private int mType;
    private float mX, mY;
    private float mControl0X, mControl0Y, mControl1X, mControl1Y;

    public PathPoint(float x, float y) {
        mX = x;
        mY = y;
    }

    public PathPoint(int type, float x, float y) {
        mType = type;
        mX = x;
        mY = y;
    }

    public PathPoint(int type, float x, float y, float control0X, float control0Y, float control1X, float control1Y) {
        mType = type;
        mControl0X = control0X;
        mControl0Y = control0Y;
        mControl1X = control1X;
        mControl1Y = control1Y;
        mX = x;
        mY = y;
    }

    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(MOVE, x, y);
    }

    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(LINE, x, y);
    }

    public static PathPoint curveTo(float x, float y, float mControl0X, float mControl0Y, float mControl1X, float mControl1Y) {
        return new PathPoint(CURVE, x, y, mControl0X, mControl0Y, mControl1X, mControl1Y);
    }

    public int getType() {
        return mType;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public float getControl0X() {
        return mControl0X;
    }

    public float getControl0Y() {
        return mControl0Y;
    }

    public float getControl1X() {
        return mControl1X;
    }

    public float getControl1Y() {
        return mControl1Y;
    }
}
