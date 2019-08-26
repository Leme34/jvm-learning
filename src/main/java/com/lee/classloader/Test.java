package com.lee.classloader;

import lombok.Data;

@Data
public class Test {

    private String name = "测试类";

    public void show() {
        System.out.println(name);
    }


    public static void main(String[] args) {
        int i =1;
        i = i++;
        System.out.println(i);
    }


}
