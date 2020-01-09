package com.lee;

/**
 *
 * 在jdk8环境下可以运行
 * vmOptions:
 *  -Xms10m -Xmx10m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintHeapAtGC
 *
 * 在jdk11环境下
 * vmOptions:
 *  -Xms10m -Xmx10m -XX:+UseG1GC -Xlog:gc,gc+heap=debug,ergo*=trace
 * 还需要设置 -XX:G1HeapRegionSize=4M 才能不 OOM
 *
 * Created by lsd
 * 2019-12-18 16:24
 */
public class GCTest {
    public static void main(String[] args) {
        int size = 1024*1024;
        byte[] m1 = new byte[size];
        System.out.println("1...");
        byte[] m2 = new byte[size];
        System.out.println("2...");
        byte[] m3 = new byte[size];
        System.out.println("3...");
        byte[] m4 = new byte[size];
        System.out.println("4...");
    }
}
