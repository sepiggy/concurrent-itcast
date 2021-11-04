package cn.itcast.n7;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * 通过加锁来解决 SimpleDateFormat 线程不安全
 */
@Slf4j(topic = "c.Test1_1")
public class Test1_1 {

    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                synchronized (sdf) {
                    try {
                        log.debug("{}", sdf.parse("1951-04-21"));
                    } catch (Exception e) {
                        log.error("{}", e);
                    }
                }
            }).start();
        }
    }

}
