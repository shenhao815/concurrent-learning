package com.it.atomic;

import sun.reflect.generics.tree.VoidDescriptor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Test39 {

    public static void main(String[] args) {
        demo(
                () -> new int[10],
                (array)->array.length,
                (array,index)->array[index]++,
                array-> System.out.println(Arrays.toString(array))
        );

        demo(
                () -> new AtomicIntegerArray(10),
                array -> array.length(),
                (array, index) -> array.getAndIncrement(index),
                array -> System.out.println(array)
        );
    }

    /**
     *
     * @param arraySupplier 提供数组、可以是线程不安全数组或线程安全数组
     * @param lengthFun 获取数组长度的方法
     * @param putConsumer 自增方法，回传 array,index
     * @param printConsumer 打印数组的方法
     * @param <T>
     */
    // supplier 提供者 无中生有 ()-> 结果
    // function 函数  一个参数一个结果 (参数)->结果   ，  BiFunction (参数1，参数2)->结果
    // consumer 消费者  一个参数没结果 (参数)-> void   ,BiConsumer(参数1，参数2)->void
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T,Integer> lengthFun,
            BiConsumer<T,Integer> putConsumer,
            Consumer<T> printConsumer
    ){
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);
        for (int i = 0; i < length; i++) {
            // 每个线程对数组作 10000 次操作
            ts.add(new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array,j%length);
                }
            }));
        }
        ts.forEach(t -> t.start()); // 启动所有线程
        ts.forEach(t->{
            try{
                t.join();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }
}
