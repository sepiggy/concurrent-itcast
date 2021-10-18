package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test18")
public class Test18 {

    static final Object lock = new Object();

    public static void main(String[] args) {

        synchronized (lock) {
            try {
                // 只有获得锁的线程，进入对象的Owner区，才能调用Object#wait方法，否则没有资格
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
