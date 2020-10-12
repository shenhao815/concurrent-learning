package com.it.DIY;

import com.it.common.Sleeper;
import com.it.reentrantLock.Timeout;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ThreadPoolTest")
public class ThreadPoolTest {
    public static void main(String[] args) {
        final ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
        for (int i = 0; i < 5; i++) {
            int j = i;
            threadPool.execute(() -> {
                Sleeper.sleep(500);
                log.debug("{}",j);
            });
        }
    }
}
@Slf4j(topic = "c.ThreadPool")
class ThreadPool{
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    // 获取任务的超时时间
    private long timeout;

    private TimeUnit timeUnit;

    public void execute(Runnable task) {
        // 当任务数没有超过coreSize时，直接交给worker对象执行
        // 如果任务数超过coreSize时，加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{},{}",worker,task);
                workers.add(worker);
                worker.start();
            } else {
                log.debug("加入任务队列{}",task);
                taskQueue.put(task);
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit,int queueCapacity) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1) 当task不为空，执行任务
            // 2) 当task执行完毕，再接着从任务队列获取任务并执行
            while (task != null|| (task = taskQueue.poll(timeout,timeUnit))!=null) {
                try{
                    log.debug("正在执行...{}",task);
                    task.run();
                }catch(Exception ex){
                    ex.printStackTrace();
                }finally{
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker 被移除{}",this);
                workers.remove(this);
            }
        }
    }
}
@Slf4j(topic = "c.BlockingQueue")
class BlockingQueue<T>{
    // 1. 任务队列
    private Deque<T> queue = new ArrayDeque<T>();

    // 2. 锁
    private ReentrantLock lock = new ReentrantLock();

    // 3. 生产者条件变量
    private Condition fullWaitSet = lock.newCondition();

    // 4. 消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 5. 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        long nanos = unit.toNanos(timeout);
        try{
            while (queue.size() <= 0) {
                try {
                    // awaitNanos() 的返回值为总等待时间(timeout)减去已花费的等待时间
                    if (nanos <= 0) {
                        return null;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            emptyWaitSet.signal();
            return t;
        }finally{
            lock.unlock();
        }

    }

    // 阻塞获取
    public T take(){
        lock.lock();
        try{
            while (queue.size() <= 0) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            emptyWaitSet.signal();
            return t;
        }finally{
            lock.unlock();
        }
    }

    // 阻塞添加
    public void put(T task) {
        lock.lock();
        try{
            while (queue.size() >= capacity) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            fullWaitSet.signal();
        }finally{
            lock.unlock();
        }
    }

    // 带超时时间的阻塞添加
    // 阻塞添加
    public boolean offer(T task,long timeout,TimeUnit timeUnit) {
        lock.lock();
        long nanos = timeUnit.toNanos(timeout);
        try{
            while (queue.size() >= capacity) {
                try {
                    log.debug("等待加入任务队列{}...",task);
                    if (nanos <= 0) {
                        return false;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列{}",task);
            queue.addLast(task);
            fullWaitSet.signal();
            return true;
        }finally{
            lock.unlock();
        }
    }

    // 获取大小
    public int size(){
        lock.lock();
        try{
            return queue.size();
        }finally{
            lock.unlock();
        }
    }
}
