/// ReentrantLock 可打断特性
/// 1. 使用 ReentrantLock#lock 方法加锁不可被打断
/// 2. 使用 ReentrantLock#lockInterruptibly 方法加锁可以被打断
/// 3. 使用 ReentrantLock#lockInterruptibly 提供的可被打断机制可以用来避免死锁

package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 演示 ReentrantLock 可打断特性
 */
@Slf4j(topic = "c.Test22_1")
public class Test22_1 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            try {
                // 如果没有竞争那么此方法就会获取 lock 对象锁
                // 如果有竞争就进入阻塞队列, 可以被其他线程用 interrupt 方法打断
                log.debug("尝试获得锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获取锁，返回");
                return;
            }

            try {
                log.debug("获取到锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock(); // 主线程先获取 lock 对象锁
        t1.start();

        sleep(1);
        log.debug("打断 t1");
        t1.interrupt();
    }

}
