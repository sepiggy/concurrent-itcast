package cn.itcast.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

interface Account_0 {

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account_0 account) {

        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance()
                + " cost: " + (end - start) / 1000_000 + " ms");
    }

    // 获取余额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

}

public class TestAccount_0 {

    public static void main(String[] args) {

//        Account_0 accountUnsafe_0 = new AccountUnsafe_0(10000);
//        Account_0.demo(accountUnsafe_0);
//
//        Account_0 accountSafeSynchronized = new AccountSafeSynchronized(10000);
//        Account_0.demo(accountSafeSynchronized);

        Account_0 accountSafeCas = new AccountSafeCas(10000);
        Account_0.demo(accountSafeCas);

    }

}

/**
 * 使用原子类无锁并发保证线程安全
 */
class AccountSafeCas implements Account_0 {

    private final AtomicInteger balance;

    public AccountSafeCas(int balance) {

        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {

        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {

        // 方法1
        /*
        while (true) {
            // 获取余额的最新值
            int prev = balance.get();
            // 要修改的余额
            int next = prev - amount;
            // 真正修改
            // 比较并设置
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
        */

        // 方法2
        balance.getAndAdd(-1 * amount);

    }

}

/**
 * 使用 synchronized 加锁保证线程安全
 */
class AccountSafeSynchronized implements Account_0 {

    private Integer balance;

    public AccountSafeSynchronized(Integer balance) {

        this.balance = balance;
    }

    @Override
    public Integer getBalance() {

        synchronized (this) {

            return balance;
        }

    }

    @Override
    public void withdraw(Integer amount) {

        synchronized (this) {
            balance -= amount;
        }

    }

}

/**
 * 线程不安全实现
 */
class AccountUnsafe_0 implements Account_0 {

    private Integer balance;

    public AccountUnsafe_0(Integer balance) {

        this.balance = balance;
    }

    @Override
    public Integer getBalance() {

        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {

        this.balance -= amount;
    }

}
