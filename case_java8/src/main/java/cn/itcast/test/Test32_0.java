package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 使用 synchronized 也可以实现共享变量的可见性
 */
@Slf4j(topic = "c.Test32_0")
public class Test32_0 {

    final static Object lock = new Object();
    static boolean run = true;

    public static void main(String[] args) {

        Thread t = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (!run) {
                        break;
                    }
                }
            }
        });
        t.start();

        sleep(1);
        log.debug("停止 t");

        synchronized (lock) {
            run = false;
        }
    }

}
