package com.atguigu.java;

// 1. 创建一个继承于 Thread 类的子类
class MyThread extends Thread {
    // 2. 重写 Thread 类的 run()
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }
}

public class ThreadTest2 {
    public static void main(String[] args) {
        // 3. 创建 Thread 类的子类对象
        MyThread t1 = new MyThread();
        // 4. 通过此对象调用 start()
        t1.start();

    }
}
