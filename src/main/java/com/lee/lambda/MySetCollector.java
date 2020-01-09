package com.lee.lambda;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 自定义Collector：底层使用 Set 数据结构，转换为 Map 输出
 * <T>：元素类型
 * <p>
 * ps：
 * 因为主线程调用Collector的这些被重写方法，然后返回lambda表达式
 * 所以重写的每个方法中 lambda表达式 外的log输出都是主线程执行的，需要在lambda表达式中打印log才能看到真实调用的线程
 * <p>
 * Created by lsd
 * 2019-12-20 20:56
 */
@Slf4j
public class MySetCollector<T> implements Collector<T, Set<T>, Map<T, T>> {

    /**
     * 创建属于该分区的容器
     */
    @Override
    public Supplier<Set<T>> supplier() {
//        log.info("supplier()...");
//        return HashSet::new;
//        return CopyOnWriteArraySet::new;
//        return () -> Collections.synchronizedSet(new HashSet<>());
        return () -> {
            log.info("supplier()...");
            return new HashSet<>();
        };
    }

    /**
     * 单个分区里的元素累加器
     */
    @Override
    public BiConsumer<Set<T>, T> accumulator() {
        return (set, item) -> {
            log.info("accumulator()...set=" + set + "，item=" + item);
            set.add(item);
        };
    }

    /**
     * 多个分区结果合并
     */
    @Override
    public BinaryOperator<Set<T>> combiner() {
        return (set1, set2) -> {
            log.info("combiner()...set1=" + set1 + "，set2=" + set2);
            set1.addAll(set2);
            return set1;
        };
    }

    /**
     * combiner()结果 转换 最终类型对象返回
     */
    @Override
    public Function<Set<T>, Map<T, T>> finisher() {
        return set -> {
            log.info("finisher()...");
            Map<T, T> map = new HashMap<>();
            set.forEach(item -> map.put(item, item));
            return map;
        };
    }

    /**
     * 传入不可变集合设置多个流的特性
     */
    @Override
    public Set<Characteristics> characteristics() {
        log.info("characteristics()...");
//        return Collections.emptySet();

        // 0、当认为数据源是无序的，比如Set，就可以添加UNORDERED特性，否则不应该添加该枚举值。因为该特性不承诺保存的顺序和元素出现的顺序一致，在空间和时间维度上带来许多优化机会。
        //    例如Collectors.joining()方法为了保证字符串的拼接顺序就没有加UNORDERED特性
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));

        // 1、下边的代码加上了IDENTITY_FINISH特性会抛异常
        //    IDENTITY_FINISH特性的作用（文档）：检查Collector<T, A, R>的A强转型到R必须要是成功的，不成功会抛出转型异常
        //    看Stream.collect()源码可知，若没有 IDENTITY_FINISH特性 就不会调用 finisher() 去把A类型的结果转换为R类型。
        //    若已知A类型可以强转T类型，则加上 IDENTITY_FINISH特性 的好处是避免调用 finisher() ，直接强转类型返回结果即可。
//        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED,Characteristics.IDENTITY_FINISH));

        // 2、下边的代码加上了 CONCURRENT特性 HashSet底层的HashMap可能抛出 ConcurrentModificationException（fail-fast）
        //    CONCURRENT特性的作用（文档）：多个线程并行调用累加器，但操作的是同一个结果容器所以无需调用 combiner()
        //    如果此collector没有 UNORDERED特性，则只能对无序的数据源并行
        //    若没有加上 CONCURRENT特性 也可以并行但是有多少个线程就会有多少个结果容器，需要调用 combiner() 去合并
//        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED, Characteristics.CONCURRENT));
    }

    public static void main(String[] args) {
        System.out.println("CPU Processors=" + Runtime.getRuntime().availableProcessors());
        final long start = System.currentTimeMillis();
//        for (int i = 0; i < 1; i++) {
        List<String> list = Arrays.asList("123", "456", "12399", "4562", "1231", "4568", "1263");
        //parallelStream只是修改了个 boolean parallel
        Map<String, String> map = list.stream().filter(i -> true).collect(new MySetCollector<>());
        System.out.println(map);
//        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start) / 1000.0);
    }

}
