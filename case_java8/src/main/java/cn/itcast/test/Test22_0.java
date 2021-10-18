package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示ReentrantLock可重入特性
 */
@Slf4j(topic = "c.Test22_0")
public class Test22_0 {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        lock.lock();
        try {
            log.debug("enter main");
            m1();
        } finally {
            lock.unlock();
        }
    }

    public static void m1() {

        lock.lock();
        try {
            log.debug("enter m1");
            m2();
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {

        lock.lock();
        try {
            log.debug("enter m2");
        } finally {
            lock.unlock();
        }
    }

}
