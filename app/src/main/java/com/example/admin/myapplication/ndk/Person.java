package com.example.admin.myapplication.ndk;

import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class Person {

    public Student student;

    public void setStudent(Student student) {
        this.student = student;
        Log.i(TAG, "setStudent: 赋值成功"+student.toString());
    }
}
