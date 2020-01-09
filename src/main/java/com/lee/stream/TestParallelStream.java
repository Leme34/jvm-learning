package com.lee.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by lsd
 * 2020-01-09 21:02
 */
@Slf4j
public class TestParallelStream {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        try {
            // 此处无需返回值，使用submit只是为了它返回的FutureTask的get()阻塞等待所有任务完成再继续往下执行
            forkJoinPool.submit(() -> {
                        final long start = System.currentTimeMillis();
                        IntStream.range(0, 10).parallel().forEach(i -> {
                            try {
                                log.info("我睡" + i + "s...");
                                TimeUnit.SECONDS.sleep(i);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        System.out.println("所有任务已完成...耗时：" + (System.currentTimeMillis() - start) / 1000.0 + "s");
                    }
            ).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            forkJoinPool.shutdown();
        }
    }
}
