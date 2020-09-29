package com.it.patten.ProducerConsumer;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author ch
 * @date 2020-9-29
 * @description
 *
 * 要点：
 *  与前面的保护性暂停中的GuardedObject不同，不需要产生结果和消费结果的线程一一对应
 *  消费队列可以用来平衡生产和消费的线程资源
 *  生产者仅负责产生结果数据，不关心数据如何处理，而消费者专心处理结果数据
 *  消息队列是有容量限制的，满时就不会再加入数据，空时不会再消耗数据
 *  JDK中各种阻塞队列，采用的就是这种模式
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(3);
        for (int i = 0; i < 5; i++) {
            int id = i;
            new Thread(() -> {
                while (true) {
                    final Message message = new Message(id, "消息内容" + id);
                    queue.put(message);
                }
                // log.debug("生产消息:{}",message);
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while (true) {
                Message message = queue.take();
                // log.debug("消费消息：{}",message);
            }
        },"消费者").start();
    }

}
@Slf4j
class MessageQueue {
    private LinkedList<Message> list = new LinkedList();
    private Integer capacity;

    public MessageQueue(Integer capacity) {
        this.capacity = capacity;
    }

    public void put(Message message) {
        synchronized (list) {
            while (list.size() >= capacity) {
                try {
                    log.debug("队列已满，无法加入");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            list.addLast(message);
            log.debug("队列加入新的消息：{}",message.toString());
            list.notifyAll();
        }
    }

    public Message take() {
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列里无消息，无法取出");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            Message message = list.removeFirst();
            list.notifyAll();
            log.debug("队列里取出消息：{}",message.toString());
            return message;
        }
    }
}

final class Message {
    private int id;
    private Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message=" + message +
                '}';
    }
}
