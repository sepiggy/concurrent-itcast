package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 可见性
 * 被 volatile 修饰的变量每次必须到主存中获取值，而不是从线程自己的工作内存中获取值
 * 使用 volatile 关键字保证了共享变量的可见性
 * volatile 只能修饰成员变量或静态成员变量
 */
@Slf4j(topic = "c.Test32")
public class Test32 {

    // 易变
    volatile static boolean run = true;

    public static void main(String[] args) throws InterruptedException {

        Thread t = new Thread(() -> {
            while (true) {
                if (!run) {
                    break;
                }
            }
        });
        t.start();

        sleep(1);
        log.debug("停止t");
        run = false; // 线程t不会如预想的停下来
    }

}
