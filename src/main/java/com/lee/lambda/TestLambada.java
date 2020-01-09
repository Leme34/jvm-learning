package com.lee.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by lsd
 * 2019-12-27 18:18
 */
public class TestLambada {

    private static Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            System.out.println("内部类方式");
        }
    };

    private static Runnable runnable2 = () -> System.out.println("lambda方式");

    public static void main(String[] args) {
        List<String> list = Arrays.asList("123", "456", "12399", "4562", "1231", "4568", "1263");
        list.stream()
                .map(item -> item + "test")
                .filter(item -> true)
                .forEach(System.out::println);
    }

}
