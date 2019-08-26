package com.lee.obj_reference;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        seeJProfiler();

//        testStrongReference();
//        testSoftReference();
//        testWeakReference();
    }

    // 查看内存分析工具JProfiler
    private static void seeJProfiler() throws InterruptedException {
        List<Object> list = new ArrayList<>();

        int elementCount = 5000;

        for (int i = 0; i < elementCount; i++) {
            byte[] buff = new byte[512  * 1024];  // 512k
            list.add(buff);

            System.out.println(String.format("----分配第%d个内存----", i + 1));
            Thread.sleep(400);
        }
    }

    // 强引用
    private static void testStrongReference() throws InterruptedException {
        List<Person> list = new ArrayList<>();

        int elementCount = 550;  // 当前JVM内存最大4G, 最多能分配 4G / 8M = 512 个对象，超过的话内存就会爆掉

        for (int i = 0; i < elementCount; i++) {

            Person person = new Person();  // 8M
            list.add(person);

            System.out.println(String.format("----分配第%d个内存----", i + 1));
            Thread.sleep(100);

            if (i % 10 == 0) {
                System.gc();  // 垃圾回收
            }
        }

        System.out.println("----内存分配完成，遍历还活着的对象----");
        for (int i = 0; i < elementCount; i++) {
            if (list.get(i) != null) {
                System.out.println(String.format("第%d个元素的数据还在，内容为：%s", i + 1, list.get(i)));
            }
        }
    }

    // 软引用
    private static void testSoftReference() throws InterruptedException {
        List<SoftReference> softList = new ArrayList<>();

        int elementCount = 550;

        for (int i = 0; i < elementCount; i++) {

            SoftReference<Person> person = new SoftReference<>(new Person());
            softList.add(person);

            System.out.println(String.format("----分配第%d个内存----", i + 1));
            Thread.sleep(100);

            if (i % 10 == 0) {
                System.gc(); // 垃圾回收
            }
        }

        System.out.println("----内存分配完成，遍历还活着的对象----");
        for (int i = 0; i < elementCount; i++) {
            if (softList.get(i).get() != null) {
                System.out.println(String.format("第%d个元素的数据还在，内容为：%s", i + 1, softList.get(i).get()));
            }
        }
    }

    // 弱引用
    private static void testWeakReference() throws InterruptedException {
        List<WeakReference> weakList = new ArrayList<>();

        int elementCount = 550;

        for (int i = 0; i < elementCount; i++) {

            WeakReference<Person> sr = new WeakReference<>(new Person());
            weakList.add(sr);

            System.out.println(String.format("----分配第%d个内存----", i + 1));
            Thread.sleep(100);

            if (i % 10 == 0) {
                System.gc(); // 垃圾回收
            }
        }

        System.out.println("----内存分配完成，遍历还活着的对象----");
        for (int i = 0; i < elementCount; i++) {
            if (weakList.get(i).get() != null) {
                System.out.println(String.format("第%d个元素的数据还在，内容为：%s", i + 1, weakList.get(i).get()));
            }
        }
    }
}


class Person {
    private String name;

    private String age;

    private byte[] data = new byte[8 * 1024 * 1024];

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        System.out.println(String.format("我是对象%d，现在在执行finalize方法", this.data.hashCode()));
    }

    @Override
    public String toString() {
        return String.valueOf(this.data.hashCode());
    }
}
