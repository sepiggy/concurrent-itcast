package cn.itcast.test;

import cn.itcast.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.Test20")
public class Test20_2 {

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            new People_0().start();
        }
        Sleeper.sleep(1);
        for (Integer id : Mailboxes_0.getIds()) {
            new Postman_0(id, "内容" + id).start();
        }
    }

}

@Slf4j(topic = "c.People")
class People_0 extends Thread {

    @Override
    public void run() {
        // 收信
        GuardedObject_2 guardedObject = Mailboxes_0.createGuardedObject();
        log.debug("开始收信 id:{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{}, 内容: {}", guardedObject.getId(), mail);
    }

}

@Slf4j(topic = "c.Postman")
class Postman_0 extends Thread {

    private final int id;
    private final String mail;

    public Postman_0(int id, String mail) {

        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {

        GuardedObject_2 guardedObject = Mailboxes_0.getGuardedObject(id);
        log.debug("送信 id:{}, 内容:{}", id, mail);
        guardedObject.complete(mail);
    }

}

class Mailboxes_0 {

    private static final Map<Integer, GuardedObject_2> boxes = new Hashtable<>();
    private static int initialId = 1;

    public static synchronized int generateId() {

        return initialId++;
    }

    public static GuardedObject_2 createGuardedObject() {

        int id = generateId();
        GuardedObject_2 guardedObject = new GuardedObject_2(id);
        boxes.put(id, guardedObject);
        return guardedObject;
    }

    public static GuardedObject_2 getGuardedObject(int id) {

        return boxes.remove(id);
    }

    public static Set<Integer> getIds() {

        return boxes.keySet();
    }

}

class GuardedObject_2 {

    // 标识 Guarded Object
    private final int id;
    // 结果
    private Object response;

    public GuardedObject_2(int id) {

        this.id = id;
    }

    public int getId() {

        return id;
    }

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
