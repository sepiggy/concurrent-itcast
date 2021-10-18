package cn.itcast.test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class Test34 {

    public static void main(String[] args) {

        AtomicInteger i = new AtomicInteger(0);

        System.out.println(i.incrementAndGet()); // 原子版本的 ++i   1
        System.out.println(i.getAndIncrement()); // 原子版本的 i++   2
        System.out.println(i.get());

        System.out.println(i.getAndAdd(5)); // 2 , 7
        System.out.println(i.addAndGet(5)); // 12, 12

        AtomicInteger i1 = new AtomicInteger(5);
        //             读取值    设置值
        i1.updateAndGet(value -> value * 10);
        System.out.println(i1.get()); // 50

        System.out.println(updateAndGet(i1, p -> p / 2)); // 25

//        i.getAndUpdate()
//        System.out.println(i.get());
    }

    /**
     * 使用 compareAndSet 自己实现 updateAndGet
     *
     * @param i
     * @param operator
     * @return
     */
    public static int updateAndGet(AtomicInteger i, IntUnaryOperator operator) {
        while (true) {
            int prev = i.get();
            int next = operator.applyAsInt(prev);
            if (i.compareAndSet(prev, next)) {
                return next;
            }
        }
    }

}
