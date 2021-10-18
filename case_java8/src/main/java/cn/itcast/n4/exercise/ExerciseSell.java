package cn.itcast.n4.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@Slf4j(topic = "c.ExerciseSell")
public class ExerciseSell {

    // Random 为线程安全
    static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow window = new TicketWindow(1000);

        // 所有线程的集合
        List<Thread> threadList = new ArrayList<>();

        // 卖出的票数统计
        List<Integer> amountList = new Vector<>();
        for (int i = 0; i < 2000; i++) {
            Thread thread = new Thread(() -> {
                // 买票
                int amount = window.sell(random(5));
                try {
                    // 为了演示明显，增加指令交错的概率
                    Thread.sleep(random(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 统计买票数
                amountList.add(amount);
            });

            // 这句在主线程中执行，所以这里可以用ArrayList
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        // 统计卖出的票数和剩余票数
        log.debug("余票：{}", window.getCount());
        log.debug("卖出的票数：{}", amountList.stream().mapToInt(i -> i).sum());
    }

    // 随机 1~5
    public static int random(int amount) {

        return random.nextInt(amount) + 1;
    }

}

// 售票窗口
class TicketWindow {

    private int count;

    public TicketWindow(int count) {

        this.count = count;
    }

    // 获取余票数量
    public int getCount() {

        return count;
    }

    // 售票
    public synchronized int sell(int amount) {

        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }

}