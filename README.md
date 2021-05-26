# 8. 多线程

- [8. 多线程](#8-多线程)
  - [8.1 大纲](#81-大纲)
  - [8.2 基本概念](#82-基本概念)
  - [8.3 线程的创建和使用](#83-线程的创建和使用)
    - [8.3.1 继承于 Thread 类](#831-继承于-thread-类)
    - [8.3.2 实现 Runnable 接口](#832-实现-runnable-接口)
    - [8.3.3 两种实现方式的比较](#833-两种实现方式的比较)
  - [8.4 线程的生命周期](#84-线程的生命周期)
  - [8.5 线程的同步](#85-线程的同步)
    - [8.5.1 同步代码块](#851-同步代码块)
    - [8.5.2 同步方法](#852-同步方法)
    - [8.5.3 懒汉式的线程安全问题](#853-懒汉式的线程安全问题)
    - [8.5.4 线程的死锁问题](#854-线程的死锁问题)
    - [8.5.5 Lock 锁](#855-lock-锁)
  - [8.6 线程的通信](#86-线程的通信)
  - [8.7 新增线程创建方式](#87-新增线程创建方式)
    - [8.7.1 实现 Callable 接口](#871-实现-callable-接口)
    - [8.7.2 线程池](#872-线程池)

## 8.1 大纲

![多线程大纲](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/多线程大纲.30gi9u7ecxm0.png)

## 8.2 基本概念

- 程序：指为完成特定任务，用某种语言编写的一组指令的集合。即指一段**静态的代码**，静态对象。
- 进程：程序的一次执行过程，或是正在运行的一个程序。**进程是资源分配的单位**。
- 线程：进程可进一步细化为线程，是一个程序内部的一条执行路径。**线程作为调度和执行的单位，每个线程拥有独立的运行栈和程序计数器**。

![JVM](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/JVM.5p224uf0krk0.png)

> JVM 内存解析：
> 类加载器：加载 class 文件，反射章节介绍；
> 虚拟机栈、程序计数器：每个线程都有一份；
> 方法区：static 等静态结构在方法区，每个进程有一份；
> 堆：对象及实例变量在堆空间中，每个进程有一份；
> 多个线程 **共享** 方法区和堆。

## 8.3 线程的创建和使用

### 8.3.1 继承于 Thread 类

1. 创建一个继承于 Thread 类的子类；
2. 重写 Thread 类的 run() 方法，将此线程执行的操作声明在 run() 中；
3. 创建 Thread 类的子类的对象；
4. 通过此对象调用 start() 方法。作用：**启动当前线程；调用当前线程的 run()**；
5. **创建多个线程需要创建多个子类对象。**

```java
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

public class ThreadTest {
    public static void main(String[] args) {
        // 3. 创建 Thread 类的子类对象
        MyThread t1 = new MyThread();
        // 4. 通过此对象调用 start()
        t1.start();

    }
}
```

### 8.3.2 实现 Runnable 接口

1. 创建一个实现了 Runnable 接口的类
2. 实现类去实现 Runnable 中的抽象方法：run()
3. 创建实现类的对象
4. 将此对象作为参数传递到 Thread 类的构造器中，创建 Thread 类的对象
5. 通过 Thread 类的对象调用 start()
6. 创建多个线程，需要创建多个**Thread 类**对象

```java
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
        Thread t1 = new Thread(mThread);
        // 5. 通过 Thread 类的对象调用 start()
        t1.start();
    }
}
```

### 8.3.3 两种实现方式的比较

- 开发中，优先选择实现 **Runnable 接口** 的方式
  - 实现的方式没有类的**单继承性**的局限性；实现的方式更适合来处理多个线程有**共享数据**的情况

- 联系：两种方式都需要**重写 run()**，将线程要执行的逻辑声明在 run() 中

![Thread类](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/Thread类.kizkv6cnm0g.png)

## 8.4 线程的生命周期

![线程的生命周期](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/线程的生命周期.5t5nn9cnpno0.png)

![线程状态变迁图](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/线程状态变迁图.5awwgwd2r800.png)

## 8.5 线程的同步

> 通过**同步机制**，来解决线程安全问题。
> 同步的方式，解决了线程的安全问题，但是操作同步代码时，只能有一个线程参与，其他线程等待，相当于是一个单线程的过程，效率低。

### 8.5.1 同步代码块

```java
synchronized(同步监视器) {
    // 需要被同步的代码
}
```

- 共享数据：即多个线程共同操作的变量；
- 操作共享数据的代码，即为需要被同步的代码；
- 同步监视器：俗称锁，任何一个类的对象，都可以充当锁；**要求，多个线程必须要共用一把锁**。

```java
Object obj = new Object();

synchronized(obj) {
    // 需要被同步的代码
}
```

- 补充说明：
  - 在**实现 Runnable 接口**创建多线程的方式中，可考虑用**当前对象**(this)充当同步监视器；
  - 在**继承 Thread 类**创建多线程的方式中，慎用 this 充当同步监视器，考虑使用**当前类**充当同步监视器(Window2.class)，它只会加载一次，

### 8.5.2 同步方法

> 如果操作共享数据的代码完整声明在一个方法中，不妨将此方法声明为同步的。

- 同步方法仍然涉及到同步监视器，只是不需要显式声明
- 非静态的同步方法，同步监视器是：this
- 静态的同步方法，同步监视器是：当前类本身

1. 实现 Runnable 接口创建多线程的方式

```java
public void run() {
    while(true) {
        show();
    }
}

// 同步监视器：this
private synchronized void show() {
    // 操作共享数据的代码
}
```

2. 继承 Thread 类创建多线程的方式

```java
// 同步监视器：类
private static synchronized void show() {

}
```

### 8.5.3 懒汉式的线程安全问题

```java
public class BankTest {
}

class Bank {

    private Bank() {
    }

    private static Bank instance = null;

    public static Bank getInstance() {
        // 方式一：效率稍差
//        synchronized (Bank.class) {
//            if (instance == null) {
//                instance = new Bank();
//            }
//            return instance;
//        }

        // 方式二：效率稍高
        if (instance == null) {
            synchronized (Bank.class) {
                if (instance == null) {
                    instance = new Bank();
                }
            }
        }
        return instance;
    }
}
```

### 8.5.4 线程的死锁问题

> 死锁：不同的线程分别占用对方需要的同步资源不放弃，都在等待对方放弃自己需要的同步资源，就形成了线程的死锁。

![线程同步](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/线程同步.5ssgnluncts0.png)

```java
public class ThreadTest {
    public static void main(String[] args) {

        StringBuffer s1 = new StringBuffer();
        StringBuffer s2 = new StringBuffer();

        new Thread() {
            @Override
            public void run() {
                synchronized (s1) {
                    s1.append("a");
                    s2.append("1");

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (s2) {
                        s1.append("b");
                        s2.append("2");
                        System.out.println(s1);
                        System.out.println(s2);
                    }
                }
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (s2) {
                    s1.append("c");
                    s2.append("3");
                    synchronized (s1) {
                        s1.append("d");
                        s2.append("4");
                        System.out.println(s1);
                        System.out.println(s2);
                    }
                }
            }
        }).start();
    }
}
```

- 解决方法
  - 专门的算法、原则；
  - 尽量减少同步资源的定义；
  - 尽量避免嵌套同步。

### 8.5.5 Lock 锁

```java
class Window implements Runnable {

    private int ticket = 100;

    // 1. 实例化 ReentrantLock
    private ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            try {
                // 2. 调用锁定方法 Lock()
                lock.lock();

                if (ticket > 0) {

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "售票，票号为：" + ticket);
                    ticket--;
                } else {
                    break;
                }
            } finally {
                // 3. 调用解锁方法
                lock.unlock();
            }

        }
    }
}
```

## 8.6 线程的通信

- 涉及到的三个方法
  - **wait()**: 一旦执行此方法，当前线程就进入阻塞状态，并**释放同步监视器**；
  - **notify()**: 一旦执行此方法，就会唤醒被 wait 的一个线程，如果有多个线程被 wait，就唤醒优先级最高的线程；
  - **notifyAll()**: 一旦执行此方法，就会唤醒所有被 wait 的线程。

- 说明
  1. wait(), notify(), notifyAll() 三个方法必须使用在**同步代码块或同步方法**中；
  2. wait(), notify(), notifyAll() 三个方法的调用者必须是同步代码块或同步方法中的同步监视器。
  3. wait(), notify(), notifyAll() 三个方法是定义在 java.lang.Object 中。

```java
public void run() {
    while (true) {
        synchronized (this) {

            notify();

            if (number <= 100) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + number);
                number++;

                try {
                    // 使得调用如下 wait() 方法的线程进入阻塞状态
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }
}
```

## 8.7 新增线程创建方式

### 8.7.1 实现 Callable 接口

1. call() 可以有返回值；
2. call() 可以抛出异常，被外面的操作捕获，获取异常信息；
3. Callable 是支持泛型的

![实现Callable接口](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/实现Callable接口.470es3lsc940.png)

```java
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
```

### 8.7.2 线程池

1. 提高响应速度（减少了创建新线程的时间）
2. 降低资源消耗（重复利用线程池中的线程，不需要每次都创建）
1. 便于线程管理

![线程池](https://cdn.jsdelivr.net/gh/Rinvay67/image-hosting@master/尚硅谷_Java教程/线程池.v584czjs2uo.png)

```java

class NumberThread1 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println(Thread.currentThread().getName() + ":" + i);
            }
        }
    }
}

public class ThreadPool {
    public static void main(String[] args) {
        // 1. 提供指定线程数量的线程池
        ExecutorService service = Executors.newFixedThreadPool(10);

        // ExecutorService 接口的实现类，ExecutorService 线程池接口
        ThreadPoolExecutor service1 = (ThreadPoolExecutor)service;

        // 设置线程池的属性
        // System.out.println(service.getClass());
        service1.setCorePoolSize(15);

        // 2. 执行指定的线程的操作，需要提供实现 Runnable 或 Callable 接口实现类的对象
        service.execute(new NumberThread1()); // 适用于 Runnable
        // service.submit(Callable callable); // 适用于 Callable
        // 3. 关闭连接池
        service.shutdown();
    }
}
```