package cn.itcast.test;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

import static cn.itcast.n2.util.Sleeper.sleep;

@Slf4j(topic = "c.Test21")
public class Test21_0 {

    public static void main(String[] args) {

        MessageQueue_0 queue = new MessageQueue_0(2);

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message_0(id, "值" + id));
            }, "生产者" + i).start();

            new Thread(() -> {
                while (true) {
                    sleep(1);
                    Message_0 message = queue.take();
                }
            }, "消费者").start();
        }
    }

}

// 消息队列类，Java线程间通信
@Slf4j(topic = "c.MessageQueue")
class MessageQueue_0 {

    // 消息队列集合
    private final LinkedList<Message_0> list = new LinkedList<>();
    // 队列容量
    private final int capcity;

    public MessageQueue_0(int capcity) {

        this.capcity = capcity;
    }

    // 获取消息
    public Message_0 take() {

        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空, 消费者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message_0 message = list.removeFirst();
            log.debug("已消费消息 {}", message);
            list.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message_0 message) {

        synchronized (list) {
            while (list.size() == capcity) {
                try {
                    log.debug("队列已满, 生产者线程等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(message);
            log.debug("已生产消息 {}", message);
            list.notifyAll();
        }
    }

}

// 使用final修饰和没有set方法是为了保证类的状态不可变
// Message_0的不可变性保证了线程安全
final class Message_0 {

    private final int id;
    private final Object value;

    public Message_0(int id, Object value) {

        this.id = id;
        this.value = value;
    }

    public int getId() {

        return id;
    }

    public Object getValue() {

        return value;
    }

    @Override
    public String toString() {

        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }

}