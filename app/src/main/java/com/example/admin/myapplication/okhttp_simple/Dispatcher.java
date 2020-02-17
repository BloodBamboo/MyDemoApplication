package com.example.admin.myapplication.okhttp_simple;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Dispatcher {
    //同时进行的最大请求数
    private int maxRequests = 64;
    //同时请求的相同的host的最大
    private int maxRequestsPreHost = 5;

    //等待执行队列
    private Deque<Call.AsyncCall> readyAsyncCalls = new ArrayDeque<>();
    //正在执行队列
    private Deque<Call.AsyncCall> runningAsyncCalls = new ArrayDeque<>();
    //线程池
    private ExecutorService executorService;

    public Dispatcher() {
        this(64, 5);
    }

    public Dispatcher(int maxRequests, int maxRequestsPreHost) {
        this.maxRequests = maxRequests;
        this.maxRequestsPreHost = maxRequestsPreHost;
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            //线程工厂 创建线程
            ThreadFactory threadFactory = new ThreadFactory() {
                @Override
                public Thread newThread(@NonNull Runnable runnable) {
                    return new Thread(runnable, "Http Client");
                }
            };
            executorService = new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory
            );
        }
      return executorService;
    }

    public void enqueue(Call.AsyncCall asyncCall) {
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(asyncCall) < maxRequestsPreHost) {
            runningAsyncCalls.add(asyncCall);
            executorService().execute(asyncCall);
        } else {
            readyAsyncCalls.add(asyncCall);
        }
    }

    private int runningCallsForHost(Call.AsyncCall asyncCall) {
        int i = 0;
        for (Call.AsyncCall call : runningAsyncCalls) {
            if (call.host().equals(asyncCall.host())) {
                i++;
            }
        }
        return i;
    }

    public void finished(Call.AsyncCall asyncCall) {
        synchronized (this) {
            //将其从runningAsync移除
            runningAsyncCalls.remove(asyncCall);
            //检查是否可以运行ready
            checkReady();
        }
    }


    private void checkReady() {
        //达到了同时请求最大数
        if (runningAsyncCalls.size() >= maxRequests) {
            return;
        }
        //没有执行的任务
        if (readyAsyncCalls.isEmpty()) {
            return;
        }
        Iterator<Call.AsyncCall> iterator = readyAsyncCalls.iterator();
        while (iterator.hasNext()) {
            //获得一个等待执行的任务
            Call.AsyncCall asyncCall = iterator.next();
            //如果获得的等待执行的任务 执行后 小于host相同最大允许数 就可以去执行
            if (runningCallsForHost(asyncCall) < maxRequestsPreHost) {
                iterator.remove();
                runningAsyncCalls.add(asyncCall);
                executorService().execute(asyncCall);
            }
            //如果正在执行的任务达到了最大
            if (runningAsyncCalls.size() >= maxRequests){
                return;
            }
        }
    }

}
