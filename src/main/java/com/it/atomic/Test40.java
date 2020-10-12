package com.it.atomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class Test40 {

    public static void main(String[] args) {
        Student stu = new Student();
        AtomicReferenceFieldUpdater updater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");
        System.out.println(updater.compareAndSet(stu,null,"张三"));
        System.out.println(stu);
    }
}
class Student{
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
