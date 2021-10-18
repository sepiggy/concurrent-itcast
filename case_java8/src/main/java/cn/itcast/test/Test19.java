package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test19")
public class Test19 {

    // 作为锁的对象最好使用final来修饰，避免锁对象被修改
    static final Object lock = new Object();

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (lock) {
                log.debug("获得锁");
                try {
//                    Thread.sleep(20000);
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1").start();

        Sleeper.sleep(1);
        synchronized (lock) {
            log.debug("获得锁");
        }
    }

}
