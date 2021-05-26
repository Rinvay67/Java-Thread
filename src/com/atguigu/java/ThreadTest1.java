package com.atguigu.java;

/**
 * 创建多线程的方式二：实现 Runnable 接口
 * 1. 创建一个实现了 Runnable 接口的类
 * 2. 实现类去实现 Runnable 中的抽象方法：run()
 * 3. 创建实现类的对象
 * 4. 将此对象作为参数传递到 Thread 类的构造器中，创建 Thread 类的对象
 * 5. 通过 Thread 类的对象调用 start()
 *
 * 比较创建线程的两种方式：
 * 开发中，优先选择实现 Runnable 接口的方式
 * 原因：1. 实现的方式没有类的单继承性的局限性
 *      2. 实现的方式更适合来处理多个线程有共享数据的情况
 *
 * 联系：两种方式都需要重写 run()，将线程要执行的逻辑声明在 run() 中
 *
 * 通过同步机制，来解决线程安全问题
 *
 * 方式一：同步代码块
 *
 * Object obj = new Object();
 *
 * synchronized(obj) {
 *     // 需要被同步的代码
 * }
 *
 * synchronized(同步监视器) {
 *     // 需要被同步的代码
 * }
 * 说明：1. 操作共享数据的代码，即为需要被同步的代码。
 *      2. 共享数据：即多个线程共同操作的变量
 *      3. 同步监视器：俗称锁，任何一个类的对象，都可以充当锁；要求，多个线程必须要共用一把锁。
 *
 * 补充：在实现 Runnable 接口创建多线程的方式中，可考虑用当前对象(this)充当同步监视器；
 *      在继承 Thread 类创建多线程的方式中，慎用 this 充当同步监视器，考虑使用当前类充当同步监视器(Window2.class)，它只会加载一次，
 *
 * 方式二：同步方法
 *
 * 如果操作共享数据的代码完整声明在一个方法中，不妨将此方法声明为同步的。
 *
 * 1. 实现 Runnable 接口创建多线程的方式
 *
 * public void run() {
 *     while(true) {
 *         show();
 *     }
 * }
 * 同步监视器：this
 * private synchronized void show() {
 *     // 操作共享数据你的代码
 * }
 *
 * 2. 继承 Thread 类创建多线程的方式
 * 同步监视器：类
 * private static synchronized void show() {
 *
 * }
 *
 * 1. 同步方法仍然涉及到同步监视器，只是不需要显式声明
 * 2. 非静态的同步方法，同步监视器是：this
 * 3. 静态的同步方法，同步监视器是：当前类本身
 *
 *
 *
 *
 *
 *
 *
 * 同步的方式，解决了线程的安全问题，但是操作同步代码时，只能有一个线程参与，其他线程等待，相当于是一个单线程的过程，效率低。
 *
 */

// 1. 创建一个实现了 Runnable 接口的类
class MThread implements Runnable {
    // 2. 实现类去实现 Runnable 中的抽象方法：run()
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }
}

public class ThreadTest1 {
    public static void main(String[] args) {
        // 3. 创建实现类的对象
        MThread mThread = new MThread();
        // 4. 将此对象作为参数传递到 Thread 类的构造器中，创建 Thread 类的对象
        Thread thread = new Thread(mThread);
        // 5. 通过 Thread 类的对象调用 start()
        thread.start();
    }
}
