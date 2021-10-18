package cn.itcast.n7;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/**
 * 演示SimpleDateFormat的线程不安全现象
 */
@Slf4j(topic = "c.Test1_0")
public class Test1_0 {

    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    log.debug("{}", sdf.parse("1951-04-21"));
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }
    }

}
