package com.it.DIY.advanceThreadPool;


import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ThreadPoolTest2")
public class ThreadPoolTest2 {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1,
                1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            // 1) 死等
            //queue.put(task);
            // 2) 带超时等待
            // queue.offer(task,500,TimeUnit.MILLISECONDS);
            // 3) 让调用者放弃任务执行
            // log.debug("放弃{}",task);
            // 4) 让调用者抛出异常
            // throw new RuntimeException("任务执行失败"+task); //与放弃不同的是，若抛出异常，后面的任务也不会执行了
            // 5) 让调用者自己执行任务
            task.run();

        });

        for (int i = 0; i < 3; i++) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}",j);
            });
        }
    }
}

@FunctionalInterface
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue, T task);
}


@Slf4j(topic = "c.ThreadPool")
class ThreadPool{
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;
    // 线程集合
    private HashSet<Worker> workers = new HashSet<Worker>();
    // 线程核心数
    private int coreSize;
    // 任务超时时间
    private long timeout;
    private TimeUnit timeUnit;

    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit,int queueCapacity,RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task) {
        // 如果任务队列数小于coreSize，直接交给worker对象执行
        // 如果任务数超过coreSize时，加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker{},{}",worker,task);
                workers.add(worker);
                worker.start();
            } else {
                // taskQueue.put(task);
                // 1) 死等
                // 2) 带超时等待
                // 3) 让调用者放弃任务执行
                // 4) 让调用者抛出异常
                // 5) 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);

            }
        }
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
    private Deque<T> queue = new ArrayDeque<>();
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

    // 获取任务
    public T take(){
        lock.lock();
        try{
            while (queue.size() <= 0) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally{
            lock.unlock();
        }
    }

    // 带超时的获取任务
    public T poll(long timeout, TimeUnit timeUnit) {
        lock.lock();
        long nanos = timeUnit.toNanos(timeout);
        try{
            while (queue.size() <= 0) {
                if (nanos <= 0) {
                    return null;
                }
                try {
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally{
            lock.unlock();
        }
    }

    // 添加任务
    public void put(T task) {
        lock.lock();
        try{
            while (queue.size() >= capacity) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            emptyWaitSet.signal();
        }finally{
            lock.unlock();
        }
    }

    // 带超时的添加任务
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
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        }finally{
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try{
            // 判断队列是否已满
            if (queue.size() >= capacity) {
                rejectPolicy.reject(this,task);
            } else { // 队列有空闲
                log.debug("加入任务队列 {}",task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally{
            lock.unlock();
        }
    }
}
