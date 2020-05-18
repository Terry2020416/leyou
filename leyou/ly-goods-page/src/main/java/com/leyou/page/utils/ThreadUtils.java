package com.leyou.page.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    //Executors用于创建线程池的工具类,Executors.newFixedThreadPool创建可用线程数量的固定线程池
    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void execute(Runnable runnable) {
        //提交一个 Runnable 任务用于执行，并返回一个表示该任务的 Future
        es.submit(runnable);
    }
}