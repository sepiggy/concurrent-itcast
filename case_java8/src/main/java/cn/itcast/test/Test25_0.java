package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 控制线程的先后执行次序
 */
@Slf4j(topic = "c.Test25_0")
public class Test25_0 {

    static final Object lock = new Object(); // 锁对象最好用 final 修饰
    static boolean isT1Runned;

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                log.debug("1");
                isT1Runned = true;
                lock.notifyAll();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock) {
                while (!isT1Runned) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("2");
            }
        }, "t2");

        t1.start();
        t2.start();
    }

}
