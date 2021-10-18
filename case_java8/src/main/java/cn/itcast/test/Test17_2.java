package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用面向对象的思想进行封装
 */
@Slf4j(topic = "c.Test17")
public class Test17_2 {

    public static void main(String[] args) throws InterruptedException {

        Room1 room = new Room1();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increntmentBy1();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrementBy1();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        log.debug("{}", room.getCounter());
    }

}

// 对共享资源的保护由封装类实现
class Room1 {

    private int counter = 0;

    public void increntmentBy1() {
        // 用封装对象作为锁对象
        synchronized (this) {
            counter++;
        }
    }

    public void decrementBy1() {

        synchronized (this) {
            counter--;
        }
    }

    public int getCounter() {
        // 为了避免获取到中间结果，获取值的时候也要加锁
        synchronized (this) {
            return counter;
        }
    }

}
