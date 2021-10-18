package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test27_0")
public class Test27_0 {

    static final Object lock = new Object();
    static boolean isT1Runned;
    static boolean isT2Runned;
    static int counter = 5;

    public static void main(String[] args) {

        WaitNotify_0 wn = new WaitNotify_0(1, 5);
        new Thread(() -> {
            wn.print("a", 1, 2);
        }).start();
        new Thread(() -> {
            wn.print("b", 2, 3);
        }).start();
        new Thread(() -> {
            wn.print("c", 3, 1);
        }).start();
    }

}

/**
 * 输出内容   等待标记   下一个标记
 * a         1         2
 * b         2         3
 * c         3         1
 */
class WaitNotify_0 {

    // 循环次数
    private final int loopNumber;
    // 等待标记
    private int flag;
    public WaitNotify_0(int flag, int loopNumber) {

        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    // 打印
    public void print(String str, int waitFlag, int nextFlag) {

        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

}