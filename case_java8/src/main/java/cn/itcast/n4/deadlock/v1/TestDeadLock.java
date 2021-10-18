package cn.itcast.n4.deadlock.v1;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过改变加锁顺序来解决哲学家就餐问题中的死锁现象
 * 但是会造成线程饥饿的现象：有的线程获得锁的机率太小了
 */
public class TestDeadLock {

    public static void main(String[] args) {

        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c1, c5).start();
    }

}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread {

    Chopstick left;
    Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {

        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {

        while (true) {
            //　尝试获得左手筷子
            synchronized (left) {
                // 尝试获得右手筷子
                synchronized (right) {
                    eat();
                }
            }
        }
    }

    private void eat() {

        log.debug("eating...");
        Sleeper.sleep(0.5);
    }

}

class Chopstick {

    String name;

    public Chopstick(String name) {

        this.name = name;
    }

    @Override
    public String toString() {

        return "筷子{" + name + '}';
    }

}