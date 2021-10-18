package cn.itcast.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static cn.itcast.n2.util.Sleeper.sleep;

public class Test30_0 {

    public static void main(String[] args) {

        AwaitSignal_0 awaitSignal = new AwaitSignal_0(5);

        // 三个线程各自有自己的休息室
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();

        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();

        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }).start();

        sleep(1);

        awaitSignal.lock();
        try {
            System.out.println("开始...");
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }

}

class AwaitSignal_0 extends ReentrantLock {

    private final int loopNumber;

    public AwaitSignal_0(int loopNumber) {

        this.loopNumber = loopNumber;
    }

    // 参数1：打印内容
    // 参数2：进入哪一间休息室
    // 参数3：下一间休息室
    public void print(String str, Condition current, Condition next) {

        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.out.print(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }

}
