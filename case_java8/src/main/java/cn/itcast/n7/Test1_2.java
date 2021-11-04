package cn.itcast.n7;

import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;

/**
 * 通过 JDK8 引入的 DateTimeFormatter 不可变对象来解决
 */
@Slf4j(topic = "c.Test1_2")
public class Test1_2 {

    public static void main(String[] args) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    log.debug("{}", dtf.parse("1951-04-21"));
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }
    }

}
