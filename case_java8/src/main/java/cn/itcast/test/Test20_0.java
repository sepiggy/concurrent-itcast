package cn.itcast.test;

import cn.itcast.pattern.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "c.Test20")
public class Test20_0 {

    // 线程1等待线程2的下载结果
    public static void main(String[] args) {

        GuardedObject_0 guardedObject = new GuardedObject_0();

        new Thread(() -> {
            // 等待结果
            log.debug("等待结果");
            List<String> strings = (List<String>) guardedObject.get();
            log.debug("结果大小: {}", strings.size());
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行下载");
            try {
                List<String> list = Downloader.download();
                guardedObject.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }

}

class GuardedObject_0 {

    // 结果
    private Object response;

    public Object get() {

        synchronized (this) {
            while (response == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return response;
        }
    }

    public void complete(Object response) {

        synchronized (this) {
            // 给结果成员变量赋值
            this.response = response;
            this.notifyAll();
        }
    }

}
