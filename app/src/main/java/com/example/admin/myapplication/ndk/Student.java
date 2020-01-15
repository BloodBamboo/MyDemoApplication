package com.example.admin.myapplication.ndk;

import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class Student {
    private String name;
    private int age;
    private String des;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
        Log.i(TAG, "Student: 被C++初始化" + toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", des='" + des + '\'' +
                '}';
    }
}
