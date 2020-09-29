package com.it.patten.guardedObject;

import com.it.common.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @author ch
 * @date 2020-9-29
 * @description
 */
@Slf4j
// 测试
public class GuardedMultiTask {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1000);
        for (Integer id : Futures.getIds()) {
            new Postman(id,"内容"+id).start();
        }
    }
}

// 业务相关类
@Slf4j
class People extends Thread{
    @Override
    public void run() {
        GuardedObj guardedObj = Futures.createGuardedObj();
        log.debug("开始收信 id:{}",guardedObj.getId());
        Object mail = guardedObj.get(5000);
        log.debug("收到信 id:{},内容：{}",guardedObj.getId(),guardedObj.getResponse());
    }
}
@Slf4j
class Postman extends Thread{
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObj guardedObj = Futures.getGuardedObj(id);
        log.debug("送信 id:{},内容：{}", id, mail);
        guardedObj.complete(mail);
    }
}


// 中间解耦类
class Futures{
    private static Map<Integer, GuardedObj> boxes = new Hashtable<>();

    private static int id = 1;

    // 产生唯一 id
    private static synchronized int generateId() {
        return id ++;
    }

    public static GuardedObj getGuardedObj(int id){
        return boxes.remove(id);
    }

    public static GuardedObj createGuardedObj(){
        GuardedObj go = new GuardedObj(generateId());
        boxes.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}


class GuardedObj{
    private final Object lock = new Object();
    // 标识Guarded Object
    private int id;
    // 结果
    private Object response;

    public GuardedObj(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Object get(long millis) {
        synchronized (lock) {
            long begin = System.currentTimeMillis();
            long passedTime = 0;
            while (response == null) {
                long waitTime = millis - passedTime;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    lock.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    public void complete(Object response) {
        synchronized (lock) {
            this.response = response;
            lock.notifyAll();
        }
    }
}
