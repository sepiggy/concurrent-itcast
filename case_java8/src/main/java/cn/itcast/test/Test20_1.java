package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test20")
public class Test20_1 {

    // 线程1等待线程2的下载结果
    public static void main(String[] args) {

        GuardedObject_1 guardedObject = new GuardedObject_1();

        new Thread(() -> {
            log.debug("begin");
            Object response = guardedObject.get(2000);
            log.debug("结果是: {}", response);
        }, "t1").start();

        new Thread(() -> {
            log.debug("begin");
            Sleeper.sleep(1);
            // 模拟虚假唤醒
            guardedObject.complete(null);
        }, "t2").start();
    }

}

class GuardedObject_1 {

    // 结果
    private Object response;

    // 获取结果
    // timeout表示要等待多久
    public Object get(long timeout) {

        synchronized (this) {
            // 记录开始时间
            long begin = System.currentTimeMillis();
            // 经历的时间
            long passedTime = 0;
            while (response == null) {
                //  这一轮循环应该等待的时间
                long waitTime = timeout - passedTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 经历的时间
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    // 产生结果
    public void complete(Object response) {

        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }

}
