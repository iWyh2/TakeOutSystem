package com.wyh.TakeOut.utils;

/**
 * @description 用于封装TheadLocal类
 * 以一个线程为单独的作用域 在该线程内设置变量 可以存可以取 只在线程内有效
 * 线程之间为线程隔离的 不会互相干扰 每一个线程访问都会是一个独立的作用域

 * 而每次客户端的一次Http请求都会是一次独立的线程
 * 设置的变量是线程内共享的 线程外是不可访问的
 */

public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
