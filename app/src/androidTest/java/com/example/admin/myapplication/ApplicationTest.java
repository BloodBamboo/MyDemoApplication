package com.example.admin.myapplication;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * <e href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</e>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

    }
}