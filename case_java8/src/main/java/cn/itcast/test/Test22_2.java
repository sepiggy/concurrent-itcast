/// ReentrantLock 锁超时特性
/// 1. 使用 ReentrantLock#tryLock 方法尝试获得锁
/// 2. ReentrantLock#tryLock 方法也支持被打断
/// 3. ReentrantLock#tryLock 方法有两个重载方法：
///   1) ReentrantLock#tryLock()
///   2) ReentrantLock#tryLock(long, java.util.concurrent.TimeUnit)

package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static cn.itcast.n2.util.Sleeper.sleep;

/**
 * 演示 ReentrantLock 锁超时
 */
@Slf4j(topic = "c.Test22_1")
public class Test22_2 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            log.debug("尝试获得锁");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获取不到锁");
                return;
            }
            try {
                log.debug("获得到锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock(); // 主线程先获得到锁
        log.debug("获得到锁");
        t1.start();
        sleep(1);
        log.debug("释放了锁");
        lock.unlock();
    }

}
