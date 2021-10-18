package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test17")
public class Test17_1 {

    static int counter = 0;

    // synchronized 关键字要配合多个线程共享的对象来实现加锁操作
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                // 可以理解为这个加锁操作是线程完成的
                synchronized (lock) {
                    counter++;
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                // 可以理解为这个加锁操作是线程完成的
                synchronized (lock) {
                    counter--;
                }
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("{}", counter);
    }

}
