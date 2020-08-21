package com.myflx.common;


/**
 * @author LuoShangLin
 */
public class Bootstrap {
    public static void main(String[] args) {
        FileClassLoader classLoader = new FileClassLoader("/");
        System.out.println("自定义类加载器的父加载器: " + classLoader.getParent());
        System.out.println("系统默认的AppClassLoader: " + ClassLoader.getSystemClassLoader());
        System.out.println("AppClassLoader的父类加载器: " + ClassLoader.getSystemClassLoader().getParent());
        System.out.println("ExtClassLoader的父类加载器: " + ClassLoader.getSystemClassLoader().getParent().getParent());
    }
}
