package com.lee.classloader;

import lombok.ToString;

import java.io.*;

/**
 * 实现自定义类加载器
 */
@ToString
public class MyClassLoader extends ClassLoader {
    // 类加载器名称，只用于toString()
    private String classLoaderName;
    // 二进制字节码文件扩展名
    private static final String FILE_EXTENSION = ".class";
    // idea存放编译好的字节码的根目录
    private static final String FILE_SPACE = "D:/ideaWorkPlace/jvm-learning/target/classes/";

    /**
     * 将系统类加载器作为该类的父加载器
     */
    public MyClassLoader(String classLoaderName) {
        super();
        this.classLoaderName = classLoaderName;
    }

    /**
     * 自己指定该类的父加载器
     */
    public MyClassLoader(ClassLoader classLoader, String classLoaderName) {
        super(classLoader);
        this.classLoaderName = classLoaderName;
    }

    /**
     * 此方法把字节码文件加载为Class对象
     * 非主动调用，会由loadClass(String className)底层调用
     *
     * @param className .class二进制字节码文件名称
     */
    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        System.out.println("MyClassLoader.findClass()：当前类加载器名称：" + classLoaderName +
                "，被加载的类：" + className);
        // 1 读取.class二进制字节码文件为byte[]
        byte[] byteData = loadClassByteData(className);
        // 2 把字节码文件加载为Class对象
        return defineClass(className, byteData, 0, byteData.length);
    }

    /**
     * 读取.class二进制字节码文件为byte[]
     *
     * @param className 全类名
     */
    private byte[] loadClassByteData(String className) {
        String classFilePath = FILE_SPACE + className.replace(".", "/") + FILE_EXTENSION;
        try (
                InputStream is = new FileInputStream(new File(classFilePath));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            int len = 0;
            while ((len = is.read()) != -1) {
                baos.write(len);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        // 创建自己的类加载器，父加载器为
        MyClassLoader myClassLoader = new MyClassLoader("myClassLoader");

        // --------------------调用ClassLoader的loadClass(String className)加载类-----------------
        // TODO 委托给了其父类加载器(系统类加载器)，若父加载器加载不了才会调用自定义类加载器的findClass方法
        Class<?> clazz = myClassLoader.loadClass("com.lee.classloader.Test");
        //实例化类对象
        Object obj = clazz.newInstance();
        System.out.println(obj + "，由类加载器 " + obj.getClass().getClassLoader() + " 加载");

        // -------------使用自己的类加载器加载com.lee.jvm.Test类，若上边已经由系统类加载器加载则不会再次加载-------------------
//        Class<?> clazz = myClassLoader.findClass("com.lee.jvm.Test");
//        // 实例化类对象
//        Object obj = clazz.newInstance();
//        System.out.println(obj + "，由类加载器 " + obj.getClass().getClassLoader() + " 加载");

    }

}
