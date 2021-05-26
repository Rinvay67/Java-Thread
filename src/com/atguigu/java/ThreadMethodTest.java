package com.atguigu.java;

/**
 * Thread 中的常用方法：
 * 1. start()：启动当前线程，调用当前线程的 run()
 * 2. run()：通常需要重写 Thread 类的此方法，将创建的线程要执行的操作声明在此方法中
 * 3. currentThread()：静态方法，返回执行当前代码的线程
 * 4. getName()：获取当前线程的名字
 * 5. setName()：设置当前线程的名字
 * 6. yield()：释放当前 CPU 的执行权
 * 7. join()：在线程 A 中调用线程 B 的 join() 方法，此时线程 A 进入阻塞状态，直到线程 B 执行完
 * 8. sleep(long millitime)：让当前线程”睡眠“指定的毫秒，在指定的毫秒时间内，当前线程是阻塞状态
 * 9. isAlive()：判断当前线程是否存活
 */

public class ThreadMethodTest {

}
