package com.myflx.common;


import sun.misc.Launcher;

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


        String rootDir = System.getProperty("user.dir") + "/myflx-common/lib/target/classes/";
        //创建自定义文件类加载器
        FileClassLoader loader = new FileClassLoader(rootDir);
        FileClassLoader loader1 = new FileClassLoader(rootDir);
        try {
            //加载指定的class文件
            Class<?> object1 = loader.loadClass("com.myflx.common.Demo");
            Class<?> object2 = loader1.loadClass("com.myflx.common.Demo");
            System.out.println("loadClass->obj1:" + object1.hashCode());
            System.out.println("loadClass->obj2:" + object2.hashCode());


            Class<?> object3 = loader.loadClass("com.myflx.common.Demo");
            Class<?> object4 = loader1.loadClass("com.myflx.common.Demo");
            System.out.println("loadClass->obj3:" + object3.hashCode());
            System.out.println("loadClass->obj4:" + object4.hashCode());

        } catch (Exception e) {
            e.printStackTrace();
        }





        ClassLoader classLoader2 = Launcher.class.getClassLoader();
        System.out.println(classLoader2);
    }
}
