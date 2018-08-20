package com.example.admin.myapplication;

import android.widget.Button;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println(System.currentTimeMillis());
        System.out.println("longToDate："+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(815602624)));//1529823960000L
    }
}