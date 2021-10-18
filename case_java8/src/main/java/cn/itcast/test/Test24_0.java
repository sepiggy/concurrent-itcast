package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Test24_0")
public class Test24_0 {

    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        // 创建一个新的条件变量（休息室）
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        lock.lock();

        // 进入休息室等待
        try {
            condition1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        condition1.signal();
    }

}
