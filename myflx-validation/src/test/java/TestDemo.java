/**
 * dead lock test
 */
public class TestDemo {
    public static void main(String[] args) throws InterruptedException {
        lock();
    }


    private static void lock() {
        final LockA lockA = new LockA();
        final LockB lockB = new LockB();
        final Thread thread = new Thread(() -> {
            synchronized (lockA) {
                System.out.println(Thread.currentThread().getName() + "拿到A锁。。。");
                synchronized (lockB) {
                    System.out.println(Thread.currentThread().getName() + "拿到B锁。。。");
                }
            }

        });

        final Thread thread1 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println(Thread.currentThread().getName() + "拿到B锁。。。");
                synchronized (lockA) {
                    System.out.println(Thread.currentThread().getName() + "拿到A锁。。。");
                }
            }

        });
        thread.start();
        thread1.start();
    }
}
class LockA {
}
class LockB {
}
