package com.atguigu.java2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程的方式三：实现 Callable 接口   ---JDK5.0新增
 *
 * 如何理解实现Callable接口创建多线程的方式比实现Runnable接口创建多线程的方式强大？
 * 1. call() 可以有返回值；
 * 2. call() 可以抛出异常，被外面的操作捕获，获取异常信息；
 * 3. Callable 是支持泛型的
 *
 */

// 1. 创建一个实现 Callable 的实现类
class NumThread implements Callable {

    // 2. 实现 call 方法，将此线程需要执行的操作声明在 call() 中
    @Override
    public Object call() throws Exception {
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
                sum += i;
            }
        }
        return sum;
    }
}

public class ThreadNew {
    public static void main(String[] args) {
        // 3. 创建 Callable 接口实现类的对象
        NumThread numThread = new NumThread();
        // 4. 将此 Callable 接口实现类的对象作为参数传递到 FutureTask 的构造器中，创建 FutureTask 的对象
        FutureTask futureTask = new FutureTask<>(numThread);
        // 5. 将 FutureTask 对象作为参数传递到 Thread 类的构造器中，创建 Thread 对象，并 start()
        new Thread(futureTask).start();
        try {
            // 6. 获取 Callable 中 call 方法的返回值
            // get() 返回值即为 FutureTask 构造器参数 Callable 实现类重写的 call() 的返回值
            Object sum = futureTask.get();
            System.out.println("总和为：" + sum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
